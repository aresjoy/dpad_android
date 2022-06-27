package com.genius.baselib.frame.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.genius.baselib.base.BaseAbstractActivity;

/**
 * Created by Hongsec on 2016-09-05.
 */
public abstract class BaseAct extends BaseAbstractActivity {

    protected final long DeLAYUI = 500L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean check_device() {
        boolean result = false;
        result |= Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
        // in xiaomi should return true must
        result |= "xiaomi".equalsIgnoreCase(Build.MANUFACTURER.toLowerCase());
        return result;
    }


    protected  android.app.ProgressDialog progressDialog2 ;

    public void showMessageLoading(String message){
        if( isFinishing()) return;

        try {
            if(progressDialog2==null){
                progressDialog2 = new android.app.ProgressDialog(this);
            }
            progressDialog2.setMessage(message);
            if(!progressDialog2.isShowing()){
                progressDialog2.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void cancleMessageLoading(){
        if( isFinishing()) return;

        try {
            if(progressDialog2==null){
                progressDialog2 = new android.app.ProgressDialog(this);
            }
            if(progressDialog2.isShowing()){
                progressDialog2.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
