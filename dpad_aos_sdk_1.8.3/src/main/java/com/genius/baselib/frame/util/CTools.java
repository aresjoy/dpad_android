package com.genius.baselib.frame.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.core.app.ActivityCompat;

import com.genius.baselib.frame.center.CStatic;
import com.genius.baselib.frame.db.DB_Error;
import com.genius.utils.UtilsLog;
import com.genius.utils.encode.AES256Cipher;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


/**
 * Created by Hongsec on 2016-09-05.
 */
public class CTools {



    public static boolean checkAbuse() {
        String[] var3 = new String[]{"/system/bin/su", "/system/xbin/su", "/system/app/SuperUser.apk", "/data/data/com.noshufou.android.su"};
        int var2 = var3.length;

        for(int var1 = 0; var1 < var2; ++var1) {
            if(isExistFile(var3[var1])) {
                return true;
            }
        }

        return false;
    }

    private static boolean isExistFile(String var0) {
        File var1;
        return (var1 = new File(var0)).exists() && var1.isFile();
    }

    public static boolean launchUrlInDefaultBrowser(Context context, String url) {
        if (context == null || TextUtils.isEmpty(url))
            return false;

        try {
            final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // browserIntent.setData(Uri.parse(url));

            // 1: Try to find the default browser and launch the URL with it
            final ResolveInfo defaultResolution = context.getPackageManager().resolveActivity(browserIntent, PackageManager.MATCH_DEFAULT_ONLY);
            if (defaultResolution != null) {
                final ActivityInfo activity = defaultResolution.activityInfo;
                if (!activity.name.equals("com.android.internal.app.ResolverActivity")) {

                    browserIntent.setClassName(activity.applicationInfo.packageName, activity.name);
                    context.startActivity(browserIntent);

                    return true;
                }

            }
            // 2: Try to find anything that we can launch the URL with. Pick up the
            // first one that can.
            final List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(browserIntent, PackageManager.MATCH_DEFAULT_ONLY);
            if (!resolveInfoList.isEmpty()) {
                browserIntent.setClassName(resolveInfoList.get(0).activityInfo.packageName, resolveInfoList.get(0).activityInfo.name);
                context.startActivity(browserIntent);
                return true;
            }
        } catch (Exception e) {
            try {
                final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(browserIntent);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return true;
        }
        return false;
    }


    public static boolean canDownloadState(Context context) {
        try {
            int state = context.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");

            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }



    /**
     * 安装应用
     *
     * @param path
     */
    public static void installapk(Context context, String path) {
        if(context == null) return;
        try {
            UtilsLog.v( "installapk path:" + path);
            UtilsLog.v("installapk file path:" + new File(path));

            Intent intent = new Intent();
//        String filepath="/SDCard/XXX.apk";
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static String getedid(Context context) {

        //imei , mac , androidid

        String deviceId = "";
        String sn = "";

         deviceId = getIMEI(context);

        sn = android.os.Build.SERIAL;

        String androidid = "";

        try {
            androidid = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //  device +  serial + androidid
        String result = "" + deviceId + sn + androidid;
        try {
            return UUID.nameUUIDFromBytes(result.getBytes("utf8")).toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";

    }


    /**
     * Get Imei
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        String deviceId = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if(Build.VERSION.SDK_INT>28){
                String androidId = Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);;
                deviceId = androidId;
            }else {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return "";
                }

                deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return deviceId;
    }



    public static Intent isexit(Context context, String pk_name){
        PackageManager packageManager = context.getPackageManager();
        Intent it= packageManager.getLaunchIntentForPackage(pk_name);
        return it;
    }

    public static void SaveError(Context context,String error){

        DB_Error db_error = new DB_Error(context, null);
        db_error.openDB();
        db_error.pushApp(error);
        db_error.closeDB();
    }

    public static void SaveError(Context context, Throwable th) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);

        Throwable casue = th;

        while (casue != null) {
            casue.printStackTrace(printWriter);
            casue = casue.getCause();
        }

        String stacktraceAsString = "";
        try {
            stacktraceAsString = result.toString();
            printWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SaveError(context, stacktraceAsString);
    }



    private static byte[] readInputStream(InputStream inputStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        inputStream.close();
        return outputStream.toByteArray();
    }




    public static String Encode(String value){

        String result = "";
        try {
            if(!TextUtils.isEmpty(value))
            result = AES256Cipher.AES_Encode(value, CStatic.KEY);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return result.trim();
    }


}
