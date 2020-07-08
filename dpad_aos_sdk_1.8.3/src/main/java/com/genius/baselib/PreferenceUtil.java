package com.genius.baselib;

import android.app.Activity;
import android.content.Context;

import java.lang.ref.WeakReference;

import com.genius.utils.UtilsSP;

/**
 * Created by Hongsec on 2016-07-21.
 */
public class PreferenceUtil {

    private  static WeakReference<UtilsSP> utilsSP;

    /**
     *
     * @param context Must Application Context
     * @return
     */
    public  static UtilsSP getInstance(Context context){

        if(context instanceof Activity){
            throw new UnsupportedOperationException("UtilsSP not allowed use activity instance of context!!!!");
        }

        if(utilsSP==null || utilsSP.get()==null){
            utilsSP = new WeakReference<UtilsSP>(new UtilsSP(context));
        }
        return utilsSP.get();
    }

    public static synchronized void setValue(Context context,String key,boolean value){
        getInstance(context).setValue(key,value);
    }

    public static synchronized void setValue(Context context,int key,boolean value){
        getInstance(context).setValue(key,value);
    }


    public static synchronized void setValue(Context context,String key,float value){
        getInstance(context).setValue(key,value);
    }



    public static synchronized void setValue(Context context,int key,float value){
        getInstance(context).setValue(key,value);
    }

    public static synchronized void setValue(Context context,String key,int value){
        getInstance(context).setValue(key,value);
    }

    public static synchronized void setValue(Context context,int key,int value){
        getInstance(context).setValue(key,value);
    }

    public static synchronized void setValue(Context context,String key,long value){
        getInstance(context).setValue(key,value);
    }

    public static synchronized void setValue(Context context,int key,long value){
        getInstance(context).setValue(key,value);
    }

    public static synchronized void setValue(Context context,String key,String value){
        getInstance(context).setValue(key,value);
    }

    public static synchronized void setValue(Context context,int key,String value){
        getInstance(context).setValue(key,value);
    }

    public static synchronized boolean getValue(Context context,String key,boolean value){
       return getInstance(context).getValue(key,value);
    }
    public static synchronized boolean getValue(Context context,int key,boolean value){
       return getInstance(context).getValue(key,value);
    }
    public static synchronized float getValue(Context context,String key,float value){
       return getInstance(context).getValue(key,value);
    }
    public static synchronized float getValue(Context context,int key,float value){
       return getInstance(context).getValue(key,value);
    }
    public static synchronized int getValue(Context context,String key,int value){
       return getInstance(context).getValue(key,value);
    }
    public static synchronized long getValue(Context context,String key,long value){
       return getInstance(context).getValue(key,value);
    }
    public static synchronized String getValue(Context context,String key,String value){
       return getInstance(context).getValue(key,value);
    }
    public static synchronized String getValue(Context context,int key,String value){
       return getInstance(context).getValue(key,value);
    }


    public static synchronized void remove(Context context,String key){
        getInstance(context).remove(key);
    }

    public static synchronized void remove(Context context){
        getInstance(context).clear();
    }

}
