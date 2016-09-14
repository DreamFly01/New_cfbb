package com.cfbb.android.features.account;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.state.AutoInvestStateEnum;
import com.cfbb.android.commom.state.AutoInvestTypeEnum;
import com.cfbb.android.commom.state.InvestPeriodEnum;
import com.cfbb.android.commom.state.RepaymentTypeEnum;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.AutoInvestInfoBean;
import com.cfbb.android.widget.dialog.YCDialogUtils;

/***
 * 编辑自动投标
 */
public class EditAutoInvestActivity extends BaseActivity  {


    public static final String AUTO_INVEST_DATA = "auto_invest_data";

    private TextView tv_title;
    private TextView tv_menu;
    private TextView tv_back;

    private RadioButton rb_01;
    private RadioButton rb_02;
    private RadioButton rb_03;
    private RadioButton rb_04;
    private RadioButton rb_day_day;
    private RadioButton rb_day_month;

    private CheckBox cb_01;
    private CheckBox cb_02;
    private CheckBox cb_03;

    private EditText et_01;
    private EditText et_02;
    private EditText et_03;
    private EditText et_04;
    private EditText et_05;
    private EditText et_06;
    private EditText et_07;
    private EditText et_08;
    private EditText et_09;

    private AutoInvestInfoBean autoInvestInfo;


    private YCDialogUtils ycDialogUtils;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_edit_auto_invest);
        mIntent = getIntent();
        if (null != getIntent()) {
            autoInvestInfo = mIntent.getExtras().getParcelable(AUTO_INVEST_DATA);
        }
    }

    @Override
    public void setUpViews() {

        ycDialogUtils = new YCDialogUtils(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_menu = (TextView) findViewById(R.id.tv_menu);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setText(getResources().getString(R.string.cancle));
        tv_back.setVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.auto_invest_str));
        tv_menu = (TextView) findViewById(R.id.tv_menu);
        tv_menu.setText(getResources().getString(R.string.complete_str));
        tv_menu.setTextColor(getResources().getColor(R.color.txt_red));
        tv_menu.setVisibility(View.VISIBLE);
        tv_menu.setOnClickListener(this);
        ViewStub viewStub = (ViewStub) findViewById(R.id.viewStub_01);
        viewStub.inflate();
        EditShow();
    }

    @Override
    public void setUpLisener() {
        tv_back.setOnClickListener(this);
    }


    @Override
    public void onUserClick(View v) {
        super.onUserClick(v);
        switch (v.getId()) {
            case R.id.tv_back:
                JumpCenter.JumpActivity(this, AutoInvestActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, true);
                break;
            case R.id.tv_menu:
                //完成
                if (CheckInput()) {
                    Submit();
                }
                break;
        }
    }


    private void Submit() {
        //TestResultUtils.getBaseSussefulResult()
        addSubscription(RetrofitClient.SetAutoInvest(null, auto_type + "", inputValue, rate_start, rate_end, day_start, day_end, inputValue2, AutoInvestStateEnum.OPEN.getValue() + "", this, new YCNetSubscriber(this, true) {
            @Override
            public void onYcNext(Object model) {
                //设置成功
                JumpCenter.JumpActivity(EditAutoInvestActivity.this, AutoInvestActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, true);
            }
        }));
    }

    private String fixed_moeny = "";
    private String zkb_ze = "";
    private String zhye_bl = "";
    private String rate_start = "";
    private String rate_end = "";
    private String day_start = "";
    private String day_end = "";
    private String yue_start = "";
    private String yue_end = "";
    private String inputValue = "";
    private String inputValue2 = "";

    private boolean CheckInput() {

        fixed_moeny = et_01.getText().toString().trim();
        zkb_ze = et_02.getText().toString().trim();
        zhye_bl = et_03.getText().toString().trim();
        rate_start = et_04.getText().toString().trim();
        rate_end = et_05.getText().toString().trim();
        day_start = et_06.getText().toString().trim();
        day_end = et_07.getText().toString().trim();
        yue_start = et_08.getText().toString().trim();
        yue_end = et_09.getText().toString().trim();

        if (auto_type == -1) {

            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getString(R.string.auto_invest_moeny_must_be_choose), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;

        }
        if (auto_type == AutoInvestTypeEnum.FIXED_AMOUNT.getValue()) {

            if (StrUtil.isEmpty(fixed_moeny)) {
                ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getString(R.string.fixed_moeny_can_not_be_empty), new YCDialogUtils.MySingleBtnclickLisener() {
                    @Override
                    public void onBtnClick(View v) {
                        ycDialogUtils.DismissMyDialog();
                    }
                }, true);
                return false;
            }
            inputValue = fixed_moeny;
        }
        if (auto_type == AutoInvestTypeEnum.PROPORTION_OF_TOTAL_BORROWINGS.getValue()) {
            if (StrUtil.isEmpty(zkb_ze)) {
                ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getString(R.string.loan_bid_rate_can_not_be_empty), new YCDialogUtils.MySingleBtnclickLisener() {
                    @Override
                    public void onBtnClick(View v) {
                        ycDialogUtils.DismissMyDialog();
                    }
                }, true);
                return false;
            }
            inputValue = zkb_ze;
        }
        if (auto_type == AutoInvestTypeEnum.ACCOUNT_BALANCE_RATIO.getValue()) {
            if (StrUtil.isEmpty(zhye_bl)) {
                ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getString(R.string.account_remains_moeny_can_not_be_empty), new YCDialogUtils.MySingleBtnclickLisener() {
                    @Override
                    public void onBtnClick(View v) {
                        ycDialogUtils.DismissMyDialog();
                    }
                }, true);
                return false;
            }
            inputValue = zhye_bl;
        }
        try {
            double rate_start_double = Double.parseDouble(rate_start);
            double rate_enf_double = Double.parseDouble(rate_end);
            if (rate_start_double > rate_enf_double) {
                ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getString(R.string.erro_loan_rate_range), new YCDialogUtils.MySingleBtnclickLisener() {
                    @Override
                    public void onBtnClick(View v) {
                        ycDialogUtils.DismissMyDialog();
                    }
                }, true);
                return false;
            }
        } catch (Exception e) {
            if ((StrUtil.isEmpty(rate_start) && !StrUtil.isEmpty(rate_end)) || (StrUtil.isEmpty(rate_end) && !StrUtil.isEmpty(rate_start))) {
                ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getString(R.string.loan_rate_can_not_be_empty), new YCDialogUtils.MySingleBtnclickLisener() {
                    @Override
                    public void onBtnClick(View v) {
                        ycDialogUtils.DismissMyDialog();
                    }
                }, true);
                return false;
            }
        }
        if (day_or_month == InvestPeriodEnum.DAY.getValue()) {
            try {
                double day_start_double = Double.parseDouble(day_start);
                double day_enf_double = Double.parseDouble(day_end);
                if (day_start_double > day_enf_double) {
                    ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getString(R.string.erro_loan_bid_range), new YCDialogUtils.MySingleBtnclickLisener() {
                        @Override
                        public void onBtnClick(View v) {
                            ycDialogUtils.DismissMyDialog();
                        }
                    }, true);
                    return false;
                }
            } catch (Exception e) {
                if ((StrUtil.isEmpty(day_start) && !StrUtil.isEmpty(day_end)) || (StrUtil.isEmpty(day_end) && !StrUtil.isEmpty(day_start))) {
                    ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getString(R.string.erro_loan_bid_range), new YCDialogUtils.MySingleBtnclickLisener() {
                        @Override
                        public void onBtnClick(View v) {
                            ycDialogUtils.DismissMyDialog();
                        }
                    }, true);
                    return false;
                }
            }
        }
        inputValue2 = "";
        if (cb_01.isChecked()) {
            inputValue2 += RepaymentTypeEnum.DISPOSABLE.getValue();
        }
        if (cb_02.isChecked()) {
            inputValue2 += "," + RepaymentTypeEnum.EQUAL_INSTALLMENT_PAYMENT.getValue();
        }
        if (cb_03.isChecked()) {
            inputValue2 += "," + RepaymentTypeEnum.MONTHLY_PAYMENT_DUE.getValue();
        }
        if (inputValue2.startsWith(",")) {
            inputValue2 = inputValue2.substring(1, inputValue2.length());
        }
        if (day_or_month == InvestPeriodEnum.YUE.getValue()) {
            try {
                double yue_start_double = Double.parseDouble(yue_start);
                double yue_enf_double = Double.parseDouble(yue_end);
                if (yue_start_double >= yue_enf_double) {
                    ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), "月标范围填写有误!", new YCDialogUtils.MySingleBtnclickLisener() {
                        @Override
                        public void onBtnClick(View v) {
                            ycDialogUtils.DismissMyDialog();
                        }
                    }, true);
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    private int auto_type = -1;
    private int day_or_month;

    private void EditShow() {
        rb_01 = (RadioButton) findViewById(R.id.rb_01);
        rb_02 = (RadioButton) findViewById(R.id.rb_02);
        rb_03 = (RadioButton) findViewById(R.id.rb_03);
        rb_04 = (RadioButton) findViewById(R.id.rb_04);
        rb_day_day = (RadioButton) findViewById(R.id.rb_05);
        rb_day_month = (RadioButton) findViewById(R.id.rb_06);

        cb_01 = (CheckBox) findViewById(R.id.cb_01);
        cb_02 = (CheckBox) findViewById(R.id.cb_02);
        cb_03 = (CheckBox) findViewById(R.id.cb_03);
        et_01 = (EditText) findViewById(R.id.et_01);
        et_02 = (EditText) findViewById(R.id.et_02);
        et_03 = (EditText) findViewById(R.id.et_03);
        et_04 = (EditText) findViewById(R.id.et_04);
        et_05 = (EditText) findViewById(R.id.et_05);
        et_06 = (EditText) findViewById(R.id.et_06);
        et_07 = (EditText) findViewById(R.id.et_07);
        et_08 = (EditText) findViewById(R.id.et_08);
        et_09 = (EditText) findViewById(R.id.et_09);
        rb_01.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rb_02.setChecked(false);
                    rb_03.setChecked(false);
                    rb_04.setChecked(false);
                    auto_type = AutoInvestTypeEnum.AVAILABLE_BALANCE.getValue();
                }
            }
        });
        rb_02.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rb_01.setChecked(false);
                    rb_03.setChecked(false);
                    rb_04.setChecked(false);
                    auto_type = AutoInvestTypeEnum.FIXED_AMOUNT.getValue();
                }
            }
        });
        rb_03.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rb_01.setChecked(false);
                    rb_02.setChecked(false);
                    rb_04.setChecked(false);
                    auto_type = AutoInvestTypeEnum.PROPORTION_OF_TOTAL_BORROWINGS.getValue();
                }
            }
        });
        rb_04.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rb_01.setChecked(false);
                    rb_02.setChecked(false);
                    rb_03.setChecked(false);
                    auto_type = AutoInvestTypeEnum.ACCOUNT_BALANCE_RATIO.getValue();
                }
            }
        });
        rb_day_day.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rb_day_month.setChecked(false);
                    day_or_month = InvestPeriodEnum.DAY.getValue();
                }
            }
        });
        rb_day_month.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rb_day_day.setChecked(false);
                    day_or_month = InvestPeriodEnum.YUE.getValue();
                }
            }
        });

        if (autoInvestInfo.auto_type == AutoInvestTypeEnum.AVAILABLE_BALANCE.getValue()) {
            rb_01.setChecked(true);
        }
        if (autoInvestInfo.auto_type == AutoInvestTypeEnum.FIXED_AMOUNT.getValue()) {
            rb_02.setChecked(true);
            et_01.setText(autoInvestInfo.auto_amount);
        }
        if (autoInvestInfo.auto_type == AutoInvestTypeEnum.PROPORTION_OF_TOTAL_BORROWINGS.getValue()) {
            rb_03.setChecked(true);
            et_02.setText(autoInvestInfo.auto_amount);
        }
        if (autoInvestInfo.auto_type == AutoInvestTypeEnum.ACCOUNT_BALANCE_RATIO.getValue()) {
            rb_04.setChecked(true);
            et_03.setText(autoInvestInfo.auto_amount);
        }
        if (StrUtil.isEmpty(autoInvestInfo.rate_begin)) {
            et_04.setText("");
        } else {
            et_04.setText(autoInvestInfo.rate_begin);
        }
        if (StrUtil.isEmpty(autoInvestInfo.rate_end)) {
            et_05.setText("");
        } else {
            et_05.setText(autoInvestInfo.rate_end);
        }
        if (autoInvestInfo.day_unit == InvestPeriodEnum.DAY.getValue()) {
            rb_day_day.setChecked(true);
            if (StrUtil.isEmpty(autoInvestInfo.day_begin)) {
                et_06.setText("");
            } else {
                et_06.setText(autoInvestInfo.day_begin);
            }
            if (StrUtil.isEmpty(autoInvestInfo.day_end)) {
                et_07.setText("");
            } else {
                et_07.setText(autoInvestInfo.day_end);
            }
        }
        if (autoInvestInfo.day_unit == InvestPeriodEnum.YUE.getValue()) {
            rb_day_month.setChecked(true);
            if (StrUtil.isEmpty(autoInvestInfo.day_begin)) {
                et_08.setText("");
            } else {
                et_08.setText(autoInvestInfo.day_begin);
            }
            if (StrUtil.isEmpty(autoInvestInfo.day_end)) {
                et_09.setText("");
            } else {
                et_09.setText(autoInvestInfo.day_end);
            }
        }
        String[] temp = autoInvestInfo.repayMode_id.split(",", -1);
        if (temp.length > 0) {
            try {
                for (int i = 0; i < temp.length; i++) {
                    if (Integer.parseInt(temp[i]) == RepaymentTypeEnum.DISPOSABLE.getValue()) {
                        cb_01.setChecked(true);
                    }
                    if (Integer.parseInt(temp[i]) == RepaymentTypeEnum.EQUAL_INSTALLMENT_PAYMENT.getValue()) {
                        cb_02.setChecked(true);
                    }
                    if (Integer.parseInt(temp[i]) == RepaymentTypeEnum.MONTHLY_PAYMENT_DUE.getValue()) {
                        cb_03.setChecked(true);
                    }
                }
            } catch (Exception e) {
            }

        } else {
            //为0，代表全部Ok
            cb_01.setChecked(true);
            cb_02.setChecked(true);
            cb_03.setChecked(true);
        }

    }

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
