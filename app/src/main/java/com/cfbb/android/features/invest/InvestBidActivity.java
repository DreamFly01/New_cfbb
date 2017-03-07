package com.cfbb.android.features.invest;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.state.MainFragmentEnum;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.features.account.withdrawAndrecharge.RechargeActivity;
import com.cfbb.android.features.account.withdrawAndrecharge.RechargeRightActivity;
import com.cfbb.android.features.authentication.RealNameAuthenticationActivity;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.BuyInitialBean;
import com.cfbb.android.protocol.bean.InComeBean;
import com.cfbb.android.protocol.bean.InvestInfoBean;
import com.cfbb.android.protocol.bean.RechargeInfoBean;
import com.cfbb.android.widget.YCLoadingBg;
import com.cfbb.android.widget.dialog.YCDialogUtils;
import com.smileback.safeinputlib.IJMInputEditText;
import com.smileback.safeinputlib.SKBConstant;

/***
 * 投标
 */
public class InvestBidActivity extends BaseActivity  {

    public static final String INVEST_TURN_TO_ACTIVITY_CLASS = "invest_turn_to_activity_class";
    public static final String INVEST_TURN_TO_MAIN_INDEX = "invest_turn_to_main_index";
    public static final String SHOW_BACK_TXT = "show_back_txt";
    public static final String PRODUCT_ID = "product_id";
    public static final String LOAN_TYPEID = "loan_typeId";
    private TextView tv_back;
    private TextView tv_title;
    private TextView tv_can_invest_money;
    private TextView tv_remains_money;
    private TextView tv_intrested_str;
    private IJMInputEditText et_money;
    private Button btn_invest;
    private String show_back_txt;
    private String product_id;
    private String loan_typeId;
    private YCLoadingBg ycLoadingBg;
    private Class turnToActivity;
    private int turnToIndex;
    private YCDialogUtils ycDialogUtils;

    private TextView tv_hint_to_recharge;
    private Bundle bundle;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_invest_bid);
        if (savedInstanceState != null) {
            bundle = savedInstanceState;
        } else {
            mIntent = getIntent();
            if (mIntent != null) {
                bundle = mIntent.getExtras();
            }
        }

        loan_typeId = bundle.getString(LOAN_TYPEID);
        show_back_txt = bundle.getString(SHOW_BACK_TXT);
        product_id = bundle.getString(PRODUCT_ID);
        turnToActivity = (Class) bundle.getSerializable(INVEST_TURN_TO_ACTIVITY_CLASS);
        turnToIndex = bundle.getInt(INVEST_TURN_TO_MAIN_INDEX, MainFragmentEnum.INVALID.getValue());
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(bundle, outPersistentState);

    }

    @Override
    public void setUpViews() {

        tv_hint_to_recharge = (TextView) findViewById(R.id.tv_10);
        ycDialogUtils = new YCDialogUtils(this);
        ycLoadingBg = (YCLoadingBg) findViewById(R.id.ycLoadingBg);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_back.setVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.buy));
        if (StrUtil.isEmpty(show_back_txt)) {
            tv_back.setText(getResources().getString(R.string.back_str));
        } else {
            tv_back.setText(show_back_txt);
        }
        tv_can_invest_money = (TextView) findViewById(R.id.tv_02);
        tv_remains_money = (TextView) findViewById(R.id.tv_03);
        tv_intrested_str = (TextView) findViewById(R.id.tv_04);
        et_money = (IJMInputEditText) findViewById(R.id.et_01);
        et_money.setKeyboardMode(SKBConstant.NUM_KEYBOARD);
        et_money.setKeyboardPwdShow(true);
        btn_invest = (Button) findViewById(R.id.btn_invest);

        et_money.addTextChangedListener(mTextWatcher);


    }

    @Override
    public void setUpLisener() {
        tv_back.setOnClickListener(this);
        tv_hint_to_recharge.setOnClickListener(this);
        btn_invest.setOnClickListener(this);
    }

    private BuyInitialBean buyInitial;

    @Override
    public void getDataOnCreate() {
        //TestResultUtils.getSussefulResult11()
        addSubscription(RetrofitClient.BuyInitial(null, loan_typeId + "", product_id, this, new YCNetSubscriber<BuyInitialBean>(this) {


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
            public void onYcNext(BuyInitialBean model) {
                buyInitial = model;
                et_money.setHint(buyInitial.hint);
                ycLoadingBg.setVisibility(View.GONE);
                tv_can_invest_money.setText(buyInitial.loanFailingAmount);
                tv_remains_money.setText(buyInitial.accountAmount);
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
            case R.id.btn_invest:
                if (CheckInput()) {
                    Submit();
                }
                break;
            //去充值
            case R.id.tv_10:
                //清空输入框
                et_money.KeyboardClear();
                et_money.hideKeyboard();
                doRechargePre();
                break;
        }
    }

    private void doRechargePre() {
        addSubscription(RetrofitClient.GetRechargeInitalInfo(null, InvestBidActivity.this, new YCNetSubscriber<RechargeInfoBean>(InvestBidActivity.this, true) {

            @Override
            public void onYcNext(RechargeInfoBean model) {

//                Bundle bundle = new Bundle();
                bundle.putString(RechargeActivity.SHOW_BACK_TXT, getResources().getString(R.string.invest_atonce_str));
                bundle.putParcelable(RechargeActivity.RECHARGEINFO_DATA, model);
                bundle.putSerializable(RechargeRightActivity.RECHARGE_RIGHT_TURN_TO_ACTIVITY_CLASS, InvestBidActivity.class);
                JumpCenter.JumpActivity(InvestBidActivity.this, RechargeActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);

            }

            @Override
            public void onYCError(APIException e){
                if (e.code == 2) {
                    //code  2  代表未认证
                    dismissLoadingView();
                    ycDialogUtils.showDialog(getResources().getString(R.string.dialog_kindly_title), e.getMessage(), new YCDialogUtils.MyTwoBtnclickLisener() {
                        @Override
                        public void onFirstBtnClick(View v) {
                            //ok
                            ycDialogUtils.DismissMyDialog();
                            // Bundle bundle = new Bundle();
                            bundle.putSerializable(JumpCenter.TO_ACTIVITY, InvestBidActivity.class);
                            bundle.putString(RealNameAuthenticationActivity.SHOW_BACK_TXT, getResources().getString(R.string.invest_atonce_str));
                            bundle.putSerializable(RealNameAuthenticationActivity.NEXT_ACTIVITY_CLASS, RechargeActivity.class);
                            JumpCenter.JumpActivity(InvestBidActivity.this, RealNameAuthenticationActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);

                        }

                        @Override
                        public void onSecondBtnClick(View v) {
                            ycDialogUtils.DismissMyDialog();
                        }
                    }, true);

                } else {
                    ycDialogUtils.showSingleDialog(InvestBidActivity.this.getResources().getString(R.string.dialog_title), InvestBidActivity.this.getResources().getString(R.string.request_erro_str), new YCDialogUtils.MySingleBtnclickLisener() {
                        @Override
                        public void onBtnClick(View v) {
                            ycDialogUtils.DismissMyDialog();
                        }
                    }, true);
                }
            }
        }));
    }

    private void Submit() {
        Invest();
    }

    private void Invest() {
        addSubscription(RetrofitClient.InvestBid(null, product_id, investMoney, this, new YCNetSubscriber<InvestInfoBean>(this, true) {

            @Override
            public void onYcNext(InvestInfoBean model) {
                if (null == bundle) {
                    bundle = new Bundle();
                }
                bundle.putSerializable(InvestBidRightActivity.INVEST_RIGHT_TURN_TO_ACTIVITY_CLASS, turnToActivity);
                bundle.putString(InvestBidRightActivity.LOAN_TYPEID, loan_typeId);
                bundle.putString(InvestBidRightActivity.PRODUCT_ID, product_id);
                bundle.putString(InvestBidRightActivity.INVEST_ID, model.investId);
                bundle.putInt(InvestBidRightActivity.INVEST_RIGHT_TURN_TO_MAIN_INDEX, turnToIndex);
                JumpCenter.JumpActivity(InvestBidActivity.this, InvestBidRightActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);

            }

        }));
    }

    private String investMoney;

    private boolean CheckInput() {
        investMoney = et_money.getKeyboardText();
        if (StrUtil.isEmpty(investMoney)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getString(R.string.please_enter_moeny), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }
        double investMoenyDouble = Double.parseDouble(investMoney);
        double canInvestMoenyDouble = Double.parseDouble(buyInitial.loanFailingAmount);
        double vailable_balance = Double.parseDouble(buyInitial.accountAmount);

        //只有minInvestMoney是数字的时候进行判断，不是的话，说明服务器返回数据有误，这里做跳过该步验证处理
        if (StrUtil.isNumeric(buyInitial.minInvestMoney)) {
            double minInvestMoney = Double.parseDouble(buyInitial.minInvestMoney);
            if (investMoenyDouble < minInvestMoney || investMoenyDouble == 0) {
                ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getString(R.string.invest_moeny_must_100), new YCDialogUtils.MySingleBtnclickLisener() {
                    @Override
                    public void onBtnClick(View v) {
                        ycDialogUtils.DismissMyDialog();
                    }
                }, true);
                return false;
            }
        }

        if (investMoenyDouble > canInvestMoenyDouble) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getString(R.string.invest_moeny_must_lower_can_investMoeny), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }
        if (investMoenyDouble > vailable_balance) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getString(R.string.invest_moeny_must_lower_accountMoeny), new YCDialogUtils.MySingleBtnclickLisener() {
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
        private String money;

        public void afterTextChanged(Editable s) {

            money = s.toString();
            editStart = et_money.getSelectionStart();

            // 先去掉监听器，否则会出现栈溢出
            et_money.removeTextChangedListener(mTextWatcher);
            et_money.setSelection(editStart);
            // 恢复监听器
            et_money.addTextChangedListener(mTextWatcher);

            if (StrUtil.isEmpty(money)) {
                btn_invest.setEnabled(false);
                tv_hint_to_recharge.setVisibility(View.GONE);
            } else {
                btn_invest.setEnabled(true);

                Double enterMoney = Double.parseDouble(money);
                Double canUserMoeny = Double.parseDouble(buyInitial.accountAmount);
                if (enterMoney > canUserMoeny) {
                    btn_invest.setEnabled(false);
                    tv_hint_to_recharge.setVisibility(View.VISIBLE);
                } else {
                    btn_invest.setEnabled(true);
                    tv_hint_to_recharge.setVisibility(View.GONE);
                }
            }


            if (!StrUtil.isEmpty(money)) {
                tv_intrested_str.setText(R.string.caculationing_str);
                RetrofitClient.IncomeCalculation(null, loan_typeId, money, buyInitial.rate, buyInitial.term, buyInitial.termType + "", InvestBidActivity.this, new YCNetSubscriber<InComeBean>(InvestBidActivity.this) {

                    @Override
                    public void onYcNext(InComeBean model) {
                        tv_intrested_str.setText(model.profit);
                    }

                    @Override
                    public void onYCError(APIException e) {
                        tv_intrested_str.setText(R.string.caculation_erro_str);
                    }
                });
            } else {
                tv_intrested_str.setText("0.00");
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
