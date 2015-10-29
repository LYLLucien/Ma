package com.lucien.team.util.common;

import android.util.Log;

import com.lucien.team.app.Config;


/**
 * Created by lucien.li on 2015/5/19.
 */
public class CommonLog {
    private static final String TAG = "Ma";

    // default global log printer
    public static void i(String msg) {
        if (Config.isDebug)
            Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (Config.isDebug)
            Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (Config.isDebug)
            Log.e(TAG, msg);
    }

    public static void v(String msg) {
        if (Config.isDebug)
            Log.v(TAG, msg);
    }

    //class log printer
    public static void i(Class<?> clazz, String msg) {
        if (Config.isDebug)
            Log.i(clazz.getSimpleName(), msg);
    }

    public static void d(Class<?> clazz, String msg) {
        if (Config.isDebug)
            Log.d(clazz.getSimpleName(), msg);
    }

    public static void e(Class<?> clazz, String msg) {
        if (Config.isDebug)
            Log.e(clazz.getSimpleName(), msg);
    }

    public static void v(Class<?> clazz, String msg) {
        if (Config.isDebug)
            Log.v(clazz.getSimpleName(), msg);
    }

    //custom tag printer
    public static void i(String tag, String msg) {
        if (Config.isDebug)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (Config.isDebug)
            Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (Config.isDebug)
            Log.e(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (Config.isDebug)
            Log.v(tag, msg);
    }
}
