package com.cfbb.android.features.account;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.cfbb.android.commom.textwatch.TextWatchForBankNumber;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.commom.utils.base.KeyboardUtils;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.APIService;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.BankBean;
import com.cfbb.android.protocol.bean.BaseResultBean;
import com.cfbb.android.protocol.bean.CertificationResultBean;
import com.cfbb.android.widget.YCLoadingBg;
import com.cfbb.android.widget.dialog.YCDialogUtils;

import java.util.ArrayList;
import java.util.List;

/***
 * 添加银行卡
 */
public class AddBankActivity extends BaseActivity {

    /**
     * 默认跳转我的银行卡
     */
    public static final String ADDBANK_RIGHT_TURN_TO_ACTIVITY_CLASS = "addbank_right_turn_to_activity_class";
    public static final String BACK_TXT = "back_txt";

    private TextView tv_back;
    private TextView tv_title;
    private Button btn_nextStep;
    private TextView tv_khm;
    private EditText et_02;
    private YCLoadingBg ycLoadingBg;
    private YCDialogUtils ycDialogUtils;
    private String back_txt;
    private ImageView iv_hint;
    private Class turnToActivity;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_bank);
        mIntent = getIntent();
        if (null != mIntent) {
            back_txt = mIntent.getExtras().getString(BACK_TXT);
            turnToActivity = (Class) mIntent.getExtras().getSerializable(ADDBANK_RIGHT_TURN_TO_ACTIVITY_CLASS);
        }
    }

    private TextView tv_bank;

    @Override
    public void setUpViews() {

        ycDialogUtils = new YCDialogUtils(this);
        ycLoadingBg = (YCLoadingBg) findViewById(R.id.ycLoadingBg);
        et_02 = (EditText) findViewById(R.id.et_02);
        tv_khm = (TextView) findViewById(R.id.tv_05);
        btn_nextStep = (Button) findViewById(R.id.btn_ok);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_hint = (ImageView) findViewById(R.id.iv_06);
        tv_title.setText(getResources().getString(R.string.bind_bank_str));
        if (StrUtil.isEmpty(back_txt)) {
            tv_back.setText(getResources().getString(R.string.app_name));
        } else {
            tv_back.setText(back_txt);
        }
        tv_back.setVisibility(View.VISIBLE);
        tv_bank = (TextView) findViewById(R.id.tv_08);
        et_02.addTextChangedListener(mTextWatcher);
        et_02.addTextChangedListener(new TextWatchForBankNumber(et_02));

    }

    @Override
    public void setUpLisener() {

        tv_back.setOnClickListener(this);
        iv_hint.setOnClickListener(this);
        tv_bank.setOnClickListener(this);
        btn_nextStep.setOnClickListener(this);

    }


    @Override
    public void onUserClick(View v) {
        super.onUserClick(v);
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            //添加银行卡
            case R.id.btn_ok:
                if (CheckInput()) {
                    Submit();
                }
                break;
            //选择银行卡
            case R.id.tv_08:
                GetSupportBank();
                break;
            case R.id.iv_06:
                ycDialogUtils.showSingle2Dialog(getResources().getString(R.string.CardholderExplain), certificationResultBean.cardHolderDesc, new YCDialogUtils.MySingleBtnclickLisener() {
                    @Override
                    public void onBtnClick(View v) {
                        ycDialogUtils.DismissMyDialog();
                    }
                }, true);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(AddBankActivity.this);
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
                tv_bank.setText(cities[which]);
                tv_bank.setTag(bankList.get(which).bankCode);
                if (StrUtil.isEmpty(et_02.getText().toString()) || tv_bank.getTag() == null || StrUtil.isEmpty(tv_bank.getTag().toString())) {
                    btn_nextStep.setEnabled(false);
                } else {
                    btn_nextStep.setEnabled(true);
                }
            }
        });
        builder.show();
    }

    private CertificationResultBean certificationResultBean;
    @Override
    public void getDataOnCreate() {
        //TestResultUtils.getSussefulResult14()
        addSubscription(RetrofitClient.getCertificationInfo(null, this, new YCNetSubscriber<CertificationResultBean>(this) {

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
            public void onYcNext(CertificationResultBean model) {
                certificationResultBean = model;
                tv_khm.setText(certificationResultBean.realName);
            }

            @Override
            public void onYcFinish() {
                ycLoadingBg.dissmiss();
            }

        }));
    }

    private void Submit() {
        KeyboardUtils.hideSoftInput(this, et_02);

        BaseResultBean resultBean = new BaseResultBean();
        resultBean.code = APIService.OK_CODE;

        addSubscription(RetrofitClient.AddBank(null, bankNo,tv_bank.getTag().toString(), this, new YCNetSubscriber(this, true) {

            @Override
            public void onYcNext(Object model) {
                TurnToOtherActivity();
            }

        }));
    }

    private void TurnToOtherActivity() {

        if (turnToActivity == null) {
            JumpCenter.JumpActivity(AddBankActivity.this, MyBankInfoActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, true);

        } else {
            JumpCenter.JumpActivity(AddBankActivity.this, turnToActivity, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, true);
        }

    }

    private String bankNo = "";

    private boolean CheckInput() {

        bankNo = et_02.getText().toString().replace(" ", "");
        if (StrUtil.isEmpty(bankNo)) {

            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.bank_no_can_not_be_empty), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }

        if (!StrUtil.isNumeric(bankNo)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.bank_no_must_be_number), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }

        if (bankNo.length() < 16 || bankNo.length() > 21) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.bank_no_not_right), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }

        return true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        KeyboardUtils.hideSoftInput(this, et_02);
        StatService.onPause(this);
    }

    private Boolean hasShow = false;

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
        if (!hasShow) {
            KeyboardUtils.hideSoftInput(this, et_02);
            hasShow = true;
        }
    }


    private TextWatcher mTextWatcher = new TextWatcher() {


        public void afterTextChanged(Editable s) {

            et_02.removeTextChangedListener(mTextWatcher);
            et_02.addTextChangedListener(mTextWatcher);

            if (et_02.getText().toString().length() >= 19 && et_02.getText().toString().length() <= 23 & tv_bank.getTag() != null && !StrUtil.isEmpty(tv_bank.getTag().toString())) {
                btn_nextStep.setEnabled(true);
            } else {
                btn_nextStep.setEnabled(false);
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
