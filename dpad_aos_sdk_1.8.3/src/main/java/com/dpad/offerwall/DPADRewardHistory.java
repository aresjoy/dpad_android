package com.dpad.offerwall;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dpad.offerwall.bean.HistoryInfo;
import com.genius.baselib.frame.api.DPAdHistory_API;
import com.genius.baselib.frame.base.BaseAct;
import com.genius.baselib.frame.util.ToastManager;
import com.genius.baselib.frame.view.widget.AutoSwipeRefreshLayout;
import com.genius.baselib.inter.ClickFilter;
import com.genius.commonrecyclerviewadpater.MultiItemTypeAdapter;
import com.genius.commonrecyclerviewadpater.base.ItemViewDelegate;
import com.genius.commonrecyclerviewadpater.base.ViewHolder;
import com.genius.commonrecyclerviewadpater.wrapper.EmptyWrapper;
import com.genius.commonrecyclerviewadpater.wrapper.HeaderAndFooterWrapper;
import com.sera.volleyhelper.imp.CallBackListener;

import java.util.ArrayList;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;

/**
 * Created by Rocklee on 2017-04-25.
 */

public class DPADRewardHistory extends BaseAct implements View.OnClickListener {

    private TextView dpad_title;
    private AutoSwipeRefreshLayout dpad_autorefresh;
    private RecyclerView dpad_recyclerview;

    MultiItemTypeAdapter<HistoryInfo> adapter;
    private HeaderAndFooterWrapper<HistoryInfo> notifyAdapter;
    private LinearLayoutManager layoutmanager;


    @Override
    protected int setContentLayoutResID() {
        return R.layout.dpad_reward_activity;
    }

    @Override
    protected void viewLoadFinished() {
        dpad_autorefresh.preformRefresh();
    }

    @Override
    protected void initViews() {
        View dpad_title_layout = findViewBId(R.id.dpad_title_layout);
        dpad_title = findViewBId(R.id.dpad_title);
        ImageView dpad_cancle = findViewBId(R.id.dpad_cancle);
        TextView dpad_qaview = findViewBId(R.id.dpad_qaview);
        dpad_cancle.setOnClickListener(this);
        dpad_autorefresh = findViewBId(R.id.dpad_autorefresh);
        dpad_autorefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CallHistoryList(0);
            }
        });
        dpad_recyclerview = findViewBId(R.id.dpad_recyclerview);
        dpad_recyclerview.setHasFixedSize(false);
        dpad_recyclerview.setLayoutManager(layoutmanager = new LinearLayoutManager(this));
        dpad_recyclerview.addOnScrollListener(onScrollListener);
        adapter = new MultiItemTypeAdapter<HistoryInfo>(this, new ArrayList<HistoryInfo>());
        adapter.addItemViewDelegate(new Delga());
        EmptyWrapper<HistoryInfo> emptyAdapter = new EmptyWrapper<HistoryInfo>(adapter);
        View emptyView = LayoutInflater.from(this).inflate(R.layout.dpad_item_empty, null);
//        int screenHeight = UtilsDensity.getScreenHeight(this);
//        screenHeight = screenHeight - UtilsDensity.dp2px(this, 40);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        TextView id_empty_view = (TextView) emptyView.findViewById(R.id.id_empty_view);
        id_empty_view.setText("적립이력이 없습니다.");
        emptyAdapter.setEmptyView(emptyView);
        notifyAdapter = new HeaderAndFooterWrapper<>(emptyAdapter);
       View view = LayoutInflater.from(this).inflate(R.layout.dpad_item_history_title,null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        notifyAdapter.addHeaderView(view);

        Bundle extras = getIntent().getExtras();
        try {
            if (extras != null) {
                String title = extras.getString("title");
                int tcolor = extras.getInt("tcolor", 0);
                int ccolor = extras.getInt("ccolor", 0);
                int bcolor = extras.getInt("bcolor", 0);

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

    private boolean isLoading = true;
    private boolean isLastPage = true;

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            /*if(dy>0){
                ViewAnimator.animate(flaating_main_contents).fadeOut();
            }else{
                ViewAnimator.animate(flaating_main_contents).fadeIn();
            }*/

            int visibleItemCount = layoutmanager.getChildCount();
            int firstVisibleItemPosition = layoutmanager.findFirstVisibleItemPosition();
            int totalItemCount = layoutmanager.getItemCount();

            if (!isLoading && !isLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= PAGE_SIZE) {
                    CallHistoryList(adapter.getDatas().size());

                }
            }

        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);


        }
    };


    ClickFilter clickFilter = new ClickFilter();

    @Override
    public void onClick(View v) {
        if (clickFilter.isClicked(v.getId())) return;

        if (v.getId() == R.id.dpad_cancle) {
            super.finish();
            return;
        }
    }


    private void CallHistoryList(final int size) {
        DPAdHistory_API dpAdHistory_api = new DPAdHistory_API(this, size);
        dpAdHistory_api.request(this, new CallBackListener<DPAdHistory_API>() {
            @Override
            public void showloadingUI() {

            }

            @Override
            public void cancleloadingUI() {

                if (dpad_autorefresh != null)
                    dpad_autorefresh.setRefreshing(false);

            }

            @Override
            public void onResponse(DPAdHistory_API dpAdHistory_api) {


                if(size<=0){
                    adapter.getmDatas().clear();
                    adapter.getmDatas().addAll(dpAdHistory_api.historyInfos);
                }else{
                    adapter.getmDatas().addAll(dpAdHistory_api.historyInfos);
                }


                if (dpad_recyclerview.getAdapter() == null) {
                    dpad_recyclerview.setAdapter(notifyAdapter);
                } else {
                    notifyAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onErrorResponse(DPAdHistory_API dpAdHistory_api) {
                ToastManager.showShort(DPADRewardHistory.this,"적립이력 호출실패");
            }
        });

    }

    private class Delga implements ItemViewDelegate<HistoryInfo> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.dpad_item_history;
        }

        @Override
        public boolean isForViewType(HistoryInfo item, int position) {
            return true;
        }

        @Override
        public void convert(ViewHolder holder, HistoryInfo historyInfo, int position) {

            holder.setText(R.id.dpad_history_date, historyInfo.getTime());
            holder.setText(R.id.dpad_history_name, historyInfo.getTitle());
            holder.setText(R.id.dpad_history_rwd, historyInfo.getRwd());

        }
    }
}
