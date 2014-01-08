package com.balnave.rambler.logging;

/**
 * Logger for Rambler
 * @author balnave
 */
public class Logger {

    public static int level = 3;
    public static int SEVERE = 4;
    public static int WARNING = 3;
    public static int DEBUG = 2;
    public static int ALLWAYS = 99;
    
    public static void setLevel(int newLevel) {
        level = newLevel;
    }

    public static void log(String msg, int msgLevel) {
        if (msgLevel >= level) {
            System.out.println(msg);
        }
    }

}
