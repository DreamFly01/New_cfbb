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
import com.cfbb.android.commom.state.VertifyCodeEnum;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.features.account.AccountSetActivity;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.VertifyCodeInfoBean;
import com.cfbb.android.widget.YCIdentifyCodeView;
import com.cfbb.android.widget.dialog.YCDialogUtils;

/**
 * 修改手机号码
 */
public class ChangeMobilePhoneStepTwoActivity extends BaseActivity  {

    private TextView tv_back;
    private TextView tv_title;
    private Button btn_ok;
    private EditText et_tel;
    private EditText et_code;
    private YCDialogUtils ycDialogUtils;

    private YCIdentifyCodeView ycIdentifyCodeView;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_change_mobile_phone_step_two);
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
        tv_title.setText(getResources().getString(R.string.modify_bind_phone_str));
        tv_back.setText(getResources().getString(R.string.original_mobile_check_str));
        tv_back.setVisibility(View.VISIBLE);

        et_tel.addTextChangedListener(mTextWatcher);
        et_code.addTextChangedListener(mTextWatcher2);

    }

    @Override
    public void setUpLisener() {
        tv_back.setOnClickListener(this);
        ycIdentifyCodeView.setActivity(this);
        ycIdentifyCodeView.setEt_tel(et_tel);
        btn_ok.setOnClickListener(this);
        ycIdentifyCodeView.setYcIdentifyCodeViewLisener(new YCIdentifyCodeView.YCIdentifyCodeViewLisener() {
            @Override
            public void onYCIdentifyCodeViewClickLisener() {

                //TestResultUtils.getSussefulResult37()
                addSubscription(RetrofitClient.GeVertifyCode(null, et_tel.getText().toString().trim(), VertifyCodeEnum.SET_NEW_PHONE_CODE.getValue(), ChangeMobilePhoneStepTwoActivity.this, new YCNetSubscriber<VertifyCodeInfoBean>(ChangeMobilePhoneStepTwoActivity.this, true) {

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
        addSubscription(RetrofitClient.ChangeBindMobile(null, tel, code, this, new YCNetSubscriber(this, true) {

            @Override
            public void onYcNext(Object model) {
                showShortToast(R.string.modify_tel_right);
                JumpCenter.JumpActivity(ChangeMobilePhoneStepTwoActivity.this, AccountSetActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, true);
            }
        }));
    }

    private String code;
    private String tel;

    private boolean CheckInput() {
        code = et_code.getText().toString().trim();
        tel = et_tel.getText().toString().trim();
        if (StrUtil.isEmpty(tel)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.tel_cant_be_nempty), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }
        if (!StrUtil.checkMobile(tel)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.please_enter_right_tel), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }
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

            if (StrUtil.isEmpty(code) || StrUtil.isEmpty(et_tel.getText().toString().trim()) || !ycIdentifyCodeView.isGetVertifyCode()) {
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


    private TextWatcher mTextWatcher2 = new TextWatcher() {

        private int editStart;
        private String tel;

        public void afterTextChanged(Editable s) {

            tel = et_tel.getText().toString().trim();
            editStart = et_tel.getSelectionStart();

            // 先去掉监听器，否则会出现栈溢出
            et_tel.removeTextChangedListener(mTextWatcher2);
            et_tel.setSelection(editStart);
            // 恢复监听器
            et_tel.addTextChangedListener(mTextWatcher2);

            if (StrUtil.isEmpty(tel) || StrUtil.isEmpty(et_tel.getText().toString().trim()) || !ycIdentifyCodeView.isGetVertifyCode()) {
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
