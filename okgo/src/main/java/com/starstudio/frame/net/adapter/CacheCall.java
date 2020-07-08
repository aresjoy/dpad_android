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
package com.starstudio.frame.net.adapter;

import com.starstudio.frame.net.cache.CacheEntity;
import com.starstudio.frame.net.cache.policy.OnCachePolicy;
import com.starstudio.frame.net.cache.policy.DefaultCachePolicy;
import com.starstudio.frame.net.cache.policy.FirstCacheRequestPolicy;
import com.starstudio.frame.net.cache.policy.NoCachePolicy;
import com.starstudio.frame.net.cache.policy.NoneCacheRequestPolicy;
import com.starstudio.frame.net.cache.policy.RequestFailedCachePolicy;
import com.starstudio.frame.net.callback.OnCallback;
import com.starstudio.frame.net.model.Response;
import com.starstudio.frame.net.request.base.RequestAbstract;
import com.starstudio.frame.net.utils.HttpUtils;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：2016/9/11
 * 描    述：带缓存的请求
 * 修订历史：
 * ================================================
 */
public class CacheCall<T> implements OnCall<T> {

    private OnCachePolicy<T> policy = null;
    private RequestAbstract<T, ? extends RequestAbstract> abstractRequest;

    public CacheCall(RequestAbstract<T, ? extends RequestAbstract> abstractRequest) {
        this.abstractRequest = abstractRequest;
        this.policy = preparePolicy();
    }

    @Override
    public Response<T> execute() {
        CacheEntity<T> cacheEntity = policy.prepareCache();
        return policy.requestSync(cacheEntity);
    }

    @Override
    public void execute(OnCallback<T> OnCallback) {
        HttpUtils.checkNotNull(OnCallback, "callback == null");

        CacheEntity<T> cacheEntity = policy.prepareCache();
        policy.requestAsync(cacheEntity, OnCallback);
    }

    private OnCachePolicy<T> preparePolicy() {
        switch (abstractRequest.getCacheMode()) {
            case DEFAULT:
                policy = new DefaultCachePolicy<>(abstractRequest);
                break;
            case NO_CACHE:
                policy = new NoCachePolicy<>(abstractRequest);
                break;
            case IF_NONE_CACHE_REQUEST:
                policy = new NoneCacheRequestPolicy<>(abstractRequest);
                break;
            case FIRST_CACHE_THEN_REQUEST:
                policy = new FirstCacheRequestPolicy<>(abstractRequest);
                break;
            case REQUEST_FAILED_READ_CACHE:
                policy = new RequestFailedCachePolicy<>(abstractRequest);
                break;
            default:
                policy = new DefaultCachePolicy<>(abstractRequest);
                break;
        }
        if (abstractRequest.getOnCachePolicy() != null) {
            policy = abstractRequest.getOnCachePolicy();
        }
        HttpUtils.checkNotNull(policy, "policy == null");
        return policy;
    }

    @Override
    public boolean isExecuted() {
        return policy.isExecuted();
    }

    @Override
    public void cancel() {
        policy.cancel();
    }

    @Override
    public boolean isCanceled() {
        return policy.isCanceled();
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public OnCall<T> clone() {
        return new CacheCall<>(abstractRequest);
    }

    public RequestAbstract getAbstractRequest() {
        return abstractRequest;
    }
}
