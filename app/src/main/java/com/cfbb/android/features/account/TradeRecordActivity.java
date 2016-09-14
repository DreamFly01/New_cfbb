package com.cfbb.android.features.account;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.TradeRecordBean;
import com.cfbb.android.widget.PullDownView;
import com.cfbb.android.widget.YCLoadingBg;

import java.util.ArrayList;
import java.util.List;

/**
 * 交易记录
 */
public class TradeRecordActivity extends BaseActivity implements  AbsListView.OnScrollListener {

    //起始页码
    private static final int START_PAGE_INDEX = 1;
    private int curentIndex = START_PAGE_INDEX;
    private TextView tv_back;
    private TextView tv_title;
    private PullDownView pullDownView;
    private ListView listView;
    private TradeRecordAdaptor tradeRecordAdaptor;

    private YCLoadingBg ycLoadingBg;

    private TextView tv_no_data;
    private RelativeLayout empty_view;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_trade_record);
    }

    private int mListViewHeight = 0;

    @Override
    public void setUpViews() {
        tv_no_data = (TextView) findViewById(R.id.tv_no_data);
        tv_no_data.setText(getString(R.string.no_record));
        empty_view = (RelativeLayout) findViewById(R.id.rl_no_data_bg);

        ycLoadingBg = (YCLoadingBg) findViewById(R.id.ycLoadingBg);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setText(R.string.nav_account);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getResources().getString(R.string.trade_details_str));
        tv_back.setVisibility(View.VISIBLE);
        pullDownView = (PullDownView) findViewById(R.id.pullDownView);
        listView = (ListView) findViewById(R.id.listView);
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
        listView.setOnScrollListener(this);
        tradeRecordAdaptor = new TradeRecordAdaptor(this);
        //  ImageView temp= new ImageView(this);
        // temp.setBackgroundResource(R.drawable.app_base_listview_divider);
        // temp.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,10));
        // listView.addHeaderView(temp);
        listView.setAdapter(tradeRecordAdaptor);
        listView.setEmptyView(empty_view);
    }

    private List<TradeRecordBean> tradeRecordBeanList = new ArrayList<>();

    @Override
    public void getDataOnCreate() {
        //TestResultUtils.getSussefulResult31()
        addSubscription(RetrofitClient.GetTradeRecordList(null, curentIndex + "", this, new YCNetSubscriber<List<TradeRecordBean>>(this) {
            @Override
            public void onYcFinish() {
                super.onYcFinish();
                pullDownView.endUpdate();
            }

            @Override
             public void onYCError(APIException e) {
                ycLoadingBg.showErroBg(new YCLoadingBg.YCErroLisener() {
                    @Override
                    public void onTryAgainClick() {
                        getDataOnCreate();
                    }
                });
            }

            @Override
            public void onYcNext(List<TradeRecordBean> model) {
                if (model != null && model != null && model.size() > 0) {
                    if (curentIndex == START_PAGE_INDEX) {
                        tradeRecordBeanList.clear();
                        tradeRecordAdaptor.clear();
                        tradeRecordAdaptor.notifyDataSetChanged();
                    }
                    tradeRecordBeanList.addAll(model);
                    tradeRecordAdaptor.addAll(model);
                    tradeRecordAdaptor.notifyDataSetChanged();
                    if (curentIndex == START_PAGE_INDEX) {
                        listView.setSelection(0);
                    }
                }
                ycLoadingBg.dissmiss();
            }
        }));
    }

    @Override
    public void setUpLisener() {
        tv_back.setOnClickListener(this);
        pullDownView.setUpdateHandle(new PullDownView.UpdateHandle() {
            @Override
            public void onUpdate() {
                curentIndex = START_PAGE_INDEX;
                getDataOnCreate();
            }
        });
    }


    @Override
    public void onUserClick(View v) {
        super.onUserClick(v);
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
        }
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //当listview没有在滚动
        if (AbsListView.OnScrollListener.SCROLL_STATE_IDLE == scrollState) {
            View lastVisibleItemView = listView.getChildAt(listView.getChildCount() - 1);
            if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == mListViewHeight) {
                // 滚动到底部
                curentIndex++;
                getDataOnCreate();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
