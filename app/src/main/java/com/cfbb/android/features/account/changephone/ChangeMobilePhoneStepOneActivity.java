package com.cfbb.android.features.account.changephone;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.textwatch.TextWatchForNoBlank;
import com.cfbb.android.commom.state.VertifyCodeEnum;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.BindPhoneBean;
import com.cfbb.android.protocol.bean.VertifyCodeInfoBean;
import com.cfbb.android.widget.YCIdentifyCodeView;
import com.cfbb.android.widget.YCLoadingBg;
import com.cfbb.android.widget.dialog.YCDialogUtils;

/***
 * 修改手机号码第一步
 */
public class ChangeMobilePhoneStepOneActivity extends BaseActivity  {

    private TextView tv_back;
    private TextView tv_title;
    private YCIdentifyCodeView ycIdentifyCodeView;
    private Button btn_ok;
    private EditText et_code;
    private TextView tv_y;
    private TextView tv_mobile;
    private YCLoadingBg ycLoadingBg;
    private YCDialogUtils ycDialogUtils;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_change_mobile_phone_step_one);
    }

    @Override
    public void setUpViews() {

        ycDialogUtils = new YCDialogUtils(this);
        ycLoadingBg = (YCLoadingBg) findViewById(R.id.ycLoadingBg);
        tv_mobile = (TextView) findViewById(R.id.tv_02);
        tv_y = (TextView) findViewById(R.id.tv_03);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        et_code = (EditText) findViewById(R.id.et_01);
        ycIdentifyCodeView = (YCIdentifyCodeView) findViewById(R.id.yCIdentifyCodeView);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        tv_title.setText(getResources().getString(R.string.original_mobile_check_str));
        tv_back.setText(getResources().getString(R.string.account_set_str));
        tv_back.setVisibility(View.VISIBLE);
        et_code.addTextChangedListener(mTextWatcher);
        et_code.addTextChangedListener(new TextWatchForNoBlank());
    }

    @Override
    public void setUpLisener() {
        tv_back.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        tv_y.setOnClickListener(this);
        ycIdentifyCodeView.setActivity(this);
        ycIdentifyCodeView.setYcIdentifyCodeViewLisener(new YCIdentifyCodeView.YCIdentifyCodeViewLisener() {
            @Override
            public void onYCIdentifyCodeViewClickLisener() {
                //TestResultUtils.getSussefulResult37()
                addSubscription(RetrofitClient.GeVertifyCode(null, "", VertifyCodeEnum.MODIFY_PHONE_CODE.getValue(), ChangeMobilePhoneStepOneActivity.this, new YCNetSubscriber<VertifyCodeInfoBean>(ChangeMobilePhoneStepOneActivity.this, true) {

                    @Override
                    public void onYcNext(VertifyCodeInfoBean model) {
                        ycIdentifyCodeView.StartCountDown(model.millisecond);
                    }
                }));

            }
        });
    }

    private BindPhoneBean bindPhone;

    @Override
    public void getDataOnCreate() {
        //TestResultUtils.getSussefulResult10()
        addSubscription(RetrofitClient.GetBindMobileInfo(null, this, new YCNetSubscriber<BindPhoneBean>(this) {

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
            public void onYcNext(BindPhoneBean model) {
                bindPhone = model;
            }

            @Override
            public void onYcFinish() {
                tv_mobile.setText(bindPhone.mobilePhone);
                ycLoadingBg.dissmiss();
            }
        }));
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
            //实名认证
            case R.id.tv_03:
                JumpCenter.JumpActivity(this, CheckUserActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
                break;
        }
    }

    private void Submit() {
        //TestResultUtils.getBaseSussefulResult()
        addSubscription(RetrofitClient.ModifyOriginalPhone(null, code, this, new YCNetSubscriber(this, true) {

            @Override
            public void onYcNext(Object model) {
                //跳转下一步
                JumpCenter.JumpActivity(ChangeMobilePhoneStepOneActivity.this, ChangeMobilePhoneStepTwoActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
            }
        }));
    }

    private String code;

    private boolean CheckInput() {
        code = et_code.getText().toString().trim();
        if (StrUtil.isEmpty(code)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.please_enter_ems_code), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }
        if (!ycIdentifyCodeView.isGetVertifyCode()) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.please_get_ems_code), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }
        return true;
    }

    private TextWatcher mTextWatcher = new TextWatcher() {

        private int editStart;
        private String code;

        public void afterTextChanged(Editable s) {

            code = et_code.getText().toString().trim();
            editStart = et_code.getSelectionStart();

            // 先去掉监听器，否则会出现栈溢出
            et_code.removeTextChangedListener(mTextWatcher);
            et_code.setSelection(editStart);
            // 恢复监听器
            et_code.addTextChangedListener(mTextWatcher);

            if (StrUtil.isEmpty(code) || !ycIdentifyCodeView.isGetVertifyCode()) {
                btn_ok.setEnabled(false);
            } else {
                btn_ok.setEnabled(true);
            }


        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

    };

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
