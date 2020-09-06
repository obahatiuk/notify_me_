package com.company.services.logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class FileLogger implements ILogger{
    public static String journal = "logs.txt";
    public void log(String message) {
        try {
            File file = new File(journal);
            file.createNewFile();
            try (PrintWriter pw = new PrintWriter(new FileOutputStream(file, true))) {
                pw.println(message);
            } catch (Throwable t) {
            }
        } catch (Throwable t) {
        }
    }
}

