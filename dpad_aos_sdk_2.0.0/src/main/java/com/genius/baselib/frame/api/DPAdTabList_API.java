package com.genius.baselib.frame.api;

import android.content.Context;

import com.dpad.offerwall.bean.DPAdTabInfo;
import com.genius.baselib.PreferenceUtil;
import com.genius.baselib.frame.base.BaseJsonApi;
import com.genius.baselib.frame.center.CStatic;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.starstudio.frame.net.model.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rocklee on 2017-04-24.
 */

public class DPAdTabList_API extends BaseJsonApi<DPAdTabList_API> {

    private final String gId;
    private final boolean gId_track;

    public DPAdTabList_API(Context context) {
        super(context);
        gId = PreferenceUtil.getInstance(context.getApplicationContext()).getValue(CStatic.SP_GID, "");
        gId_track = PreferenceUtil.getInstance(context.getApplicationContext()).getValue(CStatic.SP_GID_TRACK, false);
    }

    @Override
    public HttpMethod getRequestType() {
        return HttpMethod.GET;
    }

    @Override
    public String getRequestUrl() {
        return "/v1/ofw/tab";
    }


    public List<DPAdTabInfo> dpAdTabLists = new ArrayList<>();

    @Override
    public JSONObject getHeaders() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("google_ad_id", gId);
            jsonObject.put("google_ad_id_limited_tracking_enabled", gId_track);
            jsonObject.put("is_short_key", "Y");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Override
    public void setResponseData(Context context, JSONObject response) {

        try {
            JSONArray list = response.getJSONArray("list");
            if (list == null || list.length() <= 0) return;

            dpAdTabLists = new Gson().fromJson(list.toString(), new TypeToken<List<DPAdTabInfo>>() {
            }.getType());


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
