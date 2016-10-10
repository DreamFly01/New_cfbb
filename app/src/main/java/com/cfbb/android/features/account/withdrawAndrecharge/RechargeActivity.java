package com.cfbb.android.features.account.withdrawAndrecharge;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.baofoo.sdk.vip.BaofooPayActivity;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.config.Const;
import com.cfbb.android.commom.state.MainFragmentEnum;
import com.cfbb.android.commom.state.RechargeStateEnum;
import com.cfbb.android.commom.textwatch.TextWatchForBankNumber;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.commom.utils.image.ImageWithGlideUtils;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.BankBean;
import com.cfbb.android.protocol.bean.BaseResultBean;
import com.cfbb.android.protocol.bean.RechargeInfoBean;
import com.cfbb.android.protocol.bean.RechargeResultInfoBean;
import com.cfbb.android.widget.YCLoadingBg;
import com.cfbb.android.widget.dialog.YCDialogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 充值
 */
public class RechargeActivity extends BaseActivity {

    //申请READ_PHONE_STATE权限
    private final static int REQUEST_READ_PHONE_STATE = 1001;
    public static final String SHOW_BACK_TXT = "show_back_txt";
    public static final String RECHARGE_RIGHT_TURN_TO_ACTIVITY_CLASS = "recharge_right_turn_to_activity_class";
    public static final String RECHARGE_RIGHT_TURN_TO_MAIN_INDEX = "withdraw_right_turn_to_activity_intent_action";
    public static final String RECHARGEINFO_DATA = "rechargeinfo_data";

    private int turnToIndex;
    private String show_back_title;
    private TextView tv_back;
    private TextView tv_title;
    private Button btn_nextStep;
    private Class turnToActivity;
    private YCLoadingBg ycLoadingBg;
    private YCDialogUtils ycDialogUtils;
    private Bundle bundle;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_recharge);

        mIntent = getIntent();
        if (mIntent != null) {
            bundle = mIntent.getExtras();
            rechargeInfo = bundle.getParcelable(RECHARGEINFO_DATA);
            show_back_title = bundle.getString(SHOW_BACK_TXT);
            turnToActivity = (Class) bundle.getSerializable(RECHARGE_RIGHT_TURN_TO_ACTIVITY_CLASS);
            if (turnToActivity == null) {
                turnToActivity = (Class) bundle.getSerializable(JumpCenter.TO_ACTIVITY);
            }
            turnToIndex = bundle.getInt(RECHARGE_RIGHT_TURN_TO_MAIN_INDEX, MainFragmentEnum.INVALID.getValue());
        }
    }

    @Override
    public void getDataOnCreate() {
        if (rechargeInfo == null) {

            addSubscription(RetrofitClient.GetRechargeInitalInfo(null, this, new YCNetSubscriber<RechargeInfoBean>(RechargeActivity.this) {

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
                public void onYcNext(RechargeInfoBean model) {
                    rechargeInfo = model;
                    ycLoadingBg.dissmiss();
                    FillView();
                }

            }));
        } else {
            ycLoadingBg.dissmiss();
        }
    }

    @Override
    public void setUpViews() {

        ycDialogUtils = new YCDialogUtils(this);
        ycLoadingBg = (YCLoadingBg) findViewById(R.id.ycLoadingBg);
        btn_nextStep = (Button) findViewById(R.id.btn_ok);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getResources().getString(R.string.recharge));
        if (StrUtil.isEmpty(show_back_title)) {
            tv_back.setText(getResources().getString(R.string.back_str));
        } else {
            tv_back.setText(show_back_title);
        }
        tv_back.setVisibility(View.VISIBLE);
        FillView();
    }

    private RechargeInfoBean rechargeInfo = null;
    private TextView tv_khm;
    private EditText et_money;
    private EditText et_bankNum;
    private ImageView iv_bank;
    private TextView tv_bankName;
    private TextView tv_bankNum;
    private ImageView ivHint;
    private TextView tvAccountBalance;
    private boolean isFristRecharge = false;

    private void FillView() {

        if (rechargeInfo != null) {
            ycLoadingBg.dissmiss();
            if (rechargeInfo.rechargeState == RechargeStateEnum.FIRST_RECHARGE.getValue()) {

                tv_title.setText(getResources().getString(R.string.bind_and_recharge));
                isFristRecharge = true;

                ViewStub stub = (ViewStub) findViewById(R.id.viewStub_01);
                stub.inflate();
                tv_khm = (TextView) findViewById(R.id.tv_05);
                et_money = (EditText) findViewById(R.id.et_01);
                tv_khm.setText(rechargeInfo.accountName);
                et_bankNum = (EditText) findViewById(R.id.et_02);
                ivHint = (ImageView) findViewById(R.id.iv_06);
                tvAccountBalance = (TextView) findViewById(R.id.tv_06);
                tv_bankName = (TextView) findViewById(R.id.tv_08);
                tvAccountBalance.setText(rechargeInfo.accountBalance);
                ivHint.setOnClickListener(this);
                tv_bankName.setOnClickListener(this);
                et_money.addTextChangedListener(mTextWatcher);
                et_bankNum.addTextChangedListener(mTextWatcher2);
                et_bankNum.addTextChangedListener(new TextWatchForBankNumber(et_bankNum));

            } else {

                ViewStub stub = (ViewStub) findViewById(R.id.viewStub_02);
                stub.inflate();
                et_money = (EditText) findViewById(R.id.et_01);
                iv_bank = (ImageView) findViewById(R.id.iv_01);
                tv_bankName = (TextView) findViewById(R.id.tv_03);
                tv_bankNum = (TextView) findViewById(R.id.tv_04);
                tvAccountBalance = (TextView) findViewById(R.id.tv_06);
                tvAccountBalance.setText(rechargeInfo.accountBalance);

                ImageWithGlideUtils.lodeFromUrl(rechargeInfo.imageUrl, iv_bank, this);
                tv_bankName.setText(rechargeInfo.bankName);
                tv_bankNum.setText(rechargeInfo.bankNum);

                et_money.addTextChangedListener(mTextWatcher);

            }
        }
    }

    @Override
    public void setUpLisener() {
        tv_back.setOnClickListener(this);
        btn_nextStep.setOnClickListener(this);
    }

    @Override
    public void onUserClick(View v) {
        super.onUserClick(v);
        switch (v.getId()) {

            case R.id.tv_back:
                finish();
                break;
            //提交充值
            case R.id.btn_ok:
                //权限检查
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    //没有赋予这个权限，去申请
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            REQUEST_READ_PHONE_STATE);
                } else {
                    if (CheckInput()) {
                        Submit();
                    }
                }
                break;
            //提示
            case R.id.iv_06:
                ycDialogUtils.showSingle2Dialog(getResources().getString(R.string.CardholderExplain),  rechargeInfo.cardHolderDesc , new YCDialogUtils.MySingleBtnclickLisener() {
                    @Override
                    public void onBtnClick(View v) {
                        ycDialogUtils.DismissMyDialog();
                    }
                }, true);
                break;
            //选择银行卡
            case R.id.tv_08:
                GetSupportBank();
                break;
        }
    }

    private List<BankBean> bankList;

    private void GetSupportBank() {
        if (bankList == null) {

            RetrofitClient.getSupportBankList(null, this, new YCNetSubscriber<List<BankBean>>(this, true) {
                @Override
                public void onYcNext(List<BankBean> model) {
                    bankList = model;
                    CeateChooseBankDilaog();
                }
            });

        } else {
            CeateChooseBankDilaog();
        }
    }

    private void CeateChooseBankDilaog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RechargeActivity.this);
        builder.setIcon(R.mipmap.icon_tu);
        builder.setTitle(R.string.please_choose_bank_hint);
        List<String> bankBeanList = new ArrayList<>();
        for (BankBean bankBean : bankList) {
            bankBeanList.add(bankBean.bankName);
        }
        final String[] cities = bankBeanList.toArray(new String[0]);
        builder.setItems(cities, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tv_bankName.setText(cities[which]);
                tv_bankName.setTag(bankList.get(which).bankCode);
                if (StrUtil.isEmpty(et_money.getText().toString()) || StrUtil.isEmpty(et_bankNum.getText().toString()) || tv_bankName.getTag() == null || StrUtil.isEmpty(tv_bankName.getTag().toString())) {
                    btn_nextStep.setEnabled(false);
                } else {
                    btn_nextStep.setEnabled(true);
                }
            }
        });
        builder.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (CheckInput()) {
                        Submit();
                    }
                } else {
                    Toast.makeText(this, "无法发起充值，请授予财富中国应用相关权限。", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private String money;
    private String bankNum;

    private void RechargeOK() {

       // money ="100.0";
       // bankNum ="100000";

        bundle.putString(RechargeRightActivity.RECHARGE_MOENY, money + Const.YUAN_STR);
        if (rechargeInfo.rechargeState != RechargeStateEnum.FIRST_RECHARGE.getValue()) {
            bundle.putString(RechargeRightActivity.RECHARGE_BANK_STR, rechargeInfo.bankNum);
        } else {
            bundle.putString(RechargeRightActivity.RECHARGE_BANK_STR, bankNum);
        }
        bundle.putInt(RechargeRightActivity.RECHARGE_RIGHT_TURN_TO_MAIN_INDEX, turnToIndex);
        bundle.putSerializable(RechargeRightActivity.RECHARGE_RIGHT_TURN_TO_ACTIVITY_CLASS, turnToActivity);
        JumpCenter.JumpActivity(RechargeActivity.this, RechargeRightActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, true);
    }


    private void Submit() {

//        Intent payintent = new Intent(RechargeActivity.this, BaofooPayActivity.class);
//        payintent.putExtra(BaofooPayActivity.PAY_TOKEN, "201609200110000401558659");
//        payintent.putExtra(BaofooPayActivity.PAY_BUSINESS, false);
//        startActivityForResult(payintent,
//                REQUEST_CODE_BAOFOO_SDK);

        //TestResultUtils.getSussefulResult29()

        String selected_crad = isFristRecharge ? "0" : "1";
        addSubscription(RetrofitClient.Recarge(null, rechargeInfo.noAgree, money, bankNum, selected_crad, this, new YCNetSubscriber<RechargeResultInfoBean>(this, true) {

            @Override
            public void onYcNext(RechargeResultInfoBean model) {
                Intent payintent = new Intent(RechargeActivity.this, BaofooPayActivity.class);
                payintent.putExtra(BaofooPayActivity.PAY_TOKEN, model.payParams);
                payintent.putExtra(BaofooPayActivity.PAY_BUSINESS, false);
                startActivityForResult(payintent,
                        REQUEST_CODE_BAOFOO_SDK);
            }
        }));
    }

    //发起充值请求
    public final static int REQUEST_CODE_BAOFOO_SDK = 100;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

      // RechargeOK();

        if (requestCode == REQUEST_CODE_BAOFOO_SDK) {
            String result = "";
            String msg = "";
            if (data == null || data.getExtras() == null) {
                msg = "支付已被取消";
                ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), msg, new YCDialogUtils.MySingleBtnclickLisener() {
                    @Override
                    public void onBtnClick(View v) {
                        ycDialogUtils.DismissMyDialog();
                    }
                }, true);
            } else {
                result = data.getExtras().getString(
                        BaofooPayActivity.PAY_RESULT);// -1:失败 0:取消 1:成功 10:处理中
                msg = data.getExtras().getString(BaofooPayActivity.PAY_MESSAGE);

                if (result.equals("1")) {
                    RechargeOK();
                } else if (result.equals("-1") || result.equals("10") || result.equals("10") || result.equals("0")) {
                    ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), msg, new YCDialogUtils.MySingleBtnclickLisener() {
                        @Override
                        public void onBtnClick(View v) {
                            ycDialogUtils.DismissMyDialog();
                        }
                    }, true);
                } else {
                    msg = "未知充值状态！";
                    ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), msg, new YCDialogUtils.MySingleBtnclickLisener() {
                        @Override
                        public void onBtnClick(View v) {
                            ycDialogUtils.DismissMyDialog();
                        }
                    }, true);
                }
            }

        }
    }

    private boolean CheckInput() {
        money = et_money.getText().toString().trim();
        if (et_bankNum != null) {
            bankNum = et_bankNum.getText().toString().trim().replace(" ", "");
        } else {
            bankNum = rechargeInfo.bankNum;
        }
        if (StrUtil.isEmpty(money)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.recharge_moeny_can_not_be_empty), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }
        if (money.trim().equals("0")) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.recharge_moeny_must_over_zero), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }
        if (isFristRecharge) {
            if (StrUtil.isEmpty(bankNum)) {
                ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.bank_no_can_not_be_empty), new YCDialogUtils.MySingleBtnclickLisener() {
                    @Override
                    public void onBtnClick(View v) {
                        ycDialogUtils.DismissMyDialog();
                    }
                }, true);
                return false;
            }
        }
        return true;
    }


    private TextWatcher mTextWatcher = new TextWatcher() {

        private int editStart;
        private String money;
        private String bankNum;

        public void afterTextChanged(Editable s) {

            money = et_money.getText().toString().trim();
            editStart = et_money.getSelectionStart();

            // 先去掉监听器，否则会出现栈溢出
            et_money.removeTextChangedListener(mTextWatcher);
            et_money.setSelection(editStart);
            // 恢复监听器
            et_money.addTextChangedListener(mTextWatcher);

            if (et_bankNum == null) {
                if (StrUtil.isEmpty(money)) {
                    btn_nextStep.setEnabled(false);
                } else {
                    btn_nextStep.setEnabled(true);
                }
            } else {
                bankNum = et_bankNum.getText().toString().trim();
                if (StrUtil.isEmpty(money) || StrUtil.isEmpty(bankNum) || tv_bankName.getTag() == null || StrUtil.isEmpty(tv_bankName.getTag().toString())) {
                    btn_nextStep.setEnabled(false);
                } else {
                    btn_nextStep.setEnabled(true);
                }
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
        private String bankNum;
        private String editMoeny;

        public void afterTextChanged(Editable s) {

            bankNum = et_bankNum.getText().toString().trim();
            editStart = et_bankNum.getSelectionStart();

            // 先去掉监听器，否则会出现栈溢出
            et_bankNum.removeTextChangedListener(mTextWatcher2);
            et_bankNum.setSelection(editStart);
            // 恢复监听器
            et_bankNum.addTextChangedListener(mTextWatcher2);

            editMoeny = et_money.getText().toString().trim();
            if (StrUtil.isEmpty(editMoeny) || StrUtil.isEmpty(bankNum) || tv_bankName.getTag() == null || StrUtil.isEmpty(tv_bankName.getTag().toString())) {
                btn_nextStep.setEnabled(false);
            } else {
                btn_nextStep.setEnabled(true);
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
