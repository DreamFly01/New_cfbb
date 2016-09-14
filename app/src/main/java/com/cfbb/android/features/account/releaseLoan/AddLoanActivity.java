package com.cfbb.android.features.account.releaseLoan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.state.LoanTypeEnum;
import com.cfbb.android.commom.state.RepaymentTypeEnum;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.commom.utils.others.Utils;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.TestResultUtils;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.MyLoanInfoBean;
import com.cfbb.android.widget.YCLoadingBg;
import com.cfbb.android.widget.dialog.YCDialogUtils;

/***
 * 新增借款
 */
public class AddLoanActivity extends BaseActivity implements View.OnClickListener {

    public static final String LOAN_ID = "loan_id";
    public static final String SHOW_BACK_STR = "show_back_str";

    private String loan_id;
    private String show_back_str;

    private EditText et_loan_title;
    private EditText et_loan_moeny;
    private EditText et_loan_yearOfDate;
    private EditText et_loan_addr;
    private EditText et_loan_describ;

    private RelativeLayout rl_loan_date_type;
    private RelativeLayout rl_loan_date;
    private RelativeLayout rl_retuan_way;
    private RelativeLayout rl_bid_days;


    private ImageView iv_loan_date_type;
    private ImageView iv_loan_retuan_way;

    private TextView tv_loan_date_type;
    private TextView tv_loan_date;
    private TextView tv_return_way;
    private TextView tv_bid_days;
    private TextView tv_loan_addr;

    private TextView tv_back;
    private TextView tv_title;
    private TextView tv_menu;
    private YCLoadingBg ycLoading;

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

    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_loan);
    }

    @Override
    public void setUpViews() {

        ycDialogUtils = new YCDialogUtils(this);
        ycLoading = (YCLoadingBg) findViewById(R.id.ycLoadingBg);
        et_loan_title = (EditText) findViewById(R.id.et_01);
        et_loan_moeny = (EditText) findViewById(R.id.et_02);
        et_loan_yearOfDate = (EditText) findViewById(R.id.et_03);
        et_loan_addr = (EditText) findViewById(R.id.et_04);
        et_loan_describ = (EditText) findViewById(R.id.et_05);

        rl_loan_date_type = (RelativeLayout) findViewById(R.id.rl_01);
        rl_loan_date = (RelativeLayout) findViewById(R.id.rl_02);
        rl_retuan_way = (RelativeLayout) findViewById(R.id.rl_03);
        rl_bid_days = (RelativeLayout) findViewById(R.id.rl_04);


        iv_loan_date_type = (ImageView) findViewById(R.id.iv_07);
        iv_loan_retuan_way = (ImageView) findViewById(R.id.iv_06);

        tv_loan_date_type = (TextView) findViewById(R.id.tv_04);
        tv_loan_date = (TextView) findViewById(R.id.tv_05);
        tv_return_way = (TextView) findViewById(R.id.tv_06);
        tv_bid_days = (TextView) findViewById(R.id.tv_08);
        tv_loan_addr = (TextView) findViewById(R.id.tv_07);

        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_menu = (TextView) findViewById(R.id.tv_menu);
        tv_back.setVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.release_loan_str));
    }

    @Override
    public void setUpLisener() {
        tv_back.setOnClickListener(this);
        tv_menu.setOnClickListener(this);
    }

    @Override
    public void getDataOnCreate() {
        mIntent = getIntent();

        if (mIntent != null) {
            loan_id = mIntent.getExtras().getString(LOAN_ID);
            show_back_str = mIntent.getExtras().getString(SHOW_BACK_STR);
        }

        if (StrUtil.isEmpty(show_back_str)) {
            tv_back.setText(getResources().getString(R.string.back_str));
        } else {
            tv_back.setText(show_back_str);
        }

        if (StrUtil.isEmpty(loan_id)) {
            //新增
            ShowAddView();
        } else {
            //展示
            ShowView();
        }

    }

    private MyLoanInfoBean myLoanInfo;

    private void ShowView() {

        addSubscription(RetrofitClient.GetMyLoanInfo(TestResultUtils.getSussefulResult38(), this, new YCNetSubscriber<MyLoanInfoBean>(this) {

            @Override
            public void onYcNext(MyLoanInfoBean model) {
                myLoanInfo = model;
                FillView();
            }

            @Override
            public void onYcFinish() {
                ycLoading.dissmiss();
            }

            @Override
            public void onYCError(APIException e) {
                ycLoading.showErroBg(new YCLoadingBg.YCErroLisener() {
                    @Override
                    public void onTryAgainClick() {
                        ShowView();
                    }
                });
            }
        }));
    }

    private void FillView() {
        et_loan_title.setText(myLoanInfo.loanTitle);
        et_loan_title.setEnabled(false);

        et_loan_moeny.setText(myLoanInfo.loanMoeny);
        et_loan_moeny.setEnabled(false);

        et_loan_yearOfDate.setText(myLoanInfo.yearOfRate);
        et_loan_yearOfDate.setEnabled(false);

        tv_loan_date_type.setText(myLoanInfo.loanDateType);
        iv_loan_date_type.setVisibility(View.INVISIBLE);
        tv_loan_date.setText(myLoanInfo.loanTime);

        tv_return_way.setText(myLoanInfo.returnWay);
        tv_bid_days.setText(myLoanInfo.bindingDays);
        iv_loan_retuan_way.setVisibility(View.INVISIBLE);
        tv_loan_addr.setText(myLoanInfo.loanAddr);
        et_loan_addr.setVisibility(View.INVISIBLE);
        et_loan_describ.setText(myLoanInfo.loanDescrib);
        et_loan_describ.setEnabled(false);
    }

    private String[] loanDaysDate;
    private String[] loanMonthDate;
    private String[] loanTypes = {LoanTypeEnum.LOAN_DAY.getName(), LoanTypeEnum.LOAN_MOENTH.getName()};
    private String[] bidDays;
    private String[] returnWays = {RepaymentTypeEnum.DISPOSABLE.getName(), RepaymentTypeEnum.EQUAL_INSTALLMENT_PAYMENT.getName(), RepaymentTypeEnum.INTEREST_PAYMENT_BY_QUARTER.getName(), RepaymentTypeEnum.INTEREST_PER_ANNUM.getName(), RepaymentTypeEnum.MONTHLY_PAYMENT_DUE.getName(), RepaymentTypeEnum.SERVICE_MATURITY_PRINCIPAL.getName()};

    private boolean isLoanTypeDay = true;

    private void ShowAddView() {

        ycLoading.dissmiss();
        loanDaysDate = getResources().getStringArray(R.array.days);
        loanMonthDate = getResources().getStringArray(R.array.months);
        bidDays = getResources().getStringArray(R.array.bidDays);

        rl_loan_date_type.setOnClickListener(this);
        rl_loan_date.setOnClickListener(this);
        rl_retuan_way.setOnClickListener(this);
        rl_bid_days.setOnClickListener(this);
        tv_menu.setText(getResources().getString(R.string.complete_str));
        tv_menu.setTextColor(getResources().getColor(R.color.txt_red));
        tv_menu.setVisibility(View.VISIBLE);

        tv_loan_date_type.setText(LoanTypeEnum.LOAN_DAY.getName());
        tv_return_way.setText(RepaymentTypeEnum.MONTHLY_PAYMENT_DUE.getName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_back:
                finish();
                break;

            case R.id.tv_menu:
                if (CheckInput()) {
                    Submit();
                }
                break;

            //借款日期类型
            case R.id.rl_01:
                CreatDialog(tv_loan_date_type, "请选择借款日期类型", loanTypes);
                break;

            //借款日期
            case R.id.rl_02:
                if (isLoanTypeDay) {
                    CreatDialog(tv_loan_date, "请选择借款日期", loanDaysDate);
                } else {
                    CreatDialog(tv_loan_date, "请选择借款日期", loanMonthDate);
                }
                break;

            //还款方式
            case R.id.rl_03:
                CreatDialog(tv_return_way, "请选择还款方式", returnWays);
                break;

            //竞标日期
            case R.id.rl_04:
                CreatDialog(tv_bid_days, "请选择竞标日期", bidDays);
                break;
        }
    }

    private void Submit() {
        //TestResultUtils.getBaseSussefulResult()
        addSubscription(RetrofitClient.AddLoan(null, title, moeny, loanDateType, bidDay, yearOfRate, returnWay, loanDate, addr, loanDescrib, this, new YCNetSubscriber(this) {

            @Override
            public void onYcNext(Object model) {
                //添加成功
                ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), "添加成功!", new YCDialogUtils.MySingleBtnclickLisener() {
                    @Override
                    public void onBtnClick(View v) {
                        ycDialogUtils.DismissMyDialog();
                        finish();
                    }
                }, true);
            }
        }));
    }

    private String title;
    private String moeny;
    private String yearOfRate;
    private String addr;
    private String loanDateType;
    private String loanDate = "";
    private String returnWay;
    private String bidDay;
    private String loanDescrib;


    private boolean CheckInput() {
        title = et_loan_title.getText().toString().trim();
        moeny = et_loan_moeny.getText().toString().trim();
        yearOfRate = et_loan_yearOfDate.getText().toString().trim();
        addr = et_loan_addr.getText().toString().trim();
        loanDateType = tv_loan_date_type.getText().toString().trim();
        if (loanDateType.equals("按月借款")) {
            loanDateType = "3";
        } else {
            loanDateType = "1";
        }

        if (tv_loan_date.getText().toString().contains("天")) {
            loanDate = tv_loan_date.getText().toString().trim().replace("天", "");
        } else {
            loanDate = tv_loan_date.getText().toString().trim().replace("月", "");
        }


        returnWay = tv_return_way.getText().toString().trim();
        returnWay = Utils.getReturnWayTypeByName(returnWay) + "";


        if (tv_bid_days.getText().toString().contains("天")) {
            bidDay = tv_bid_days.getText().toString().trim().replace("天", "");
        } else {
            bidDay = tv_bid_days.getText().toString().trim().replace("月", "");
        }

        // bidDay = tv_bid_days.getText().toString().trim().replace("天", "");
        //bidDay = bidDay.replace("月", "");

        loanDescrib = et_loan_describ.getText().toString().trim();
        if (StrUtil.isEmpty(title)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), "借款标题不能为空!", new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }
        if (StrUtil.isEmpty(moeny)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), "借款金额不能为空!", new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }
        if (StrUtil.isEmpty(yearOfRate)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), "预期年化收益不能为空!", new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }
        if (StrUtil.isEmpty(addr)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), "借款地区不能为空!", new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }
        if (StrUtil.isEmpty(loanDescrib)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), "借款描述不能为空!", new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }
        return true;
    }


    private void CreatDialog(final TextView tv_show, String title, final String[] itmes) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.icon_tu);
        builder.setTitle(title);
        builder.setItems(itmes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (itmes[which].equals(LoanTypeEnum.LOAN_MOENTH.getName()) && !itmes[which].equals(tv_show.getText().toString().trim())) {
                    tv_loan_date.setText("2月");
                    isLoanTypeDay = false;
                }
                if (itmes[which].equals(LoanTypeEnum.LOAN_DAY.getName()) && !itmes[which].equals(tv_show.getText().toString().trim())) {
                    tv_loan_date.setText("30天");
                    isLoanTypeDay = true;
                }
                tv_show.setText(itmes[which]);
            }
        });
        builder.show();

    }

}
