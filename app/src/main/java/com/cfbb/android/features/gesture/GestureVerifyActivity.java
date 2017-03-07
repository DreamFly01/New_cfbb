package com.cfbb.android.features.gesture;

import android.app.Activity;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.app.MyApplication;
import com.cfbb.android.commom.config.Const;
import com.cfbb.android.commom.state.MainFragmentEnum;
import com.cfbb.android.commom.utils.image.ImageWithGlideUtils;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.commom.utils.others.SPUtils;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.db.user.UserBiz;
import com.cfbb.android.commom.utils.others.Utils;
import com.cfbb.android.features.account.AccountSetActivity;
import com.cfbb.android.features.authentication.LoginActivity;
import com.cfbb.android.features.main.MainActivity;
import com.cfbb.android.widget.dialog.YCDialogUtils;


/**
 * 手势绘制/校验界面
 */
public class GestureVerifyActivity extends Activity implements
        View.OnClickListener {

    public static final String TITLE_STR = "title_str";
    public static final String IS_CAN_CANCEL = "is_can_cancel";
    public static final String IS_MODIFY = "is_modify";

    private TextView mTextCancel;
    private TextView mTextTip;
    private FrameLayout mGestureContainer;
    private GestureContentView mGestureContentView;
    private TextView mTextForget;
    private TextView mTextOther;
    private Intent intent;
    private LinearLayout ll_hint;

    private int gueture_times = 3;
    private int pre_date_month = 0;
    private int curren_date_month = 0;

    private boolean is_can_cancel = false;
    private boolean is_modify = false;

    private ImageView user_logo;
    private TextView text_phone_number;

    private String title;
    private String key;
    private TextView tv_title;

    private YCDialogUtils ycDialogUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.SetFullScreen(this);
        setContentView(R.layout.activity_gesture_verify);
        ObtainExtraData();
        setUpViews();
        setUpListeners();


        String strResult = (String) SPUtils.get(this, Const.GESTURE_TIMES,
                "3,0");
        gueture_times = Integer.parseInt(strResult.split(",")[0]);
        pre_date_month = Integer.parseInt(strResult.split(",")[1]);
        Time t = new Time(); // or Time t=new Time("GMT+8");
        t.setToNow(); // 取得系统时间。
        curren_date_month = t.monthDay;
        if (curren_date_month != pre_date_month) {
            gueture_times = 3;
        }

    }

    private void ObtainExtraData() {

        intent = getIntent();
        if (null != intent &&  intent.getExtras() != null) {
            title = intent.getExtras().getString(TITLE_STR);
            is_can_cancel = intent.getExtras().getBoolean(IS_CAN_CANCEL, false);
            is_modify = intent.getExtras().getBoolean(IS_MODIFY, false);
            key  = intent.getStringExtra("key");
        }


    }

    private ServiceConnection serviceConnection;

    private void setUpViews() {

        ycDialogUtils = new YCDialogUtils(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        if (!StrUtil.isEmpty(title)) {
            tv_title.setText(title);
            tv_title.setVisibility(View.VISIBLE);
        }
        text_phone_number = (TextView) findViewById(R.id.text_phone_number);
        user_logo = (ImageView) findViewById(R.id.user_logo);
        ll_hint = (LinearLayout) findViewById(R.id.ll_hint);
        mTextCancel = (TextView) findViewById(R.id.text_cancel);
        if (is_can_cancel) {
            mTextCancel.setVisibility(View.VISIBLE);
        } else {
            mTextCancel.setVisibility(View.INVISIBLE);
        }
        mTextTip = (TextView) findViewById(R.id.text_tip);
        mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
        mTextForget = (TextView) findViewById(R.id.text_forget_gesture);
        mTextOther = (TextView) findViewById(R.id.text_other_account);

        // 初始化一个显示各个点的viewGroup
        mGestureContentView = new GestureContentView(this, true,
                UserBiz.getInstance(this).GetGesturePassWord(),
                new GestureDrawline.GestureCallBack() {

                    @Override
                    public void onGestureCodeInput(String inputCode) {

                    }

                    @Override
                    public void checkedSuccess() {
                        MyApplication.getInstance().setGestureCanShow(false);
                        SPUtils.put(GestureVerifyActivity.this,
                                Const.GESTURE_TIMES, "3," + curren_date_month);
                        mGestureContentView.clearDrawlineState(0L);


                        SPUtils.put(GestureVerifyActivity.this, Const.GESTURE_SHOW_TIME, Long.valueOf(System.currentTimeMillis()));


                        if (is_modify) {
                            Bundle bundle = new Bundle();
                            bundle.putBoolean(GestureEditActivity.IS_SHOW_BACK, true);
                            bundle.putBoolean(GestureEditActivity.IS_FINISH_ACTIVITY, true);
                            JumpCenter.JumpActivity(GestureVerifyActivity.this, GestureEditActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);

                        }
                        if(key!=null){
                            JumpCenter.JumpActivity(GestureVerifyActivity.this, MainActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);

                        }
                        GestureVerifyActivity.this.finish();
                    }

                    @Override
                    public void checkedFail() {
                        // 设置错误手势 动画时间
                        ll_hint.setVisibility(View.VISIBLE);
                        mGestureContentView.clearDrawlineState(800L);

                        if (gueture_times > 1) {
                            SPUtils.put(
                                    GestureVerifyActivity.this,
                                    Const.GESTURE_TIMES,
                                    gueture_times + ","
                                            + curren_date_month);
                            gueture_times--;
                            mTextTip.setText("密码绘制错误,还可绘制"
                                    + gueture_times
                                    + "次");
                            // 左右移动动画
                            Animation shakeAnimation = AnimationUtils
                                    .loadAnimation(GestureVerifyActivity.this,
                                            R.anim.shake);
                            shakeAnimation
                                    .setAnimationListener(new AnimationListener() {

                                        @Override
                                        public void onAnimationStart(
                                                Animation animation) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(
                                                Animation animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(
                                                Animation animation) {


                                        }
                                    });
                            ll_hint.startAnimation(shakeAnimation);
                        } else {
                            //跳转登录
                            SPUtils.put(
                                    GestureVerifyActivity.this,
                                    Const.GESTURE_TIMES, "3,"
                                            + curren_date_month);
                            ycDialogUtils.showSingleDialog(GestureVerifyActivity.this.getResources().getString(R.string.dialog_title), "超过输入限制次数,请重新登录!", new YCDialogUtils.MySingleBtnclickLisener() {
                                @Override
                                public void onBtnClick(View v) {
                                    ycDialogUtils.DismissMyDialog();
                                    UserBiz.getInstance(GestureVerifyActivity.this).ExitLogin();
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(MainActivity.SHOW_FRAGMENT_INDEX, MainFragmentEnum.HOME.getValue());
                                    JumpCenter.JumpActivity(GestureVerifyActivity.this, MainActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, Intent.FLAG_ACTIVITY_NEW_TASK, true, false);

                                }
                            }, true);
                            SPUtils.put(GestureVerifyActivity.this, UserBiz.getInstance(GestureVerifyActivity.this).GetUserName(), "");
                        }

                    }
                });

        // 设置手势解锁显示到哪个布局里面
        mGestureContentView.setParentView(mGestureContainer);

    }

    @Override
    protected void onResume() {
        super.onResume();
        text_phone_number.setText(UserBiz.getInstance(this).GetUserName());
        ImageWithGlideUtils.lodeFromUrlRoundTransform(UserBiz.getInstance(this).GetUserImg(), R.mipmap.default_user_photo_large_bg, user_logo, this);
    }

    private void setUpListeners() {

        mTextCancel.setOnClickListener(this);
        mTextForget.setOnClickListener(this);
        mTextOther.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_cancel:
                this.finish();
                break;
            // 忘记手势密码
            case R.id.text_forget_gesture:
            // 其它账户登录
            case R.id.text_other_account:
                SPUtils.put(GestureVerifyActivity.this, UserBiz.getInstance(GestureVerifyActivity.this).GetUserName(), "");
                JumpCenter.JumpActivity(GestureVerifyActivity.this, LoginActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, false);
                UserBiz.getInstance(this).ClearLoginState();
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != serviceConnection) {
            unbindService(serviceConnection);
        }
    }
}
