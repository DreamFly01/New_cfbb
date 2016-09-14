package com.cfbb.android.features.authentication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.textwatch.TextWatchForNoBlank;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.features.webview.OtherActivity;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.ServiceTermBean;
import com.cfbb.android.widget.dialog.YCDialogUtils;
import com.smileback.safeinputlib.IJMInputEditText;

/***
 * 注册第一步
 */
public class RegisterStepOneActivity extends BaseActivity  {

    private TextView tv_back;
    private TextView tv_title;
    private EditText et_referrer;
    private EditText et_name;
    private IJMInputEditText et_password;
    private IJMInputEditText et_password_again;
    private TextView tv_xy;
    private CheckBox cb_is_agree;
    private Button btn_ok;
    private YCDialogUtils ycDialogUtils;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register_step_one);
    }

    @Override
    public void setUpViews() {

        ycDialogUtils = new YCDialogUtils(this);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_back.setVisibility(View.VISIBLE);
        tv_back.setText(getResources().getString(R.string.login));
        tv_title.setText(getResources().getString(R.string.register));
        btn_ok = (Button) findViewById(R.id.btn_ok);
        et_name = (EditText) findViewById(R.id.et_01);
        et_password = (IJMInputEditText) findViewById(R.id.et_02);
        et_password_again = (IJMInputEditText) findViewById(R.id.et_03);
        et_referrer = (EditText) findViewById(R.id.et_04);
        tv_xy = (TextView) findViewById(R.id.tv_02);
        cb_is_agree = (CheckBox) findViewById(R.id.cb_01);
        et_password.setKeyboardMaxLength(22);
        et_password_again.setKeyboardMaxLength(22);
        et_name.addTextChangedListener(new TextWatchForNoBlank());
        et_password.addTextChangedListener(new TextWatchForNoBlank());
        et_password_again.addTextChangedListener(new TextWatchForNoBlank());
        et_referrer.addTextChangedListener(new TextWatchForNoBlank());
    }

    private boolean is_agree = false;

    @Override
    public void setUpLisener() {
        btn_ok.setOnClickListener(this);
        tv_back.setOnClickListener(this);
        tv_xy.setOnClickListener(this);
        cb_is_agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                is_agree = isChecked;
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
                    addSubscription(RetrofitClient.CheckRegisterStep(null, this, name, password, et_referrer.getText().toString().trim(), new YCNetSubscriber(this, true) {

                        @Override
                        public void onYcNext(Object model) {
                            Bundle bundle = new Bundle();
                            bundle.putString(RegisterStepTwoActivity.USER_NAME, name);
                            bundle.putString(RegisterStepTwoActivity.PASSWORD, password);
                            bundle.putString(RegisterStepTwoActivity.REFERRER, et_referrer.getText().toString().trim());
                            JumpCenter.JumpActivity(RegisterStepOneActivity.this, RegisterStepTwoActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, false);
                        }
                    }));
                }
                break;
            //协议
            case R.id.tv_02:
                if (null != serviceTermBean) {
                    Bundle bundle = new Bundle();
                    bundle.putString(OtherActivity.BACK_STR, getResources().getString(R.string.register));
                    bundle.putString(OtherActivity.URL, serviceTermBean.serviceUrl);
                    JumpCenter.JumpActivity(this, OtherActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, false);
                }
                break;
        }
    }



    private String name;
    private String password;
    private String password_again;

    private boolean CheckInput() {

        name = et_name.getText().toString();
        password = et_password.getKeyboardText().toString();
        password_again = et_password_again.getKeyboardText().toString();

        if (StrUtil.isEmpty(name)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getString(R.string.input_tips_user_cant_be_nempty), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }


        if (StrUtil.isEmpty(password)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getString(R.string.input_tips_password_cant_be_nempty), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }


        if (StrUtil.isEmpty(password_again)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getString(R.string.check_new_psw_cant_be_empty), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }
        if (!password_again.equals(password)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getString(R.string.twice_psw_are_not_same), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }


        if (!is_agree) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getString(R.string.please_agree_compay_rules), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }

        return true;

    }

    private ServiceTermBean serviceTermBean;

    @Override
    public void getDataOnCreate() {
        addSubscription(RetrofitClient.RegisterServiceTerm(null, this, new YCNetSubscriber<ServiceTermBean>(this) {

            @Override
            public void onYcNext(ServiceTermBean model) {
                serviceTermBean = model;
                tv_xy.setOnClickListener(RegisterStepOneActivity.this);
            }
        }));
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
