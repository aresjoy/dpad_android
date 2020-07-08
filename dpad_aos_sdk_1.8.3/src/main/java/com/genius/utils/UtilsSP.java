package com.genius.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class UtilsSP {
    private String TAG = TAG = UtilsSP.class.getSimpleName();

    private WeakReference<Context> context;
    private SharedPreferences sp = null;
    private SharedPreferences.Editor edit = null;

    public UtilsSP(Context context) {
        this(context, context.getSharedPreferences("filename", Context.MODE_PRIVATE));
    }

    public UtilsSP(Context context, String filename) {
        this(context, context.getSharedPreferences(filename, Context.MODE_PRIVATE));
    }


    public UtilsSP(Context context, SharedPreferences sp) {

        if(context instanceof Activity){
            throw new UnsupportedOperationException("UtilsSP not allowed use activity instance of context!!!!");
        }

        this.context = new WeakReference<Context>(context);
        this.sp = sp;
        edit = sp.edit();
    }


    public synchronized void setValue(String key, boolean value) {
        edit.putBoolean(key, value);
        edit.commit();
    }

    public synchronized void setValue(int resKey, boolean value) {
        if (context == null || context.get() == null) return;
        setValue(this.context.get().getString(resKey), value);
    }

    // Float
    public synchronized void setValue(String key, float value) {
        edit.putFloat(key, value);
        edit.commit();
    }

    public synchronized void setValue(int resKey, float value) {
        if (context == null || context.get() == null) return;
        setValue(this.context.get().getString(resKey), value);
    }

    // Integer
    public synchronized void setValue(String key, int value) {
        if (context == null || context.get() == null) return;
        edit.putInt(key, value);
        edit.commit();
    }

    public synchronized void setValue(int resKey, int value) {
        if (context == null || context.get() == null) return;
        setValue(this.context.get().getString(resKey), value);
    }

    // Long
    public synchronized void setValue(String key, long value) {
        edit.putLong(key, value);
        edit.commit();
    }

    public synchronized void setValue(int resKey, long value) {
        if (context == null || context.get() == null) return;
        setValue(this.context.get().getString(resKey), value);
    }

    // String
    public synchronized void setValue(String key, String value) {
        edit.putString(key, value);
        edit.commit();
    }

    public synchronized void setValue(int resKey, String value) {
        if (context == null || context.get() == null) return;
        setValue(this.context.get().getString(resKey), value);
    }

    public synchronized void setValue(String key, Set<String> value) {
        edit.putStringSet(key, value);
        edit.commit();
    }

    public synchronized void setValue(int resKey, Set<String> value) {
        if (context == null || context.get() == null) return;
        setValue(this.context.get().getString(resKey), value);
    }

    public Set<String> getValue(String key, Set<String> defaultValue) {
        return sp.getStringSet(key, defaultValue);
    }

    public Set<String> getValue(int resKey, Set<String> defaultValue) {
        if (context == null || context.get() == null) return null;
        return getValue(this.context.get().getString(resKey), defaultValue);
    }


    public synchronized void setValue(String key, ArrayList<String> value) {
        Set<String> strings = new HashSet<>();
        int i = 0;
        for (String str : value) {
            strings.add((i++) + "=>" + str);
        }
        edit.putStringSet(key, strings);
        edit.commit();
    }

    public synchronized void setValue(int resKey, ArrayList<String> value) {
        if (context == null || context.get() == null) return;
        setValue(this.context.get().getString(resKey), value);
    }

    public ArrayList<String> getValue(String key, ArrayList<String> defaultValue) {

        Set<String> stringSet = sp.getStringSet(key, new HashSet<String>());
        Iterator<String> iterator = stringSet.iterator();

        HashMap<String, String> stringStringHashMap = new HashMap<>();
        ArrayList<String> result = new ArrayList<>();
        while (iterator.hasNext()) {
            String next = iterator.next();
            String[] split = next.split("=>");
            if (split != null) {
                if (split.length > 1) {
                    stringStringHashMap.put(split[0], next.substring(next.indexOf(split[1])));
                } else {
                    stringStringHashMap.put(split[0], "");
                }
            }
        }

        for (int i = 0; i < stringStringHashMap.size(); i++) {
            result.add(stringStringHashMap.get(i + "").toString());
        }

        if (result.size() == 0) {
            return defaultValue;
        }

        return result;
    }

    public ArrayList<String> getValue(int resKey, ArrayList<String> defaultValue) {
        if (context == null || context.get() == null) return null;
        return getValue(this.context.get().getString(resKey), defaultValue);
    }


    // Get

    // Boolean
    public boolean getValue(String key, boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    public boolean getValue(int resKey, boolean defaultValue) {
        if (context == null || context.get() == null) return false;
        return getValue(this.context.get().getString(resKey), defaultValue);
    }

    // Float
    public float getValue(String key, float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }

    public float getValue(int resKey, float defaultValue) {
        if (context == null || context.get() == null) return -1.0f;
        return getValue(this.context.get().getString(resKey), defaultValue);
    }

    // Integer
    public int getValue(String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    public int getValue(int resKey, int defaultValue) {
        return getValue(this.context.get().getString(resKey), defaultValue);
    }

    // Long
    public long getValue(String key, long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    public long getValue(int resKey, long defaultValue) {
        if (context == null || context.get() == null) return -1L;
        return getValue(this.context.get().getString(resKey), defaultValue);
    }

    // String
    public String getValue(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public String getValue(int resKey, String defaultValue) {
        if (context == null || context.get() == null) return "null";
        return getValue(this.context.get().getString(resKey), defaultValue);
    }

    // Delete
    public synchronized void remove(String key) {
        edit.remove(key);
        edit.commit();
    }

    public synchronized void clear() {
        edit.clear();
        edit.commit();
    }


}
