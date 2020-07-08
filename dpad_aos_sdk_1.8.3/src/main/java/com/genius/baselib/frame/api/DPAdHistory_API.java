package com.genius.baselib.frame.api;

import android.content.Context;

import com.dpad.offerwall.bean.HistoryInfo;
import com.genius.baselib.frame.base.BaseJsonApi;
import com.starstudio.frame.net.model.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rocklee on 2017-04-25.
 */

public class DPAdHistory_API extends BaseJsonApi<DPAdHistory_API> {

    private   int offset=0;

    public DPAdHistory_API(Context context, int offset) {
        super(context);
        this.offset =offset ;
    }

    @Override
    public HttpMethod getRequestType() {
        return HttpMethod.GET;
    }

    @Override
    public String getRequestUrl() {
        return "/v1/ofw/rwd_list";
    }

    @Override
    public JSONObject getHeaders() {
        JSONObject jsonObject = new JSONObject();
        try {
            if(offset!=0){
                jsonObject.put("offset",offset);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public List<HistoryInfo> historyInfos = new ArrayList<>();

    @Override
    public void setResponseData(Context context, JSONObject response) {

        try {
            JSONArray jsonArray = response.getJSONArray("list");
            if(jsonArray==null || jsonArray.length()<=0){
                return;
            }


            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                HistoryInfo historyInfo = new HistoryInfo();

                historyInfo.setTitle(getJString(jsonObject,"title",""));
                historyInfo.setRwd(getJString(jsonObject,"rwd",""));
                historyInfo.setTime(getJString(jsonObject,"date",""));

                historyInfos.add(historyInfo);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
