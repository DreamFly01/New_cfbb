package com.cfbb.android.features.account.releaseLoan;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.MyLoanBean;
import com.cfbb.android.widget.PullDownView;
import com.cfbb.android.widget.YCLoadingBg;

import java.util.ArrayList;
import java.util.List;

public class MyLoanActivity extends BaseActivity implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    private int mListViewHeight = 0;

    private TextView tv_no_data;
    private TextView tv_title;
    private TextView tv_back;
    private ListView listView;
    private PullDownView pullDownView;
    private MyLoanAdapter myLoanAdapter;
    private YCLoadingBg ycLoadingBg;
    private List<MyLoanBean.MyLoanArray> myLoanBeanList = new ArrayList<>();
    private RelativeLayout empty_view;//没有数据时展示的页面
    private FrameLayout fl;
    private static final int START_PAGE_INDEX = 1;
    private int curentIndex = START_PAGE_INDEX;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_loan);
    }

    /**
     * 获取数据
     */
    @Override
    public void getDataOnCreate() {
        super.getDataOnCreate();

        addSubscription(RetrofitClient.GetMyLoans(null, curentIndex + "", this, new YCNetSubscriber<MyLoanBean>(this) {

            @Override
            public void onYCError(APIException erro) {
                ycLoadingBg.showErroBg(new YCLoadingBg.YCErroLisener() {
                    @Override
                    public void onTryAgainClick() {
                        getDataOnCreate();
                        showLongToast("请求失败");
                    }
                });
            }

            @Override
            public void onYcNext(MyLoanBean model) {
                if (model != null ) {
                    if (curentIndex == START_PAGE_INDEX) {
                        myLoanBeanList.clear();
                        myLoanAdapter.clear();
                        myLoanAdapter.notifyDataSetChanged();
                        if(model.myloanarray.size()<=0){
                            ycLoadingBg.dissmiss();
                            fl.setVisibility(View.GONE);
                            empty_view.setVisibility(View.VISIBLE);
                            tv_no_data.setText("暂无借款");
                        }
                    }
                        myLoanBeanList.addAll(model.myloanarray);
                        myLoanAdapter.addAll(model.myloanarray);
                        myLoanAdapter.notifyDataSetChanged();
                    if (curentIndex == START_PAGE_INDEX) {
                        listView.setSelection(0);
                    }
                    ycLoadingBg.dissmiss();
                }



            }

            @Override
            public void onYcFinish() {
                super.onYcFinish();
                pullDownView.endUpdate();
            }
        }));
    }

    /**
     * 所有的初始化控件这个方法里面
     */
    @Override
    public void setUpViews() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_back = (TextView) findViewById(R.id.tv_back);
        listView = (ListView) findViewById(R.id.listView);
        pullDownView = (PullDownView) findViewById(R.id.pullDownView);
        ycLoadingBg = (YCLoadingBg) findViewById(R.id.ycLoadingBg);
        empty_view = (RelativeLayout) findViewById(R.id.rl_no_data_bg);
        tv_no_data = (TextView) findViewById(R.id.tv_no_data);
        fl = (FrameLayout) findViewById(R.id.fl_01);


        tv_title.setText(R.string.release_myloan_str);
        tv_back.setText(R.string.nav_account);
        tv_back.setVisibility(View.VISIBLE);
        myLoanAdapter = new MyLoanAdapter(MyLoanActivity.this);
        listView.setAdapter(myLoanAdapter);
        tv_no_data.setText("暂无投资");


    }

    /**
     * 设置监听事件
     */
    @Override
    public void setUpLisener() {
        //下拉刷新
        pullDownView.setUpdateHandle(new PullDownView.UpdateHandle() {
            @Override
            public void onUpdate() {
                curentIndex = START_PAGE_INDEX;
                getDataOnCreate();
            }
        });

        //监听view树的改变，获取listview的高度
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
        listView.setOnItemClickListener(this);
        tv_back.setOnClickListener(this);
    }




    /**
     * 监听上拉加载，判断是否为listview到达底部
     *
     * @param view
     * @param scrollState
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (AbsListView.OnScrollListener.SCROLL_STATE_IDLE == scrollState) {
            View lastVisibleItemView = listView.getChildAt(listView.getChildCount() - 1);
            if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == mListViewHeight) {
                // 滚动到底部
                curentIndex++;
                System.out.println(curentIndex + "--------底部+1---------");
//                Toast.makeText(MyLoanActivity.this, curentIndex, Toast.LENGTH_SHORT).show();
                getDataOnCreate();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putString("loanTypeId", myLoanBeanList.get(position).loanTypeId);
        bundle.putString("prodcutId", myLoanBeanList.get(position).prodcutId);
        JumpCenter.JumpActivity(this, LoanDetailsActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, false);
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
}
