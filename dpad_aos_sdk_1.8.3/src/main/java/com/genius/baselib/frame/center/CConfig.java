package com.genius.baselib.frame.center;

import android.content.Context;

import com.genius.baselib.center.Config;

/**
 * Created by Hongsec on 2016-09-05.
 */
public class CConfig {
    //TODO
    public static boolean is_debug = false;
    public static boolean is_log = false;

    public static String BASE_URL = "";
    public static String BASE_WEB_URL="";

    public static void init(Context context){

        Config.init(is_log,"I will be back");
        if(is_debug){
            BASE_WEB_URL = CStatic.DEV_WEB_URL;
        }else{
            BASE_WEB_URL = CStatic.REAL_WEB_URL;
        }

    }

}
