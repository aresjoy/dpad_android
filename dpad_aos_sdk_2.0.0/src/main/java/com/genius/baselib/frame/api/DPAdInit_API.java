package com.genius.baselib.frame.api;

import android.content.Context;

import com.genius.baselib.PreferenceUtil;
import com.genius.baselib.frame.base.BaseJsonApi;
import com.genius.baselib.frame.center.CStatic;
import com.starstudio.frame.net.model.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by Rocklee on 2017-04-27.
 */

public class DPAdInit_API extends BaseJsonApi<DPAdInit_API> {

    private boolean tf=false;

    public DPAdInit_API(Context context) {
        super(context);
        long value = PreferenceUtil.getInstance(context.getApplicationContext()).getValue(CStatic.SP_FIRST_DAY, 0L);
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(System.currentTimeMillis());
        instance.set(Calendar.HOUR_OF_DAY,0);
        instance.set(Calendar.MINUTE,0);
            if(instance.getTimeInMillis()/1000 - value >24*3600){
                PreferenceUtil.getInstance(context.getApplicationContext()).setValue(CStatic.SP_FIRST_DAY, instance.getTimeInMillis()/1000);
                tf= true;
            }
    }

    @Override
    public HttpMethod getRequestType() {
        return HttpMethod .GET;
    }

    @Override
    public String getRequestUrl() {
        return "/v1/sdk/init";
    }

    @Override
    public JSONObject getHeaders() {
        JSONObject jsonObject = new JSONObject();
        try {
            if(tf){
                jsonObject.put("tf","Y");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void setResponseData(Context context, JSONObject response) {
//        "f": ""


    }
}
