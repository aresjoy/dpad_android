package com.genius.baselib.frame.base;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Patterns;

import com.dpad.offerwall.DPAD;
import com.genius.baselib.PreferenceUtil;
import com.genius.baselib.frame.center.CConfig;
import com.genius.baselib.frame.center.CStatic;
import com.genius.baselib.frame.util.CTools;
import com.genius.utils.Utils_Alert;
import com.sera.volleyhelper.bean.Result;
import com.starstudio.frame.net.model.HttpMethod;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.regex.Pattern;

import static android.os.Build.MODEL;

/**
 * Created by hongseok on 2017-01-16.
 */

public class CommonApi {

    private String phonenumber = "";


    private String getEmailAccount(Context context) {
        Account[] accounts = AccountManager.get(context).getAccountsByType("com.google");
        if (accounts != null && accounts.length > 0) {
            int i = 0;
            Pattern pattern = Patterns.EMAIL_ADDRESS;
            while (i < accounts.length) {
                Account account = accounts[i];
                if (pattern.matcher(account.name).matches()) {
                    return account.name;
                }
                i++;

            }
        }

        return "";
    }

    String email = "";
    String gId = "";
    String imei = "";
    String appid = "";
    String pubid = "";

    public void init(Context context) {

      /*  if(context instanceof Application){
            throw  new UnsupportedOperationException("API context cant't create with application context");
        }*/

        if (CConfig.is_debug) {
            CConfig.BASE_URL = CStatic.DEV_URL;
        } else {
            CConfig.BASE_URL = CStatic.REAL_URL;
        }




        if (PreferenceUtil.getInstance(context.getApplicationContext()).getValue(CStatic.SP_ALLOW, false)) {
            email = getEmailAccount(context);

//        edid = CTools.getedid(context.getApplicationContext());
            imei = CTools.getIMEI(context);
        }


//        if (CConfig.is_debug) {
//            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            phonenumber = telephonyManager.getSimOperator() + ":" + telephonyManager.getLine1Number() + ":" + telephonyManager.getSimState();
//        }


        appid = DPAD.getAppId();
        if (TextUtils.isEmpty(appid)) {
            appid = PreferenceUtil.getInstance(context.getApplicationContext()).getValue(CStatic.APPID, "");
        }
        pubid = DPAD.getPubid();
        if (TextUtils.isEmpty(pubid)) {
            pubid = PreferenceUtil.getInstance(context.getApplicationContext()).getValue(CStatic.PUBID, "");
        }
    }


    public String getCommonParams() {

        StringBuilder stringBuilder = new StringBuilder();
        try {
//            stringBuilder.append("&");
//            stringBuilder.append("ve=" + version_name);
//            stringBuilder.append("&");
//            stringBuilder.append("bu=" + version);
            stringBuilder.append("&");
            stringBuilder.append("locale=" + URLEncoder.encode(Locale.getDefault().toString(),"utf8"));
            if(!TextUtils.isEmpty(email)){
                stringBuilder.append("&");
                stringBuilder.append("auid=" + URLEncoder.encode(CTools.Encode(email), "utf8"));
            }
            stringBuilder.append("&");
            stringBuilder.append("imei=" + imei);
            stringBuilder.append("&");
            stringBuilder.append("appid=" + appid);
            stringBuilder.append("&");
            stringBuilder.append("pubid=" + pubid);
            stringBuilder.append("&");
            stringBuilder.append("user_id=" + URLEncoder.encode(DPAD.getUserId(), "utf8"));
            stringBuilder.append("&");
            stringBuilder.append("ver=" + com.dpad.offerwall.BuildConfig.VERSION_CODE);
            stringBuilder.append("&");
            stringBuilder.append("mo=" + URLEncoder.encode(MODEL, "utf8"));
            stringBuilder.append("&");
            stringBuilder.append("ov=" + URLEncoder.encode(Build.VERSION.RELEASE, "utf8"));
//            stringBuilder.append("&");
//            stringBuilder.append("ed=" + URLEncoder.encode(CTools.Encode(edid), "utf8"));

           /* if (!TextUtils.isEmpty(ses)) {
                stringBuilder.append("&");
                stringBuilder.append("se=" + ses);
            }*/

            long time = System.currentTimeMillis() / 1000;
            String encode = time + "^aresjoy";
            stringBuilder.append("&");
            stringBuilder.append("mm=" + URLEncoder.encode(CTools.Encode(encode), "utf8"));

         /*   if (!TextUtils.isEmpty(em)) {
                stringBuilder.append("&");
                stringBuilder.append("em=" + em);
            }*/
           /* if (!TextUtils.isEmpty(reg_id)) {
                stringBuilder.append("&");
                stringBuilder.append("re=" + reg_id);
            }*/

            if (!TextUtils.isEmpty(phonenumber)) {
                stringBuilder.append("&");
                stringBuilder.append("pi=" + URLEncoder.encode(phonenumber, "utf8"));
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    public void ErrorStatusProcess(final Context context, int statusCode, String message, Result result) {


        switch (statusCode) {

            case 401://logout

//                CTools.logout(context);
                if (result.eror == -9991) {
                    Utils_Alert.showAlertDialog(context, 0, "pubid 값 잘못됨", false, android.R.string.ok, null, 0, null, null);
                } else if (result.eror == -9992) {
                    Utils_Alert.showAlertDialog(context, 0, "appid 값 잘못됨", false, android.R.string.ok, null, 0, null, null);
                }

                break;
            case 409:

                if (!TextUtils.isEmpty(result.msg)) {
                 /*   try {
                        AnimateDialog.getDialogBuilder(mContext , Effectstype.Slidetop,false).withMessage(result.msg).withButton1Text(mContext .getString(android.R.string.ok), new FilterdOnClickListener() {
                            @Override
                            public void onFilterdClick(View v) {

                            }
                        }).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                    Utils_Alert.showAlertDialog(context, 0, result.msg, false, android.R.string.ok, null, 0, null, null);
                }


                break;

        }

    }

    public void ErrorParsing(Context context, String requestUrl, HttpMethod requestType, JSONObject headers) {

    }


}
