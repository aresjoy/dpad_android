package com.sera.volleyhelper.base;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.genius.utils.UtilsLog;
import com.sera.volleyhelper.bean.Result;
import com.sera.volleyhelper.imp.CallBackListener;
import com.sera.volleyhelper.imp.StringCallBackListener;
import com.sera.volleyhelper.imp.VolleyParam;
import com.sera.volleyhelper.ssl.HttpsTrustManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by Hongsec on 2016-08-21.
 * email : piaohongshi0506@gmail.com
 * QQ: 251520264
 */
public abstract class BaseRequest<T> extends JsonParseUtil implements VolleyParam {

    /**
     * loading dialog dissmiss delay
     */
    public final long DelayTime = 200;

    /**
     * Api basic result
     */
    public Result result = new Result();


    public String tag="";

    public BaseRequest() {
        tag = getClass().getSimpleName()+"_"+getClass().toString();
    }

    protected  Handler handler = new Handler();
    /**
     * 主页地址
     */
    public  abstract  String baseUrl();

    public abstract void ErrorStatusProcess(Context context,int statusCode,String errormessage);
    public  void ErrorParsing(Context context){};
    /**
     *
     * @param context   must Application Context
     * @param tCallBackListener
     */
    public void request(Context context, final CallBackListener<T> tCallBackListener){
        HttpsTrustManager.allowAllSSL();

    };
    public void request(Context context, final StringCallBackListener<T> tCallBackListener){
        HttpsTrustManager.allowAllSSL();

    };
    protected int socketTimeout = 15000;


    public String getRealUrl() {

        StringBuilder stringBuilder = new StringBuilder();
        if (getRequestUrl().contains("http://") || getRequestUrl().contains("https://")) {
            stringBuilder.append(getRequestUrl());
        } else {
            stringBuilder.append(baseUrl());
            stringBuilder.append(getRequestUrl());
        }
        stringBuilder.append("?");
//        if(getRequestType() == Request.Method.GET){
//            stringBuilder.append(getHeaderLink());
//        }
        stringBuilder.append(getCommonParams());

        return stringBuilder.toString();
    }

    public abstract  String getCommonParams();

    /**
     * 添加的属性值
     *
     * @return
     */
    public String getHeaderLink() {

        JSONObject headers = getHeaders();
        StringBuilder stringBuilder = new StringBuilder();
        if (headers != null) {
            Iterator<String> keys = headers.keys();

            while (keys.hasNext()) {
                String next = keys.next();
                try {
                    stringBuilder.append("&");
                    stringBuilder.append(URLEncoder.encode(next, "utf8"));
                    stringBuilder.append("=");
                    if (!TextUtils.isEmpty(getJString(headers, next, ""))) {
                        stringBuilder.append(headers.getString(next));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException se) {
                    se.printStackTrace();
                }

            }

        }
//        MultipartRequestParams multiParamHeaders = getMultiParamHeaders();
//        multiParamHeaders.

        return stringBuilder.toString();
    }


    public void setResult(JSONObject response) {
        result.status = getJBoolean(response, "status", false);
        result.eror = getJInteger(response, "error", 0);
        result.msg = getJString(response, "msg", "");
        try {
            UtilsLog.i(response.toString(4));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }






}
