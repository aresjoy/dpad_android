package com.genius.baselib.frame.base;

import android.content.Context;

import com.genius.baselib.frame.center.CConfig;
import com.sera.volleyhelper.JsonRequest;

/**
 * Created by Hongsec on 2016-09-05.
 */
public abstract class BaseJsonApi<T> extends JsonRequest<T> {


    CommonApi commonApi = new CommonApi();

    public BaseJsonApi(Context context) {
        super();
        commonApi.init(context);
    }

    @Override
    public String baseUrl() {

        return CConfig.BASE_URL;
    }

    @Override
    public String getCommonParams() {
        return commonApi.getCommonParams();
    }


    @Override
    public void ErrorStatusProcess(final Context context, int statusCode, String message) {

        commonApi.ErrorStatusProcess(context, statusCode, message, result);

    }


    @Override
    public void ErrorParsing(Context context) {
        super.ErrorParsing(context);
        commonApi.ErrorParsing(context,getRequestUrl(),getRequestType(),getHeaders());
    }
}
