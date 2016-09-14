package com.cfbb.android.features.account.releaseLoan;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.TestResultUtils;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.MyLoanListBean;
import com.cfbb.android.widget.PullDownView;
import com.cfbb.android.widget.YCLoadingBg;
import com.cfbb.android.widget.dialog.YCDialogUtils;

import java.util.ArrayList;
import java.util.List;

/***
 * 发布借款
 */
public class ReleaseLoanActivity extends BaseActivity implements View.OnClickListener, AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    private TextView tv_back;
    private TextView tv_title;
    private TextView tv_add;
    private PullDownView pullDownView;
    private ListView listView;
    private YCLoadingBg ycLoadingBg;
    private int curentIndex = START_PAGE_INDEX;
    //起始页码
    private static final int START_PAGE_INDEX = 1;

    private YCDialogUtils ycDialogUtils;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_release_loan);
    }

    @Override
    public void setUpViews() {

        ycDialogUtils = new YCDialogUtils(this);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_add = (TextView) findViewById(R.id.tv_menu);
        pullDownView = (PullDownView) findViewById(R.id.pullDownView);
        listView = (ListView) findViewById(R.id.listView);
        ycLoadingBg = (YCLoadingBg) findViewById(R.id.ycLoadingBg);
        tv_back.setText(getResources().getString(R.string.nav_account));
        tv_back.setVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.release_loan_str));
        tv_add.setTextColor(getResources().getColor(R.color.txt_red));
        tv_add.setText(getResources().getString(R.string.add));
        tv_add.setVisibility(View.VISIBLE);
        pullDownView.setUpdateHandle(new PullDownView.UpdateHandle() {
            @Override
            public void onUpdate() {
                curentIndex = START_PAGE_INDEX;
                RefrushData();
            }
        });
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
        releaseLoanAdaptor = new ReleaseLoanAdaptor(getApplicationContext());
        listView.setAdapter(releaseLoanAdaptor);
    }

    @Override
    public void setUpLisener() {
        tv_back.setOnClickListener(this);
        tv_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            //添加
            case R.id.tv_menu:
                mIntent = new Intent(this, AddLoanActivity.class);
                startActivity(mIntent);
                break;
        }
    }


    @Override
    public void getDataOnCreate() {
        RefrushData();
    }

    private List<MyLoanListBean> myLoanListList = new ArrayList<>();
    private ReleaseLoanAdaptor releaseLoanAdaptor;

    private void RefrushData() {

        addSubscription(RetrofitClient.GetMyLoanList(TestResultUtils.getSussefulResult39(), this, new YCNetSubscriber<List<MyLoanListBean>>(this) {
            @Override
            public void onYcFinish() {
                pullDownView.endUpdate();
            }


            @Override
            public void onYCError(APIException e) {
                ycLoadingBg.showErroBg(new YCLoadingBg.YCErroLisener() {
                    @Override
                    public void onTryAgainClick() {
                        RefrushData();
                    }
                });
                ycLoadingBg.dissmiss();
            }

            @Override
            public void onYcNext(List<MyLoanListBean> model) {
                if (model != null && model != null && model.size() > 0) {
                    if (curentIndex == START_PAGE_INDEX) {
                        myLoanListList.clear();
                        releaseLoanAdaptor.clear();
                        releaseLoanAdaptor.notifyDataSetChanged();
                    }
                    myLoanListList.addAll(model);
                    releaseLoanAdaptor.addAll(model);
                    releaseLoanAdaptor.notifyDataSetChanged();
                    if (curentIndex == START_PAGE_INDEX) {
                        listView.setSelection(0);
                    }
                } else {
                    ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.dialog_no_more_data), new YCDialogUtils.MySingleBtnclickLisener() {
                        @Override
                        public void onBtnClick(View v) {
                            ycDialogUtils.DismissMyDialog();
                        }
                    }, true);
                }
            }
        }));

    }

    private int mListViewHeight = 0;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //当listview没有在滚动
        if (AbsListView.OnScrollListener.SCROLL_STATE_IDLE == scrollState) {
            View lastVisibleItemView = listView.getChildAt(listView.getChildCount() - 1);
            if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == mListViewHeight) {
                // 滚动到底部
                curentIndex++;
                RefrushData();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MyLoanListBean model = myLoanListList.get(position);
        if (null != model) {
            mIntent = new Intent(this, AddLoanActivity.class);
            mIntent.putExtra(AddLoanActivity.LOAN_ID, model.loanId);
            mIntent.putExtra(AddLoanActivity.SHOW_BACK_STR, getResources().getString(R.string.add));
            startActivity(mIntent);
        }
        model = null;
    }
}
