package com.cfbb.android.features.authentication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.textwatch.TextWatchForNoBlank;
import com.cfbb.android.commom.textwatch.TextWatchForOnlyChinese;
import com.cfbb.android.commom.state.CertificationEnum;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.CertificationResultBean;
import com.cfbb.android.widget.YCLoadingBg;
import com.cfbb.android.widget.dialog.YCDialogUtils;

/**
 * 实名认证
 */
public class RealNameAuthenticationActivity extends BaseActivity {


    public static final String CertificationState = "certificationstate";
    public static final String NEXT_ACTIVITY_CLASS = "next_activity_class";
    public static final String SHOW_BACK_TXT = "show_back_txt";

    private TextView tv_back;
    private TextView tv_title;
    private EditText et_realName;
    private EditText et_idcard;
    private Button btn_ok;
    private YCLoadingBg ycLoadingBg;
    private Class nextActivityClass;
    private String back_str;
    private YCDialogUtils ycDialogUtils;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_real_name_authentication);
        mIntent = getIntent();
        if (mIntent != null) {
            back_str = mIntent.getExtras().getString(SHOW_BACK_TXT);
            nextActivityClass = (Class) mIntent.getExtras().getSerializable(NEXT_ACTIVITY_CLASS);
        }
    }

    @Override
    public void setUpViews() {


        ycDialogUtils = new YCDialogUtils(this);

        ycLoadingBg = (YCLoadingBg) findViewById(R.id.ycLoadingBg);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        et_realName = (EditText) findViewById(R.id.et_01);
        et_idcard = (EditText) findViewById(R.id.et_02);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        et_realName.addTextChangedListener(new TextWatchForOnlyChinese());
        et_idcard.addTextChangedListener(new TextWatchForNoBlank());
        tv_back.setVisibility(View.VISIBLE);
        if (StrUtil.isEmpty(back_str)) {
            tv_back.setText(getResources().getString(R.string.account_set_str));
        } else {
            tv_back.setText(back_str);
        }

        tv_title.setText(getResources().getString(R.string.certification_str));

    }

    @Override
    public void setUpLisener() {
        tv_back.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void getDataOnCreate() {
        mIntent = getIntent();
        if (mIntent != null) {
            if (!(mIntent.getIntExtra(CertificationState, CertificationEnum.UNPASS_CERTIFICATION.getValue()) == CertificationEnum.UNPASS_CERTIFICATION.getValue())) {
                //已认证
                btn_ok.setVisibility(View.GONE);

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
                        et_realName.setText(model.realName);
                        et_idcard.setText(model.idCard);
                        et_realName.setEnabled(false);
                        et_idcard.setEnabled(false);
                        ycLoadingBg.dissmiss();
                    }
                }));
            } else {
                ycLoadingBg.dissmiss();
            }
        }

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
                    SubmitDate();
                }
                break;
        }
    }



    private String realName;
    private String idCrad;


    private void SubmitDate() {
        //TestResultUtils.getBaseSussefulResult()
        addSubscription(RetrofitClient.Certification(null, realName, idCrad, this, new YCNetSubscriber(this, true) {

            @Override
            public void onYcNext(Object model) {
                ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getString(R.string.authentication_right), new YCDialogUtils.MySingleBtnclickLisener() {
                    @Override
                    public void onBtnClick(View v) {
                        ycDialogUtils.DismissMyDialog();
                        setResult(RESULT_OK);
                        if (nextActivityClass == null) {
                            finish();
                        } else {
                            JumpCenter.JumpActivity(RealNameAuthenticationActivity.this, nextActivityClass, mIntent.getExtras(), null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, true);
                        }

                    }
                }, false);
            }
        }));
    }

    private boolean CheckInput() {
        realName = et_realName.getText().toString().trim();
        idCrad = et_idcard.getText().toString().trim();
        if (StrUtil.isEmpty(realName)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getString(R.string.real_name_can_not_be_empty), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }
        if (!StrUtil.isChinese(realName)) {
            ycDialogUtils.showSingleDialog(getString(R.string.dialog_title), getString(R.string.real_name_must_be_chinese), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }
        if (StrUtil.isEmpty(idCrad)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getString(R.string.id_card_can_not_be_empty), new YCDialogUtils.MySingleBtnclickLisener() {
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
