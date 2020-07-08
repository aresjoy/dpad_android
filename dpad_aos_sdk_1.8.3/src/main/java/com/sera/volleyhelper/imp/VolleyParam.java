package com.sera.volleyhelper.imp;

import android.content.Context;

import com.starstudio.frame.net.model.HttpMethod;

import org.json.JSONObject;

/**
 * Created by Hongsec on 2016-08-21.
 * email : piaohongshi0506@gmail.com
 * QQ: 251520264
 */
public interface VolleyParam<T>{
    public HttpMethod getRequestType();
    public String getRequestUrl();
    public JSONObject getHeaders();

    /**
     * OnlyBackground
     * @param context
     * @param response
     */
    public void setResponseData(Context context, JSONObject response);

}
