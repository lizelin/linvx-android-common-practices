package net.linvx.android.libs.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by lizelin on 16/2/5.
 * 本地存储的简单封装
 */
public class SharedPrefUtils {
    private SharedPrefUtils() {
    }

    public static void saveField(Context context, String key, String value) {
        SharedPreferences spf = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        Editor editor = spf.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void saveField(Context context, String key, int value) {
        SharedPreferences spf = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        Editor editor = spf.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void saveField(Context context, String key, float value) {
        SharedPreferences spf = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        Editor editor = spf.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static void saveField(Context context, String key, long value) {
        SharedPreferences spf = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        Editor editor = spf.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static void saveField(Context context, String key, boolean value) {
        SharedPreferences spf = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        Editor editor = spf.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static String readFieldString(Context context, String key, String defValue) {
        SharedPreferences spf = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        String value = spf.getString(key, defValue);
        return value;
    }

    public static int readFieldInt(Context context, String key, int defValue) {
        SharedPreferences spf = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        int value = spf.getInt(key, defValue);
        return value;
    }

    public static float readFieldFloat(Context context, String key, float defValue) {
        SharedPreferences spf = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        float value = spf.getFloat(key, defValue);
        return value;
    }

    public static long readFieldLong(Context context, String key, long defValue) {
        SharedPreferences spf = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        long value = spf.getLong(key, defValue);
        return value;
    }

    public static boolean readFieldBoolean(Context context, String key, boolean defValue) {
        SharedPreferences spf = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        boolean value = spf.getBoolean(key, defValue);
        return value;
    }
}
