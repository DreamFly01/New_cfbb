package com.cfbb.android.features.account.myinvest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.config.Const;
import com.cfbb.android.commom.state.MyInvestStateEnum;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.commom.utils.others.Utils;
import com.cfbb.android.db.base.UserBizHelper;
import com.cfbb.android.features.invest.InvestBidActivity;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.MyInvestDetailsBean;
import com.cfbb.android.widget.YCLoadingBg;

/***
 * 投资中
 */
public class InvestingActivity extends BaseActivity implements View.OnClickListener {

    public static final String PRODUCT_ID = "product_id";
    public static final String INVEST_ID = "invest_id";
    public static final String LOAN_TYPEID = "loan_typeid";
    public static final String CAN_INVEST = "can_invest";

    private TextView tv_title;
    private TextView tv_back;

    private Boolean can_invest =true;
    private TextView tv_productName;
    private TextView tv_returnmoney_way;
    private TextView tv_yearOfrate;
    private TextView tv_investDate;
    private TextView tv_buyDate;
    private TextView tv_investTotalMoney;
    private TextView tv_arrreadyGetIntrested;
    private Button btn_invest;
    private YCLoadingBg ycLoadingBg;
    private UserBizHelper userBizHelper;
    private String laon_typeId;
    private String product_id;
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
        setContentView(R.layout.activity_investing);
        userBizHelper = new UserBizHelper(this);
        mIntent = getIntent();
        if (mIntent != null) {
            product_id = mIntent.getExtras().getString(PRODUCT_ID);
            invest_id = mIntent.getExtras().getString(INVEST_ID);
            laon_typeId= mIntent.getExtras().getString(LOAN_TYPEID);
            can_invest= mIntent.getExtras().getBoolean(CAN_INVEST,true);
        }
    }

    @Override
    public void setUpViews() {
        ycLoadingBg = (YCLoadingBg) findViewById(R.id.ycLoadingBg);
        btn_invest = (Button) findViewById(R.id.btn_invest);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setText(getResources().getString(R.string.my_invest_str));
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getResources().getString(R.string.investing));
        tv_title.setVisibility(View.VISIBLE);
        tv_back.setVisibility(View.VISIBLE);

        tv_productName = (TextView) findViewById(R.id.tv_04);
        tv_returnmoney_way = (TextView) findViewById(R.id.tv_07);
        tv_yearOfrate = (TextView) findViewById(R.id.tv_05);
        tv_investDate = (TextView) findViewById(R.id.tv_06);
        tv_buyDate = (TextView) findViewById(R.id.tv_08);
        tv_investTotalMoney = (TextView) findViewById(R.id.tv_03);
        tv_arrreadyGetIntrested = (TextView) findViewById(R.id.tv_02);

        if(can_invest)
        {
            btn_invest.setEnabled(true);
        }
        else {
            btn_invest.setEnabled(false);
        }
    }

    @Override
    public void setUpLisener() {
        tv_back.setOnClickListener(this);
        btn_invest.setOnClickListener(this);
    }

    private String invest_id;
    private MyInvestDetailsBean myInvestDetailsBean;

    @Override
    public void getDataOnCreate() {

        addSubscription(RetrofitClient.GetMyInvestDeatils(null, MyInvestStateEnum.INVESTING.getValue() + "", invest_id, this, new YCNetSubscriber<MyInvestDetailsBean>(this) {

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
        tv_buyDate.setText(myInvestDetailsBean.buyTime);
        tv_investTotalMoney.setText(myInvestDetailsBean.totalInvestMoeny);
        tv_arrreadyGetIntrested.setText(myInvestDetailsBean.interestReceivable);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.btn_invest:
                TurnToInvestBidActivity();
                break;
        }
    }

    private void TurnToInvestBidActivity() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(InvestBidActivity.INVEST_TURN_TO_ACTIVITY_CLASS,InvestingActivity.class);
        bundle.putString(InvestBidActivity.SHOW_BACK_TXT, MyInvestStateEnum.INVESTING.getName());
        bundle.putString(InvestBidActivity.PRODUCT_ID,product_id );
        bundle.putString(InvestBidActivity.LOAN_TYPEID, laon_typeId);
        JumpCenter.JumpActivity(this, InvestBidActivity.class, bundle,null,JumpCenter.NORMALL_REQUEST,JumpCenter.INVAILD_FLAG,false,true);
    }

}
