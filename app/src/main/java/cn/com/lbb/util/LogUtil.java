package cn.com.lbb.util;

import android.util.Log;

public class LogUtil {
    public static void v(String tag, String message, Object... args) {
        printLog(Log.VERBOSE, tag, message, null, args);
    }

    public static void v(String tag, String message, Throwable t) {
        v(t, tag, message);
    }

    public static void v(Throwable t, String tag, String message, Object... args) {
        printLog(Log.VERBOSE, tag, message, t, args);
    }

    private static void printLog(int priority, String tag, String msg, Throwable tr, Object... args) {
        Log.v(tag, msg);
    }
}
