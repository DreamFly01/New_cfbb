package com.cfbb.android.features.gesture;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cfbb.android.R;
import com.cfbb.android.app.MyApplication;
import com.cfbb.android.commom.state.IsOpenGestureEnum;
import com.cfbb.android.commom.state.MainFragmentEnum;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.commom.utils.image.ImageWithGlideUtils;
import com.cfbb.android.commom.utils.others.SPUtils;
import com.cfbb.android.commom.utils.others.Utils;
import com.cfbb.android.db.user.UserBiz;
import com.cfbb.android.features.main.MainActivity;


/**
 * 手势密码设置界面
 */
public class GestureEditActivity extends Activity implements OnClickListener {

    public static final String IS_SHOW_BACK = "is_show_back";
    public static final String IS_FINISH_ACTIVITY = "is_finish_activity";

    private TextView mTextTip;
    private TextView mTextTipFrist;
    private FrameLayout mGestureContainer;
    private GestureContentView mGestureContentView;
    private boolean mIsFirstInput = true;
    private String mFirstPassword = null;
    private TextView mTextPhoneNumber;
    private ImageView mImgUserLogo;
    private TextView tv_back;
    private Intent intent;
    private boolean isShow_back = false;
    private boolean isFinishActivity = false;

    private LinearLayout ll_hint;
    private ImageView iv_exclamation;

    private Bundle bundle;
    private TextView tv_reset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.SetFullScreen(this);
        setContentView(R.layout.activity_gesture_edit);
        intent = getIntent();
        if (intent != null) {
            bundle = intent.getExtras();
            isShow_back = bundle.getBoolean(IS_SHOW_BACK, false);
            isFinishActivity = bundle.getBoolean(IS_FINISH_ACTIVITY, false);
        }
        setUpViews();
        setUpListeners();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 设置用户名以及头像
        mTextPhoneNumber.setText(UserBiz.getInstance(this).GetUserName());
        ImageWithGlideUtils.lodeFromUrlRoundTransform(UserBiz.getInstance(this).GetUserImg(), R.mipmap.default_user_photo_large_bg, mImgUserLogo, this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    private void setUpViews() {

        tv_reset = (TextView) findViewById(R.id.text_reset);
        ll_hint = (LinearLayout) findViewById(R.id.ll_hint);
        iv_exclamation = (ImageView) findViewById(R.id.iv_exclamation);

        mTextTipFrist = (TextView) findViewById(R.id.text_tip2);
        tv_back = (TextView) findViewById(R.id.tv_junp);
        if (!isShow_back) {
            tv_back.setText("跳过");
            mTextTipFrist.setVisibility(View.VISIBLE);
        } else {
            tv_back.setText("取消");
            mTextTipFrist.setVisibility(View.INVISIBLE);
        }

        mImgUserLogo = (ImageView) findViewById(R.id.user_logo);
        mTextPhoneNumber = (TextView) findViewById(R.id.text_phone_number);
        mTextTip = (TextView) findViewById(R.id.text_tip);
        mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
        // 初始化一个显示各个点的viewGroup
        mGestureContentView = new GestureContentView(this, false, "",
                new GestureDrawline.GestureCallBack() {
                    @Override
                    public void onGestureCodeInput(String inputCode) {

                        mTextTipFrist.setVisibility(View.INVISIBLE);

                        if (!isInputPassValidate(inputCode)) {
                            iv_exclamation.setVisibility(View.VISIBLE);
                            mTextTip.setText(Html
                                    .fromHtml("<font color='#c70c1e'>密码位数不足四位, 请重新绘制</font>"));
                            mGestureContentView.clearDrawlineState(0L);
                            return;
                        }
                        if (mIsFirstInput) {
                            iv_exclamation.setVisibility(View.INVISIBLE);
                            mFirstPassword = inputCode;
                            mGestureContentView.clearDrawlineState(0L);
                            mTextTip.setText("请再次绘制手势密码");
                        } else {
                            if (inputCode.equals(mFirstPassword)) {
                                setGestrueResult();

                            } else {
                                iv_exclamation.setVisibility(View.VISIBLE);
                                mTextTip.setText(Html
                                        .fromHtml("<font color='#c70c1e'>与首次绘制不一致，请重新绘制</font>"));
                                // 左右移动动画
                                Animation shakeAnimation = AnimationUtils
                                        .loadAnimation(
                                                GestureEditActivity.this,
                                                R.anim.shake);
                                ll_hint.startAnimation(shakeAnimation);
                                // 保持绘制的线，1.5秒后清除
                                mGestureContentView.clearDrawlineState(1300L);
                            }
                        }
                        mIsFirstInput = false;
                    }

                    @Override
                    public void checkedSuccess() {

                    }

                    @Override
                    public void checkedFail() {

                    }
                });
        // 设置手势解锁显示到哪个布局里面
        mGestureContentView.setParentView(mGestureContainer);
    }

    private void setGestrueResult() {
        Toast.makeText(GestureEditActivity.this,
                "您的手势密码设置成功", Toast.LENGTH_SHORT).show();
        mGestureContentView.clearDrawlineState(0L);
        // 手势密码设置成功,保存到sqlite,SP
        UserBiz.getInstance(GestureEditActivity.this).UpdateUserGesturePassword(mFirstPassword);
        UserBiz.getInstance(GestureEditActivity.this).UpdateUserGestureState(IsOpenGestureEnum.OPEN.getValue());
        MyApplication.getInstance().setGestureCanShow(false);
        SPUtils.put(GestureEditActivity.this,  UserBiz.getInstance(GestureEditActivity.this).GetUserName(),mFirstPassword+"."+IsOpenGestureEnum.OPEN.getValue());

        if (!isFinishActivity) {
            if (bundle != null) {
                bundle.putInt(MainActivity.SHOW_FRAGMENT_INDEX, bundle.getInt(MainActivity.SHOW_FRAGMENT_INDEX, MainFragmentEnum.HOME.getValue()));
                Class toActivity = (Class) bundle.getSerializable(JumpCenter.TO_ACTIVITY);
                if (toActivity == null) {
                    toActivity = MainActivity.class;
                }
                JumpCenter.JumpActivity(GestureEditActivity.this, toActivity, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, true);
            } else {
                bundle = new Bundle();
                bundle.putInt(MainActivity.SHOW_FRAGMENT_INDEX, MainFragmentEnum.HOME.getValue());
                JumpCenter.JumpActivity(GestureEditActivity.this, MainActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, Intent.FLAG_ACTIVITY_CLEAR_TASK, false, true);
            }


        }
        GestureEditActivity.this.finish();
    }

    private void setUpListeners() {
        tv_back.setOnClickListener(this);
        tv_reset.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //跳过
            case R.id.tv_junp:
                if (!isFinishActivity) {
                    if (bundle != null) {
                        bundle.putInt(MainActivity.SHOW_FRAGMENT_INDEX, bundle.getInt(MainActivity.SHOW_FRAGMENT_INDEX, MainFragmentEnum.HOME.getValue()));
                        Class toActivity = (Class) bundle.getSerializable(JumpCenter.TO_ACTIVITY);
                        if (toActivity == null) {
                            toActivity = MainActivity.class;
                        }
                        JumpCenter.JumpActivity(GestureEditActivity.this, toActivity, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
                    } else {
                        bundle = new Bundle();
                        bundle.putInt(MainActivity.SHOW_FRAGMENT_INDEX, MainFragmentEnum.HOME.getValue());
                        JumpCenter.JumpActivity(GestureEditActivity.this, MainActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, Intent.FLAG_ACTIVITY_CLEAR_TASK, false, true);
                    }
                }
                this.finish();
                break;
            case R.id.text_reset:
                mIsFirstInput = true;
                iv_exclamation.setVisibility(View.GONE);
                mTextTip.setText(getString(R.string.reset_gesture_code));
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {

    }

    private boolean isInputPassValidate(String inputPassword) {
        if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
