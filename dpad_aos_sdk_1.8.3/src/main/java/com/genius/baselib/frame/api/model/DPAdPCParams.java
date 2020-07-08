package com.genius.baselib.frame.api.model;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Patterns;

import com.genius.baselib.PreferenceUtil;
import com.genius.baselib.frame.center.CStatic;
import com.genius.baselib.frame.util.CTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

/**
 * Created by Rocklee on 2017-04-24.
 */

public class DPAdPCParams {
    private String gId = "";
    private String imei = "";
    private String androidId = "";
    private String ntype = "";
    private String account = "";
    private String mac = "";
    private String carrier = "";
    private String telecom = "";
    private final boolean gId_track;

    public DPAdPCParams(Context context) {
        gId = PreferenceUtil.getInstance(context.getApplicationContext()).getValue(CStatic.SP_GID, "");
        gId_track = PreferenceUtil.getInstance(context.getApplicationContext()).getValue(CStatic.SP_GID_TRACK,false);
        imei = CTools.getIMEI(context);
        androidId = Settings.Secure.getString((ContentResolver) context.getContentResolver(), Settings.Secure.ANDROID_ID);

        ntype = getWifi(context);
        carrier =getCarrier(context);
        account = getEmailAccount(context);
        mac = getMac(context);
        telecom = existTelecom(context);
    }

    private String existTelecom(Context context){
        boolean tstore = false;
        boolean uplus = false;
        boolean olleh = false;

        if(CTools.isexit(context.getApplicationContext(),"com.skt.skaf.A000Z00040")!=null){
            tstore = true;
        }
        if(CTools.isexit(context.getApplicationContext(),"com.kt.olleh.istore")!=null){
            olleh = true;
        }
        if(CTools.isexit(context.getApplicationContext(),"com.kt.olleh.app.store")!=null){
            olleh = true;
        }
        if(CTools.isexit(context.getApplicationContext(),"com.kt.olleh.storefront")!=null){
            olleh = true;
        }
        if(CTools.isexit(context.getApplicationContext(),"android.lgt.appstore")!=null){
            uplus = true;
        }
        if(CTools.isexit(context.getApplicationContext(),"com.lguplus.appstore")!=null){
            uplus = true;
        }

        String tp = "";
        if(tstore){
            tp = "1";
        }
        if(olleh) {
            if (tp != "") tp += "||";
            tp += "2";
        }
        if(uplus){
            if (tp != "") tp += "||";
            tp += "3";
        }
        return tp;
    }

    private String getEmailAccount(Context context) {
        Account[] accounts = AccountManager.get(context).getAccountsByType("com.google");
        String result = "";
        if (accounts != null && accounts.length > 0) {
            int i = 0;
            Pattern pattern = Patterns.EMAIL_ADDRESS;

            while (i < accounts.length) {
                Account account = accounts[i];
                if (pattern.matcher(account.name).matches()) {
                    result += account.name;
                }
                if (++i != accounts.length) {
                    result += ",";
                }
            }
        }

        return result;
    }

    private String getMac(Context context) {
        WifiManager wifiManager;
        try {
            wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            return wifiManager.getConnectionInfo().getMacAddress();
        } catch (Exception v1) {

        }

        return "";
    }

    private String getCarrier(Context context){
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        return telephonyManager.getSimOperator();
    }

    private static String getWifi(Context context) {
        String ntype = "";
        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(context.getApplicationContext().CONNECTIVITY_SERVICE);
        int SDK_INT = Build.VERSION.SDK_INT;
        if(SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
            if (activeNetwork != null) {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    ntype = "2";
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    ntype = "1";
                }
            }
        } else {
            NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (wifi.isConnected()) {
                ntype = "2";
            } else if(mobile.isConnected()) {
                ntype ="1";
            }
        }
        return ntype;
    }

    public JSONObject getHeaders() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ifa", gId);
            jsonObject.put("imei", imei);
            jsonObject.put("android_id", androidId);
            jsonObject.put("mac_addr", mac);
            jsonObject.put("device_name",Build.MODEL);
            jsonObject.put("carrier", carrier);
            jsonObject.put("mf", Build.MANUFACTURER);
            jsonObject.put("brand", Build.BRAND);
            jsonObject.put("google_ad_id_limited_tracking_enabled",gId_track);
            jsonObject.put("account",account);
            jsonObject.put("ntype",ntype);
            jsonObject.put("telecom",telecom);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
