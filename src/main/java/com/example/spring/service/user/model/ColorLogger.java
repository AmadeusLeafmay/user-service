package com.example.spring.service.user.model;


import org.slf4j.LoggerFactory;

public class ColorLogger {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ColorLogger.class);

    public void logGreenInfo(String logging){
        logger.info("\u001B[32m" + logging + "\u001B[0m");
    }
    public void logYellowInfo(String logging){
        logger.info("\u001B[33m" + logging + "\u001B[0m");
    }
    public void logRedInfo(String logging){
        logger.info("\u001B[31m" + logging + "\u001B[0m");
    }
}