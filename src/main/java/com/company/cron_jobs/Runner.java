package com.company.cron_jobs;

import com.typesafe.config.Config;

import java.util.Timer;
import java.util.TimerTask;

public class Runner {
    Timer t;
    TimerTask tt;
    CronJob cronJob;
    Config config;

    public Runner(CronJob cronJob, Config config) {
        this.cronJob = cronJob;
        this.config = config;
    }

    public void run() {
        t = new Timer();
        tt = new TimerTask() {
            @Override
            public void run() {
                cronJob.run();
            };
        };
        t.schedule(tt, 0,config.getInt("configuration.frequency"));
        System.out.println("task scheduled");
    }

    public void stop() {
        tt.cancel();
        t.cancel();
        System.out.println("task stopped");
    }
}
