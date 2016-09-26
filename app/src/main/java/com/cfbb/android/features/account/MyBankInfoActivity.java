package com.cfbb.android.features.account;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.utils.image.ImageWithGlideUtils;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.MyBankInfoBean;
import com.cfbb.android.widget.dialog.YCDialogUtils;

/**
 * 我的银行卡
 */
public class MyBankInfoActivity extends BaseActivity {

    public static final String MY_BANK_INFO = "my_bank_info";

    private TextView tv_back;
    private TextView tv_title;
    private YCDialogUtils ycDialogUtils;
    private MyBankInfoBean myBankInfo;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_bank_info);
        mIntent = getIntent();
        if (mIntent != null) {
            myBankInfo = mIntent.getParcelableExtra(MY_BANK_INFO);
        }
    }


    private ImageView iv_bankLogo;
    private TextView tv_bankName;
    private ImageView iv_deleteViewBtn;
    private TextView tv_bankNum;
    private TextView tv_userName;
    private TextView tv_inUseMoney;
    private ImageView iv_arrow;
    private TextView tv_accountBlance;
    private TextView tv_holdingMoney;
    private TextView tv_investingMoney;
    private TextView tv_hint;
    private LinearLayout ll_moneyDetails;
    private LinearLayout getLl_moneyDetailsContent;

    @Override
    public void setUpViews() {


        ycDialogUtils = new YCDialogUtils(this);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_back.setText(getResources().getString(R.string.nav_account));
        tv_back.setVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.my_bankcard_list_str));

        iv_bankLogo = (ImageView) findViewById(R.id.iv_01);
        tv_bankName = (TextView) findViewById(R.id.tv_10);
        iv_deleteViewBtn = (ImageView) findViewById(R.id.iv_02);
        tv_bankNum = (TextView) findViewById(R.id.tv_11);
        tv_userName = (TextView) findViewById(R.id.tv_05);
        tv_inUseMoney = (TextView) findViewById(R.id.tv_12);
        iv_arrow = (ImageView) findViewById(R.id.iv_03);
        tv_accountBlance = (TextView) findViewById(R.id.tv_13);
        tv_holdingMoney = (TextView) findViewById(R.id.tv_14);
        tv_investingMoney = (TextView) findViewById(R.id.tv_15);
        tv_hint = (TextView) findViewById(R.id.tv_16);
        ll_moneyDetails = (LinearLayout) findViewById(R.id.ll_01);
        getLl_moneyDetailsContent = (LinearLayout) findViewById(R.id.ll_02);
        FillView();

    }

    private void FillView() {
        if (null != myBankInfo) {
            ImageWithGlideUtils.lodeFromUrl(myBankInfo.imageUrl, R.mipmap.yuan_logo, iv_bankLogo, this);
            tv_bankName.setText(myBankInfo.bankName);
            tv_bankNum.setText(myBankInfo.bankNum);
            tv_userName.setText(myBankInfo.realName);
            tv_inUseMoney.setText(myBankInfo.inUseMoeny);
            tv_accountBlance.setText(myBankInfo.accountBalance);
            tv_holdingMoney.setText(myBankInfo.holdingMoney);
            tv_investingMoney.setText(myBankInfo.investingMoney);
            tv_hint.setText(Html.fromHtml(myBankInfo.inUseMoeny));
        }
    }

    @Override
    public void setUpLisener() {
        tv_back.setOnClickListener(this);
        iv_deleteViewBtn.setOnClickListener(this);
        ll_moneyDetails.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }


    @Override
    public void onUserClick(View v) {
        super.onUserClick(v);
        switch (v.getId()) {

            case R.id.tv_back:
                finish();
                break;
            // 解绑
            case R.id.iv_02:
                DeleteBank();
                break;
            //下拉显示
            case R.id.ll_01:
                if (getLl_moneyDetailsContent.getVisibility() == View.VISIBLE) {
                    iv_arrow.setImageResource(R.mipmap.arrow_red_up);
                    getLl_moneyDetailsContent.setVisibility(View.GONE);
                } else {
                    iv_arrow.setImageResource(R.mipmap.arrow_red_down);
                    getLl_moneyDetailsContent.setVisibility(View.VISIBLE);
                }
                break;

        }
    }


    private void DeleteBank() {

        ycDialogUtils.showDialog(getResources().getString(R.string.dialog_title), getString(R.string.sure_to_delete_bank), new YCDialogUtils.MyTwoBtnclickLisener() {
            @Override
            public void onFirstBtnClick(View v) {
                //ok
                ycDialogUtils.DismissMyDialog();
                addSubscription(RetrofitClient.DeleteBank(null, myBankInfo.bankCardId, MyBankInfoActivity.this, new YCNetSubscriber(MyBankInfoActivity.this, true) {

                    @Override
                    public void onYcNext(Object model) {
                        //跳转绑定
                    }

                }));
            }

            @Override
            public void onSecondBtnClick(View v) {
                ycDialogUtils.DismissMyDialog();
            }
        }, true);

    }


    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
