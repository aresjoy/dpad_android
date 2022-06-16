package com.dpad.offerwall;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import com.bumptech.glide.Glide;
import com.dpad.offerwall.bean.DPAdInfo;
import com.genius.baselib.inter.FilterdOnClickListener;


/**
 * Created by Rocklee on 2017-04-25.
 */

public class DPDetailDialog extends Dialog {


    protected DPDetailDialog(@NonNull Context context, DPAdInfo dpAdInfo) {
        super(context);
        initView(context, dpAdInfo);
    }

    protected DPDetailDialog(@NonNull Context context, @StyleRes int themeResId, DPAdInfo dpAdInfo) {
        super(context, themeResId);
        initView(context, dpAdInfo);
    }

    protected DPDetailDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, DPAdInfo dpAdInfo) {
        super(context, cancelable, cancelListener);
        initView(context, dpAdInfo);
    }

    TextView dpad_dialog_confirm;

    private void initView(Context context, DPAdInfo dpAdInfo) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(R.layout.dpad_item_dialog, null);
        View dpad_dialog_cancle = view.findViewById(R.id.dpad_dialog_cancle);
        ImageView dpad_dialog_logo = (ImageView) view.findViewById(R.id.dpad_dialog_logo);
        TextView dpad_dialog_title = (TextView) view.findViewById(R.id.dpad_dialog_title);
        TextView dpad_dialog_detail = (TextView) view.findViewById(R.id.dpad_dialog_detail);
        TextView dpad_dialog_type = (TextView) view.findViewById(R.id.dpad_dialog_type);
        TextView dpad_dialog_content = (TextView) view.findViewById(R.id.dpad_dialog_content);
        dpad_dialog_confirm = (TextView) view.findViewById(R.id.dpad_dialog_confirm);
        dpad_dialog_cancle.setOnClickListener(new FilterdOnClickListener() {
            @Override
            public void onFilterdClick(View v) {
                DPDetailDialog.this.cancel();
            }
        });
        if (dpAdInfo != null) {
            if (!TextUtils.isEmpty(dpAdInfo.getIcon())) {
                Glide.with(context).load(dpAdInfo.getIcon()).into(dpad_dialog_logo);
            }
            dpad_dialog_detail.setText(dpAdInfo.getDescription());
            dpad_dialog_title.setText(dpAdInfo.getTitle());
            dpad_dialog_type.setText(dpAdInfo.getRevenue_type_str());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(dpAdInfo.getAction_description());
            stringBuilder.append("\n\n");
            stringBuilder.append("[상세소개]");
            stringBuilder.append("\n");
            stringBuilder.append("- " + dpAdInfo.getDescription());
            dpad_dialog_content.setText(stringBuilder.toString());
            if (dpAdInfo.isParted()) {
                dpad_dialog_confirm.setText(dpAdInfo.getRwd() + " 적립하기");
            } else {
                dpad_dialog_confirm.setText("참여하기");
            }
        }
        dpad_dialog_confirm.setOnClickListener(new FilterdOnClickListener() {
            @Override
            public void onFilterdClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(v);
                }
                DPDetailDialog.this.cancel();
            }
        });

        setCancelable(false);
        setContentView(view);
    }

    View.OnClickListener onClickListener;

    public void setonConfirmListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
