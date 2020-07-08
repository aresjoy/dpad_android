package com.sera.volleyhelper;

import android.content.Context;

import com.genius.utils.UtilsLog;
import com.sera.volleyhelper.base.BaseRequest;
import com.sera.volleyhelper.imp.CallBackListener;
import com.starstudio.frame.net.OkGo;
import com.starstudio.frame.net.callback.StringCallbackAbstract;
import com.starstudio.frame.net.model.HttpMethod;
import com.starstudio.frame.net.request.DeleteRequest;
import com.starstudio.frame.net.request.GetRequest;
import com.starstudio.frame.net.request.PostRequest;
import com.starstudio.frame.net.request.PutRequest;
import com.starstudio.frame.net.request.base.RequestAbstract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Most of all ok
 * Created by Hongsec on 2016-08-21.
 * email : piaohongshi0506@gmail.com
 * QQ: 251520264
 */
public abstract class JsonRequest<T> extends BaseRequest<T> {

    private final String TYPE_ARRAY="[";
    private final int RESPONSECODE_200=200;
    private final int RESPONSECODE_201=201;

    @Override
    public void request(final Context context, final CallBackListener<T> tCallBackListener) {
        super.request(context, tCallBackListener);
        UtilsLog.i("request Url:" + getRealUrl());
        try {
            if (getHeaders() != null) {
                UtilsLog.i("request Header:" + getHeaders().toString(4));
            } else {
                UtilsLog.i("request Header:null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringCallbackAbstract stringCallback = new StringCallbackAbstract() {

            @Override
            public void onStart(RequestAbstract<String, ? extends RequestAbstract> request) {
                super.onStart(request);
                if (tCallBackListener != null) {
                    tCallBackListener.showloadingUI();
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
                        if ((response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) && ((!jsonObject.has("status") && !getRealUrl().contains(baseUrl())) || result.status)) {
                            setResponseData(context, jsonObject);
                        }
                    } else {
                        JSONObject jsonObject = new JSONObject(body);
                        setResult(jsonObject);
                        if ((response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) && ((!jsonObject.has("status") && !getRealUrl().contains(baseUrl())) || result.status)) {
                            setResponseData(context, jsonObject);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
//                    setResponseData(context, response.body().string());
//                }

                return super.convertResponse(response);
            }

            @Override
            public void onSuccess(com.starstudio.frame.net.model.Response<String> response) {

                try {
                    if (tCallBackListener != null) {
                        if ((response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) && result.status) {
                            tCallBackListener.onResponse((T) JsonRequest.this);
                        } else {
                            tCallBackListener.onErrorResponse((T) JsonRequest.this);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (response.code() != RESPONSECODE_200 && response.code() != RESPONSECODE_201) {
                        ErrorStatusProcess(context, response.code(), "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(com.starstudio.frame.net.model.Response<String> response) {
                super.onError(response);
                try {
                    try {
                        okhttp3.Response rawResponse = response.getRawResponse();
                        if (rawResponse != null) {
                            JSONObject jsonObject = new JSONObject(rawResponse.body().string());
                            setResult(jsonObject);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (tCallBackListener != null) {
                    tCallBackListener.onErrorResponse((T) JsonRequest.this);
                }
                if (response.code() != RESPONSECODE_200 && response.code() != RESPONSECODE_201) {
                    ErrorStatusProcess(context, response.code(), "");
                }
            }


            @Override
            public void onFinish() {
                super.onFinish();

                if (tCallBackListener != null) {
                    tCallBackListener.cancleloadingUI();
                }

            }

            @Override
            public void onCacheSuccess(com.starstudio.frame.net.model.Response<String> response) {
                super.onCacheSuccess(response);
            }
        };

        if (HttpMethod.GET == getRequestType()) {
            GetRequest requst = (GetRequest) OkGo.<String>get(getRealUrl());
            JSONObject headers = getHeaders();
            Iterator<String> keys = headers.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                requst.params(key, getJString(headers, key, ""));
            }
            requst.execute(stringCallback);
        } else if (HttpMethod.POST == getRequestType()) {
            PostRequest requst = (PostRequest) OkGo.<String>post(getRealUrl());
            JSONObject headers = getHeaders();
            Iterator<String> keys = headers.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                requst.params(key, getJString(headers, key, ""));
            }
            requst.execute(stringCallback);
        } else if (HttpMethod.PUT == getRequestType()) {
            PutRequest requst = (PutRequest) OkGo.<String>put(getRealUrl());
            JSONObject headers = getHeaders();
            Iterator<String> keys = headers.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                requst.params(key, getJString(headers, key, ""));
            }
            requst.execute(stringCallback);
        } else if (HttpMethod.DELETE == getRequestType()) {
            DeleteRequest requst = (DeleteRequest) OkGo.<Integer>delete(getRealUrl());
            JSONObject headers = getHeaders();
            Iterator<String> keys = headers.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                requst.params(key, getJString(headers, key, ""));
            }
            requst.execute(stringCallback);
        }

//        if (requst == null) {
//            try {
//                throw new Exception("not supported method");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }

//        requst =getParams(requst);
//        requst = getHeaders(requst);
//        requst = getCommonRequest(requst);
//        if(Request.Method.GET== getRequestType()){
//            requst = getUrlParams(requst);
//        }


    }

}
