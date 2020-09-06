package com.company.services.logger;

import java.util.ArrayList;
import java.util.List;

public class BatchOfLoggeres implements ILogger {
    public List<ILogger> loggers;

    public BatchOfLoggeres(List<ILogger> loggers) {
        this.loggers = loggers;
    }


    @Override
    public void log(String message) {
        for(ILogger logger : loggers) {
            logger.log(message);
        }
    }
}
