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
import com.cfbb.android.commom.textwatch.TextWatchForOnlyChinese;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.widget.dialog.YCDialogUtils;


/***
 * 验证身份
 */
public class CheckUserActivity extends BaseActivity  {

    private TextView tv_back;
    private TextView tv_title;
    private EditText et_realName;
    private EditText et_idcard;
    private Button btn_ok;
    private YCDialogUtils ycDialogUtils;

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
        setContentView(R.layout.activity_check_user);
    }

    @Override
    public void setUpViews() {

        ycDialogUtils = new YCDialogUtils(this);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        et_realName = (EditText) findViewById(R.id.et_01);
        et_idcard = (EditText) findViewById(R.id.et_02);
        btn_ok = (Button) findViewById(R.id.btn_ok);

        tv_back.setVisibility(View.VISIBLE);
        tv_back.setText(getResources().getString(R.string.original_mobile_check_str));
        tv_title.setText(getResources().getString(R.string.identity_verification_str));

        et_realName.addTextChangedListener(mTextWatcher);
        et_idcard.addTextChangedListener(mTextWatcher2);

        et_realName.addTextChangedListener(new TextWatchForOnlyChinese());
        et_idcard.addTextChangedListener(new TextWatchForNoBlank());
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
            case R.id.btn_ok:
                if (CheckInput()) {
                    SubmitDate();
                }
                break;
        }
    }


    private String realName;
    private String idCrad;

    private void SubmitDate() {
        //TestResultUtils.getBaseSussefulResult()
        addSubscription(RetrofitClient.CheckIdentity(null, realName, idCrad, this, new YCNetSubscriber(this, true) {

            @Override
            public void onYcNext(Object model) {
                JumpCenter.JumpActivity(CheckUserActivity.this, ChangeMobilePhoneStepTwoActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, true);
            }
        }));
    }

    private boolean CheckInput() {
        realName = et_realName.getText().toString().trim();
        idCrad = et_idcard.getText().toString().trim();
        if (StrUtil.isEmpty(realName)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.real_name_can_not_be_empty), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }
        if (!StrUtil.isChinese(realName)) {
            ycDialogUtils.showSingleDialog(getString(R.string.dialog_title), getString(R.string.real_name_must_be_chinese), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }
        if (StrUtil.isEmpty(idCrad)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.id_card_can_not_be_empty), new YCDialogUtils.MySingleBtnclickLisener() {
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

        public void afterTextChanged(Editable s) {

            editStart = et_realName.getSelectionStart();

            // 先去掉监听器，否则会出现栈溢出
            et_realName.removeTextChangedListener(mTextWatcher);
            et_realName.setSelection(editStart);
            // 恢复监听器
            et_realName.addTextChangedListener(mTextWatcher);

            if (StrUtil.isEmpty(et_realName.getText().toString().trim()) || StrUtil.isEmpty(et_idcard.getText().toString().trim())) {
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

        public void afterTextChanged(Editable s) {

            editStart = et_idcard.getSelectionStart();

            // 先去掉监听器，否则会出现栈溢出
            et_idcard.removeTextChangedListener(mTextWatcher2);
            et_idcard.setSelection(editStart);
            // 恢复监听器
            et_idcard.addTextChangedListener(mTextWatcher2);

            if (StrUtil.isEmpty(et_realName.getText().toString().trim()) || StrUtil.isEmpty(et_idcard.getText().toString().trim())) {
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


}
