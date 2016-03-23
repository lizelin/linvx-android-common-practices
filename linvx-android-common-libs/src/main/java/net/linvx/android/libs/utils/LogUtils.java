package net.linvx.android.libs.utils;

import android.util.Log;

/**
 * LogCat的简单封装
 *
 * 缺省日志输出模式为关off
 * 通过在app module中调用LogUtils.turnOn  或者  LogUtils.turnOff开关日志输出
 * 一般做法是在app的module的build.gradle中，设置开关。
 *
 * @author lizelin
 */
public class LogUtils {
    public static boolean LOG_DEBUG_FLAG = false;

    public static void turnOn() {
        synchronized (LogUtils.class) {
            LOG_DEBUG_FLAG = true;
        }
    }

    public static void turnOff() {
        synchronized (LogUtils.class) {
            LOG_DEBUG_FLAG = false;
        }
    }



    private String tag = "net.linvx.android.log";

    private LogUtils(String tag) {
        if (tag != null && !tag.equals(""))
            this.tag = tag;
    }

    @SuppressWarnings("rawtypes")
    public static LogUtils getLogger(Class c) {
        return new LogUtils(c.getName());
    }

    public static LogUtils getLogger(String t) {
        return new LogUtils(t);
    }

    public static LogUtils getLogger() {
        return new LogUtils("");
    }

    public static void debug(Object o) {
        LogUtils.getLogger().d(o);
    }

    public static void warn(Object o) {
        LogUtils.getLogger().w(o);
    }

    public static void info(Object o) {
        LogUtils.getLogger().i(o);
    }

    public static void error(Object o) {
        LogUtils.getLogger().e(o);
    }


    public void d(Object o) {
        if (LOG_DEBUG_FLAG)
            Log.d(tag, "" + o);
    }

    public void w(Object o) {
        if (LOG_DEBUG_FLAG)
            Log.w(tag, "" + o);
    }

    public void i(Object o) {
        if (LOG_DEBUG_FLAG)
            Log.i(tag, "" + o);
    }

    public void e(Object o) {
        if (LOG_DEBUG_FLAG)
            Log.e(tag, "" + o);
    }

}