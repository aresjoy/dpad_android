package com.genius.baselib.frame.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by hongseok on 2016-10-24.
 */

public class ToastManager {



    public static void showShort(Context context,int str){
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
    public static void showShort(Context context,String str){
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

}
