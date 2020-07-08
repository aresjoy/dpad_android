package com.starstudio.frame.net.request.base;

import com.starstudio.frame.net.utils.HttpUtils;

import okhttp3.RequestBody;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：2017/6/21
 * 描    述：
 * 修订历史：
 * ================================================
 */
public abstract class NoBodyRequestAbstract<T, R extends NoBodyRequestAbstract> extends RequestAbstract<T, R> {
    private static final long serialVersionUID = 1200621102761691196L;

    public NoBodyRequestAbstract(String url) {
        super(url);
    }

    @Override
    public RequestBody generateRequestBody() {
        return null;
    }

    protected okhttp3.Request.Builder generateRequestBuilder(RequestBody requestBody) {
        url = HttpUtils.createUrlFromParams(baseUrl, params.urlParamsMap);
        okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder();
        return HttpUtils.appendHeaders(requestBuilder, headers);
    }
}
