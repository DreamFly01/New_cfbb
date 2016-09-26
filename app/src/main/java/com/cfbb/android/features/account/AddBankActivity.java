package com.cfbb.android.features.account;

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
import com.cfbb.android.commom.textwatch.TextWatchForBankNumber;
import com.cfbb.android.commom.utils.base.KeyboardUtils;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.CertificationResultBean;
import com.cfbb.android.widget.YCLoadingBg;
import com.cfbb.android.widget.dialog.YCDialogUtils;

/***
 * 添加银行卡
 */
public class AddBankActivity extends BaseActivity {

    private TextView tv_back;
    private TextView tv_title;
    private Button btn_nextStep;
    private TextView tv_khm;
    private EditText et_02;
    private YCLoadingBg ycLoadingBg;
    private YCDialogUtils ycDialogUtils;


    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_bank);
    }

    @Override
    public void setUpViews() {

        ycDialogUtils = new YCDialogUtils(this);
        ycLoadingBg = (YCLoadingBg) findViewById(R.id.ycLoadingBg);
        et_02 = (EditText) findViewById(R.id.et_02);
        tv_khm = (TextView) findViewById(R.id.tv_05);
        btn_nextStep = (Button) findViewById(R.id.btn_ok);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);

        tv_title.setText(getResources().getString(R.string.add_bank_str));
        tv_back.setText(getResources().getString(R.string.my_bankcard_list_str));
        tv_back.setVisibility(View.VISIBLE);

        et_02.addTextChangedListener(mTextWatcher);
        et_02.addTextChangedListener(new TextWatchForBankNumber(et_02));


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
            //下一步
            case R.id.btn_ok:
                if (CheckInput()) {
                    Submit();
                }
                break;
        }
    }


    @Override
    public void getDataOnCreate() {
        //TestResultUtils.getSussefulResult14()
        addSubscription(RetrofitClient.getCertificationInfo(null, this, new YCNetSubscriber<CertificationResultBean>(this, true) {

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
                tv_khm.setText(model.realName);
            }

            @Override
            public void onYcFinish() {
                ycLoadingBg.dissmiss();
            }
        }));
    }

    private void Submit() {
        KeyboardUtils.hideSoftInput(this,et_02 );
        addSubscription(RetrofitClient.AddBank(null, bankNo, this, new YCNetSubscriber(this, true) {

            @Override
            public void onYcNext(Object model) {

                ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.add_right), new YCDialogUtils.MySingleBtnclickLisener() {
                    @Override
                    public void onBtnClick(View v) {
                        ycDialogUtils.DismissMyDialog();
                        finish();
                    }
                }, false);

            }

        }));
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
        KeyboardUtils.hideSoftInput(this,et_02 );
        StatService.onPause(this);
    }

    private Boolean hasShow = false;

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
        if (!hasShow) {
            KeyboardUtils.hideSoftInput(this,et_02 );
            hasShow = true;
        }
    }


    private TextWatcher mTextWatcher = new TextWatcher() {


        public void afterTextChanged(Editable s) {

            et_02.removeTextChangedListener(mTextWatcher);
            et_02.addTextChangedListener(mTextWatcher);

            if (et_02.getText().toString().length() >= 19 && et_02.getText().toString().length() <= 23) {
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
