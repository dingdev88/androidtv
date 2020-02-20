package com.demo.network.common;

import android.util.Log;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DebugHelper {

    private static final String TAG = "DebugHelper";

    public static void trackError(final Error error) {

        DebugHelper.printError(DebugHelper.TAG, error);
    }

    public static void trackError(final String tag, final Error error) {

        DebugHelper.printError(tag, error);
    }

    public static void printLogE(final String tag, final String msg) {
            Log.e(tag, msg);
    }

    public static void printLogE(final String tag, final String msg, Throwable throwable) {

        if (true) {
            Log.e(tag, msg, throwable);
        }
    }

    public static void printLogD(final String tag, final String msg) {

        if (true) {
            Log.d(tag, msg);
        }
    }

    public static void printLogW(final String tag, final String msg) {
        if (true) {
            Log.w(tag, msg);
        }
    }

    public static void printLogI(final String tag, final String msg) {

        if (true) {
            Log.i(tag, msg);
        }
    }

    public static void printLogV(final String tag, final String msg) {

        if (true) {
            Log.v(tag, msg);
        }
    }

    public static void printSystemOut(final String msg) {

        if (true) {
            System.out.print(msg);
        }
    }

    public static void printError(final String tag, final Error error) {
        try {
            if (true) {
                DebugHelper.printData(tag, "Error = " + error.toString());
                error.printStackTrace();
                Logger.getAnonymousLogger().log(Level.CONFIG, "Error", error);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    public static void trackException(final Exception exception) {
        DebugHelper.print(DebugHelper.TAG, exception, true);
    }

    public static void trackException(final String tag, final Exception exception) {

        DebugHelper.print(tag, exception, true);
    }

    public static void print(final String tag, final Exception exception, final boolean track) {
        try {
            if (true) {
                DebugHelper.printData(tag, "Exception = " + exception.toString());
                exception.printStackTrace();
                Logger.getAnonymousLogger().log(Level.CONFIG, "Exception", exception);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    public static void logError(final String message) {
    }

    public static void printException(final Exception exception) {

        DebugHelper.print(DebugHelper.TAG, exception, false);
    }

    public static void printException(final String tag, final Exception exception) {

        DebugHelper.print(tag, exception, false);
    }

    public static void printData(final String data) {

        DebugHelper.printData(DebugHelper.TAG, data);
    }

    public static void printData(final String tag, final String data) {

        if (true) {
            Log.e(ConstantsString.EMPTY_STRING + tag, "Data = " + data);
        }
    }
}
