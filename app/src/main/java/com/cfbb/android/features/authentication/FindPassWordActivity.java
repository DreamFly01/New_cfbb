package com.cfbb.android.features.authentication;

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
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.VertifyCodeInfoBean;
import com.cfbb.android.widget.YCIdentifyCodeView;
import com.cfbb.android.widget.dialog.YCDialogUtils;
import com.smileback.safeinputlib.IJMInputEditText;

/***
 * 找回密码
 */
public class FindPassWordActivity extends BaseActivity {


    public static final String BACK_TXT = "back_txt";

    private TextView tv_back;
    private TextView tv_title;
    private EditText et_tel;
    private EditText et_code;
    private IJMInputEditText et_new_password;
    private YCIdentifyCodeView ycIdentifyCodeView;
    private Button btn_ok;
    private String back_txt;
    private YCDialogUtils ycDialogUtils;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_find_pass_word);
        mIntent = getIntent();
        if (null != mIntent) {
            back_txt = mIntent.getExtras().getString(BACK_TXT);
        }
    }

    @Override
    public void setUpViews() {

        ycDialogUtils = new YCDialogUtils(this);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        et_tel = (EditText) findViewById(R.id.et_01);
        et_code = (EditText) findViewById(R.id.et_02);
        et_new_password = (IJMInputEditText) findViewById(R.id.et_03);
        ycIdentifyCodeView = (YCIdentifyCodeView) findViewById(R.id.yCIdentifyCodeView);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        tv_title.setText(getResources().getString(R.string.find_password_str));
        tv_back.setText(back_txt);
        tv_back.setVisibility(View.VISIBLE);

        et_tel.addTextChangedListener(new TextWatchForNoBlank());
        et_code.addTextChangedListener(new TextWatchForNoBlank());
        et_new_password.addTextChangedListener(new TextWatchForNoBlank());

        et_tel.addTextChangedListener(mTextWatcher);
        et_code.addTextChangedListener(mTextWatcher2);
        et_new_password.addTextChangedListener(mTextWatcher3);
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
                addSubscription(RetrofitClient.GeVertifyCode(null, et_tel.getText().toString().trim(), VertifyCodeEnum.FIND_PASSWORD_CODE.getValue(), FindPassWordActivity.this, new YCNetSubscriber<VertifyCodeInfoBean>(FindPassWordActivity.this, true) {


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



    private String tel;
    private String code;
    private String new_password;

    private void Submit() {
        //TestResultUtils.getBaseSussefulResult()
        addSubscription(RetrofitClient.FindPassWord(null, tel, code, new_password, this, new YCNetSubscriber(this, true) {


            @Override
            public void onYcNext(Object model) {
                ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), "重置密码成功!", new YCDialogUtils.MySingleBtnclickLisener() {
                    @Override
                    public void onBtnClick(View v) {
                        ycDialogUtils.DismissMyDialog();
                        finish();
                    }
                }, true);
            }
        }));
    }

    private boolean CheckInput() {
        tel = et_tel.getText().toString().trim();
        code = et_code.getText().toString().trim();
        new_password = et_new_password.getKeyboardText();
        if (StrUtil.isEmpty(tel)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), "手机号码不能为空!", new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }
        if (!StrUtil.checkMobile(tel)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), "请输入正确的手机号码!", new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }
        if (StrUtil.isEmpty(code)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), "请输入短信验证码!", new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }
        if (StrUtil.isEmpty(new_password)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), "请输入新密码!", new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }
        if (!ycIdentifyCodeView.isGetVertifyCode()) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), "请获取短信验证码!", new YCDialogUtils.MySingleBtnclickLisener() {
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
        private String tel;

        public void afterTextChanged(Editable s) {

            tel = et_tel.getText().toString().trim();
            editStart = et_tel.getSelectionStart();

            // 先去掉监听器，否则会出现栈溢出
            et_tel.removeTextChangedListener(mTextWatcher);
            et_tel.setSelection(editStart);
            // 恢复监听器
            et_tel.addTextChangedListener(mTextWatcher);

            if (StrUtil.isEmpty(tel) || StrUtil.isEmpty(et_new_password.getKeyboardText()) || StrUtil.isEmpty(et_code.getText().toString().trim()) || !ycIdentifyCodeView.isGetVertifyCode()) {
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
        private String mCode;

        public void afterTextChanged(Editable s) {

            mCode = et_code.getText().toString().trim();
            editStart = et_code.getSelectionStart();

            // 先去掉监听器，否则会出现栈溢出
            et_code.removeTextChangedListener(mTextWatcher2);
            et_code.setSelection(editStart);
            // 恢复监听器
            et_code.addTextChangedListener(mTextWatcher2);

            if (StrUtil.isEmpty(mCode) || StrUtil.isEmpty(et_new_password.getKeyboardText()) || StrUtil.isEmpty(et_code.getText().toString().trim()) || !ycIdentifyCodeView.isGetVertifyCode()) {
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
        private String newPassWord;

        public void afterTextChanged(Editable s) {

            newPassWord = et_new_password.getKeyboardText();
            editStart = et_new_password.getSelectionStart();

            // 先去掉监听器，否则会出现栈溢出
            et_new_password.removeTextChangedListener(mTextWatcher3);
            et_new_password.setSelection(editStart);
            // 恢复监听器
            et_new_password.addTextChangedListener(mTextWatcher3);

            if (StrUtil.isEmpty(newPassWord) || StrUtil.isEmpty(et_new_password.getKeyboardText()) || StrUtil.isEmpty(et_code.getText().toString().trim()) || !ycIdentifyCodeView.isGetVertifyCode()) {
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
