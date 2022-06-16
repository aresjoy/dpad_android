package com.genius.baselib.frame.api;

import android.content.Context;

import com.dpad.offerwall.bean.QaInfo;
import com.genius.baselib.frame.base.BaseJsonApi;
import com.starstudio.frame.net.model.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rocklee on 2017-04-25.
 */

public class DPQA_API extends BaseJsonApi<DPQA_API> {

    private final QaInfo qaInfo;

    public DPQA_API(Context context, QaInfo qaInfo) {
        super(context);
        this.qaInfo=qaInfo;
    }

    @Override
    public HttpMethod getRequestType() {
        return HttpMethod.POST;
    }

    @Override
    public String getRequestUrl() {
        return "/v1/ofw/qna";
    }

    @Override
    public JSONObject getHeaders() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name",qaInfo.getName());
            jsonObject.put("email",qaInfo.getEmail());
            jsonObject.put("phone",qaInfo.getPhone());
            jsonObject.put("body",qaInfo.getBody());
            jsonObject.put("body",qaInfo.getBody());
            jsonObject.put("ad_title",qaInfo.getTitle());
            jsonObject.put("adid",qaInfo.getAdid());
            jsonObject.put("part_id",qaInfo.getPartid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void setResponseData(Context context, JSONObject response) {

    }
}
