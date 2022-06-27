package com.dpad.offerwall;

/**
 * Created by Rocklee on 2017-04-24.
 */

public interface AdComCallBack {
    public void onSuccess(String msg);
    public void onFailed(int code, String msg);
    public void onComplete();
}
