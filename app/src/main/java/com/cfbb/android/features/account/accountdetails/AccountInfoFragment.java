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
import com.cfbb.android.protocol.bean.AccountDetailsInfoBean;
import com.cfbb.android.widget.YCLoadingBg;

/**
 * 账户详情
 */
public class AccountInfoFragment extends BaseFragment {

    public boolean isInit() {
        return isInit;
    }

    public void setInit(boolean init) {
        isInit = init;
    }

    private boolean isInit =true; // 是否可以开始加载数据

    private TextView tv_01;
    private TextView tv_02;
    private TextView tv_03;
    private TextView tv_04;
    private TextView tv_05;
    private TextView tv_06;
    private TextView tv_07;
    private TextView tv_08;
    private YCLoadingBg ycLoading;
    private ScrollView sc_view;


    public AccountInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public int initContentView() {
       // isInit = true;
        return R.layout.fragment_account_info;
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


    public void MyGetData() {
        if (isInit) {
            isInit = false;//加载数据完成

            //TestResultUtils.getSussefulResult2()
            addSubscription(RetrofitClient.GetMyAccountDetailsInfo(null, getActivity(), new YCNetSubscriber<AccountDetailsInfoBean>(getActivity()) {

                        @Override
                        public void onYcNext(AccountDetailsInfoBean model) {
                            FillView(model);
                            ycLoading.dissmiss();
                        }

                        @Override
                        public void onYCError(APIException e) {
                            // super.onError(e);
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

    private void FillView(AccountDetailsInfoBean model) {
        tv_01.setText(model.totalMoney);
        tv_02.setText(model.canUseTotal);
        tv_03.setText(model.freezeMoeny);
        tv_04.setText(model.investFreezeMoney);
        tv_05.setText(model.withDrawFreezeMoeny);
        tv_06.setText(model.repentMoeny);
        tv_07.setText(model.rechangeMoeny);
        tv_08.setText(model.withDrawMoeny);
    }

    @Override
    public void setUpViews(View view) {
        tv_01 = (TextView) view.findViewById(R.id.tv_02);
        tv_02 = (TextView) view.findViewById(R.id.tv_03);
        tv_03 = (TextView) view.findViewById(R.id.tv_04);
        tv_04 = (TextView) view.findViewById(R.id.tv_05);
        tv_05 = (TextView) view.findViewById(R.id.tv_06);
        tv_06 = (TextView) view.findViewById(R.id.tv_07);
        tv_07 = (TextView) view.findViewById(R.id.tv_08);
        tv_08 = (TextView) view.findViewById(R.id.tv_10);
        ycLoading = (YCLoadingBg) view.findViewById(R.id.ycLoadingBg);
        sc_view = (ScrollView) view.findViewById(R.id.sc_view);
    }

    @Override
    public void setUpLisener() {

    }
}
