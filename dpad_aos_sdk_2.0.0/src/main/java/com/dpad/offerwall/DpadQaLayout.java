package com.dpad.offerwall;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.genius.baselib.inter.FilterdOnClickListener;

/**
 * Created by Rocklee on 2017-04-24.
 */

public class DpadQaLayout extends RelativeLayout {

    public DpadQaLayout(Context context,DPBuilder dpBuilder) {
        super(context);
        initView(context,dpBuilder);
    }

    private void initView(final Context context, final DPBuilder dpBuilder) {
        final View inflate = LayoutInflater.from(context).inflate(R.layout.dpad_offerwall_head, null);
        TextView dpad_qa = (TextView) inflate.findViewById(R.id.dpad_qa);
        TextView dpad_qa_version = (TextView) inflate.findViewById(R.id.dpad_qa_version);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dpad_qa.setText(Html.fromHtml(context.getString(R.string.dpad_qa_txt),0));
        }else{
            dpad_qa.setText(Html.fromHtml(context.getString(R.string.dpad_qa_txt)));
        }
        dpad_qa_version.setText(context.getString(R.string.dpad_vs,""+ DPAD.getSDKVersion()+""));

        inflate.findViewById(R.id.dpad_qa_layout).setOnClickListener(new FilterdOnClickListener() {
            @Override
            public void onFilterdClick(View v) {

                Intent intent  = new Intent(context,DPADQaActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if(dpBuilder!=null){
                    intent.putExtras(dpBuilder.build());
                }
                context.startActivity(intent);

            }
        });
        addView(inflate);

    }

    public DpadQaLayout(Context context, AttributeSet attrs,DPBuilder dpBuilder) {
        super(context, attrs);
        initView(context,dpBuilder);
    }

    public DpadQaLayout(Context context, AttributeSet attrs, int defStyleAttr,DPBuilder dpBuilder) {
        super(context, attrs, defStyleAttr);
        initView(context,dpBuilder);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DpadQaLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes,DPBuilder dpBuilder) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context,dpBuilder);
    }



}
