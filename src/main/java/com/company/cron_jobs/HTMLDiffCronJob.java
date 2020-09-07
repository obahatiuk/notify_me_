package com.company.cron_jobs;

import com.company.services.logger.BatchOfLoggeres;
import com.company.services.logger.EmailLogger;
import com.company.services.logger.FileLogger;
import com.company.services.logger.ILogger;
import com.company.services.notification.EmailNotificationService;
import com.company.services.notification.IObserver;
import com.company.services.notification.ISubject;
import com.company.services.notification.sound.ServerSoundNotificationService;
import com.company.services.notification.email.EmailService;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;

public class HTMLDiffCronJob extends CronJob implements ISubject {
    private ILogger errorLogger;
    private ILogger successLogger;
    private Config config;
    private List<IObserver> observerList = new ArrayList();

    public HTMLDiffCronJob() {
        setUp();
    }


    public void setUp(ILogger errorLogger, ILogger successLogger, Config config) {
        this.errorLogger = errorLogger;
        this.successLogger = successLogger;
        this.config = config;
    }

    public void setUp() {
        Config emailConfig = null;
        try {
            emailConfig = ConfigFactory.parseFile(new File(getClass().getResource("/email_accounts.conf").toURI()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        successLogger = new FileLogger();
        EmailService emailService = new EmailService(emailConfig, successLogger);
        //loggers set up
        List<ILogger> loggers = new ArrayList<ILogger>() { {  add(successLogger); add(new EmailLogger(emailService));  } };
        errorLogger = new BatchOfLoggeres(loggers);
        //observers
        EmailNotificationService emailNotificationService = new EmailNotificationService(emailService);
        ServerSoundNotificationService serverSoundNotificationService = new ServerSoundNotificationService();
        observerList.add(emailNotificationService);
        observerList.add(serverSoundNotificationService);
        try {
            config = ConfigFactory.parseFile(new File(getClass().getResource("/html_files.conf").toURI()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void register(IObserver observer) {
        observerList.add(observer);
    }

    @Override
    public void unregister(IObserver observer) {
        observerList.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (IObserver observer: observerList) {
            observer.update();
        }
    }

    @Override
    public void run() {
        try{
            String out = new Scanner(new URL(config.getString("configuration.url")).openStream(), "UTF-8").useDelimiter("\\A").next();
            File page = new File(config.getString("configuration.file_name"));
            if (page.createNewFile()) {
                try(PrintWriter pw = new PrintWriter(page)){
                    pw.print(out);
                }
                successLogger.log("no changes on: " + new Date());
            } else {
                String retrospective = new String(Files.readAllBytes(page.toPath()));
                if (!retrospective.equals(out)) {
                    successLogger.log("html changed during run on: " + new Date());
                    notifyObservers();
                    try(PrintWriter pw = new PrintWriter(new FileOutputStream(config.getString("configuration.file_name"), false))) {
                        pw.print(out);
                    }
                } else {
                    successLogger.log("no changes on: " + new Date());
                }
            }
            successLogger.log("success run on: " + new Date());
        } catch (Throwable t) {
            t.printStackTrace();
            try {
                errorLogger.log(t.getMessage());
            } catch (Throwable t1) {
                System.out.println(t1);
            }
        }
    }
}
