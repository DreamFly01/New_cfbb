package com.cfbb.android.features.account;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.MyGiftBean;
import com.cfbb.android.widget.PullDownView;
import com.cfbb.android.widget.YCLoadingBg;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的礼品
 */
public class MyGiftActivity extends BaseActivity implements  AbsListView.OnScrollListener {

    private TextView tv_back;
    private TextView tv_title;
    private PullDownView pullDownView;
    private GridView gridView;
    private MyGiftAdaptor myGiftAdaptor;
    private List<MyGiftBean> myGiftArrayList = new ArrayList<>();
    private YCLoadingBg ycLoadingBg;

    private RelativeLayout empty_view;
    private TextView tv_no_data;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_gift);
    }

    private int mListViewHeight = 0;

    @Override
    public void setUpViews() {

        tv_no_data = (TextView) findViewById(R.id.tv_no_data);
        tv_no_data.setText(getString(R.string.no_gift));
        empty_view = (RelativeLayout) findViewById(R.id.rl_no_data_bg);
        ycLoadingBg = (YCLoadingBg) findViewById(R.id.ycLoadingBg);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setText(getResources().getString(R.string.nav_account));
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getResources().getString(R.string.my_gift_str));
        tv_back.setVisibility(View.VISIBLE);
        pullDownView = (PullDownView) findViewById(R.id.pullDownView);
        gridView = (GridView) findViewById(R.id.gridView);
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mListViewHeight = gridView.getHeight();
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                    gridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    gridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
        gridView.setOnScrollListener(this);
        myGiftAdaptor = new MyGiftAdaptor(this);
        gridView.setAdapter(myGiftAdaptor);
        gridView.setEmptyView(empty_view);
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
    public void getDataOnCreate() {
        //TestResultUtils.getSussefulResult40()
        addSubscription(RetrofitClient.GetMyGift(null, curentIndex + "", this, new YCNetSubscriber<List<MyGiftBean>>(this) {
            @Override
            public void onYcFinish() {
                super.onYcFinish();
                pullDownView.endUpdate();
            }

            @Override
            public void onYCError(APIException e){
                ycLoadingBg.showErroBg(new YCLoadingBg.YCErroLisener() {
                    @Override
                    public void onTryAgainClick() {
                        getDataOnCreate();
                    }
                });
            }

            @Override
            public void onYcNext(List<MyGiftBean> model) {
                if (model != null && model != null && model.size() > 0) {
                    if (curentIndex == START_PAGE_INDEX) {
                        myGiftArrayList.clear();
                        myGiftAdaptor.clear();
                        myGiftAdaptor.notifyDataSetChanged();
                    }
                    myGiftArrayList.addAll(model);
                    myGiftAdaptor.addAll(model);
                    myGiftAdaptor.notifyDataSetChanged();
                    if (curentIndex == START_PAGE_INDEX) {
                        gridView.setSelection(0);
                    }
                } else {
                    //myGiftAdaptor.notifyDataSetChanged();
                }
                ycLoadingBg.dissmiss();
            }
        }));
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



    //起始页码
    private static final int START_PAGE_INDEX = 1;
    private int curentIndex = START_PAGE_INDEX;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //当listview没有在滚动
        if (AbsListView.OnScrollListener.SCROLL_STATE_IDLE == scrollState) {
            View lastVisibleItemView = gridView.getChildAt(gridView.getChildCount() - 1);
            if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == (mListViewHeight - 10)) {
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
