package com.dpad.offerwall;

import com.dpad.offerwall.bean.DPAdInfo;

/**
 * Created by Rocklee on 2017-04-24.
 */

public interface AdPartCallBack {
    public void onSuccess(DPAdInfo dpAdInfo1);
    public void onFailed(int code, String msg);
    public void onComplete();
}
