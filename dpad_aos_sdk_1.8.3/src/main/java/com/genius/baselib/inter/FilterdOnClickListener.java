package com.genius.baselib.inter;

import android.view.View;

/**
 * Created by Hongsec on 2016-08-04.
 */
public abstract class FilterdOnClickListener implements View.OnClickListener {

    private ClickFilter clickFilter = new ClickFilter();

    @Override
    public void onClick(View v) {
        if(clickFilter.isClicked(v.getId())) return;

        onFilterdClick(v);
    }


    public  abstract  void onFilterdClick(View v);
}
