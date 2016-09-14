package com.cfbb.android.features.account;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.state.RedPaperStateEnum;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.MyRedPaperBean;
import com.cfbb.android.widget.PullDownView;
import com.cfbb.android.widget.YCLoadingBg;

import java.util.ArrayList;
import java.util.List;


/***
 * 我的红包
 */
public class MyRedPaperActivity extends BaseActivity implements  AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    private TextView tv_back;
    private TextView tv_title;
    private PullDownView pullDownView;
    private ListView listView;
    private MyRedPaperAdaptor myRedPaperAdaptor;
    private List<MyRedPaperBean> myRedPaperList = new ArrayList<>();
    private YCLoadingBg ycLoadingBg;
    private RelativeLayout empty_view;
    private TextView tv_no_data_empty_view;
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_red_paper);
    }

    private int mListViewHeight = 0;
    private LinearLayout footerView;
    private TextView tv_no_data;

    @Override
    public void setUpViews() {

        tv_no_data_empty_view = (TextView) findViewById(R.id.tv_no_data);
        tv_no_data_empty_view.setText(getString(R.string.no_red_paper));

        empty_view = (RelativeLayout) findViewById(R.id.rl_no_data_bg);

        ycLoadingBg = (YCLoadingBg) findViewById(R.id.ycLoadingBg);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setText(getResources().getString(R.string.nav_account));
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getResources().getString(R.string.my_red_paper_str));
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
        myRedPaperAdaptor = new MyRedPaperAdaptor(this);

        footerView = new LinearLayout(this);
        View tempView = LayoutInflater.from(this).inflate(R.layout.listview_no_more_data_item, null);
        tempView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        tv_no_data = (TextView) (tempView.findViewById(R.id.tv_no_data));
        tv_no_data.setText(getString(R.string.no_more_red_paper));
        footerView.addView(tempView);
        footerView.setVisibility(View.GONE);
        listView.addFooterView(footerView);

        listView.setAdapter(myRedPaperAdaptor);
        listView.setOnItemClickListener(this);
        listView.setEmptyView(empty_view);
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
    public void getDataOnCreate() {
        //TestResultUtils.getSussefulResult22()
        addSubscription(RetrofitClient.GetMyRedPaperList(null, curentIndex + "", this, new YCNetSubscriber<List<MyRedPaperBean>>(this) {
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
            public void onYcNext(List<MyRedPaperBean> model) {
                if (model != null && model != null && model.size() > 0) {
                    if (curentIndex == START_PAGE_INDEX) {
                        myRedPaperList.clear();
                        myRedPaperAdaptor.clear();
                        myRedPaperAdaptor.notifyDataSetChanged();
                    }
                    myRedPaperList.addAll(model);
                    myRedPaperAdaptor.addAll(model);
                    myRedPaperAdaptor.notifyDataSetChanged();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(myRedPaperList.size() > position) {
            MyRedPaperBean myRedPaper = myRedPaperList.get(position);
            if (myRedPaper != null && myRedPaper.state == RedPaperStateEnum.UNUSER_REDPAPER.getValue()) {
                dialog(getString(R.string.sure_to_use_this_red_paper), myRedPaper.redPaperId);
            }
        }
    }

    protected void dialog(String msg, final String packetId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setTitle(R.string.dialog_title);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //TestResultUtils.getBaseSussefulResult()
                addSubscription(RetrofitClient.UseMyRedPaper(null, packetId, MyRedPaperActivity.this, new YCNetSubscriber(MyRedPaperActivity.this, true) {

                    @Override
                    public void onYcNext(Object model) {
                        UseRight();
                    }
                }));
            }
        });
        builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void UseRight() {
        curentIndex = START_PAGE_INDEX;
        getDataOnCreate();
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
