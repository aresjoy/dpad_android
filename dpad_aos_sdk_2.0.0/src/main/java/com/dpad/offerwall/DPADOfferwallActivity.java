package com.dpad.offerwall;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.dpad.offerwall.bean.DPAdInfo;
import com.genius.baselib.PreferenceUtil;
import com.genius.baselib.frame.base.BaseAct;
import com.genius.baselib.frame.center.CStatic;
import com.genius.baselib.frame.util.CTools;
import com.genius.baselib.frame.util.ToastManager;
import com.genius.baselib.frame.view.widget.AutoSwipeRefreshLayout;
import com.genius.baselib.inter.ClickFilter;
import com.genius.baselib.inter.FilterdOnClickListener;
import com.genius.commonrecyclerviewadpater.MultiItemTypeAdapter;
import com.genius.commonrecyclerviewadpater.base.ItemViewDelegate;
import com.genius.commonrecyclerviewadpater.base.ViewHolder;
import com.genius.commonrecyclerviewadpater.wrapper.EmptyWrapper;
import com.genius.commonrecyclerviewadpater.wrapper.HeaderAndFooterWrapper;
import com.genius.utils.Utils_Alert;
import com.sera.volleyhelper.imp.OnRequestCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Rocklee on 2017-04-24.
 */

public class DPADOfferwallActivity extends BaseAct implements View.OnClickListener {
    private TextView dpad_title;
    private ImageView dpad_cancle;

    @Override
    protected int setContentLayoutResID() {
        return R.layout.dpad_activity;
    }

    private void showContent(){
        getSupportFragmentManager().beginTransaction().replace(R.id.contentframe,new DPADOfferwallFragment(),"dpadofferwall").commit();
    }

    @Override
    protected void viewLoadFinished() {
        if (DPAD.checkPermissionAgree(this, new OnRequestCallback() {
            @Override
            public void onResult(boolean result) {
                if (result) {
                     showContent();
                }
            }
        })) {
            long value = PreferenceUtil.getInstance(getApplicationContext()).getValue(CStatic.SP_ENDTIME, 0L);
            if (value != 0L && System.currentTimeMillis() > value) {
                DPAD.showdialog(this, new OnRequestCallback() {
                    @Override
                    public void onResult(boolean result) {
                        if (result) {
                            showContent();
                        }else{
                            finish();
                        }
                    }
                });

            } else {
                showContent();
            }
        }
    }

    @Override
    public void finish() {

        if (PreferenceUtil.getInstance(getApplicationContext()).getValue(CStatic.SP_ALLOW, false)) {
            PreferenceUtil.getInstance(getApplicationContext()).setValue(CStatic.SP_ENDTIME, System.currentTimeMillis() + 3600000L);
        }
        super.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int grant : grantResults) {
            if (grant == PackageManager.PERMISSION_DENIED) {
                finish();
                return;
            }
        }

        if (requestCode == 99) {
            showContent();
        }

    }

    @Override
    protected void initViews() {


        View dpad_title_layout = findViewBId(R.id.dpad_title_layout);
        dpad_title = findViewBId(R.id.dpad_title);
//        id_webview = findViewBId(R.id.id_webview);
//        id_webview.setWebChromeClient(new WebChromeClient());
//        id_webview.setWebViewClient(new WebViewClient());
//        id_webview.getSettings().setJavaScriptEnabled(true);


        dpad_cancle = findViewBId(R.id.dpad_cancle);
        TextView dpad_qaview = findViewBId(R.id.dpad_qaview);
        dpad_cancle.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dpad_qaview.setText(Html.fromHtml(getString(R.string.dpad_qa_txt), 0));
        } else {
            dpad_qaview.setText(Html.fromHtml(getString(R.string.dpad_qa_txt)));
        }
        dpad_qaview.setOnClickListener(new FilterdOnClickListener() {
            @Override
            public void onFilterdClick(View v) {
                Intent intent = new Intent(DPADOfferwallActivity.this, DPADQaActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (getIntent().getExtras() != null) {
                    intent.putExtras(getIntent().getExtras());
                }
                DPADOfferwallActivity.this.startActivity(intent);
            }
        });

        Bundle extras = getIntent().getExtras();
        try {
            if (extras != null) {
                String title = extras.getString("title");
                int tcolor = extras.getInt("tcolor", 0);
                int ccolor = extras.getInt("ccolor", 0);
                int bcolor = extras.getInt("bcolor", 0);
                if (!TextUtils.isEmpty(title)) {
                    dpad_title.setText(title);
                }
                if (tcolor != 0) {
                    dpad_qaview.setTextColor(tcolor);
                    dpad_title.setTextColor(tcolor);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (ccolor != 0) {
                        int[][] states = new int[][]{
                                new int[]{android.R.attr.state_enabled}, // enabled
                                new int[]{-android.R.attr.state_enabled}, // disabled
                                new int[]{-android.R.attr.state_checked}, // unchecked
                                new int[]{android.R.attr.state_pressed}  // pressed
                        };
                        int[] colors = new int[]{
                                ccolor,
                                ccolor,
                                ccolor,
                                ccolor
                        };
                        ColorStateList colorStateList = new ColorStateList(states, colors);
                        dpad_cancle.setImageTintList(colorStateList);

                    }
                }

                if (bcolor != 0) {
                    dpad_title_layout.setBackgroundColor(bcolor);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }





    ClickFilter clickFilter = new ClickFilter();

    @Override
    public void onClick(View v) {
        if (clickFilter.isClicked(v.getId())) return;

        if (v.getId() == R.id.dpad_cancle) {
            finish();
            return;
        }


    }

}
