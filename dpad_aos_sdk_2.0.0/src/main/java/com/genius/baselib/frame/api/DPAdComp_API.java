package com.genius.baselib.frame.api;

import android.content.Context;

import com.dpad.offerwall.bean.DPAdInfo;
import com.dpad.offerwall.bean.PartInfo;
import com.genius.baselib.frame.api.model.DPAdPCParams;
import com.genius.baselib.frame.base.BaseJsonApi;
import com.genius.baselib.frame.db.DB_Offerwall_CompAds;
import com.starstudio.frame.net.model.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rocklee on 2017-04-24.
 */

public class DPAdComp_API extends BaseJsonApi<DPAdComp_API> {

    private final String partid;
    private final DPAdInfo dpAdInfo;
    private DPAdPCParams dpAdPCParams;

    public DPAdComp_API(Context context, DPAdInfo dpAdInfo) {
        super(context);
        this.dpAdInfo = dpAdInfo;
        this.partid = dpAdInfo.getPartId();
        dpAdPCParams = new DPAdPCParams(context);
    }

    @Override
    public HttpMethod getRequestType() {
        return HttpMethod.POST;
    }

    @Override
    public String getRequestUrl() {
        return "/v1/ofw/comp";
    }

    @Override
    public JSONObject getHeaders() {
        JSONObject jsonObject = dpAdPCParams.getHeaders();
        try {
            jsonObject.put("part_id", partid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void setResponseData(Context context, JSONObject response) {

        DB_Offerwall_CompAds db_offerwall_compAds = new DB_Offerwall_CompAds(context,null);

        db_offerwall_compAds.openDB();
        PartInfo partInfo =new PartInfo();
        partInfo.setCampaign_id(dpAdInfo.getAdid());
        db_offerwall_compAds.pushPartInfo(partInfo);
        db_offerwall_compAds.closeDB();
    }

    @Override
    public void ErrorParsing(Context context) {
        super.ErrorParsing(context);

    }

    @Override
    public void ErrorStatusProcess(Context context, int statusCode, String message) {
        super.ErrorStatusProcess(context, statusCode, message);
        if(result.eror==-1){
            DB_Offerwall_CompAds db_offerwall_compAds = new DB_Offerwall_CompAds(context,null);
            db_offerwall_compAds.openDB();
            PartInfo partInfo =new PartInfo();
            partInfo.setCampaign_id(dpAdInfo.getAdid());
            db_offerwall_compAds.pushPartInfo(partInfo);
            db_offerwall_compAds.closeDB();
        }

    }
}
