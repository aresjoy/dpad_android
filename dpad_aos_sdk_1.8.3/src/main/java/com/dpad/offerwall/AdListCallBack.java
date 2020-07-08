package com.dpad.offerwall;

import com.dpad.offerwall.bean.DPAdInfo;

import java.util.List;

/**
 * Created by Rocklee on 2017-04-24.
 */

public interface AdListCallBack {
    public void onSuccess(List<DPAdInfo> dpads);
    public void onFailed(int code, String msg);
    public void onComplete();
}
