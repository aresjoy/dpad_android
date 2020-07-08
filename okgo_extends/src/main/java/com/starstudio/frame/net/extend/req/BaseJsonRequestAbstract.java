package com.starstudio.frame.net.extend.req;

import android.content.Context;

import com.starstudio.frame.net.OkGo;
import com.starstudio.frame.net.callback.StringCallbackAbstract;
import com.starstudio.frame.net.extend.imp.OnCallBackListener;
import com.starstudio.frame.net.model.HttpMethod;
import com.starstudio.frame.net.model.Progress;
import com.starstudio.frame.net.model.Response;
import com.starstudio.frame.net.request.base.RequestAbstract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hongsec on 17. 7. 3.
 */

public abstract class BaseJsonRequestAbstract<T,R extends RequestAbstract> extends BaseRequestAbstract<T,R> {

    public BaseJsonRequestAbstract() {

    }

    private final String TYPE_ARRAY="[";
    private final int RESPONSECODE_200=200;
    private final int RESPONSECODE_201=201;

    @Override
    public R getSpecialDatas(R request) {
        return request;
    }

    @Override
    public R postRequest(final Context context, final OnCallBackListener<T> tOnCallBackListener) {
        StringCallbackAbstract stringCallback = new StringCallbackAbstract() {

            @Override
            public void onStart(RequestAbstract<String, ? extends RequestAbstract> request) {
                super.onStart(request);
                if (tOnCallBackListener != null) {
                    tOnCallBackListener.showloadingUI(request);
                }

            }

            @Override
            public void uploadProgress(Progress progress) {
                super.uploadProgress(progress);
                if (tOnCallBackListener != null) {
                    tOnCallBackListener.uploadProgress(progress);
                }
            }

            @Override
            public String convertResponse(okhttp3.Response response) throws Throwable {


                try {
                    String body = response.body().string().trim();


                    if (body.startsWith(TYPE_ARRAY)) {
                        body = "{ \"list\":" + body + "}";
                        JSONObject jsonObject = new JSONObject(body);
                        setResult(jsonObject);
                        if ((response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) &&((!jsonObject.has("status")&& !getRealUrl().contains(baseUrl())) ||result.status)) {
                            setResponseData(context, jsonObject);
                        }
                    } else {
                        JSONObject jsonObject = new JSONObject(body);
                        setResult(jsonObject);
                        if ((response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) && ((!jsonObject.has("status")&& !getRealUrl().contains(baseUrl())) ||result.status)) {
                            setResponseData(context, jsonObject);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                        setResponseData(context, response.body().string());
                    }
                }


                return super.convertResponse(response);
            }

            @Override
            public void onSuccess(Response<String> response) {

                try {
                    if (tOnCallBackListener != null) {
                        if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                            tOnCallBackListener.onResponse( );
                        } else {
                            tOnCallBackListener.onErrorResponse();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (response.code() != RESPONSECODE_200 && response.code() != RESPONSECODE_201) {
                        errorStatusProcess(context, response.code(), result.msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(response.getRawResponse().body().string());
                        setResult(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (tOnCallBackListener != null) {
                    tOnCallBackListener.onErrorResponse( );
                }
                if (response.code() != RESPONSECODE_200 && response.code() != RESPONSECODE_201) {
                    errorStatusProcess(context, response.code(), result.msg);
                }
            }


            @Override
            public void onFinish() {
                super.onFinish();

                if (tOnCallBackListener != null) {
                    tOnCallBackListener.cancleloadingUI();
                }

            }

            @Override
            public void onCacheSuccess(Response<String> response) {
                super.onCacheSuccess(response);
            }
        };

         R requst=null;
        if (HttpMethod.GET == getRequestType()) {
            requst = (R) OkGo.<String>get(getRealUrl());

        } else if (HttpMethod.POST == getRequestType()) {
            requst = (R) OkGo.<String>post(getRealUrl());

        } else if (HttpMethod.PUT == getRequestType()) {
            requst = (R) OkGo.<String>put(getRealUrl());
        } else if (HttpMethod.DELETE == getRequestType()) {
            requst = (R) OkGo.<Integer>delete(getRealUrl());
        }

        if (requst == null) {
            try {
                throw new Exception("not supported method");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        requst =getParams(requst);
        requst = getHeaders(requst);
        requst = getCommonRequest(requst);
        if( HttpMethod.GET == getRequestType()){
            requst = getUrlParams(requst);
        }

        requst = getSpecialDatas(requst);
        requst.execute(stringCallback);
        return requst;
    }

    @Override
    public HttpMethod getRequestType() {
        return getCustomRequestType();
    }


}
