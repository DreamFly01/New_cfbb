package com.cfbb.android.features.account.accountdetails;


import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseFragment;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.AccountLoanInfoBean;
import com.cfbb.android.widget.YCLoadingBg;

/**
 * 借款详情
 */
public class LoanInfoFragment extends BaseFragment {

    private boolean isInit = true; // 是否可以开始加载数据

    private TextView tv_01;
    private TextView tv_02;
    private TextView tv_03;
    private TextView tv_04;
    private TextView tv_05;
    private TextView tv_06;
    private YCLoadingBg ycLoading;

    public LoanInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public int initContentView() {
        //isInit = true;
        return R.layout.fragment_loan_info;
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
            MyGetData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isHidden = !isVisibleToUser;
        // 每次切换fragment时调用的方法
        if (isVisibleToUser) {
            MyGetData();
            StatService.onResume(this);
        }
        else
        {
            if(!isInit) {
                StatService.onPause(this);
            }
        }
    }


    public  void MyGetData() {
        if (isInit) {
            isInit = false;//加载数据完成
            //TestResultUtils.getSussefulResult5()
            addSubscription(RetrofitClient.GetMyAccountLoanInfo(null, getActivity(), new YCNetSubscriber<AccountLoanInfoBean>(getActivity()) {
                        @Override
                        public void onYcNext(AccountLoanInfoBean model) {
                            FillView(model);
                            ycLoading.dissmiss();
                        }

                        @Override
                        public void onYCError(APIException e) {
                            //      super.onError(e);
                            ycLoading.showErroBg(new YCLoadingBg.YCErroLisener() {
                                @Override
                                public void onTryAgainClick() {
                                    isInit = true;
                                    MyGetData();
                                }
                            });
                        }

                    }
            ));
        }
    }

    private void FillView(AccountLoanInfoBean model) {
        tv_01.setText(model.loanMoney);
        tv_02.setText(model.returnMoeny);
        tv_03.setText(model.waiMoeny);
        tv_04.setText(model.payInterest);
        tv_05.setText(model.recentReturnMoneyDate);
        tv_06.setText(model.expectReturnMoeny);
    }

    private ScrollView sc_view;

    @Override
    public  void setUpViews(View view) {
        tv_01 = (TextView) view.findViewById(R.id.tv_02);
        tv_02 = (TextView) view.findViewById(R.id.tv_03);
        tv_03 = (TextView) view.findViewById(R.id.tv_04);
        tv_04 = (TextView) view.findViewById(R.id.tv_05);
        tv_05 = (TextView) view.findViewById(R.id.tv_06);
        tv_06 = (TextView) view.findViewById(R.id.tv_07);
        ycLoading = (YCLoadingBg) view.findViewById(R.id.ycLoadingBg);
        sc_view = (ScrollView) view.findViewById(R.id.sc_view);
    }

    @Override
    public void setUpLisener() {

    }

}
