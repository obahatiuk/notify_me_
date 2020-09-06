package com.company;

import com.company.cron_jobs.HTMLDiffCronJob;
import com.company.cron_jobs.Runner;
import com.company.cron_jobs.RunnerSwitch;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        RunnerSwitch rw = new RunnerSwitch();
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.print("Enter stop or run: ");
            String changeState = scanner.nextLine();
            rw.changeState(changeState);
        }
    }
}
