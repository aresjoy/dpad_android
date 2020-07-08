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

import com.starstudio.frame.net.cache.CacheEntity;
import com.starstudio.frame.net.callback.OnCallback;
import com.starstudio.frame.net.exception.CacheException;
import com.starstudio.frame.net.model.Response;
import com.starstudio.frame.net.request.base.RequestAbstract;

import okhttp3.Call;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：2017/5/25
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class DefaultCachePolicy<T> extends BaseCachePolicy<T> {

    public DefaultCachePolicy(RequestAbstract<T, ? extends RequestAbstract> abstractRequest) {
        super(abstractRequest);
    }

    @Override
    public void onSuccess(final Response<T> success) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mOnCallback.onSuccess(success);
                mOnCallback.onFinish();
            }
        });
    }

    @Override
    public void onError(final Response<T> error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mOnCallback.onError(error);
                mOnCallback.onFinish();
            }
        });
    }

    private final int RESPONSECODE_304=304;
    @Override
    public boolean onAnalysisResponse(final Call call, final okhttp3.Response response) {
        if (response.code() != RESPONSECODE_304) {
            return false;
        }

        if (cacheEntity == null) {
            final Response<T> error = Response.error(true, call, response, CacheException.nonAnd304(abstractRequest.getCacheKey()));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mOnCallback.onError(error);
                    mOnCallback.onFinish();
                }
            });
        } else {
            final Response<T> success = Response.success(true, cacheEntity.getData(), call, response);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mOnCallback.onCacheSuccess(success);
                    mOnCallback.onFinish();
                }
            });
        }
        return true;
    }

    @Override
    public Response<T> requestSync(CacheEntity<T> cacheEntity) {
        try {
            prepareRawCall();
        } catch (Throwable throwable) {
            return Response.error(false, rawCall, null, throwable);
        }
        Response<T> response = requestNetworkSync();
        //HTTP cache protocol
        if (response.isSuccessful() && response.code() == RESPONSECODE_304) {
            if (cacheEntity == null) {
                response = Response.error(true, rawCall, response.getRawResponse(), CacheException.nonAnd304(abstractRequest.getCacheKey()));
            } else {
                response = Response.success(true, cacheEntity.getData(), rawCall, response.getRawResponse());
            }
        }
        return response;
    }

    @Override
    public void requestAsync(CacheEntity<T> cacheEntity, OnCallback<T> OnCallback) {
        mOnCallback = OnCallback;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mOnCallback.onStart(abstractRequest);

                try {
                    prepareRawCall();
                } catch (Throwable throwable) {
                    Response.error(false, rawCall, null, throwable);
                    return;
                }
                requestNetworkAsync();
            }
        });
    }
}
