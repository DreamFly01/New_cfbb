package com.cfbb.android.features.authentication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.config.Const;
import com.cfbb.android.commom.state.IsOpenGestureEnum;
import com.cfbb.android.commom.state.VertifyCodeEnum;
import com.cfbb.android.commom.textwatch.TextWatchForNoBlank;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.commom.utils.base.NetworkUtils;
import com.cfbb.android.commom.utils.others.SPUtils;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.db.base.UserBizHelper;
import com.cfbb.android.features.gesture.GestureEditActivity;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.UserBean;
import com.cfbb.android.protocol.bean.VertifyCodeInfoBean;
import com.cfbb.android.widget.YCIdentifyCodeView;
import com.cfbb.android.widget.dialog.YCDialogUtils;

/***
 * 注册第二步
 */
public class RegisterStepTwoActivity extends BaseActivity {

    public static final String USER_NAME = "user_name";
    public static final String PASSWORD = "password";
    public static final String REFERRER = "referrer";

    private TextView tv_back;
    private TextView tv_title;

    private Button btn_ok;
    private EditText et_tel;
    private EditText et_code;

    private String userName;
    private String password;
    private String referrer;
    private UserBizHelper userBizHelper;
    private YCIdentifyCodeView ycIdentifyCodeView;
    private YCDialogUtils ycDialogUtils;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register_step_two);
        mIntent = getIntent();
        if (null != mIntent) {
            userName = mIntent.getExtras().getString(USER_NAME);
            password = mIntent.getExtras().getString(PASSWORD);
            referrer = mIntent.getExtras().getString(REFERRER);
        }
    }

    @Override
    public void setUpViews() {

        ycDialogUtils = new YCDialogUtils(this);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        et_code = (EditText) findViewById(R.id.et_02);
        et_tel = (EditText) findViewById(R.id.et_01);

        ycIdentifyCodeView = (YCIdentifyCodeView) findViewById(R.id.yCIdentifyCodeView);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        tv_title.setText(getResources().getString(R.string.obile_check_str));
        tv_back.setText(getResources().getString(R.string.register));
        tv_back.setVisibility(View.VISIBLE);
        et_code.addTextChangedListener(new TextWatchForNoBlank());

        et_code.addTextChangedListener(new TextWatchForNoBlank());
        et_tel.addTextChangedListener(new TextWatchForNoBlank());
    }

    @Override
    public void setUpLisener() {
        tv_back.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        ycIdentifyCodeView.setActivity(this);
        ycIdentifyCodeView.setEt_tel(et_tel);
        ycIdentifyCodeView.setYcIdentifyCodeViewLisener(new YCIdentifyCodeView.YCIdentifyCodeViewLisener() {
            @Override
            public void onYCIdentifyCodeViewClickLisener() {
                //TestResultUtils.getSussefulResult37()
                addSubscription(RetrofitClient.GeVertifyCode(null, et_tel.getText().toString().trim(), VertifyCodeEnum.REGISTER_CODE.getValue(), RegisterStepTwoActivity.this, new YCNetSubscriber<VertifyCodeInfoBean>(RegisterStepTwoActivity.this, true) {
                    @Override
                    public void onYcNext(VertifyCodeInfoBean model) {
                        ycIdentifyCodeView.StartCountDown(model.millisecond);
                    }
                }));

            }
        });
    }


    @Override
    public void onUserClick(View v) {
        super.onUserClick(v);
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.btn_ok:
                if (CheckInput()) {
                    Submit();
                }
                break;
        }
    }


    private void Submit() {
        //TestResultUtils.getBaseSussefulResult()
        addSubscription(RetrofitClient.Register(null, userName, password, tel, code, referrer, NetworkUtils.getLocalIpAddress(), this, new YCNetSubscriber(this, true) {

            @Override
            public void onYcNext(Object model) {
                AutoLogin();
            }
        }));
    }

    private void AutoLogin() {
        //TestResultUtils.getSussefulResult16()
        addSubscription(RetrofitClient.loginRequest(null, userName, password, this, new YCNetSubscriber<UserBean>(RegisterStepTwoActivity.this, true) {

            @Override
            public void onYcNext(UserBean model) {
                SPUtils.put(RegisterStepTwoActivity.this, Const.PRE_USER_NAME, userName);
                userBizHelper = new UserBizHelper(RegisterStepTwoActivity.this);
                userBizHelper.StoreUserInfo(model,"", IsOpenGestureEnum.CLOSE.getValue());
                //注册成功跳转手势密码
                JumpCenter.JumpActivity(RegisterStepTwoActivity.this, GestureEditActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, true);

            }


            @Override
            public void onYCError(APIException e) {
                //自动登陆失败跳转登录页面
                showShortToast(R.string.auto_login_failed);
                JumpCenter.JumpActivity(RegisterStepTwoActivity.this, LoginActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, true);
            }
        }));
    }


    private String code;
    private String tel;

    private boolean CheckInput() {
        code = et_code.getText().toString().trim();
        tel = et_tel.getText().toString().trim();
        if (StrUtil.isEmpty(tel)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getString(R.string.tel_cant_be_nempty), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }
        if (!StrUtil.checkMobile(tel)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getString(R.string.please_enter_right_tel), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }
        if (StrUtil.isEmpty(code)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getString(R.string.please_enter_ems_code), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }
        if (!ycIdentifyCodeView.isGetVertifyCode()) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getString(R.string.please_get_ems_code), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }
        return true;
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
