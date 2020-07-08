package com.genius.baselib.frame.api;

import android.content.Context;
import android.text.TextUtils;

import com.dpad.offerwall.bean.DPAdInfo;
import com.dpad.offerwall.bean.PartInfo;
import com.genius.baselib.frame.api.model.DPAdPCParams;
import com.genius.baselib.frame.base.BaseJsonApi;
import com.genius.baselib.frame.db.DB_Offerwall_CompAds;
import com.genius.baselib.frame.db.DB_Offerwall_History;
import com.genius.baselib.frame.db.DB_Offerwall_PartAds;
import com.genius.baselib.frame.db.HistoryItem;
import com.starstudio.frame.net.model.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rocklee on 2017-04-24.
 */

public class DPAdPart_API extends BaseJsonApi<DPAdPart_API> {

    public final DPAdInfo dpAdInfo;
    private DPAdPCParams dpAdPCParams;
    private String landing_url;

    public DPAdPart_API(Context context, DPAdInfo dpAdInfo) {
        super(context);
        this.dpAdInfo =dpAdInfo;
        dpAdPCParams = new DPAdPCParams(context);
    }

    @Override
    public HttpMethod getRequestType() {
        return HttpMethod.POST;
    }

    @Override
    public String getRequestUrl() {
        return "/v1/ofw/part";
    }

    @Override
    public JSONObject getHeaders() {
        JSONObject jsonObject = dpAdPCParams.getHeaders();
        try {
            jsonObject.put("adid", dpAdInfo.getAdid()+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void setResponseData(Context context, JSONObject response) {

        String part_id = getJString(response,"part_id","");
        landing_url = getJString(response,"landing_url","");
        dpAdInfo.setLanding_url(landing_url);

        DB_Offerwall_History db_offerwall_history = new DB_Offerwall_History(context,null);
        db_offerwall_history.openDB();
        HistoryItem historyItem = new HistoryItem();
        historyItem.setTitle(dpAdInfo.getTitle());
        historyItem.setAdid(dpAdInfo.getAdid());
        historyItem.setPartId(part_id);
        historyItem.setAction_str(dpAdInfo.getAction_description());
        db_offerwall_history.pushPartInfo(historyItem);

        db_offerwall_history.closeDB();


        if(dpAdInfo.getRevenue_type().equalsIgnoreCase("cpi")){
            if (TextUtils.isEmpty(part_id) || TextUtils.isEmpty(landing_url)) {


            }else{
            /*if(!dpAdInfo.getRevenue_type().toLowerCase().equalsIgnoreCase("cpi")){
                return;
            }*/
                dpAdInfo.setParted(true);
                dpAdInfo.setPartId(part_id);


                DB_Offerwall_PartAds db_offerwall_partAds = new DB_Offerwall_PartAds(context,null);
                db_offerwall_partAds.openDB();
                PartInfo partInfo =new PartInfo();
                partInfo.setPart_id(part_id);
                partInfo.setCampaign_id(dpAdInfo.getAdid()+"");
                partInfo.setLandingUrl(landing_url);
                db_offerwall_partAds.pushPartInfo(partInfo);
                db_offerwall_partAds.closeDB();
            }
        }





    }

    @Override
    public void ErrorStatusProcess(Context context, int statusCode, String message) {
        super.ErrorStatusProcess(context, statusCode, message);
        if(result.eror==-1){
            DB_Offerwall_CompAds db_offerwall_compAds = new DB_Offerwall_CompAds(context,null);
            db_offerwall_compAds.openDB();
            PartInfo partInfo =new PartInfo();
            partInfo.setCampaign_id(dpAdInfo.getAdid()+"");
            db_offerwall_compAds.pushPartInfo(partInfo);
            db_offerwall_compAds.closeDB();
        }

    }
}
