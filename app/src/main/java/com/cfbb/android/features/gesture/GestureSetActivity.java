package com.cfbb.android.features.gesture;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.state.IsOpenGestureEnum;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.commom.utils.others.SPUtils;
import com.cfbb.android.db.user.UserBiz;
import com.cfbb.android.widget.YCToggleButton;

/***
 * 手势密码设置
 */
public class GestureSetActivity extends BaseActivity {

    private YCToggleButton ycToggleButton;
    private LinearLayout ll_modify;
    private TextView tv_back;
    private TextView tv_title;


    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_gesture_set);
    }

    @Override
    public void setUpViews() {
        ycToggleButton = (YCToggleButton) findViewById(R.id.yCToggleButton);
        ll_modify = (LinearLayout) findViewById(R.id.ll_02);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(R.string.gesture_password_str);
        tv_back.setText(R.string.account_set_str);
        tv_back.setVisibility(View.VISIBLE);
        if (UserBiz.getInstance(this).Is_Open_Gesture()) {
            ll_modify.setVisibility(View.VISIBLE);
            ycToggleButton.setChecked(true);

        } else {
            ll_modify.setVisibility(View.GONE);
            ycToggleButton.setChecked(false);
        }
    }

    @Override
    public void setUpLisener() {
        ycToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll_modify.setVisibility(View.VISIBLE);
                    UserBiz.getInstance(GestureSetActivity.this).UpdateUserGestureState(IsOpenGestureEnum.OPEN.getValue());
                } else {
                    ll_modify.setVisibility(View.GONE);
                    UserBiz.getInstance(GestureSetActivity.this).UpdateUserGestureState(IsOpenGestureEnum.CLOSE.getValue());
                }
            }
        });
        ll_modify.setOnClickListener(this);
        tv_back.setOnClickListener(this);
    }

    @Override
    public void onUserClick(View v) {
        super.onUserClick(v);
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            //修改手势密码
            case R.id.ll_02:
                Bundle bundle = new Bundle();
                bundle.putString(GestureVerifyActivity.TITLE_STR, getString(R.string.draw_original_gesture_password));
                bundle.putBoolean(GestureVerifyActivity.IS_CAN_CANCEL, true);
                bundle.putBoolean(GestureVerifyActivity.IS_MODIFY, true);
                JumpCenter.JumpActivity(this, GestureVerifyActivity.class, bundle,null,JumpCenter.NORMALL_REQUEST,JumpCenter.INVAILD_FLAG,false,true);

                break;
        }
    }


}
