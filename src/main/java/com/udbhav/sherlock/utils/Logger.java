package com.udbhav.sherlock.utils;

public class Logger {
    
    public static void info(String message) {
        System.out.println("[INFO]: " + message);
    }
    public static void error(String message) {
        System.err.println("[ERROR]: " + message);
    }
}

