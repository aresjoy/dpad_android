package com.dpad.offerwall;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.dpad.offerwall.bean.QaInfo;
import com.genius.baselib.frame.api.DPQA_API;
import com.genius.baselib.frame.base.BaseAct;
import com.genius.baselib.frame.db.DB_Offerwall_CompAds;
import com.genius.baselib.frame.db.DB_Offerwall_History;
import com.genius.baselib.frame.db.DB_Offerwall_PartAds;
import com.genius.baselib.frame.db.HistoryItem;
import com.genius.baselib.frame.util.ToastManager;
import com.genius.baselib.inter.FilterdOnClickListener;
import com.genius.utils.UtilsDensity;
import com.genius.utils.Utils_Alert;
import com.sera.volleyhelper.imp.CallBackListener;

import java.util.List;

/**
 * Created by Rocklee on 2017-04-24.
 */

public class DPADQaActivity extends BaseAct {
    private TextView dpad_qa_servicename;

    private boolean seletec_flag = false;

    @Override
    protected int setContentLayoutResID() {
        return R.layout.dpad_qa_activity;
    }
    List<HistoryItem> allIndex;
    @Override
    protected void viewLoadFinished() {


    }

    HistoryItem historyItem= new HistoryItem() ;


    EditText dpad_qa_email, dpad_qa_phone, dpad_qa_adtitle, dpad_qa_message, dpad_qa_username;
    CheckBox dpad_qa_check;

    @Override
    protected void initViews() {

        View dpad_title_layout = findViewBId(R.id.dpad_title_layout);
        TextView dpad_title = findViewBId(R.id.dpad_title);
        ImageView dpad_cancle = findViewBId(R.id.dpad_cancle);

        findViewBId(R.id.dpad_qa_history).setOnClickListener(new FilterdOnClickListener() {
            @Override
            public void onFilterdClick(View v) {
                Intent intent = new Intent(DPADQaActivity.this, DPADRewardHistory.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (getIntent().getExtras() != null) {
                    intent.putExtras(getIntent().getExtras());
                }
                startActivity(intent);

            }
        });
        dpad_cancle.setOnClickListener(new FilterdOnClickListener() {
            @Override
            public void onFilterdClick(View v) {
                DPADQaActivity.super.finish();
            }
        });
        dpad_qa_email = findViewBId(R.id.dpad_qa_email);
        dpad_qa_phone = findViewBId(R.id.dpad_qa_phone);
        dpad_qa_adtitle = findViewBId(R.id.dpad_qa_adtitle);
        dpad_qa_message = findViewBId(R.id.dpad_qa_message);
        dpad_qa_username = findViewBId(R.id.dpad_qa_username);

        dpad_qa_servicename = findViewBId(R.id.dpad_qa_servicename);
        dpad_qa_servicename.setOnClickListener(new FilterdOnClickListener() {
            @Override
            public void onFilterdClick(View v) {
                DB_Offerwall_History db_offerwall_history = new DB_Offerwall_History(DPADQaActivity.this,null);
                db_offerwall_history.openDB();
                allIndex = db_offerwall_history.getAllIndex(DPADQaActivity.this);
                db_offerwall_history.closeDB();

                String[] titles = new String[allIndex.size() + 1];
                titles[0] = "기타";
                for (int i = 0; i < allIndex.size(); i++) {
                    titles[i+1] = allIndex.get(i).getTitle();
                }

                AlertDialog.Builder dialogBuilder = Utils_Alert.getDialogBuilder(DPADQaActivity.this, 0, 0, true, 0, null, 0, null);
                dialogBuilder.setItems(titles, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        seletec_flag = true;
                        if(which!=0){
                            historyItem = allIndex.get(which-1);
                            dpad_qa_servicename.setText(historyItem.getTitle());
                        }else{
                            historyItem = new HistoryItem();
                            dpad_qa_servicename.setText("기타");
                        }


                    }
                });
                dialogBuilder.create().show();

            }
        });

        dpad_qa_check = findViewBId(R.id.dpad_qa_check);

        findViewBId(R.id.dpad_qa_confirm).setOnClickListener(new FilterdOnClickListener() {
            @Override
            public void onFilterdClick(View v) {

                if(!seletec_flag){
                    ToastManager.showShort(DPADQaActivity.this, "문의할 서비스명을 선택 해주세요.");
                    return;
                }

                if (!dpad_qa_check.isChecked()) {
                    ToastManager.showShort(DPADQaActivity.this, "개인정보수집을 동의 해주세요.");
                    return;
                }

                if (TextUtils.isEmpty(dpad_qa_username.getText().toString())) {
                    ToastManager.showShort(DPADQaActivity.this, "이름을 입력해주세요.");
                    return;
                }
                if (TextUtils.isEmpty(dpad_qa_email.getText().toString())) {
                    ToastManager.showShort(DPADQaActivity.this, "이메일을 입력해주세요.");
                    return;
                }
                if (TextUtils.isEmpty(dpad_qa_phone.getText().toString())) {
                    ToastManager.showShort(DPADQaActivity.this, "휴대폰 번호를 입력해주세요.");
                    return;
                }
             /*   if (TextUtils.isEmpty(dpad_qa_adtitle.getText().toString())) {
                    ToastManager.showShort(DPADQaActivity.this, "광고제목을 입력해주세요.");
                    return;
                }*/

                if (TextUtils.isEmpty(dpad_qa_message.getText().toString())) {
                    ToastManager.showShort(DPADQaActivity.this, "문의 내용을 입력해주세요.");
                    return;
                }
                QaInfo qaInfo = new QaInfo();

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("문의내용:\n" + dpad_qa_message.getText().toString() + "\n\n\n");
                stringBuilder.append("MODEL:" + Build.MODEL + "\n");
                stringBuilder.append("SDK_INT:" + Build.VERSION.SDK_INT + "\n");
                stringBuilder.append("SDK_VERSION:" + DPAD.getSDKVersion() + "\n");
                DB_Offerwall_CompAds db_offerwall_compAds = new DB_Offerwall_CompAds(DPADQaActivity.this, null);
                DB_Offerwall_PartAds db_offerwall_partAds = new DB_Offerwall_PartAds(DPADQaActivity.this, null);
                db_offerwall_compAds.openDB();
                db_offerwall_partAds.openDB();
                List<String> allIndex_camp = db_offerwall_compAds.getAllIndex_Camp(DPADQaActivity.this);
                stringBuilder.append("Completed DB (camp_ids):" + "\n");
                for (String camp : allIndex_camp) {
                    stringBuilder.append(camp + ",");
                }
                List<String> allIndex_camp1 = db_offerwall_partAds.getAllIndex_Camp(DPADQaActivity.this);
                stringBuilder.append("Parted DB (camp_ids):" + "\n");
                for (String camp : allIndex_camp1) {
                    stringBuilder.append(camp + ",");
                }
                db_offerwall_compAds.closeDB();
                db_offerwall_partAds.closeDB();
                qaInfo.setBody(stringBuilder.toString());
                qaInfo.setPhone(dpad_qa_phone.getText().toString());
                qaInfo.setEmail(dpad_qa_email.getText().toString());
                qaInfo.setName(dpad_qa_username.getText().toString());
                qaInfo.setTitle(historyItem.getTitle());
                qaInfo.setAdid(historyItem.getAdid());
                qaInfo.setPartid(historyItem.getPartId());
                CallQaAPI(qaInfo);
                UtilsDensity.hideKeyboard(DPADQaActivity.this);

            }
        });


        Bundle extras = getIntent().getExtras();
        try {
            if (extras != null) {
                String title = extras.getString("title");
                int tcolor = extras.getInt("tcolor", 0);
                int ccolor = extras.getInt("ccolor", 0);
                int bcolor = extras.getInt("bcolor", 0);

                if (tcolor != 0) {
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

    private void CallQaAPI(QaInfo qaInfo) {
        showMessageLoading("접수 신청중");
        DPQA_API dpqa_api = new DPQA_API(this, qaInfo);
        dpqa_api.request(this, new CallBackListener<DPQA_API>() {
            @Override
            public void showloadingUI() {

            }

            @Override
            public void cancleloadingUI() {
                cancleMessageLoading();
            }

            @Override
            public void onResponse(DPQA_API dpqa_api) {

                CustomDialog customDialog = new CustomDialog(DPADQaActivity.this);
                customDialog.setMessage("문의내용이 접수되었습니다.영업일 기준 2일 이내에 이메일로 회신드리겠습니다.");
                customDialog.setonConfirmListener(new FilterdOnClickListener() {
                    @Override
                    public void onFilterdClick(View v) {
                        DPADQaActivity.super.finish();
                    }
                });
                customDialog.show();
            }

            @Override
            public void onErrorResponse(DPQA_API dpqa_api) {
                ToastManager.showShort(DPADQaActivity.this, "문의 접수 실패하었습니다.");
            }
        });


    }


}
