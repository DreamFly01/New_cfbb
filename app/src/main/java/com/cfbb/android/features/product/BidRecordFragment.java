package com.cfbb.android.features.product;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseFragment;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.BidRecordBean;
import com.cfbb.android.widget.YCLoadingBg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrChang45 on 2016/3/24.
 * 投标记录
 */
public class BidRecordFragment extends BaseFragment implements AbsListView.OnScrollListener {

    private boolean isInit =true; // 是否可以开始加载数据
    private static final String PRODUCT_ID = "product_id";
    private String product_id;
    private BidRecordAdaptor bidRecordAdaptor;
    private ListView listView;

    public static BidRecordFragment newInstance(String productId) {
        BidRecordFragment fragment = new BidRecordFragment();
        Bundle args = new Bundle();
        args.putString(PRODUCT_ID, productId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        product_id = getArguments().getString(PRODUCT_ID);
    }

    @Override
    public int initContentView() {

        return R.layout.fragment_bidrecord;
    }

    private YCLoadingBg ycLoadingBg;
    private LinearLayout ll_bg;
    private int mListViewHeight = 0;


    @Override
    public void setUpViews(View view) {

        ll_bg = (LinearLayout) view.findViewById(R.id.ll_01);
        ycLoadingBg = (YCLoadingBg) view.findViewById(R.id.ycLoadingBg);
        listView = (ListView) view.findViewById(R.id.listView);
        bidRecordAdaptor = new BidRecordAdaptor(getActivity());
        listView.setOnScrollListener(this);

        footerView = new LinearLayout(getActivity());
        View tempView = LayoutInflater.from(getActivity()).inflate(R.layout.listview_no_more_data_item, null);
        tempView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        tv_no_data = (TextView) (tempView.findViewById(R.id.tv_no_data));
        tv_no_data.setText(R.string.no_more_invest_record);
        footerView.addView(tempView);
        footerView.setVisibility(View.GONE);
        listView.addFooterView(footerView);
        listView.setAdapter(bidRecordAdaptor);

    }

    @Override
    public void setUpLisener() {

    }
    private boolean isHidden;
    @Override
    public void onPause() {
        super.onPause();
        if (!isHidden) {
            StatService.onPause(this);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        // 判断当前fragment是否显示
        if (getUserVisibleHint()) {
            showData();
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isHidden = !isVisibleToUser;
        // 每次切换fragment时调用的方法
        if (isVisibleToUser) {
            showData();
            StatService.onResume(this);
        }
        else
        {
            if(!isInit) {
                StatService.onPause(this);
            }
        }
    }

    private LinearLayout footerView;
    private TextView tv_no_data;

    /**
     * 初始化数据
     */
    private void showData() {
        if (isInit) {
            isInit = false;//加载数据完成
            // 加载各种数据
            //TestResultUtils.getSussefulResult9()
            addSubscription(RetrofitClient.getBidRecord(null, product_id, curentIndex + "", getActivity(), new YCNetSubscriber<List<BidRecordBean>>(getActivity()) {

                @Override
                public void onYcNext(List<BidRecordBean> model) {

                    if (model != null && model != null && model.size() > 0) {
                        if (curentIndex == START_PAGE_INDEX) {
                            bidRecordBeanArrayList.clear();
                            bidRecordAdaptor.clear();
                            bidRecordAdaptor.notifyDataSetChanged();
                        }
                        bidRecordBeanArrayList.addAll(model);
                        bidRecordAdaptor.addAll(model);
                        bidRecordAdaptor.notifyDataSetChanged();
                        if (curentIndex == START_PAGE_INDEX) {
                            listView.setSelection(0);
                        }
                    } else {
                        if (footerView.getVisibility() == View.GONE) {
                            footerView.setVisibility(View.VISIBLE);
                        }
                    }
                    listView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            mListViewHeight = listView.getHeight();
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                                listView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            } else {
                                listView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            }
                        }
                    });
                    ycLoadingBg.dissmiss();
                }

                @Override
                public void onYCError(APIException e) {
                    ycLoadingBg.showErroBg(new YCLoadingBg.YCErroLisener() {
                        @Override
                        public void onTryAgainClick() {
                            isInit = true;
                            showData();
                        }
                    });
                }
            }));

        }
    }

    private List<BidRecordBean> bidRecordBeanArrayList = new ArrayList<>();

    //起始页码
    private static final int START_PAGE_INDEX = 1;
    private int curentIndex = START_PAGE_INDEX;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (AbsListView.OnScrollListener.SCROLL_STATE_IDLE == scrollState) {
            View lastVisibleItemView = listView.getChildAt(listView.getChildCount() - 1);
            if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == mListViewHeight) {
                // 滚动到底部
                curentIndex++;
                if (footerView.getVisibility() == View.GONE) {
                    MyGetData();
                }
            }
        }
    }

    private void MyGetData() {
        addSubscription(RetrofitClient.getBidRecord(null, product_id, curentIndex + "", getActivity(), new YCNetSubscriber<List<BidRecordBean>>(getActivity()) {

            @Override
            public void onYcNext(List<BidRecordBean> model) {

                if (model != null && model != null && model.size() > 0) {
                    if (curentIndex == START_PAGE_INDEX) {
                        bidRecordBeanArrayList.clear();
                        bidRecordAdaptor.clear();
                        bidRecordAdaptor.notifyDataSetChanged();
                    }
                    bidRecordBeanArrayList.addAll(model);
                    bidRecordAdaptor.addAll(model);
                    bidRecordAdaptor.notifyDataSetChanged();
                    if (curentIndex == START_PAGE_INDEX) {
                        listView.setSelection(0);
                    }
                } else {
                    if (footerView.getVisibility() == View.GONE) {
                        footerView.setVisibility(View.VISIBLE);
                    }
                }
                ycLoadingBg.dissmiss();
            }

            @Override
            public void onYCError(APIException e) {
                ycLoadingBg.showErroBg(new YCLoadingBg.YCErroLisener() {
                    @Override
                    public void onTryAgainClick() {
                        isInit = true;
                        showData();
                    }
                });
            }
        }));

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }


}
