package com.sxq.backgammon.util;

import android.util.Log;

/**
 * Created by zhangliang on 16/8/30.
 * <p/>
 * Create a simple and more understandable Android logs.
 */

public class ServicesLog {

    static String sTAG = "Backgammon";

    static String sClassName;

    static String sMethodName;

    static int sLineNumber;

    private static boolean sEnable = false;

    public static void enable(boolean enable) {
        sEnable = enable;
    }

    private static String createLog(String log) {

        return Thread.currentThread().getName() + "[" + sClassName + ":" + sMethodName + ":" + sLineNumber + "]" + log;
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        sClassName = sElements[1].getFileName();
        sMethodName = sElements[1].getMethodName();
        sLineNumber = sElements[1].getLineNumber();
    }

    public static void e(String message) {
        if (!sEnable) {
            return;
        }

        // Throwable sInstance must be created before any methods
        getMethodNames(new Throwable().getStackTrace());
        Log.e(sTAG, createLog(message));
    }

    public static void e(String tag, String message) {
        if (!sEnable) {
            return;
        }

        // Throwable sInstance must be created before any methods
        getMethodNames(new Throwable().getStackTrace());
        Log.e(tag, createLog(message));
    }

    public static void d(String message) {
        if (!sEnable) {
            return;
        }

        getMethodNames(new Throwable().getStackTrace());
        Log.d(sTAG, createLog(message));
    }

    public static void d(String tag, String message) {
        if (!sEnable) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.d(tag, createLog(message));
    }

    public static void d(int [][] board){
        d(sTAG , board);
    }

    public static void d(String tag , int [][] board){
        if (!sEnable) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        StringBuilder sb =  new StringBuilder();
        for(int i = 0 ; i < board.length ; i ++){
            sb.append("\n");
            for(int j = 0 ; j < board[0].length ; j ++){
                sb.append(board[i][j] + "\t");
            }
        }
        Log.d(tag, createLog(sb.toString()));
    }
    public static boolean isEnable() {
        return sEnable;
    }
}
