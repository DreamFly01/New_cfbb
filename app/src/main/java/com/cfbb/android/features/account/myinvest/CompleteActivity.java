package com.cfbb.android.features.account.myinvest;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.config.Const;
import com.cfbb.android.commom.state.MyInvestStateEnum;
import com.cfbb.android.commom.utils.others.Utils;
import com.cfbb.android.db.base.UserBizHelper;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.MyInvestDetailsBean;
import com.cfbb.android.widget.YCLoadingBg;

/***
 * 已完成
 */
public class CompleteActivity extends BaseActivity  {

    public static final String INVEST_ID = "invest_id";
    private TextView tv_title;
    private TextView tv_back;
    private TextView tv_productName;
    private TextView tv_returnmoney_way;
    private TextView tv_yearOfrate;
    private TextView tv_investDate;
    private TextView tv_finishDate;
    private TextView tv_investTotalMoney;
    private TextView tv_arrreadyGetIntrested;
    private YCLoadingBg ycLoadingBg;
    private UserBizHelper userBizHelper;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_complete);
        userBizHelper = new UserBizHelper(this);
        mIntent = getIntent();
        if (mIntent != null) {
            invest_id = mIntent.getExtras().getString(INVEST_ID);
        }
    }

    @Override
    public void setUpViews() {

        ycLoadingBg = (YCLoadingBg) findViewById(R.id.ycLoadingBg);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setText(getResources().getString(R.string.my_invest_str));
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getResources().getString(R.string.is_over));
        tv_title.setVisibility(View.VISIBLE);
        tv_back.setVisibility(View.VISIBLE);

        tv_productName = (TextView) findViewById(R.id.tv_04);
        tv_returnmoney_way = (TextView) findViewById(R.id.tv_07);
        tv_yearOfrate = (TextView) findViewById(R.id.tv_05);
        tv_investDate = (TextView) findViewById(R.id.tv_06);
        tv_finishDate = (TextView) findViewById(R.id.tv_08);
        tv_investTotalMoney = (TextView) findViewById(R.id.tv_03);
        tv_arrreadyGetIntrested = (TextView) findViewById(R.id.tv_02);
    }

    @Override
    public void setUpLisener() {
        tv_back.setOnClickListener(this);
    }

    private String invest_id;
    private MyInvestDetailsBean myInvestDetailsBean;

    @Override
    public void getDataOnCreate() {

        addSubscription(RetrofitClient.GetMyInvestDeatils(null, MyInvestStateEnum.COMPLETE.getValue() + "", invest_id, this, new YCNetSubscriber<MyInvestDetailsBean>(this) {

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
            public void onYcNext(MyInvestDetailsBean model) {
                myInvestDetailsBean = model;
                FillView();
                ycLoadingBg.dissmiss();
            }

        }));
    }

    private void FillView() {

        tv_productName.setText(myInvestDetailsBean.productName);
        tv_returnmoney_way.setText(Utils.getReturnWayNameByType(myInvestDetailsBean.repaymentType));
        tv_yearOfrate.setText(myInvestDetailsBean.yearOfRate + Const.PER);
        tv_investDate.setText(myInvestDetailsBean.investDate + Utils.getInvestUnitNameByFlag(myInvestDetailsBean.unit));
        tv_finishDate.setText(myInvestDetailsBean.finishTime);
        tv_investTotalMoney.setText(myInvestDetailsBean.totalInvestMoeny);
        tv_arrreadyGetIntrested.setText(myInvestDetailsBean.interestReceivable);

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
