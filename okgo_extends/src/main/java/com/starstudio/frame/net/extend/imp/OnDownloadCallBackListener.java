package com.starstudio.frame.net.extend.imp;

/**
 * Created by Hongsec on 2016-08-21.
 * email : piaohongshi0506@gmail.com
 * QQ: 251520264
 */
public interface OnDownloadCallBackListener<T> {
    public void showloadingUI();
    public void cancleloadingUI();
    public void onResponse(T t);
    public void update(long read, long count, boolean done);
    public void onErrorResponse(T t);
    public void onResponseString(T t, String response);
}
