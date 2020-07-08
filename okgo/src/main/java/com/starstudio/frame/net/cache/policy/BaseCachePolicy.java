/*
 * Copyright 2016 jeasonlzy(廖子尧)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.starstudio.frame.net.cache.policy;

import android.graphics.Bitmap;

import com.starstudio.frame.net.OkGo;
import com.starstudio.frame.net.cache.CacheEntity;
import com.starstudio.frame.net.cache.CacheMode;
import com.starstudio.frame.net.callback.OnCallback;
import com.starstudio.frame.net.db.CacheManager;
import com.starstudio.frame.net.exception.HttpException;
import com.starstudio.frame.net.model.Response;
import com.starstudio.frame.net.request.base.RequestAbstract;
import com.starstudio.frame.net.utils.HeaderParser;
import com.starstudio.frame.net.utils.HttpUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Headers;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：2017/5/25
 * 描    述：
 * 修订历史：
 * ================================================
 */
public abstract class BaseCachePolicy<T> implements OnCachePolicy<T> {

    protected RequestAbstract<T, ? extends RequestAbstract> abstractRequest;
    protected volatile boolean canceled;
    protected volatile int currentRetryCount = 0;
    protected boolean executed;
    protected okhttp3.Call rawCall;
    protected OnCallback<T> mOnCallback;
    protected CacheEntity<T> cacheEntity;
    private final String ERROR_1 = "Already executed!";
    private final int RESPONSECODE_404 = 404;
    private final int RESPONSECODE_500 = 500;

    public BaseCachePolicy(RequestAbstract<T, ? extends RequestAbstract> abstractRequest) {
        this.abstractRequest = abstractRequest;
    }

    @Override
    public boolean onAnalysisResponse(Call call, okhttp3.Response response) {
        return false;
    }

    @Override
    public CacheEntity<T> prepareCache() {
        //check the config of cache
        if (abstractRequest.getCacheKey() == null) {
            abstractRequest.cacheKey(HttpUtils.createUrlFromParams(abstractRequest.getBaseUrl(), abstractRequest.getParams().urlParamsMap));
        }
        if (abstractRequest.getCacheMode() == null) {
            abstractRequest.cacheMode(CacheMode.NO_CACHE);
        }

        CacheMode cacheMode = abstractRequest.getCacheMode();
        if (cacheMode != CacheMode.NO_CACHE) {
            //noinspection unchecked
            cacheEntity = (CacheEntity<T>) CacheManager.getInstance().get(abstractRequest.getCacheKey());
            HeaderParser.addCacheHeaders(abstractRequest, cacheEntity, cacheMode);
            if (cacheEntity != null && cacheEntity.checkExpire(cacheMode, abstractRequest.getCacheTime(), System.currentTimeMillis())) {
                cacheEntity.setExpire(true);
            }
        }

        if (cacheEntity == null || cacheEntity.isExpire() || cacheEntity.getData() == null || cacheEntity.getResponseHeaders() == null) {
            cacheEntity = null;
        }
        return cacheEntity;
    }

    @Override
    public synchronized okhttp3.Call prepareRawCall() throws Throwable {
        if (executed) {
            throw HttpException.common(ERROR_1);
        }
        executed = true;
        rawCall = abstractRequest.getRawCall();
        if (canceled) {
            rawCall.cancel();
        }
        return rawCall;
    }


    protected Response<T> requestNetworkSync() {
        try {
            okhttp3.Response response = rawCall.execute();
            int responseCode = response.code();

            //network error
            if (responseCode == RESPONSECODE_404 || responseCode >= RESPONSECODE_500) {
                return Response.error(false, rawCall, response, HttpException.netError());
            }

            T body = abstractRequest.getOnConverter().convertResponse(response);
            //save cache when request is successful
            saveCache(response.headers(), body);
            return Response.success(false, body, rawCall, response);
        } catch (Throwable throwable) {
            if (throwable instanceof SocketTimeoutException && currentRetryCount < abstractRequest.getRetryCount()) {
                currentRetryCount++;
                rawCall = abstractRequest.getRawCall();
                if (canceled) {
                    rawCall.cancel();
                } else {
                    requestNetworkSync();
                }
            }
            return Response.error(false, rawCall, null, throwable);
        }
    }

    protected void requestNetworkAsync() {
        rawCall.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                if (e instanceof SocketTimeoutException && currentRetryCount < abstractRequest.getRetryCount()) {
                    //retry when timeout
                    currentRetryCount++;
                    rawCall = abstractRequest.getRawCall();
                    if (canceled) {
                        rawCall.cancel();
                    } else {
                        rawCall.enqueue(this);
                    }
                } else {
                    if (!call.isCanceled()) {
                        Response<T> error = Response.error(false, call, null, e);
                        onError(error);
                    }
                }
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                int responseCode = response.code();

                //network error
                if (responseCode == RESPONSECODE_404 || responseCode >= RESPONSECODE_500) {
                    Response<T> error = Response.error(false, call, response, HttpException.netError());
                    onError(error);
                    return;
                }

                if (onAnalysisResponse(call, response)) {
                    return;
                }

                try {
                    T body = abstractRequest.getOnConverter().convertResponse(response);
                    //save cache when request is successful
                    saveCache(response.headers(), body);
                    Response<T> success = Response.success(false, body, call, response);
                    onSuccess(success);
                } catch (Throwable throwable) {
                    Response<T> error = Response.error(false, call, response, throwable);
                    onError(error);
                }
            }
        });
    }

    /**
     * 请求成功后根据缓存模式，更新缓存数据
     *
     * @param headers 响应头
     * @param data    响应数据
     */
    private void saveCache(Headers headers, T data) {
        if (abstractRequest.getCacheMode() == CacheMode.NO_CACHE) {
            return;    //不需要缓存,直接返回
        }
        if (data instanceof Bitmap) {
            return;             //Bitmap没有实现Serializable,不能缓存
        }

        CacheEntity<T> cache = HeaderParser.createCacheEntity(headers, data, abstractRequest.getCacheMode(), abstractRequest.getCacheKey());
        if (cache == null) {
            //服务器不需要缓存，移除本地缓存
            CacheManager.getInstance().remove(abstractRequest.getCacheKey());
        } else {
            //缓存命中，更新缓存
            CacheManager.getInstance().replace(abstractRequest.getCacheKey(), cache);
        }
    }

    protected void runOnUiThread(Runnable run) {
        OkGo.getInstance().getDelivery().post(run);
    }

    @Override
    public boolean isExecuted() {
        return executed;
    }

    @Override
    public void cancel() {
        canceled = true;
        if (rawCall != null) {
            rawCall.cancel();
        }
    }

    @Override
    public boolean isCanceled() {
        if (canceled) {
            return true;
        }
        synchronized (this) {
            return rawCall != null && rawCall.isCanceled();
        }
    }
}
