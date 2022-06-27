package com.dpad.offerwall;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import com.genius.baselib.inter.FilterdOnClickListener;

/**
 * Created by Rocklee on 2017-04-25.
 */

public class CustomDialog extends Dialog {
    private View.OnClickListener onClickListener;

    public CustomDialog(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public CustomDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);initView(context);
        initView(context);
    }

    protected CustomDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }
    TextView dpad_dialog_content;

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dpad_item_dialog_custom,null);
        dpad_dialog_content = (TextView) view.findViewById(R.id.dpad_dialog_content);
        view.findViewById(R.id.dpad_dialog_cancle).setOnClickListener(new FilterdOnClickListener() {
            @Override
            public void onFilterdClick(View v) {
                CustomDialog.this.cancel();
                if(onClickListener!=null){
                    onClickListener.onClick(v);
                }
            }
        });
        view.findViewById(R.id.dpad_dialog_confirm).setOnClickListener(new FilterdOnClickListener() {
            @Override
            public void onFilterdClick(View v) {
                CustomDialog.this.cancel();
                if(onClickListener!=null){
                    onClickListener.onClick(v);
                }
            }
        });
        setCancelable(false);
        setContentView(view);
    }
    public void setMessage(String message){
        dpad_dialog_content.setText(message);
    }

    public void setonConfirmListener(View.OnClickListener onClickListener){

    this.onClickListener  =onClickListener;
    }
}
