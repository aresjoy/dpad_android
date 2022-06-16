package com.dpad.offerwall;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.dpad.offerwall.bean.DPAdInfo;
import com.genius.baselib.PreferenceUtil;
import com.genius.baselib.frame.api.DPAdComp_API;
import com.genius.baselib.frame.api.DPAdInit_API;
import com.genius.baselib.frame.api.DPAdList_API;
import com.genius.baselib.frame.api.DPAdPart_API;
import com.genius.baselib.frame.center.CConfig;
import com.genius.baselib.frame.center.CStatic;
import com.genius.baselib.frame.util.AdvertisingIdClient;
import com.genius.baselib.frame.util.CTools;
import com.genius.baselib.frame.util.ToastManager;
import com.genius.utils.Utils_Alert;
import com.sera.volleyhelper.imp.CallBackListener;
import com.sera.volleyhelper.imp.OnRequestCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Rocklee on 2017-04-24.
 */

public class DPAD {

    private static String appId = "";
    private static String userId = "";
    private static String pubid = "";

    public static String getPubid() {
        return pubid;
    }


    public static String getAppId() {
        return appId;
    }


    public static String getUserId() {
        return userId;
    }

    /**
     * UserId설정
     *
     * @param userId
     */
    public static void setUserId(String userId) {
        DPAD.userId = userId;
    }


    /**
     * SDK 버전
     *
     * @return
     */
    public static int getSDKVersion() {
        return 183;//com.dpad.offerwall.BuildConfig.VERSION_CODE;
    }

    /**
     * 문의하기 뷰
     *
     * @param context
     * @return
     */
    public static DpadQaLayout getQaView(Context context) {
        return getQaView(context, null);
    }

    /**
     * 문의하기 뷰
     *
     * @param context
     * @param dpBuilder
     * @return
     */
    public static DpadQaLayout getQaView(Context context, DPBuilder dpBuilder) {
        DpadQaLayout dpadQaLayout = new DpadQaLayout(context, dpBuilder);
        dpadQaLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return dpadQaLayout;
    }

    /**
     * 초기화
     *
     * @param context
     * @param pub_id
     * @param app_Id
     * @param user_id
     */
    public static void init(Context context, String pub_id, String app_Id, String user_id) {
        if (context == null) return;
        userId = user_id;
        pubid = pub_id;
        appId = app_Id;
        String va_a = PreferenceUtil.getInstance(context.getApplicationContext()).getValue(CStatic.APPID, "");
        String va_p = PreferenceUtil.getInstance(context.getApplicationContext()).getValue(CStatic.PUBID, "");
        PreferenceUtil.getInstance(context.getApplicationContext()).setValue(CStatic.PUBID, pubid);
        PreferenceUtil.getInstance(context.getApplicationContext()).setValue(CStatic.APPID, app_Id);
        CConfig.init(context.getApplicationContext());
        getGoogleAdId(context);


        long value = PreferenceUtil.getInstance(context.getApplicationContext()).getValue(CStatic.SP_FIRST_DAY, 0L);
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(System.currentTimeMillis());
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        if (instance.getTimeInMillis() / 1000 - value > 24 * 3600) {

            DPAdInit_API dpAdInit_api = new DPAdInit_API(context);
            dpAdInit_api.request(context, new CallBackListener<DPAdInit_API>() {
                @Override
                public void showloadingUI() {

                }

                @Override
                public void cancleloadingUI() {

                }

                @Override
                public void onResponse(DPAdInit_API dpAdInit_api) {

                }

                @Override
                public void onErrorResponse(DPAdInit_API dpAdInit_api) {

                }
            });

        }
    }


    public static void setUserInfo(Context context, int birth, DPGENDER gender) {
        PreferenceUtil.getInstance(context.getApplicationContext()).setValue(CStatic.BIRTH, birth);
        PreferenceUtil.getInstance(context.getApplicationContext()).setValue(CStatic.GENDER, gender.ordinal());
    }


    /**
     * 오퍼워 보여주기
     *
     * @param activity
     */
    public static void showOfferwall(Activity activity) {
        showOfferwall(activity, null);
    }

    /**
     * Only  23API ,권한체크 <br>
     * 원래는 6.0 이상만 체크하였지만 쇼미때문에 모두 체크하게됨
     *
     * @param manifests
     * @param requestCode
     * @return true means  need request permission
     */
    private static boolean CheckPermission_request(Activity contexts, String[] manifests, int requestCode) {


        String[] requestmanifests = null;
        ArrayList<String> requestmanifests_list = new ArrayList<String>();

        boolean result = false;
        boolean temp = true;
        for (String manifest : manifests) {
            temp = isPermissioned(contexts, manifest);

            if (!temp) {
                requestmanifests_list.add(manifest);
                result = true;
            }
        }

        if (result) {

            requestmanifests = new String[requestmanifests_list.size()];
            for (int i = 0; i < requestmanifests_list.size(); i++) {
                requestmanifests[i] = requestmanifests_list.get(i);
            }

            ActivityCompat.requestPermissions(contexts, requestmanifests, requestCode);
        }

        return result;
    }

    /**
     * 권한부여 여부
     *
     * @param manifest_permission
     * @return
     */
    private static boolean isPermissioned(Activity context, String manifest_permission) {

        try {
            return ActivityCompat.checkSelfPermission(context, manifest_permission) == PackageManager.PERMISSION_GRANTED;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static Fragment getOfferwallFrag(Activity activity) {
        return getOfferwallFrag(activity, null);
    }

    /*public void loadOfferwallFrag(final Activity activity, final DPBuilder dpBuilder, final OnLoadFragmentCallback onLoadFragmentCallback) {

        if (CTools.checkAbuse()) {
            if (onLoadFragmentCallback != null) {
                onLoadFragmentCallback.onResult(null);
            }
            return;
        }

        String[] permissions = new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.GET_ACCOUNTS,
        };


        if (!PreferenceUtil.getInstance(activity.getApplicationContext()).getValue(CStatic.SP_ALLOW, false)) {

            Utils_Alert.showAlertDialog(activity, "개인정보 수집 동의", R.string.dpad_allow_policy, false, android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PreferenceUtil.getInstance(activity.getApplicationContext()).setValue(CStatic.SP_ALLOW, true);
                    if (onLoadFragmentCallback != null) {
                        onLoadFragmentCallback.onResult(getOfferwallFrag(activity, dpBuilder));
                    }
                }
            }, android.R.string.cancel, null, null);
            if (onLoadFragmentCallback != null) {
                onLoadFragmentCallback.onResult(null);
            }
            return;

        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (onLoadFragmentCallback != null) {
                onLoadFragmentCallback.onResult(null);
            }
            return;
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            if (onLoadFragmentCallback != null) {
                onLoadFragmentCallback.onResult(null);
            }
            return;
        }


        DPADOfferwallFragment dpadOfferwallFragment = new DPADOfferwallFragment();
        if (dpBuilder != null) {
            dpadOfferwallFragment.setArguments(dpBuilder.build());
        }
        if (onLoadFragmentCallback != null) {
            onLoadFragmentCallback.onResult(dpadOfferwallFragment);
        }
        return;
    }*/


    public static boolean checkPermissionAgree(final Activity activity,OnRequestCallback onRequestCallback){

        if (!PreferenceUtil.getInstance(activity.getApplicationContext()).getValue(CStatic.SP_ALLOW, false)) {
            showdialog(activity,onRequestCallback);
            return false;
        }
        String[] permissions = new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.GET_ACCOUNTS,
        };

        CheckPermission_request(activity, permissions, 99);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            return false;
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {

            return false;
        }

        return true;

    }

    public static void showdialog(final Activity activity, final OnRequestCallback onRequestCallback) {
        AlertDialog.Builder dialogBuilder = Utils_Alert.getDialogBuilder(activity, 0, 0, false, 0, null, 0, null);
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_dialog,null);
        dialogBuilder.setView(view);
        final AlertDialog alertDialog = dialogBuilder.create();

        view.findViewById(R.id.id_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtil.getInstance(activity.getApplicationContext()).remove(CStatic.SP_ALLOW);
                alertDialog.cancel();
                if(onRequestCallback!=null){
                    onRequestCallback.onResult(false);
                }

                if(activity instanceof DPADOfferwallActivity){
                    activity.finish();
                }
            }
        });
        TextView id_title= (TextView) view.findViewById(R.id.id_title);
        TextView id_content= (TextView) view.findViewById(R.id.id_content);
        id_title.setText("개인정보 수집 동의");
        id_content.setText(Html.fromHtml("(주)오스트빅이 제공하는 충전소 서비스의 이용을 위해 아래의 정보 수집에 대한 동의가 필요합니다.<br>아래 수집된 정보는 충전소 서비스 제공 결과 확인을위한 용도로만 활용되며,서비스 제공을 위한 필수 정보이므로 동의를 부탁드리며,거부하실 경우 해당 서비스를 이용할수 없습니다.<br><br> <font color='#000000'><b>- 구글계정에 등록한 이메일주소<br>(중복참여 방지용도로 사용)<br><br>- imei 및 구글 광고 ID와 같은 기기정보<br>(유저식별 및 중복참여 방지용도로 사용)</b></font><br><br>동의하시겠습니까?"));
        view.findViewById(R.id.id_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtil.getInstance(activity.getApplicationContext()).setValue(CStatic.SP_ALLOW, true);
                String[] permissions = new String[]{
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.GET_ACCOUNTS,
                };
                alertDialog.cancel();
                CheckPermission_request(activity, permissions, 99);

                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    if(onRequestCallback!=null){
                        onRequestCallback.onResult(false);
                    }
                    return  ;
                }
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                    if(onRequestCallback!=null){
                        onRequestCallback.onResult(false);
                    }
                    return  ;
                }


                if(onRequestCallback!=null){
                    onRequestCallback.onResult(true);
                }
            }
        });
        alertDialog.show();
    }

    public static DPADOfferwallFragment getOfferwallFrag(final Activity activity, final DPBuilder dpBuilder) {

        if (CTools.checkAbuse()) {
            ToastManager.showShort(activity, R.string.deny_user);
            return null;
        }

      /*  String[] permissions = new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.GET_ACCOUNTS,
        };


        if (!PreferenceUtil.getInstance(activity.getApplicationContext()).getValue(CStatic.SP_ALLOW, false)) {

            Utils_Alert.showAlertDialog(activity, "개인정보 수집 동의", R.string.dpad_allow_policy, false, android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PreferenceUtil.getInstance(activity.getApplicationContext()).setValue(CStatic.SP_ALLOW, true);
//                    showOfferwall(activity,dpBuilder);
                }
            }, android.R.string.cancel, null, null);
            return null;
        }

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }*/


        DPADOfferwallFragment dpadOfferwallFragment = new DPADOfferwallFragment();
        if (dpBuilder != null) {
            dpadOfferwallFragment.setArguments(dpBuilder.build());
        }
        return dpadOfferwallFragment;
    }


    /**
     * 오퍼워 보여주기
     *
     * @param activity
     * @param dpBuilder 커스터마이징
     */
    public static void showOfferwall(final Activity activity, final DPBuilder dpBuilder) {
        if (activity == null) return;
        if (CTools.checkAbuse()) {
            ToastManager.showShort(activity, R.string.deny_user);
            return;
        }

       /* if (!PreferenceUtil.getInstance(activity.getApplicationContext()).getValue(CStatic.SP_ALLOW, false)) {

            Utils_Alert.showAlertDialog(activity, "개인정보 수집 동의", R.string.dpad_allow_policy, false, android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PreferenceUtil.getInstance(activity.getApplicationContext()).setValue(CStatic.SP_ALLOW, true);
                    showOfferwall(activity, dpBuilder);
                }
            }, android.R.string.cancel, null, null);
            return;
        }


        String[] permissions = new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.GET_ACCOUNTS,
        };

        CheckPermission_request(activity, permissions, 99);

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            ToastManager.showShort(activity, "앱의 기본정보 접근권한이 거부되어, 서비스를 이용할 수 없습니다.권한을 허럭해주세요.");
            return;
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
//            ToastManager.showShort(activity, "앱의 계정상태 접근권한이 거부되어, 서비스를 이용할 수 없습니다.");
            return;
        }*/
       /* if (Build.VERSION.SDK_INT >= 23) {
            if(!Settings.canDrawOverlays(activity)) {
                //can't
                   Utils_Alert.showAlertDialog(activity, 0, "앱위에 그리기 권한이 거부되여 서비스를 이용할수 없습니다.\n 지금 이동하여 켜시겠습니까?", true, android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName()));
//                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        activity.startActivityForResult(intent,232);
                    }
                },android.R.string.cancel,null,null);
                return;

            }
        }*/

      /*  if (!PreferenceUtil.getInstance(activity.getApplicationContext()).getValue(CStatic.SP_ALLOW, false)) {

            Utils_Alert.showAlertDialog(activity, "이용안내", R.string.dpad_allow_policy, false, android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PreferenceUtil.getInstance(activity.getApplicationContext()).setValue(CStatic.SP_ALLOW, true);
                    Intent intent = new Intent(activity, DPADOfferwallActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (dpBuilder != null) {
                        intent.putExtras(dpBuilder.build());
                    }
                    activity.startActivity(intent);
                }
            }, android.R.string.cancel, null, null);
            return;
        }*/
        Intent intent = new Intent(activity, DPADOfferwallActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (dpBuilder != null) {
            intent.putExtras(dpBuilder.build());
        }
        activity.startActivity(intent);
    }


    /**
     * 광고리스트 불러오기
     *
     * @param context
     * @param adListCallBack
     */
    public static void getAdList(Context context, final AdListCallBack adListCallBack) {
        DPAdList_API dpAdList_api = new DPAdList_API(context);
        dpAdList_api.request(context, new CallBackListener<DPAdList_API>() {
            @Override
            public void showloadingUI() {

            }

            @Override
            public void cancleloadingUI() {
                if (adListCallBack != null) {
                    adListCallBack.onComplete();
                }
            }

            @Override
            public void onResponse(DPAdList_API dpAdList_api) {
                if (adListCallBack != null) {
                    adListCallBack.onSuccess(dpAdList_api.dpAdInfoLists);
                }
            }

            @Override
            public void onErrorResponse(DPAdList_API dpAdList_api) {
                if (adListCallBack != null) {
                    adListCallBack.onFailed(dpAdList_api.result.eror, dpAdList_api.result.msg);
                }
            }
        });


    }

    /**
     * 참여하기
     *
     * @param context
     * @param dpAdInfo
     * @param adCallBack
     */
    public static void conFirmPartIn(Context context, DPAdInfo dpAdInfo, final AdPartCallBack adCallBack) {
        DPAdPart_API dpAdPart_api = new DPAdPart_API(context, dpAdInfo);
        dpAdPart_api.request(context, new CallBackListener<DPAdPart_API>() {
            @Override
            public void showloadingUI() {

            }

            @Override
            public void cancleloadingUI() {

                if (adCallBack != null) {
                    adCallBack.onComplete();
                }

            }

            @Override
            public void onResponse(DPAdPart_API dpAdPart_api) {
                if (adCallBack != null) {
                    adCallBack.onSuccess(dpAdPart_api.dpAdInfo);
                }

            }

            @Override
            public void onErrorResponse(DPAdPart_API dpAdPart_api) {
                if (adCallBack != null) {
                    adCallBack.onFailed(dpAdPart_api.result.eror, dpAdPart_api.result.msg);
                }
            }
        });


    }

    /**
     * 완료확인
     *
     * @param context
     * @param dpAdInfo
     * @param adCallBack
     */
    public static void conFirmComplete(Context context, DPAdInfo dpAdInfo, final AdComCallBack adCallBack) {
        DPAdComp_API dpAdComp_api = new DPAdComp_API(context, dpAdInfo);
        dpAdComp_api.request(context, new CallBackListener<DPAdComp_API>() {
            @Override
            public void showloadingUI() {

            }

            @Override
            public void cancleloadingUI() {
                if (adCallBack != null) {
                    adCallBack.onComplete();
                }

            }

            @Override
            public void onResponse(DPAdComp_API dpAdComp_api) {
                if (adCallBack != null) {
                    adCallBack.onSuccess(dpAdComp_api.result.msg);
                }
            }

            @Override
            public void onErrorResponse(DPAdComp_API dpAdComp_api) {
                if (adCallBack != null) {
                    adCallBack.onFailed(dpAdComp_api.result.eror, dpAdComp_api.result.msg);
                }
            }
        });
    }


    private static void getGoogleAdId(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    AdvertisingIdClient.AdInfo advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(context.getApplicationContext());
                    if (advertisingIdInfo != null) {
                        PreferenceUtil.getInstance(context.getApplicationContext()).setValue(CStatic.SP_GID_TRACK, advertisingIdInfo.isLimitAdTrackingEnabled());
                        PreferenceUtil.getInstance(context.getApplicationContext()).setValue(CStatic.SP_GID, advertisingIdInfo.getId() + "");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception ee) {
                    ee.printStackTrace();
                }

            }
        }).start();
    }


}
