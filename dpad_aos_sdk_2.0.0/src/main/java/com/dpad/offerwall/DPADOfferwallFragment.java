package com.dpad.offerwall;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.dpad.offerwall.bean.DPAdInfo;
import com.dpad.offerwall.bean.DPAdTabInfo;
import com.genius.baselib.base.LazyBaseFragment;
import com.genius.baselib.frame.api.DPAdTabList_API;
import com.sera.volleyhelper.imp.CallBackListener;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rocklee on 2017-05-03.
 */

public class DPADOfferwallFragment extends LazyBaseFragment {

    List<DPAdTabInfo> tabList = new ArrayList<>();
    MagicIndicator magicIndicator;
    ViewPager2 viewpager2;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.dpad_fragment);

        magicIndicator = findViewBId(R.id.indicator);
        viewpager2 = findViewBId(R.id.viewpager2);

        tabInit();
    }

    private void tabUI() {
        CommonNavigator navigator = new CommonNavigator(getContext());
        navigator.setAdjustMode(false);
        navigator.setLeftPadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, getResources().getDisplayMetrics()));
        CommonNavigatorAdapter navigatorAdapter = new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return tabList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int pageIndex) {
                CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);
                commonPagerTitleView.setContentView(R.layout.dpad_tab_item);
                final TextView tabText = commonPagerTitleView.findViewById(R.id.tabText);
                tabText.setText(tabList.get(pageIndex).title);
                tabText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewpager2.setCurrentItem(pageIndex, false);
                    }
                });
                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {
                    @Override
                    public void onSelected(int index, int totalCount) {
                        tabText.setSelected(true);
                    }

                    @Override
                    public void onDeselected(int index, int totalCount) {
                        tabText.setSelected(false);
                    }

                    @Override
                    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {

                    }

                    @Override
                    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {

                    }
                });
                return commonPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        };
        navigator.setAdapter(navigatorAdapter);
        magicIndicator.setNavigator(navigator);
        TabsAdapter tabsAdapter = new TabsAdapter(this);
        viewpager2.setAdapter(tabsAdapter);
        viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                magicIndicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                magicIndicator.onPageScrollStateChanged(state);
            }
        });
    }

    private void tabInit() {
        DPAdTabList_API tabList_api = new DPAdTabList_API(getContext());
        tabList_api.request(getContext(), new CallBackListener<DPAdTabList_API>() {
            @Override
            public void showloadingUI() {

            }

            @Override
            public void cancleloadingUI() {

            }

            @Override
            public void onResponse(DPAdTabList_API dpAdTabList_api) {
                tabList.clear();
                tabList.addAll(dpAdTabList_api.dpAdTabLists);
                tabUI();
            }

            @Override
            public void onErrorResponse(DPAdTabList_API dpAdTabList_api) {
                if(getContext()!=null && getActivity()!=null && !getActivity().isFinishing()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setMessage("로딩실패 하였습니다. 재시도 하시겠습니까?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            tabInit();
                        }
                    });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();
                }
            }
        });


    }

    List<DPAdInfo> itemList = new ArrayList<>();

    class TabsAdapter extends FragmentStateAdapter {


        public TabsAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        private DPADPageFragment getPageFrag(int index) {
            DPADPageFragment dpadPageFragment = new DPADPageFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("data", tabList.get(index));
            dpadPageFragment.setArguments(bundle);
            return dpadPageFragment;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return getPageFrag(position);
        }

        @Override
        public int getItemCount() {
            return tabList.size();
        }
    }

}
