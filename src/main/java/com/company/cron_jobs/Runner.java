package com.company.cron_jobs;

import com.typesafe.config.Config;

import java.util.Timer;

public class Runner {
    Timer t;
    CronJob cronJob;
    Config config;

    public Runner(CronJob cronJob, Config config) {
        this.cronJob = cronJob;
        this.config = config;
        t = new Timer();
    }

    public void run() {
        t.scheduleAtFixedRate(cronJob, 0,  config.getInt("configuration.frequency"));
    }

    public void stop() {
        t.cancel();
    }
}
