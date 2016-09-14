package com.cfbb.android.features.account.withdrawAndrecharge;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.WithdrawRecordInfoBean;
import com.cfbb.android.widget.PullDownView;
import com.cfbb.android.widget.YCLoadingBg;

import java.util.ArrayList;
import java.util.List;

/***
 * 提现记录
 */
public class WithDrawRecordActivity extends BaseActivity implements AbsListView.OnScrollListener {


    //起始页码
    private static final int START_PAGE_INDEX = 1;
    private int curentIndex = START_PAGE_INDEX;
    private TextView tv_back;
    private TextView tv_title;
    private PullDownView pullDownView;
    private ListView listView;
    private WithDrawRecordAdaptor withDrawRecordAdaptor;

    private YCLoadingBg ycLoadingBg;
    private TextView tv_no_data;
    private LinearLayout footerView;
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

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_with_draw_record);
    }

    private int mListViewHeight = 0;

    @Override
    public void setUpViews() {

        ycLoadingBg = (YCLoadingBg) findViewById(R.id.ycLoadingBg);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setText(getResources().getString(R.string.withdraw_str));
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getResources().getString(R.string.withdraw_record_str));
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
        withDrawRecordAdaptor = new WithDrawRecordAdaptor(this);

        footerView = new LinearLayout(this);
        View tempView = LayoutInflater.from(this).inflate(R.layout.listview_no_more_data_item, null);
        tempView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        tv_no_data = (TextView) (tempView.findViewById(R.id.tv_no_data));
        tv_no_data.setText(getString(R.string.no_more_withdraw_record));
        footerView.addView(tempView);
        footerView.setVisibility(View.GONE);
        listView.addFooterView(footerView);

        listView.setAdapter(withDrawRecordAdaptor);
    }

    private List<WithdrawRecordInfoBean> tradeRecordBeanList = new ArrayList<>();

    @Override
    public void getDataOnCreate() {
        //TestResultUtils.getSussefulResult35()
        addSubscription(RetrofitClient.GetWithdrawRecordList(null, curentIndex + "", this, new YCNetSubscriber<List<WithdrawRecordInfoBean>>(this) {
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
            public void onYcNext(List<WithdrawRecordInfoBean> model) {
                if (model != null && model != null && model.size() > 0) {
                    if (curentIndex == START_PAGE_INDEX) {
                        tradeRecordBeanList.clear();
                        withDrawRecordAdaptor.clear();
                        withDrawRecordAdaptor.notifyDataSetChanged();
                    }
                    tradeRecordBeanList.addAll(model);
                    withDrawRecordAdaptor.addAll(model);
                    withDrawRecordAdaptor.notifyDataSetChanged();
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
        }));
    }

    @Override
    public void setUpLisener() {
        tv_back.setOnClickListener(this);
        pullDownView.setUpdateHandle(new PullDownView.UpdateHandle() {
            @Override
            public void onUpdate() {
                if(footerView != null)
                {
                    footerView.setVisibility(View.GONE);
                }
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
                if (footerView.getVisibility() == View.GONE) {
                    getDataOnCreate();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
