package com.dasu.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dasu on 2018/5/5.
 * 微信公众号：dasuAndroidTv
 * blog：https://www.jianshu.com/u/bb52a2918096
 *
 */

public class SpUtils {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public SpUtils(Context context, String spName) {
        sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.apply();
    }

    public void putString(String key, String value) {
        editor.putString(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value).apply();
    }

    public int getInt(String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    public void putLong(String key, long value) {
        editor.putLong(key, value).apply();
    }

    public long getLong(String key, long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    public void putFloat(String key, float value) {
        editor.putFloat(key, value).apply();
    }

    public float getFloat(String key, float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }
}
