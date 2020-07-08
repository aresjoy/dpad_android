package com.starstudio.frame.net.extend.imp;

import android.content.Context;

import com.starstudio.frame.net.model.HttpMethod;
import com.starstudio.frame.net.request.base.RequestAbstract;

import org.json.JSONObject;

/**
 * Created by Hongsec on 2016-08-21.
 * email : piaohongshi0506@gmail.com
 * QQ: 251520264
 */
public interface OkhttpParam<T,R extends RequestAbstract>{

    public HttpMethod getRequestType();
    public String getRequestUrl();

    public R postRequest(Context context, OnCallBackListener<T> tOnCallBackListener);

   /* *//**
     * OnlyBackground
     * @param context
     * @param response
     *//*
    public void setResponseData(Context context, JSONObject response);
    *//**
     * OnlyBackground
     *//*
    public void setResponseData(Context context, String response);
*/


    public R getParams(R request);
    public R getHeaders(R request);
    public R getUrlParams(R request);
    public R getSpecialDatas(R request);


    public void setResponseData(Context context, JSONObject response);

    public void setResponseData(Context context, String response);

    HttpMethod getCustomRequestType();
}
