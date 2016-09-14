package com.cfbb.android.features.account.myinvest;

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
import com.cfbb.android.commom.baseview.BaseFragment;
import com.cfbb.android.commom.state.MyInvestStateEnum;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.MyInvestBean;
import com.cfbb.android.widget.PullDownView;
import com.cfbb.android.widget.YCLoadingBg;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的投资内容页
 */
public class MyInvestFragment extends BaseFragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    private static final String MY_INVEST_STATE = "my_invest_state";
    private boolean isInit = true;
    private String myInvestState;
    private int curentIndex = START_PAGE_INDEX;

    //起始页码
    private static final int START_PAGE_INDEX = 1;
    private List<MyInvestBean> myInvestBeanList = new ArrayList<>();
    private MyInvestAdaptor myInvestAdaptor;
    private ListView listView;
    private PullDownView pullDownView;
    private YCLoadingBg ycLoadingBg;
    private TextView tv_no_data;
    private RelativeLayout empty_view;


    public MyInvestFragment() {
        // Required empty public constructor
    }

    public static MyInvestFragment newInstance(String param1) {
        MyInvestFragment fragment = new MyInvestFragment();
        Bundle args = new Bundle();
        args.putString(MY_INVEST_STATE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            myInvestState = getArguments().getString(MY_INVEST_STATE);
        }
    }


    @Override
    public int initContentView() {
        return R.layout.fragment_my_invest;
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
        // 每次切换fragment时调用的方法
        if (isVisibleToUser) {
            showData();
        }
    }

    /**
     * 初始化数据
     */
    private void showData() {
        if (isInit) {
            isInit = false;//加载数据完成
            addSubscription(RetrofitClient.MyInvest(null, myInvestState, curentIndex + "", getActivity(), new YCNetSubscriber<List<MyInvestBean>>(getActivity()) {

                @Override
                public void onYcFinish() {
                    pullDownView.endUpdate();
                }


                @Override
                public void onYCError(APIException e) {
                    ycLoadingBg.showErroBg(new YCLoadingBg.YCErroLisener() {
                        @Override
                        public void onTryAgainClick() {
                            getDataOnActivityCreated();
                        }
                    });
                }

                @Override
                public void onYcNext(List<MyInvestBean> model) {
                    if (model != null && model != null) {
                        if (curentIndex == START_PAGE_INDEX) {
                            myInvestBeanList.clear();
                            myInvestAdaptor.clear();
                            myInvestAdaptor.notifyDataSetChanged();
                        }
                        myInvestBeanList.addAll(model);
                        myInvestAdaptor.addAll(model);
                        myInvestAdaptor.notifyDataSetChanged();
                        if (curentIndex == START_PAGE_INDEX) {
                            listView.setSelection(0);
                        }

                    } else {

                    }
                    ycLoadingBg.dissmiss();
                }
            }));
        }
    }

    @Override
    public void setUpViews(View view) {
        tv_no_data = (TextView) view.findViewById(R.id.tv_no_data);
        tv_no_data.setText(getResources().getString(R.string.no_invest));
        empty_view = (RelativeLayout) view.findViewById(R.id.rl_no_data_bg);
        myInvestAdaptor = new MyInvestAdaptor(getActivity());
        ycLoadingBg = (YCLoadingBg) view.findViewById(R.id.ycLoadingBg);
        pullDownView = (PullDownView) view.findViewById(R.id.pullDownView);
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(myInvestAdaptor);
        listView.setEmptyView(empty_view);
    }

    private int mListViewHeight = 0;

    @Override
    public void setUpLisener() {
        pullDownView.setUpdateHandle(new PullDownView.UpdateHandle() {
            @Override
            public void onUpdate() {
                curentIndex = START_PAGE_INDEX;
                getDataOnActivityCreated();
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
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //当listview没有在滚动
        if (AbsListView.OnScrollListener.SCROLL_STATE_IDLE == scrollState) {
            View lastVisibleItemView = listView.getChildAt(listView.getChildCount() - 1);
            if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == mListViewHeight) {
                // 滚动到底部
                curentIndex++;
                getDataOnActivityCreated();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MyInvestBean model = myInvestBeanList.get(position);
        if (null != model) {
            Bundle bundle = new Bundle();
            if (model.state == MyInvestStateEnum.COMPLETE.getValue()) {
              //  bundle.putString(CompleteActivity.INVEST_ID, model.investId);
              //  JumpCenter.JumpActivity(getActivity(), CompleteActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
            }
            if (model.state == MyInvestStateEnum.INVESTING.getValue()) {
                bundle.putString(InvestingActivity.PRODUCT_ID, model.productId);
                bundle.putString(InvestingActivity.INVEST_ID, model.investId);
                bundle.putString(InvestingActivity.LOAN_TYPEID, model.loanTypeId);
                if (StrUtil.isEmpty(model.progress)) {
                    bundle.putBoolean(InvestingActivity.CAN_INVEST, false);
                } else {
                    if (Double.parseDouble(model.progress) < 100) {
                        bundle.putBoolean(InvestingActivity.CAN_INVEST, true);
                    } else {
                        bundle.putBoolean(InvestingActivity.CAN_INVEST, false);
                    }
                }
                JumpCenter.JumpActivity(getActivity(), InvestingActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
            }
            if (model.state == MyInvestStateEnum.HOLDING.getValue()) {
                bundle.putString(CompleteActivity.INVEST_ID, model.investId);
                JumpCenter.JumpActivity(getActivity(), HoldingActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
            }
        }
    }
    @Override
    public void getDataOnActivityCreated() {
        if (!isInit) {
            addSubscription(RetrofitClient.MyInvest(null, myInvestState, curentIndex + "", getActivity(), new YCNetSubscriber<List<MyInvestBean>>(getActivity()) {

                @Override
                public void onYcFinish() {
                    pullDownView.endUpdate();
                }

                @Override
                public void onYCError(APIException e) {
                    ycLoadingBg.showErroBg(new YCLoadingBg.YCErroLisener() {
                        @Override
                        public void onTryAgainClick() {
                            getDataOnActivityCreated();
                        }
                    });
                }

                @Override
                public void onYcNext(List<MyInvestBean> model) {
                    if (model != null && model != null) {
                        if (curentIndex == START_PAGE_INDEX) {
                            myInvestBeanList.clear();
                            myInvestAdaptor.clear();
                            myInvestAdaptor.notifyDataSetChanged();
                        }
                        myInvestBeanList.addAll(model);
                        myInvestAdaptor.addAll(model);
                        myInvestAdaptor.notifyDataSetChanged();
                        if (curentIndex == START_PAGE_INDEX) {
                            listView.setSelection(0);
                        }

                    } else {

                    }
                    ycLoadingBg.dissmiss();
                }
            }));
        }
    }
}
