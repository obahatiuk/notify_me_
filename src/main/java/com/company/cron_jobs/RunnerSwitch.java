package com.company.cron_jobs;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;

public class RunnerSwitch {
    private boolean running;
    private Runner runner;

    public RunnerSwitch() {
        setUp();
    }

    public void setUp() {
        try {
            HTMLDiffCronJob htmlDiffCronJob = new HTMLDiffCronJob();
            Config runnerConfig = ConfigFactory.parseFile(new File(getClass().getResource("/cron_job.conf").toURI()));
            runner = new Runner(htmlDiffCronJob, runnerConfig);
            runner.run();
            running = true;
        } catch(Throwable t) {
            System.out.println(t);
        }
    }

    public void changeState(String state) {
        switch (state) {
            case "stop":
                if (running) {
                    runner.stop();
                    running = false;
                }
                break;
            case "run":
                if ( ! running ) {
                    runner.run();
                    running = true;
                }
                break;
            default:
                break;
        }
    }
}
