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

/**
 * 充值成功
 */
public class RechargeRightActivity extends BaseActivity {

    public static final String RECHARGE_RIGHT_TURN_TO_ACTIVITY_CLASS = "recharge_right_turn_to_activity_class";
    public static final String RECHARGE_RIGHT_TURN_TO_MAIN_INDEX = "withdraw_right_turn_to_activity_intent_action";

    public static final String RECHARGE_MOENY = "recharge_moeny";
    public static final String RECHARGE_BANK_STR = "recharge_bank_str";

    private TextView tv_back;
    private TextView tv_title;
    private TextView tv_moeny;
    private TextView tv_bank_str;
    private Button btn_ok;
    private int index;
    private String moeny;
    private String bank_str;
    private Class turnToActivity;
    private Bundle bundle;

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
        setContentView(R.layout.activity_recharge_right);
        mIntent = getIntent();
        if (mIntent != null) {
            bundle = mIntent.getExtras();
            bank_str = bundle.getString(RECHARGE_BANK_STR);
            moeny = bundle.getString(RECHARGE_MOENY);
            turnToActivity = (Class) bundle.getSerializable(RECHARGE_RIGHT_TURN_TO_ACTIVITY_CLASS);
            index = bundle.getInt(RECHARGE_RIGHT_TURN_TO_MAIN_INDEX, MainFragmentEnum.INVALID.getValue());

        }
    }

    @Override
    public void setUpViews() {
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setText(getResources().getString(R.string.recharge));
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getResources().getString(R.string.recharge_right));
        tv_back.setVisibility(View.VISIBLE);
        tv_moeny = (TextView) findViewById(R.id.tv_04);
        tv_bank_str = (TextView) findViewById(R.id.tv_06);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        tv_moeny.setText(moeny);
        tv_bank_str.setText(bank_str);
    }

    @Override
    public void setUpLisener() {
        tv_back.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void onUserClick(View v) {
        super.onUserClick(v);
        switch (v.getId()) {
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

        if (turnToActivity == null) {

            Bundle bundle = new Bundle();
            if (index != MainFragmentEnum.INVALID.getValue()) {
                bundle.putInt(MainActivity.SHOW_FRAGMENT_INDEX, index);
            } else {
                bundle.putInt(MainActivity.SHOW_FRAGMENT_INDEX, MainFragmentEnum.ACCOUNT.getValue());
            }
            JumpCenter.JumpActivity(RechargeRightActivity.this, MainActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, true);

        } else {

            JumpCenter.JumpActivity(RechargeRightActivity.this, turnToActivity, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, true);

        }

    }
}
