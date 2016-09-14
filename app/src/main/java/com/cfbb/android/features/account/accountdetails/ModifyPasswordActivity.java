package com.cfbb.android.features.account.accountdetails;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.textwatch.TextWatchForNoBlank;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.features.authentication.FindPassWordActivity;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.widget.dialog.YCDialogUtils;
import com.smileback.safeinputlib.IJMInputEditText;

/***
 * 修改登录密码
 */
public class ModifyPasswordActivity extends BaseActivity {

    private TextView tv_back;
    private TextView tv_title;
    private IJMInputEditText et_oldpassword;
    private IJMInputEditText et_new_password;
    private IJMInputEditText et_new_password_again;
    private Button btn_ok;
    private TextView tv_forget;
    private YCDialogUtils ycDialogUtils;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_modify_password);

    }

    @Override
    public void setUpViews() {


        ycDialogUtils = new YCDialogUtils(this);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);

        et_oldpassword = (IJMInputEditText) findViewById(R.id.et_01);
        et_new_password = (IJMInputEditText) findViewById(R.id.et_02);
        et_new_password_again = (IJMInputEditText) findViewById(R.id.et_03);

        btn_ok = (Button) findViewById(R.id.btn_ok);
        tv_title.setText(getResources().getString(R.string.modify_pass_word));
        tv_back.setText(getResources().getString(R.string.account_set_str));
        tv_back.setVisibility(View.VISIBLE);
        tv_forget = (TextView) findViewById(R.id.tv_02);

        et_oldpassword.addTextChangedListener(new TextWatchForNoBlank());
        et_new_password.addTextChangedListener(new TextWatchForNoBlank());
        et_new_password_again.addTextChangedListener(new TextWatchForNoBlank());
        et_oldpassword.addTextChangedListener(mTextWatcher);
        et_new_password.addTextChangedListener(mTextWatcher);
        et_new_password_again.addTextChangedListener(mTextWatcher);


    }

    @Override
    public void setUpLisener() {
        tv_back.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        tv_forget.setOnClickListener(this);
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
            case R.id.tv_02:
                Bundle bundle = new Bundle();
                bundle.putString(FindPassWordActivity.BACK_TXT, getResources().getString(R.string.modify_pass_word));
                JumpCenter.JumpActivity(ModifyPasswordActivity.this, FindPassWordActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
                break;
        }
    }

    private void Submit() {
        //TestResultUtils.getBaseSussefulResult()
        addSubscription(RetrofitClient.ModifyPassWord(null, old_password, new_password, new_password_again, this, new YCNetSubscriber(this) {

            @Override
            public void onYcNext(Object model) {
                showShortToast(R.string.modify_login_psw_right);
                finish();
            }
        }));
    }

    private String old_password;
    private String new_password;
    private String new_password_again;

    private boolean CheckInput() {
        old_password = et_oldpassword.getKeyboardText().toString();
        new_password = et_new_password.getKeyboardText().toString();
        new_password_again = et_new_password_again.getKeyboardText().toString();

        if (StrUtil.isEmpty(old_password)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.old_psw_cant_be_empty), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }

        if (old_password.trim().length() < 6 || old_password.trim().length() > 16) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.psw_rules), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }

        if (StrUtil.isEmpty(new_password)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.new_psw_cant_be_empty), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }
        if (StrUtil.isEmpty(new_password_again)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.check_new_psw_cant_be_empty), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }

        if (!new_password.equals(new_password_again)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.twice_psw_are_not_same), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }

        if (new_password.trim().length() < 6 || new_password.trim().length() > 16) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.psw_rules), new YCDialogUtils.MySingleBtnclickLisener() {
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
        private String oldPassWord;

        public void afterTextChanged(Editable s) {

            oldPassWord = et_oldpassword.getKeyboardText().toString();
            editStart = et_oldpassword.getSelectionStart();

            // 先去掉监听器，否则会出现栈溢出
            et_oldpassword.removeTextChangedListener(mTextWatcher);
            et_oldpassword.setSelection(editStart);
            // 恢复监听器
            et_oldpassword.addTextChangedListener(mTextWatcher);

            if (StrUtil.isEmpty(oldPassWord) || StrUtil.isEmpty(et_new_password.getKeyboardText().toString().trim()) || StrUtil.isEmpty(et_new_password_again.getKeyboardText().toString().trim())) {
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
        private String newPassWord;

        public void afterTextChanged(Editable s) {

            newPassWord = et_new_password.getKeyboardText().toString().trim();
            editStart = et_new_password.getSelectionStart();

            // 先去掉监听器，否则会出现栈溢出
            et_new_password.removeTextChangedListener(mTextWatcher2);
            et_new_password.setSelection(editStart);
            // 恢复监听器
            et_new_password.addTextChangedListener(mTextWatcher2);

            if (StrUtil.isEmpty(newPassWord) || StrUtil.isEmpty(et_new_password.getKeyboardText().toString().trim()) || StrUtil.isEmpty(et_new_password_again.getKeyboardText().toString().trim())) {
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


    private TextWatcher mTextWatcher3 = new TextWatcher() {

        private int editStart;
        private String newPassWordAgain;

        public void afterTextChanged(Editable s) {

            newPassWordAgain = et_new_password_again.getKeyboardText().toString().trim();
            editStart = et_new_password_again.getSelectionStart();

            // 先去掉监听器，否则会出现栈溢出
            et_new_password_again.removeTextChangedListener(mTextWatcher3);
            et_new_password_again.setSelection(editStart);
            // 恢复监听器
            et_new_password_again.addTextChangedListener(mTextWatcher3);

            if (StrUtil.isEmpty(newPassWordAgain) || StrUtil.isEmpty(et_new_password.getKeyboardText().toString().trim()) || StrUtil.isEmpty(et_new_password_again.getKeyboardText().toString().trim())) {
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
        et_oldpassword.hideKeyboard();
        et_new_password.hideKeyboard();
        et_new_password_again.hideKeyboard();
        StatService.onPause(this);
    }
}
