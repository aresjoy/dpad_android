package com.genius.baselib.frame.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.dpad.offerwall.bean.DPAdInfo;
import com.dpad.offerwall.bean.PartInfo;
import com.genius.baselib.PreferenceUtil;
import com.genius.baselib.frame.base.BaseJsonApi;
import com.genius.baselib.frame.center.CStatic;
import com.genius.baselib.frame.db.DB_Offerwall_CompAds;
import com.genius.baselib.frame.db.DB_Offerwall_PartAds;
import com.genius.baselib.frame.util.CTools;
import com.starstudio.frame.net.model.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Rocklee on 2017-04-24.
 */

public class DPAdList_API extends BaseJsonApi<DPAdList_API> {

    private final String gId;
    private final boolean gId_track;

    public DPAdList_API(Context context) {
        super(context);
        gId = PreferenceUtil.getInstance(context.getApplicationContext()).getValue(CStatic.SP_GID,"");
        gId_track = PreferenceUtil.getInstance(context.getApplicationContext()).getValue(CStatic.SP_GID_TRACK,false);
    }

    @Override
    public HttpMethod getRequestType() {
        return HttpMethod.GET;
    }

    @Override
    public String getRequestUrl() {
        return "/v1/ofw/list";
    }


    public List<DPAdInfo> dpAdInfoLists = new ArrayList<>();

    @Override
    public JSONObject getHeaders() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("google_ad_id",gId);
            jsonObject.put("google_ad_id_limited_tracking_enabled",gId_track);
            jsonObject.put("is_short_key","Y");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Override
    public void setResponseData(Context context, JSONObject response) {

//        PreferenceUtil.getInstance(context.getApplicationContext()).setValue(CStatic.SP_HURL,getJString(response,"hurl",""));

        DB_Offerwall_PartAds db_offerwall_partAds = new DB_Offerwall_PartAds(context, null);
        DB_Offerwall_CompAds db_offerwall_compAds = new DB_Offerwall_CompAds(context, null);
        db_offerwall_partAds.openDB();
        db_offerwall_compAds.openDB();
        List<PartInfo> allIndex = db_offerwall_compAds.getAllIndex(context);
        for(PartInfo partInfo : allIndex){
            Log.v("fdsf",partInfo.getCampaign_id()+"campId");
        }
        try {
            JSONArray list = response.getJSONArray("list");
            if (list == null || list.length() <= 0) return;
            int birth = PreferenceUtil.getInstance(context.getApplicationContext()).getValue(CStatic.BIRTH, 0);
            int gender = PreferenceUtil.getInstance(context.getApplicationContext()).getValue(CStatic.GENDER, 2);

            for (int i = 0; i < list.length(); i++) {
                JSONObject jsonObject = list.getJSONObject(i);
                DPAdInfo dpAdInfo = new DPAdInfo();
                dpAdInfo.setAdid(getJString(jsonObject, "adid", ""));

                PartInfo checkCompleted = db_offerwall_compAds.getInfo(dpAdInfo.getAdid() + "");
                if (checkCompleted != null) {
                    continue;
                }
//                dpAdInfo.setUdid_required(getJInteger(jsonObject, "udid_required", 0));
                dpAdInfo.setDescription(getJString(jsonObject, "dc", ""));
                dpAdInfo.setPackage_name(getJString(jsonObject, "p", ""));
                dpAdInfo.setRwd(getJString(jsonObject, "rwd", ""));
                dpAdInfo.setRevenue_type(getJString(jsonObject, "rt", ""));
                dpAdInfo.setTarget_sex(getJString(jsonObject, "ts", ""));//mF
                dpAdInfo.setTarget_age_from(getJString(jsonObject, "taf", ""));
                dpAdInfo.setTarget_age_to(getJString(jsonObject, "tat", ""));
                dpAdInfo.setIcon(getJString(jsonObject, "ic", ""));
                dpAdInfo.setAction_description(getJString(jsonObject, "adc", ""));
                dpAdInfo.setTitle(getJString(jsonObject, "tt", ""));
                dpAdInfo.setRevenue_type_str(getJString(jsonObject, "rts", ""));

                try {
                    int i1 = Calendar.getInstance().get(Calendar.YEAR);
                    int tbirth_from = i1 - Integer.parseInt(dpAdInfo.getTarget_age_from());
                    int tbirth_to = i1 - Integer.parseInt(dpAdInfo.getTarget_age_to());
                    if (tbirth_from < birth || tbirth_to > birth) {

                        continue;
                    }
                } catch (NumberFormatException e) {
//                    e.printStackTrace();
                }
                if (!TextUtils.isEmpty(dpAdInfo.getTarget_sex())) {
                    if (dpAdInfo.getTarget_sex().equalsIgnoreCase("M") && gender == 0) {
                        continue;
                    } else if (dpAdInfo.getTarget_sex().equalsIgnoreCase("F") && gender == 1) {
                        continue;
                    }
                }


                PartInfo partedInfo = db_offerwall_partAds.getInfo(dpAdInfo.getAdid() + "");
                if (partedInfo != null) {
                    if (TextUtils.isEmpty(partedInfo.getPart_id()) || TextUtils.isEmpty(partedInfo.getLandingUrl())) {
                        db_offerwall_partAds.remodePartInfo(partedInfo);
                    } else {
                        dpAdInfo.setParted(true);
                        dpAdInfo.setPartId(partedInfo.getPart_id());
                        dpAdInfo.setLanding_url(partedInfo.getLandingUrl());
                    }

                }
                if (!dpAdInfo.isParted() && CTools.isexit(context,dpAdInfo.getPackage_name())!=null) {
                    continue;
                }

                //TODO age setting

                dpAdInfoLists.add(dpAdInfo);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        db_offerwall_partAds.closeDB();
        db_offerwall_compAds.closeDB();


    }
}
