package com.cfbb.android.features.account.withdrawAndrecharge;

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
import com.cfbb.android.commom.textwatch.TextWatchForNoBlank;
import com.cfbb.android.commom.state.MainFragmentEnum;
import com.cfbb.android.commom.state.VertifyCodeEnum;
import com.cfbb.android.commom.utils.image.ImageWithGlideUtils;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.VertifyCodeInfoBean;
import com.cfbb.android.protocol.bean.WithDrawInfoBean;
import com.cfbb.android.protocol.bean.WithdrawCashBean;
import com.cfbb.android.protocol.bean.WithdrawCheckBean;
import com.cfbb.android.widget.YCIdentifyCodeView;
import com.cfbb.android.widget.dialog.YCDialogUtils;

/**
 * 提现
 */
public class WithDrawActivity extends BaseActivity  {

    public static final String SHOW_BACK_TXT = "show_back_txt";

    public static final String WITHDRAW_RIGHT_TURN_TO_ACTIVITY_CLASS = "withdraw_right_turn_to_activity_class";
    public static final String WITHDRAW_RIGHT_TURN_TO_MAIN_INDEX = "withdraw_right_turn_to_main_index";

    public static final String WITHDRAW_INFO = "withdraw_info";

    private String show_back_title;
    private TextView tv_back;
    private TextView tv_title;
    private TextView tv_menu;
    private int turnToIndex;
    private Class turnToActivity;
    private YCDialogUtils ycDialogUtils;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_with_draw);
        mIntent = getIntent();
        if (mIntent != null) {
            withDrawInfo = mIntent.getExtras().getParcelable(WITHDRAW_INFO);
            show_back_title = mIntent.getExtras().getString(SHOW_BACK_TXT);
            turnToActivity = (Class) mIntent.getExtras().getSerializable(WITHDRAW_RIGHT_TURN_TO_ACTIVITY_CLASS);
            turnToIndex = mIntent.getExtras().getInt(WITHDRAW_RIGHT_TURN_TO_MAIN_INDEX, MainFragmentEnum.INVALID.getValue());
        }
    }


    private ImageView iv_bankUrl;
    private TextView tv_bankName;
    private TextView tv_bankNum;
    private TextView tv_tips;
    private TextView tips;
    private TextView tv_moeny;
    private EditText et_money;
    private EditText et_code;
    private YCIdentifyCodeView ycIdentifyCodeView;
    private Button btn_withhdraw;

    @Override
    public void setUpViews() {


        ycDialogUtils = new YCDialogUtils(this);
        tv_menu = (TextView) findViewById(R.id.tv_menu);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getResources().getString(R.string.withdraw_str));
        if (StrUtil.isEmpty(SHOW_BACK_TXT)) {
            tv_back.setText(getResources().getString(R.string.account_details_str));
        } else {
            tv_back.setText(show_back_title);
        }
        tv_back.setVisibility(View.VISIBLE);
        tv_menu.setVisibility(View.VISIBLE);
        iv_bankUrl = (ImageView) findViewById(R.id.iv_01);
        tv_bankName = (TextView) findViewById(R.id.tv_03);
        tv_bankNum = (TextView) findViewById(R.id.tv_04);
        tv_tips = (TextView) findViewById(R.id.tv_05);
        tips = (TextView)findViewById(R.id.tv_07);
        tv_moeny = (TextView) findViewById(R.id.tv_06);
        et_money = (EditText) findViewById(R.id.et_01);
        et_code = (EditText) findViewById(R.id.et_02);
        ycIdentifyCodeView = (YCIdentifyCodeView) findViewById(R.id.yCIdentifyCodeView);
        btn_withhdraw = (Button) findViewById(R.id.btn_withdraw);

        et_money.addTextChangedListener(mTextWatcher);
        et_code.addTextChangedListener(mTextWatcher2);
        et_code.addTextChangedListener(new TextWatchForNoBlank());
    }

    @Override
    public void setUpLisener() {
        tv_menu.setOnClickListener(this);
        tv_back.setOnClickListener(this);
        btn_withhdraw.setOnClickListener(this);
        ycIdentifyCodeView.setActivity(this);
        ycIdentifyCodeView.setYcIdentifyCodeViewLisener(new YCIdentifyCodeView.YCIdentifyCodeViewLisener() {
            @Override
            public void onYCIdentifyCodeViewClickLisener() {
                //TestResultUtils.getSussefulResult37()
                addSubscription(RetrofitClient.GeVertifyCode(null, "", VertifyCodeEnum.WITHDRAW_CODE.getValue(), WithDrawActivity.this, new YCNetSubscriber<VertifyCodeInfoBean>(WithDrawActivity.this, true) {

                    @Override
                    public void onYcNext(VertifyCodeInfoBean model) {
                        ycIdentifyCodeView.StartCountDown(model.millisecond);
                    }
                }));

            }
        });
    }

    private WithDrawInfoBean withDrawInfo;

    @Override
    public void getDataOnCreate() {
        FillView();
    }


    private void FillView() {
        ImageWithGlideUtils.lodeFromUrl(withDrawInfo.imageUrl, iv_bankUrl, this);
        tv_bankName.setText(withDrawInfo.bankName);
        tv_bankNum.setText(withDrawInfo.bankCardNum);
        tv_tips.setText(withDrawInfo.tips);
        tips.setText(withDrawInfo.rapidWithdrawal);
        tv_moeny.setText(withDrawInfo.showCanWithdrawAmount);
        System.out.println(withDrawInfo.rapidWithdrawal+"-------------------------"+withDrawInfo.bankName);
    }


    @Override
    public void onUserClick(View v) {
        super.onUserClick(v);
        switch (v.getId()) {
            //提现记录
            case R.id.tv_menu:
                JumpCenter.JumpActivity(this,WithDrawRecordActivity.class,null,null,JumpCenter.NORMALL_REQUEST,JumpCenter.INVAILD_FLAG,false,true);
                break;
            case R.id.tv_back:
                finish();
                break;
            case R.id.btn_withdraw:
                CheckInput();
                break;
        }
    }


    private void Submit() {

        //TestResultUtils.getBaseSussefulResult()
        RetrofitClient.SubmitWithdraw(null, withDrawInfo.bankCardId, money, code, this, new YCNetSubscriber<WithdrawCashBean>(this, true) {

            @Override
            public void onYcNext(WithdrawCashBean model) {
                WithDrawOk(model);
            }

        });
    }

    /***
     * 提现申请成功
     */
    private void WithDrawOk(WithdrawCashBean withdrawCashBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(WithDrawRightActivity.WITHDRAW_RIGHT_TURN_TO_ACTIVITY_CLASS, turnToActivity);
        bundle.putInt(WithDrawRightActivity.WITHDRAW_RIGHT_TURN_TO_MAIN_INDEX, turnToIndex);
        bundle.putString(WithDrawRightActivity.WITHDRAWCASHID, withdrawCashBean.withdrawCashId);
        JumpCenter.JumpActivity(WithDrawActivity.this, WithDrawRightActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
    }


    private String code;
    private String money;

    private void CheckInput() {
        code = et_code.getText().toString();
        money = et_money.getText().toString();
        if (StrUtil.isEmpty(code)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.vertifycode_cant_be_nempty), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
        }
        if (StrUtil.isEmpty(money)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.withdraw_moeny_cant_be_nempty), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
        }
        if (!ycIdentifyCodeView.isGetVertifyCode()) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.please_get_ems_code), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
        }

        float enterMoeny = 0.0f;
        try {
            enterMoeny = Float.parseFloat(money);
        } catch (Exception e) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.withdraw_moeny_must_be_number), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
        }
        double remainMoeny = 0.0;
        try {
            remainMoeny = Double.parseDouble(withDrawInfo.showCanWithdrawAmount);

        } catch (Exception e) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.withdraw_moeny_initial_erro_please_return_in), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
        }

        if (remainMoeny < enterMoeny) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.withdraw_moeny_must_be_lower_enableCanWithdrawMoeny), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
        } else {

            if (StrUtil.isEmpty(withDrawInfo.bankCardNum)) {
                ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.get_bankList_is_null), null, true);
            } else {
                if (Integer.parseInt(withDrawInfo.bankCardCount) > 1) {
                    ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.withdraw_rules), null, true);
                } else {

                    addSubscription(RetrofitClient.WithdrawCheck(null, et_money.getText().toString().trim(), this, new YCNetSubscriber<WithdrawCheckBean>(this, true) {

                        @Override
                        public void onYcNext(WithdrawCheckBean model) {
                            if (model.isShow == 1) {
                                ycDialogUtils.showDialog(getResources().getString(R.string.check_withdraw), model.calculatorResult, new YCDialogUtils.MyTwoBtnclickLisener() {
                                    @Override
                                    public void onFirstBtnClick(View v) {
                                        ycDialogUtils.DismissMyDialog();
                                        Submit();
                                    }

                                    @Override
                                    public void onSecondBtnClick(View v) {
                                        ycDialogUtils.DismissMyDialog();
                                    }
                                }, false);
                            } else {
                                Submit();
                            }
                        }
                    }));

                }
            }
        }
    }


    private TextWatcher mTextWatcher = new TextWatcher() {

        private int editStart;

        public void afterTextChanged(Editable s) {

            editStart = et_money.getSelectionStart();

            // 先去掉监听器，否则会出现栈溢出
            et_money.removeTextChangedListener(mTextWatcher);
            et_money.setSelection(editStart);
            // 恢复监听器
            et_money.addTextChangedListener(mTextWatcher);

            if (StrUtil.isEmpty(et_code.getText().toString().trim()) || StrUtil.isEmpty(et_money.getText().toString().trim()) || !ycIdentifyCodeView.isGetVertifyCode()) {
                btn_withhdraw.setEnabled(false);
            } else {
                btn_withhdraw.setEnabled(true);
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

            editStart = et_code.getSelectionStart();

            // 先去掉监听器，否则会出现栈溢出
            et_code.removeTextChangedListener(mTextWatcher2);
            et_code.setSelection(editStart);
            // 恢复监听器
            et_code.addTextChangedListener(mTextWatcher2);


            if (StrUtil.isEmpty(et_code.getText().toString().trim()) || StrUtil.isEmpty(et_money.getText().toString().trim()) || !ycIdentifyCodeView.isGetVertifyCode()) {
                btn_withhdraw.setEnabled(false);
            } else {
                btn_withhdraw.setEnabled(true);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
