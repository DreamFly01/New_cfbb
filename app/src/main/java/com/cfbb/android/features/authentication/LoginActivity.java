package com.cfbb.android.features.authentication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.config.Const;
import com.cfbb.android.commom.state.IsOpenGestureEnum;
import com.cfbb.android.commom.state.MainFragmentEnum;
import com.cfbb.android.commom.textwatch.TextWatchForNoBlank;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.commom.utils.others.SPUtils;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.db.user.UserBiz;
import com.cfbb.android.features.gesture.GestureEditActivity;
import com.cfbb.android.features.main.MainActivity;
import com.cfbb.android.features.welcome.SplashActivity;
import com.cfbb.android.protocol.APIService;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.BaseResultBean;
import com.cfbb.android.protocol.bean.UnsupportedBankCardBean;
import com.cfbb.android.protocol.bean.UserBean;
import com.cfbb.android.widget.dialog.YCDialogUtils;
import com.smileback.safeinputlib.IJMInputEditText;

/***
 * 登录
 */
public class LoginActivity extends BaseActivity {

    private ImageView iv_close;
    private EditText et_name;
    private Button btn_login;
    private TextView tv_forget_password;
    private TextView tv_rigister;
    private UserBiz userBiz;
    private ImageView iv_delete_userName;
    private ImageView iv_delete_passWord;
    private YCDialogUtils ycDialogUtils;

    private Bundle bundle;
    private IJMInputEditText ijmEditText;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        mIntent = getIntent();
        if (mIntent != null) {
            bundle = mIntent.getExtras();
        }
    }

    private String preUserName = "";

    @Override
    public void setUpViews() {

        ijmEditText = (IJMInputEditText) findViewById(R.id.ijmKB);
        ijmEditText.setKeyboardMaxLength(22);
        ycDialogUtils = new YCDialogUtils(this);
        iv_delete_userName = (ImageView) findViewById(R.id.iv_delete1);
        iv_delete_passWord = (ImageView) findViewById(R.id.iv_delete2);
        iv_close = (ImageView) findViewById(R.id.iv_01);
        et_name = (EditText) findViewById(R.id.et_01);
        btn_login = (Button) findViewById(R.id.btn_ok);
        tv_forget_password = (TextView) findViewById(R.id.tv_title);
        tv_rigister = (TextView) findViewById(R.id.tv_02);
        preUserName = (String) SPUtils.get(this, Const.PRE_USER_NAME, "");
        et_name.setText(preUserName);

        ijmEditText.addTextChangedListener(new TextWatchForNoBlank());
        et_name.addTextChangedListener(new TextWatchForNoBlank());
        et_name.addTextChangedListener(mTextWatcher);
        ijmEditText.addTextChangedListener(mTextWatcher2);

    }

    @Override
    public void setUpLisener() {
        iv_delete_userName.setOnClickListener(this);
        iv_delete_passWord.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);
        tv_rigister.setOnClickListener(this);
        et_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && et_name.getText().toString().trim().length() > 0) {
                    iv_delete_userName.setVisibility(View.VISIBLE);
                } else {
                    iv_delete_userName.setVisibility(View.GONE);
                }
            }
        });

    }


    @Override
    public void onUserClick(View v) {
        super.onUserClick(v);
        switch (v.getId()) {
            //关闭
            case R.id.iv_01:
                bundle = new Bundle();
                bundle.putInt(MainActivity.SHOW_FRAGMENT_INDEX, MainFragmentEnum.HOME.getValue());
                JumpCenter.JumpActivity(LoginActivity.this, MainActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, false);
                break;
            //登录
            case R.id.btn_ok:
                Login();
                break;
            //忘记密码
            case R.id.tv_title:
                bundle = new Bundle();
                bundle.putString(FindPassWordActivity.BACK_TXT, getResources().getString(R.string.login));
                JumpCenter.JumpActivity(LoginActivity.this, FindPassWordActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, false);
                break;
            //注册
            case R.id.tv_02:
                JumpCenter.JumpActivity(LoginActivity.this, RegisterStepOneActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, false);
                break;
            //用户名
            case R.id.iv_delete1:
                et_name.setText("");
                break;
            //密码
            case R.id.iv_delete2:
                ijmEditText.KeyboardClear();
                break;
        }
    }

    private void Login() {

        ijmEditText.hideKeyboard();

        String name = et_name.getText().toString();
        String password = ijmEditText.getKeyboardText().toString();
        SPUtils.put(LoginActivity.this, Const.PRE_USER_NAME, name);

        if (InputCheck(name, password)) {

            //TestResultUtils.getSussefulResult16()
            addSubscription(RetrofitClient.loginRequest(null, name, password, this, new YCNetSubscriber<UserBean>(this, true) {


                @Override
                public void onYcNext(UserBean model) {

                    // 将手势密码和是否开启 保存在SP中，
                    // 每次登录2通过UserName作为key取值，存在就一并插入到数据库中，不存在跳转设置界面。
                    String gestureStr = (String) SPUtils.get(LoginActivity.this, model.userName, "");
                    String gesturePsw = "";
                    int gestureState = IsOpenGestureEnum.CLOSE.getValue();
                    if (!StrUtil.isEmpty(gestureStr)) {
                        gesturePsw = gestureStr.split("\\.")[0];
                        gestureState = Integer.parseInt(gestureStr.split("\\.")[1]);
                    }

                    UserBiz.getInstance(LoginActivity.this).StoreUserInfo(model, gesturePsw, gestureState);

                    if (!StrUtil.isEmpty(gestureStr)) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(MainActivity.SHOW_FRAGMENT_INDEX, MainFragmentEnum.ACCOUNT.getValue());
                        JumpCenter.JumpActivity(LoginActivity.this, MainActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, true);

                    } else {
                        JumpCenter.JumpActivity(LoginActivity.this, GestureEditActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, true);
                    }
                }

            }));
        }
    }

    private boolean InputCheck(String name, String password) {
        boolean result = false;
        if (StrUtil.isEmpty(name)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.input_tips_user_cant_be_nempty), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return result;
        }
        if (StrUtil.isEmpty(password)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.input_tips_password_cant_be_nempty), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return result;
        }
        result = true;
        return result;
    }


    private TextWatcher mTextWatcher = new TextWatcher() {

        private int editStart;

        public void afterTextChanged(Editable s) {

            editStart = et_name.getSelectionStart();

            // 先去掉监听器，否则会出现栈溢出
            et_name.removeTextChangedListener(mTextWatcher);
            et_name.setSelection(editStart);
            // 恢复监听器
            et_name.addTextChangedListener(mTextWatcher);

            if (StrUtil.isEmpty(et_name.getText().toString().trim())) {
                iv_delete_userName.setVisibility(View.GONE);
            } else {
                iv_delete_userName.setVisibility(View.VISIBLE);
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

            editStart = ijmEditText.getSelectionStart();

            // 先去掉监听器，否则会出现栈溢出
            ijmEditText.removeTextChangedListener(mTextWatcher2);
            ijmEditText.setSelection(editStart);
            // 恢复监听器
            ijmEditText.addTextChangedListener(mTextWatcher2);

            if (StrUtil.isEmpty(ijmEditText.getKeyboardText().toString().trim())) {
                iv_delete_passWord.setVisibility(View.GONE);
            } else {
                iv_delete_passWord.setVisibility(View.VISIBLE);
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
