package com.dpad.offerwall;

import android.content.DialogInterface;
import android.content.Intent;
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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.dpad.offerwall.bean.DPAdInfo;
import com.dpad.offerwall.bean.DPAdTabInfo;
import com.genius.baselib.PreferenceUtil;
import com.genius.baselib.base.LazyBaseFragment;
import com.genius.baselib.frame.center.CStatic;
import com.genius.baselib.frame.util.CTools;
import com.genius.baselib.frame.util.ToastManager;
import com.genius.baselib.frame.view.widget.AutoSwipeRefreshLayout;
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

public class DPADPageFragment  extends LazyBaseFragment {



    private AutoSwipeRefreshLayout dpad_autorefresh;
    private RecyclerView dpad_recyclerview;

    MultiItemTypeAdapter<DPAdInfo> adapter;
    private HeaderAndFooterWrapper<DPAdInfo> notifyAdapter;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView( R.layout.dpad_page_fragment);

        DPAdTabInfo tabInfo  = getArguments().getParcelable("data"); //TODO

        dpad_autorefresh = findViewBId(R.id.dpad_autorefresh);
        dpad_autorefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!DPAD.checkPermissionAgree(getActivity(), new OnRequestCallback() {
                    @Override
                    public void onResult(boolean result) {
                        if(result){
                            CallAdList();
                        }
                    }
                })){
                    return ;
                }
                CallAdList();
            }
        });
        dpad_recyclerview = findViewBId(R.id.dpad_recyclerview);
        dpad_recyclerview.setHasFixedSize(false);
        dpad_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));


        adapter = new MultiItemTypeAdapter<DPAdInfo>(getActivity(), new ArrayList<DPAdInfo>());
        adapter.addItemViewDelegate(new Delga());
        adapter.addItemViewDelegate(new Delga2());
        EmptyWrapper<DPAdInfo> emptyAdapter = new EmptyWrapper<DPAdInfo>(adapter);
        View emptyView = LayoutInflater.from(getActivity()).inflate(R.layout.dpad_item_empty, null);
//        int screenHeight = UtilsDensity.getScreenHeight(this);
//        screenHeight = screenHeight - UtilsDensity.dp2px(this, 40);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        TextView id_empty_view = (TextView) emptyView.findViewById(R.id.id_empty_view);
        id_empty_view.setText(getString(R.string.dpad_empty_list));
        emptyAdapter.setEmptyView(emptyView);
        notifyAdapter = new HeaderAndFooterWrapper<>(emptyAdapter);
        DpadQaLayout dpadQaLayout = DPAD.getQaView(getActivity());
        notifyAdapter.addFootView(dpadQaLayout);


        checkPermission();
    }

    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
        checkPermission();
    }

    private void checkPermission() {
        if(DPAD.checkPermissionAgree(getActivity(), new OnRequestCallback() {
            @Override
            public void onResult(boolean result) {
                if (result) {
                    dpad_autorefresh.preformRefresh();
                }
            }
        })){
            long value = PreferenceUtil.getInstance(getActivity().getApplicationContext()).getValue(CStatic.SP_ENDTIME, 0L);
            if (!PreferenceUtil.getInstance(getActivity().getApplicationContext()).getValue(CStatic.SP_ALLOW, false) && value!=0L && System.currentTimeMillis()>value) {
                DPAD.showdialog(getActivity(), new OnRequestCallback() {
                    @Override
                    public void onResult(boolean result) {
                        if (result) {
                            dpad_autorefresh.preformRefresh();
                        }
                    }
                });

            }else{
                dpad_autorefresh.preformRefresh();
            }
        }else{

        }
    }

    @Override
    protected void onDestroyViewLazy() {
        if (PreferenceUtil.getInstance(getActivity().getApplicationContext()).getValue(CStatic.SP_ALLOW, false)) {
            PreferenceUtil.getInstance(getActivity().getApplicationContext()).setValue(CStatic.SP_ENDTIME,System.currentTimeMillis()+3600000L);
        }
        super.onDestroyViewLazy();
    }

    private class Delga implements ItemViewDelegate<DPAdInfo> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.dpad_item_list;
        }

        @Override
        public boolean isForViewType(DPAdInfo item, int position) {
            return true;
        }

        @Override
        public void convert(ViewHolder holder, final DPAdInfo dpAdInfo, final int position) {

            ImageView dpad_item_icon = holder.getView(R.id.dpad_item_icon);
//            if (!TextUtils.isEmpty(dpAdInfo.getIcon()) && getActivity()!=null && getActivity().isFinishing()==false) {
            Glide.with(getActivity()).load(dpAdInfo.getIcon()).transform(
                    new CenterCrop(),
                    new RoundedCorners((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,6f,getResources().getDisplayMetrics()))
            ).into(dpad_item_icon);
//            }
            try {
                holder.setText(R.id.dpad_item_title, dpAdInfo.getTitle()+"");
                holder.setText(R.id.dpad_item_des, dpAdInfo.getDescription()+"");
                holder.setText(R.id.dpad_item_type, dpAdInfo.getRevenue_type_str()+"");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (dpAdInfo.isParted()) {
                holder.setTextColor(R.id.dpad_item_confirm, Color.parseColor("#ffffffff"));
                holder.setBackgroundRes(R.id.dpad_item_confirm, R.drawable.dpad_bg_round_blue_press);
                holder.setText(R.id.dpad_item_confirm, getString(R.string.dpad_confirm));
            } else {
                holder.setBackgroundRes(R.id.dpad_item_confirm, R.drawable.dpad_bg_round_blue_default);
                holder.setTextColor(R.id.dpad_item_confirm, Color.parseColor("#059ce6"));
                holder.setText(R.id.dpad_item_confirm, dpAdInfo.getRwd()+"");
            }

            holder.setOnClickListener(R.id.dpad_item_layout, new FilterdOnClickListener() {
                @Override
                public void onFilterdClick(View v) {
                    ItemClickEvent(dpAdInfo);
                }
            });

            holder.setOnClickListener(R.id.dpad_item_confirm, new FilterdOnClickListener() {
                @Override
                public void onFilterdClick(View v) {
                    ItemClickEvent(dpAdInfo);
                }


            });


        }

        private void ItemClickEvent(final DPAdInfo dpAdInfo) {

            if(dpAdInfo.isParted()){
                if (!dpAdInfo.isParted() || !dpAdInfo.getRevenue_type().toLowerCase().equalsIgnoreCase("cpi")) {
                    //part

                    CallAdPart(dpAdInfo);


                } else {
                    //complete
                    if (TextUtils.isEmpty(dpAdInfo.getPackage_name()) || CTools.isexit(getActivity(), dpAdInfo.getPackage_name()) != null) {
                        // installed or other
                        //  dialog
                        CallAdComplete(dpAdInfo);
                    } else {
                        Utils_Alert.showAlertDialog(getActivity(), 0, "아직 앱 설치가 되지 않았습니다. 스토어로 이동할까요?", false, android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CTools.launchUrlInDefaultBrowser(getActivity(), dpAdInfo.getLanding_url());
                            }
                        },android.R.string.cancel,null,null);

                    }
                }
            }else{

                if(Pattern.matches("^[0-9,.]*\\w$",dpAdInfo.getRwd())) {
                    DPDetailDialog dpDetailDialog = new DPDetailDialog(getActivity(), dpAdInfo);
                    dpDetailDialog.setonConfirmListener(new FilterdOnClickListener() {
                        @Override
                        public void onFilterdClick(View v) {
                            if (!dpAdInfo.isParted() || !dpAdInfo.getRevenue_type().toLowerCase().equalsIgnoreCase("cpi")) {
                                //part

                                CallAdPart(dpAdInfo);


                            } else {
                                //complete
                                if (TextUtils.isEmpty(dpAdInfo.getPackage_name()) || CTools.isexit(getActivity(), dpAdInfo.getPackage_name()) != null) {
                                    // installed or other
                                    //  dialog
                                    CallAdComplete(dpAdInfo);
                                } else {
                                    Utils_Alert.showAlertDialog(getActivity(), 0, "아직 앱 설치가 되지 않았습니다. 스토어로 이동할까요?", false, android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            CTools.launchUrlInDefaultBrowser(getActivity(), dpAdInfo.getLanding_url());
                                        }
                                    }, android.R.string.cancel, null, null);
                                }
                            }
                        }
                    });
                    dpDetailDialog.show();
                }else{
                    if (!dpAdInfo.isParted() || !dpAdInfo.getRevenue_type().toLowerCase().equalsIgnoreCase("cpi")) {
                        //part

                        CallAdPart(dpAdInfo);


                    } else {
                        //complete
                        if (TextUtils.isEmpty(dpAdInfo.getPackage_name()) || CTools.isexit(getActivity(), dpAdInfo.getPackage_name()) != null) {
                            // installed or other
                            //  dialog
                            CallAdComplete(dpAdInfo);
                        } else {
                            Utils_Alert.showAlertDialog(getActivity(), 0, "아직 앱 설치가 되지 않았습니다. 스토어로 이동할까요?", false, android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    CTools.launchUrlInDefaultBrowser(getActivity(), dpAdInfo.getLanding_url());
                                }
                            }, android.R.string.cancel, null, null);
                        }
                    }
                }

            }



          /*  DPDetailDialog dpDetailDialog = new DPDetailDialog(getActivity(), dpAdInfo);
            dpDetailDialog.setonConfirmListener(new FilterdOnClickListener() {
                @Override
                public void onFilterdClick(View v) {

                }
            });
            dpDetailDialog.show();*/

        }

    }

    private int getRPosition(DPAdInfo dpAdInfo) {
        if (dpAdInfo == null) return -1;
        for (int i = 0; i < adapter.getmDatas().size(); i++) {
            if (adapter.getmDatas().get(i).getAdid() == dpAdInfo.getAdid()) {
                return i;
            }
        }
        return -1;
    }

    private void CallAdComplete(final DPAdInfo dpAdInfo) {
        showMessageLoading("완료 확인중 입니다.");
        DPAD.conFirmComplete(getActivity(), dpAdInfo, new AdComCallBack() {
            @Override
            public void onSuccess(String msg) {
//                ToastManager.showShort(getActivity(), "적립 완료되었습니다.");
                if(!TextUtils.isEmpty(msg)){
                    ToastManager.showShort(getActivity(), msg);
                }
                int rPosition = getRPosition(dpAdInfo);
                if (rPosition != -1) {
                    adapter.getmDatas().remove(rPosition);
                    notifyAdapter.notifyItemRemoved(rPosition);
                }
            }

            @Override
            public void onFailed(int code, String msg) {

                if (code == -1) {
                    ToastManager.showShort(getActivity(), " 완료확인 할 수 없습니다.\n" + msg);
                    int rPosition = getRPosition(dpAdInfo);
                    if (rPosition != -1) {
                        adapter.getmDatas().remove(rPosition);
                        notifyAdapter.notifyItemRemoved(rPosition);
                    }
                } else {
                    ToastManager.showShort(getActivity(), "완료 확인 실패했습니다.\n" + msg);
                    CTools.launchUrlInDefaultBrowser(getActivity(), dpAdInfo.getLanding_url());
                }
            }

            @Override
            public void onComplete() {
                cancleMessageLoading();
            }
        });
    }

    private void CallAdPart(final DPAdInfo dpAdInfo) {
        showMessageLoading("참여 신청중 입니다.");
        DPAD.conFirmPartIn(getActivity(), dpAdInfo, new AdPartCallBack() {
            @Override
            public void onSuccess(DPAdInfo dpAdInfo1) {
                CTools.launchUrlInDefaultBrowser(getActivity(), dpAdInfo.getLanding_url());
                int rPosition = getRPosition(dpAdInfo);
                if (rPosition != -1) {
                    adapter.getmDatas().set(rPosition, dpAdInfo1);
                    notifyAdapter.notifyItemChanged(rPosition);
                }

            }

            @Override
            public void onFailed(int code, String msg) {
                if (code == -1) {
                    ToastManager.showShort(getActivity(), " 참여할 수 없습니다." + msg);
                    int rPosition = getRPosition(dpAdInfo);
                    if (rPosition != -1) {
                        adapter.getmDatas().remove(rPosition);
                        notifyAdapter.notifyItemRemoved(rPosition);
                    }
                } else {
                    ToastManager.showShort(getActivity(), "참여신청했으나 실패했습니다." + msg);
                }
            }

            @Override
            public void onComplete() {
                cancleMessageLoading();
            }
        });

    }



    private void CallAdList() {
        DPAD.getAdList(getActivity(), new AdListCallBack() {
            @Override
            public void onSuccess(List<DPAdInfo> dpads) {

                adapter.setmDatas(dpads);

//                DPAdInfo dpAdInfo_t = new DPAdInfo();
//                dpAdInfo_t.setRwd("30P");
//                adapter.getmDatas().add(0,dpAdInfo_t);

                if (dpad_recyclerview.getAdapter() == null) {
                    dpad_recyclerview.setAdapter(notifyAdapter);
                } else {
                    notifyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailed(int code, String msg) {

            }

            @Override
            public void onComplete() {
                if (dpad_autorefresh != null)
                    dpad_autorefresh.setRefreshing(false);
            }
        });


    }


    private class Delga2 implements ItemViewDelegate<DPAdInfo> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.dpad_item_list_beat;
        }

        @Override
        public boolean isForViewType(DPAdInfo item, int position) {
            if(TextUtils.isEmpty(item.getAdid())){
                return true;
            }
            return false;
        }

        @Override
        public void convert(ViewHolder holder, DPAdInfo dpAdInfo, int position) {

            ImageView dpad_item_icon = holder.getView(R.id.dpad_item_icon);
            if (!TextUtils.isEmpty(dpAdInfo.getIcon()) && getActivity()!=null && getActivity().isFinishing()==false) {
                Glide.with(getActivity()).load(dpAdInfo.getIcon()).into(dpad_item_icon);
            }
            boolean value = PreferenceUtil.getInstance(getActivity().getApplicationContext()).getValue(CStatic.SP_BECON, false);
            if(value){
                holder.setText(R.id.dpad_item_title,getString(R.string.dpad_beat_title_on));
                holder.setText(R.id.dpad_item_des,getString(R.string.dpad_beat_des_on));
                holder.getView(R.id.dpad_item_switch).setSelected(true);
            }else{
                holder.setText(R.id.dpad_item_title,getString(R.string.dpad_beat_title_off));
                holder.setText(R.id.dpad_item_des,getString(R.string.dpad_beat_des_off));
                holder.getView(R.id.dpad_item_switch).setSelected(false);
            }

            holder.setOnClickListener(R.id.dpad_item_layout, new FilterdOnClickListener() {
                @Override
                public void onFilterdClick(View v) {

                  /*  boolean value1 = PreferenceUtil.getInstance(getActivity().getApplicationContext()).getValue(CStatic.SP_BECON, false);
                    if(value1){

                        Utils_Alert.showAlertDialog(getActivity(), android.R.string.dialog_alert_title, getString(R.string.dpad_close_beat_alert), false, R.string.always_close_beat, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boolean value1 = PreferenceUtil.getInstance(getActivity().getApplicationContext()).getValue(CStatic.SP_BECON, false);
                                PreferenceUtil.getInstance(getActivity().getApplicationContext()).setValue(CStatic.SP_BECON, !value1);
                                notifyAdapter.notifyDataSetChanged();
                            }
                        },android.R.string.cancel,null,null);

                    }else{
                        ADMXBeaconAdServiceLib.dispose(getActivity());
                        ADMXBeaconAdServiceLib.removeLocationAcceptInfo(getActivity());
                        PreferenceUtil.getInstance(getActivity().getApplicationContext()).setValue(CStatic.SP_BECON, !value1);
                        notifyAdapter.notifyDataSetChanged();
                    }*/

                }
            });


        }
    }
}
