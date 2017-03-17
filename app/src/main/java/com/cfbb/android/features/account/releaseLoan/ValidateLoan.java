package com.cfbb.android.features.account.releaseLoan;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.state.VertifyCodeEnum;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.features.main.MainActivity;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.BaseResultBean;
import com.cfbb.android.protocol.bean.BindPhoneBean;
import com.cfbb.android.protocol.bean.VertifyCodeInfoBean;
import com.cfbb.android.widget.YCLoadingBg;
import com.cfbb.android.widget.dialog.YCDialogUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ValidateLoan extends BaseActivity {
    private TextView huoqu, tijiao, tv_mobile, et_code,back;
    private YCLoadingBg ycLoadingBg;
    private YCDialogUtils ycDialogUtils;
    private BindPhoneBean bindPhone;

    private MyCountDownTimer mc;
    private String result;
    private String result2;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_validate_loan);

    }

    @Override
    public void getDataOnCreate() {


        try {
            File file = new File(Environment.getExternalStorageDirectory(),
                    "result.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String readline = "";
            StringBuffer sb = new StringBuffer();
            while ((readline = br.readLine()) != null) {
                sb.append(readline);
            }
            br.close();
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            File file = new File(Environment.getExternalStorageDirectory(),
                    "imgArr.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String readline = "";
            StringBuffer sb = new StringBuffer();
            while ((readline = br.readLine()) != null) {
                sb.append(readline);
            }
            br.close();
            result2 = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mc = new MyCountDownTimer(90000, 1000);
        super.getDataOnCreate();
        addSubscription(RetrofitClient.GetBindMobileInfo(null, this, new YCNetSubscriber<BindPhoneBean>(this) {

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
            public void onYcNext(BindPhoneBean model) {
                bindPhone = model;
                tv_mobile.setText(bindPhone.mobilePhone);
                ycLoadingBg.dissmiss();
            }

            @Override
            public void onYcFinish() {
            }
        }));
    }

    @Override
    public void setUpViews() {
        huoqu = (TextView) findViewById(R.id.huoquyanz);
        tijiao = (TextView) findViewById(R.id.tv_tijiao);
        tv_mobile = (TextView) findViewById(R.id.tv_mob);
        back = (TextView)findViewById(R.id.tv_back);
        back.setVisibility(View.VISIBLE);
        back.setText("返回");
        et_code = (EditText) findViewById(R.id.sms_ed);
        ycDialogUtils = new YCDialogUtils(this);
        ycLoadingBg = (YCLoadingBg) findViewById(R.id.ycLoadingBg);
    }

    @Override
    public void setUpLisener() {
        huoqu.setOnClickListener(this);
        tijiao.setOnClickListener(this);
    }

    private String code;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.huoquyanz:
                addSubscription(RetrofitClient.GeVertifyCode(null, "", VertifyCodeEnum.REQUEST_SMS_CODE.getValue(), ValidateLoan.this, new YCNetSubscriber<VertifyCodeInfoBean>(ValidateLoan.this, true) {
                    @Override
                    public void onYcNext(VertifyCodeInfoBean model) {
//                        code = model.mark;
                        huoqu.setEnabled(false);
                        mc.start();
                    }
                }));

                break;
            case R.id.tv_tijiao:
                code = et_code.getText().toString();
                addSubscription(RetrofitClient.GetSMS(null, code, ValidateLoan.this, new YCNetSubscriber<BaseResultBean>(ValidateLoan.this, true) {
                    @Override
                    public void onYcNext(BaseResultBean model) {
//                        showShortToast(model.msg);
                        upLoadData(result, result2);
                    }
                }));
                break;
            case R.id.tv_back:
                this.finish();
                break;
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

    private void upLoadData(String result, final String result1) {
        addSubscription(RetrofitClient.IntroduceLoan(null, result, result1, ValidateLoan.this, new YCNetSubscriber<BaseResultBean>(ValidateLoan.this, true) {
            @Override
            public void onYcNext(BaseResultBean model) {
//                showShortToast(model.msg);
                dialog1("发布借款成功");
            }
        }));
    }

    class MyCountDownTimer extends CountDownTimer {
        /**
         * @param millisInFuture    表示以毫秒为单位 倒计时的总数
         *                          <p/>
         *                          例如 millisInFuture=1000 表示1秒
         * @param countDownInterval 表示 间隔 多少微秒 调用一次 onTick 方法
         *                          <p/>
         *                          例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            huoqu.setText("获取验证码");
            huoqu.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            huoqu.setText(millisUntilFinished / 1000 + "s");
        }
    }

    /**
     * 弹窗
     */
    private void dialog1(String str) {
        ycDialogUtils.showSingleDialog("提示", str, new YCDialogUtils.MySingleBtnclickLisener() {
            @Override
            public void onBtnClick(View v) {
                ycDialogUtils.DismissMyDialog();
                File file = new File(Environment.getExternalStorageDirectory(),
                        "imgArr.txt");
                file.delete();
                File file1 = new File(Environment.getExternalStorageDirectory(),
                        "result.txt");
                file1.delete();
                JumpCenter.JumpActivity(ValidateLoan.this, MainActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, false);
            }
        }, true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK) {
            this.finish();
            return false;
        }else{
        return super.onKeyDown(keyCode, event);
        }
    }
}
