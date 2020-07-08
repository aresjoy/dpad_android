package com.genius.baselib.frame.api;

import android.content.Context;

import com.genius.baselib.frame.base.BaseJsonApi;
import com.starstudio.frame.net.model.HttpMethod;

import org.json.JSONObject;

/**
 * Created by Hongsec on 2016-09-05.
 */
public class Test  extends BaseJsonApi<Test>{

    public Test(Context context) {
        super(context);
    }

    @Override
    public HttpMethod getRequestType() {
        return HttpMethod.GET;
    }

    @Override
    public String getRequestUrl() {
        return null;
    }

    @Override
    public JSONObject getHeaders() {
        return null;
    }

    @Override
    public void setResponseData(Context context, JSONObject response) {

    }
}
