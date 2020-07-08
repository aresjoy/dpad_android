package com.genius.baselib.center;

import com.genius.utils.UtilsLog;

/**
 * Created by Hongsec on 2016-08-01.
 */
public class Config {


    /**
     *
     * @param isshowlog 是否显示log
     * @param default_logmsg 默认的logmessage
     */
    public static  void init(boolean isshowlog,String default_logmsg){

        //是否在每个界面检测网路状态
        UtilsLog.init(isshowlog,default_logmsg);

    }


}
