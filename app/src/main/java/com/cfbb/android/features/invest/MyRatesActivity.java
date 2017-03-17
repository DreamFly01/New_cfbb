package com.cfbb.android.features.invest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.TestResultUtils;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.MyRatesBean;
import com.cfbb.android.protocol.bean.RatesBean;
import com.cfbb.android.widget.PullDownView;
import com.cfbb.android.widget.YCLoadingBg;
import com.cfbb.android.widget.dialog.YCDialogUtils;

import java.util.ArrayList;
import java.util.List;

public class MyRatesActivity extends BaseActivity implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {
    private int mListViewHeight = 0;
    private String flag;
    private String state;
    private TextView back, menu, title, tv_no_data;
    private RelativeLayout empty_view;
    private PullDownView pullDownView;
    private YCLoadingBg ycLoadingBg;
    private YCDialogUtils ycDialogUtils;
    private ListView listView;
    private RateAdapter adapter;
    private MyRateAdapter myRateAdapter;
    private static final int START_PAGE_INDEX = 1;
    private int curentIndex = START_PAGE_INDEX;
    private String item2;//加息券使用说明字段

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_rates);
    }

    @Override
    public void setUpViews() {
        ycDialogUtils = new YCDialogUtils(this);

        back = (TextView) findViewById(R.id.tv_back);
        menu = (TextView) findViewById(R.id.tv_menu);
        title = (TextView) findViewById(R.id.tv_title);
        tv_no_data = (TextView) findViewById(R.id.tv_no_data);


        menu.setTextColor(Color.GRAY);
        pullDownView = (PullDownView) findViewById(R.id.pullDownView);
        ycLoadingBg = (YCLoadingBg) findViewById(R.id.ycLoadingBg);
        empty_view = (RelativeLayout) findViewById(R.id.rl_no_data_bg);
        listView = (ListView) findViewById(R.id.listView);

        back.setVisibility(View.VISIBLE);


        back.setText("返回");
        menu.setText("使用说明");
    }

    List<RatesBean.Item1> listItem = new ArrayList<>();
    List<MyRatesBean> listRate = new ArrayList<>();

    /**
     * 获取数据
     */

    @Override
    public void getDataOnCreate() {
        super.getDataOnCreate();
        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");
        state = intent.getStringExtra("state");
        if (flag.equals("bid")) {
            title.setText("加息券");
            menu.setVisibility(View.VISIBLE);
            adapter = new RateAdapter(this);
            adapter.setFlag(flag);
            if (state != null) {
                adapter.setState(state);
            }
            listView.setAdapter(adapter);
            addSubscription(RetrofitClient.GetRates(TestResultUtils.getSussefulResult50(), this, new YCNetSubscriber<RatesBean>(this) {

                @Override
                public void onYcNext(RatesBean model) {
                    if (model != null) {
                        item2 = model.item2;
                        if (curentIndex == START_PAGE_INDEX) {
                            listItem.clear();
                            adapter.clear();
                            adapter.notifyDataSetChanged();
                        }
                            listItem.addAll(model.item1);
                            adapter.addAll(listItem);
                            adapter.notifyDataSetChanged();
                            if (curentIndex == START_PAGE_INDEX) {
                                listView.setSelection(0);
                            }
                            ycLoadingBg.dissmiss();
                    }
                }
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
                public void onYcFinish() {
                    super.onYcFinish();
                    pullDownView.endUpdate();
                }
            }));
        } else {
            menu.setVisibility(View.GONE);
            title.setText("我的加息券");
            myRateAdapter = new MyRateAdapter(this);
            listView.setAdapter(myRateAdapter);
            addSubscription(RetrofitClient.GetMyRates(TestResultUtils.getSussefulResult51(), curentIndex, this, new YCNetSubscriber<List<MyRatesBean>>(this) {


                @Override
                public void onYcNext(List<MyRatesBean> model) {
                    if (model .size()>0) {
                        if(curentIndex==START_PAGE_INDEX){
                            listRate.clear();
                            myRateAdapter.clear();
                            myRateAdapter.notifyDataSetChanged();
                            if(model.size()<=0){
                                ycLoadingBg.dissmiss();
                                empty_view.setVisibility(View.VISIBLE);
                                tv_no_data.setText("暂无加息卷");
                            }
                        }
//                        listRate = model;
                        listRate.addAll(model);
                        myRateAdapter.addAll(model);
                        myRateAdapter.notifyDataSetChanged();
//                        if (curentIndex == START_PAGE_INDEX) {
//                            listView.setSelection(0);
//                        }
                        ycLoadingBg.dissmiss();
                    } else {
                        empty_view.setVisibility(View.VISIBLE);
                        tv_no_data.setText("暂无加息卷");
                        return;
                    }
                }

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
                public void onYcFinish() {
                    super.onYcFinish();
                    pullDownView.endUpdate();
                }
            }));
        }
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
        back.setOnClickListener(this);
        menu.setOnClickListener(this);
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
            View lastVisibleItemView = listView.getChildAt(listView.getChildCount());
            if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == mListViewHeight) {
                // 滚动到底部
                curentIndex++;
                System.out.println(curentIndex + "--------底部+1---------");
                getDataOnCreate();
            }
        }
    }

    /**
     * 弹窗
     */
    private void dialog1(String str) {
        ycDialogUtils.showSingleDialog("使用说明", str, new YCDialogUtils.MySingleBtnclickLisener() {
            @Override
            public void onBtnClick(View v) {
                ycDialogUtils.DismissMyDialog();
            }
        }, true);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    private Intent intent = new Intent();
    private String name;
    private String rate;
    private String interest_id;
    private int stateFlag;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        name = listItem.get(position).interest_name;
        rate = listItem.get(position).interest_rate;
        interest_id = listItem.get(position).interest_id;
        //判断传过来的sate是否为空是否等于当前position，若都满足则再次点击，设置返回结果为2 隐藏选中图片
        if (state != null && Integer.parseInt(state) == position) {
            this.setResult(2, intent);
            if(stateFlag!=1){
            stateFlag = 1;
            adapter.setStateFlag(stateFlag);
            }else{
                stateFlag = 0;
                adapter.setStateFlag(stateFlag);
                state = position + "";
                intent.putExtra("state", state);
                intent.putExtra("name", name);
                intent.putExtra("rate", rate);
                intent.putExtra("interest_id", interest_id);
                this.setResult(1, intent);
                this.finish();
            }
        }else{
            //若不为空则讲参数返回到投资页面
            state = position + "";
            intent.putExtra("state", state);
            intent.putExtra("name", name);
            intent.putExtra("rate", rate);
            intent.putExtra("interest_id", interest_id);
            this.setResult(1, intent);
        this.finish();
        }
    }

    @Override
    public void onUserClick(View v) {
        super.onUserClick(v);
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_menu:
                dialog1(item2);
                break;
        }
    }
}
