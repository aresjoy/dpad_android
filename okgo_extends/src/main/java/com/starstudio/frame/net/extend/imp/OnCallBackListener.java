package com.starstudio.frame.net.extend.imp;

import com.starstudio.frame.net.model.Progress;
import com.starstudio.frame.net.request.base.RequestAbstract;

/**
 * Created by Hongsec on 2016-08-21.
 * email : piaohongshi0506@gmail.com
 * QQ: 251520264
 */
public abstract class  OnCallBackListener<T> {
    public abstract void showloadingUI(RequestAbstract<String, ? extends RequestAbstract> abstractRequest);
    public abstract void cancleloadingUI();
    public abstract void onResponse( );
    public abstract void onErrorResponse( );

    public void uploadProgress(Progress progress){};
}
