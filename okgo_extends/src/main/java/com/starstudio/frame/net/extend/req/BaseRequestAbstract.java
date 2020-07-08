package com.starstudio.frame.net.extend.req;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.starstudio.frame.net.cache.CacheMode;
import com.starstudio.frame.net.extend.imp.OkhttpParam;
import com.starstudio.frame.net.extend.model.Result;
import com.starstudio.frame.net.extend.utils.JsonParseUtilAbstract;
import com.starstudio.frame.net.request.base.RequestAbstract;

import org.json.JSONObject;

/**
 * Created by hongsec on 17. 6. 29.
 */

public abstract class BaseRequestAbstract<T,R extends RequestAbstract> extends JsonParseUtilAbstract implements OkhttpParam<T,R> {


    /**
     * Api basic result
     */
    public Result result = new Result();

    private String tag = "";

    private int retryCount = 3;

    private long cacheTime = 5000L;

    private CacheMode cacheMode = CacheMode.DEFAULT;

    private Gson gson;

    /**
     * From json (json ,class)
     * @return
     */
    protected Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    public BaseRequestAbstract() {
    }

    public String getCacheKey() {
        if (TextUtils.isEmpty(tag)) {
            tag = getClass().getSimpleName() + "_" + getClass().toString();
        }
        return tag;
    }

    public void setCacheKey(String key) {
        tag = key;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public long getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(long cacheTime) {
        this.cacheTime = cacheTime;
    }

    public void setCacheMode(CacheMode cacheMode) {
        this.cacheMode = cacheMode;
    }

    public CacheMode getCacheMode() {
        return cacheMode;
    }

    public R getCommonRequest(R request) {
        request.tag(this)
                .retryCount(getRetryCount())
                .cacheKey(getCacheKey())
                .cacheTime(getCacheTime())
                .cacheMode(getCacheMode());
        return request;
    }


    /**
     * 主页地址
     */
    public abstract String baseUrl();

    public abstract void errorStatusProcess(Context context, int statusCode, String errormessage);
//    public  void ErrorParsing(Context context){};

    public String getRealUrl() {

        StringBuilder stringBuilder = new StringBuilder();
        if (getRequestUrl().contains("http://") || getRequestUrl().contains("https://")) {
            stringBuilder.append(getRequestUrl());
        } else {
            stringBuilder.append(baseUrl());
            stringBuilder.append(getRequestUrl());
        }
        return stringBuilder.toString();
    }


    public void setResult(JSONObject response) {
        result.status = getJBoolean(response, "status", false);
        result.eror = getJInteger(response, "error", 0);
        result.msg = getJString(response, "msg", "");
    }


}
