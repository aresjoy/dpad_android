package com.dpad.offerwall;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

/**
 * Created by Rocklee on 2017-04-24.
 */

public class DPBuilder {

    private String title= "";
    private int tcolor= 0;
    private int ccolor= 0;
    private int bcolor= 0;
    public DPBuilder setTitle(String title){
        this.title = title;
        return this;
    }
    public DPBuilder setTitleColor(int tcolor){
        this.tcolor = tcolor;
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DPBuilder setCancleColor(int ccolor){
        this.ccolor = ccolor;
        return this;
    }

    public DPBuilder setTitleBackgroundColor(int bcolor){
        this.bcolor = bcolor;
        return this;
    }

    public Bundle build(){
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        bundle.putInt("tcolor",tcolor);
        bundle.putInt("ccolor",ccolor);
        bundle.putInt("bcolor",bcolor);
        return bundle;
    }
}
