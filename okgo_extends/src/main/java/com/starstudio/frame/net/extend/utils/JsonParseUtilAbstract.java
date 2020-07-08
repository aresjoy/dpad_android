package com.starstudio.frame.net.extend.utils;

import com.starstudio.frame.net.extend.imp.OnJsonArrayCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Hongsec on 2016-08-21.
 * email : piaohongshi0506@gmail.com
 * QQ: 251520264
 */
public abstract class JsonParseUtilAbstract<T> {

    public int getJInteger(JSONObject jsonObject, String name, int defaultValue) {
        if (jsonObject.has(name) && !jsonObject.isNull(name)) {
            try {

                return jsonObject.getInt(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return defaultValue;

    }

    public ArrayList<T> getJJsonArray(JSONObject jsonObject, String name, OnJsonArrayCallBack<T> onJsonArrayCallBack) {
        ArrayList<T> objects = new ArrayList<>();
        if (jsonObject.has(name) && !jsonObject.isNull(name)) {
            try {
                JSONArray jsonArray = jsonObject.getJSONArray(name);
                for (int i = 0; i < jsonArray.length(); i++) {
                    Object o = jsonArray.get(i);
                    if (onJsonArrayCallBack != null) {
                        T t = onJsonArrayCallBack.getItem(i, o);
                        if (t != null) {
                            objects.add(t);
                        }
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return objects;
    }


    public boolean getJBoolean(JSONObject jsonObject, String name, boolean defaultValue) {

        if (jsonObject.has(name) && !jsonObject.isNull(name)) {
            try {
                return jsonObject.getBoolean(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return defaultValue;
    }

    public JSONObject getJJsonObject(JSONObject jsonObject, String name, JSONObject defaultValue) {

        if (jsonObject.has(name) && !jsonObject.isNull(name)) {
            try {
                return jsonObject.getJSONObject(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return defaultValue;
    }

    public JSONObject getJJsonObject(String str, JSONObject defaultValue) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            if (jsonObject != null) {
                return jsonObject;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public String getJString(JSONObject jsonObject, String name, String defaultValue) {

        if (jsonObject.has(name) && !jsonObject.isNull(name)) {
            try {
                return jsonObject.getString(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return defaultValue;
    }

    public long getJLong(JSONObject jsonObject, String name, long defaultValue) {

        if (jsonObject.has(name) && !jsonObject.isNull(name)) {
            try {
                return jsonObject.getLong(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return defaultValue;
    }

}
