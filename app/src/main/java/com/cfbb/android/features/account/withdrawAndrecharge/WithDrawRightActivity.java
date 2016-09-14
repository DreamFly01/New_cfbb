package com.cfbb.android.features.account.withdrawAndrecharge;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.state.MainFragmentEnum;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.features.main.MainActivity;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.WithdrawRightInfoBean;
import com.cfbb.android.widget.YCLoadingBg;


/***
 * 提现成功界面
 */
public class WithDrawRightActivity extends BaseActivity {

    /***
     * 跳转activity的class
     */
    public static final String WITHDRAW_RIGHT_TURN_TO_ACTIVITY_CLASS = "withdraw_right_turn_to_activity_class";
    public static final String WITHDRAW_RIGHT_TURN_TO_MAIN_INDEX = "withdraw_right_turn_to_main_index";
    public static final String WITHDRAWCASHID = "withdrawcashid";

    private TextView tv_back;
    private TextView tv_title;
    private TextView tv_menu;
    private TextView tv_moeny;
    private TextView tv_actul_moeny;
    private TextView tv_bank_str;
    private Button btn_ok;
    private int index;
    private String withdrawcashid;
    private YCLoadingBg ycLoadingBg;
    private Class turnToActivity;

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
        setContentView(R.layout.activity_with_draw_right);
        mIntent = getIntent();
        if (mIntent != null) {
            withdrawcashid = mIntent.getExtras().getString(WITHDRAWCASHID);
            turnToActivity = (Class) mIntent.getExtras().getSerializable(WITHDRAW_RIGHT_TURN_TO_ACTIVITY_CLASS);
            index = mIntent.getExtras().getInt(WITHDRAW_RIGHT_TURN_TO_MAIN_INDEX, MainFragmentEnum.INVALID.getValue());
        }
    }

    @Override
    public void setUpViews() {
        ycLoadingBg = (YCLoadingBg) findViewById(R.id.ycLoadingBg);
        tv_menu = (TextView) findViewById(R.id.tv_menu);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setText(getResources().getString(R.string.withdraw_str));
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getResources().getString(R.string.withdraw_right));
        tv_back.setVisibility(View.VISIBLE);
        tv_menu.setVisibility(View.VISIBLE);
        tv_moeny = (TextView) findViewById(R.id.tv_04);
        tv_actul_moeny = (TextView) findViewById(R.id.tv_05);
        tv_bank_str = (TextView) findViewById(R.id.tv_06);
        btn_ok = (Button) findViewById(R.id.btn_ok);
    }

    @Override
    public void setUpLisener() {
        tv_menu.setOnClickListener(this);
        tv_back.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
    }

    private WithdrawRightInfoBean withdrawRightInfo;

    @Override
    public void getDataOnCreate() {
        //TestResultUtils.getSussefulResult36()
        addSubscription(RetrofitClient.GetWithdrawRightInfo(null, withdrawcashid, this, new YCNetSubscriber<WithdrawRightInfoBean>(this) {

            @Override
            public void onYCError(APIException e){
                ycLoadingBg.showErroBg(new YCLoadingBg.YCErroLisener() {
                    @Override
                    public void onTryAgainClick() {
                        getDataOnCreate();
                    }
                });
            }

            @Override
            public void onYcNext(WithdrawRightInfoBean model) {
                withdrawRightInfo = model;
                FillView();
                ycLoadingBg.dissmiss();
            }

        }));
    }

    private void FillView() {
        tv_moeny.setText(withdrawRightInfo.withdrawMoney);
        tv_actul_moeny.setText(withdrawRightInfo.actualMoeny);
        tv_bank_str.setText(withdrawRightInfo.bankStr);
    }

    @Override
    public void onUserClick(View v) {
        super.onUserClick(v);
        switch (v.getId()) {
            //提现记录
            case R.id.tv_menu:
                JumpCenter.JumpActivity(WithDrawRightActivity.this, WithDrawRecordActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, true);
                break;
            case R.id.tv_back:
                finish();
                break;
            //完成
            case R.id.btn_ok:
                TurnToOtherActivity();
                break;
        }
    }


    private void TurnToOtherActivity() {
        Bundle bundle = new Bundle();
        if (turnToActivity == null) {
            if (index != MainFragmentEnum.INVALID.getValue()) {
                bundle.putInt(MainActivity.SHOW_FRAGMENT_INDEX, index);
            } else {
                bundle.putInt(MainActivity.SHOW_FRAGMENT_INDEX, MainFragmentEnum.ACCOUNT.getValue());
            }
            JumpCenter.JumpActivity(WithDrawRightActivity.this, MainActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, true);
        } else {
            JumpCenter.JumpActivity(WithDrawRightActivity.this, turnToActivity, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, true);
        }
    }

}
