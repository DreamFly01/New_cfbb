package com.cfbb.android.widget;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.cfbb.android.R;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.widget.dialog.YCDialogUtils;

/**
 * @author MrChang45
 * @time 2016/5/26
 * @desc 验证码按钮控件
 */
public class YCIdentifyCodeView extends LinearLayout implements View.OnClickListener {


    private Button btn_yzm;
    private CountDownTimer countDownTimer;
    private EditText et_tel;
    private boolean isGetVertifyCode = false;
    private YCDialogUtils ycDialogUtils;

    public void setYcIdentifyCodeViewLisener(YCIdentifyCodeViewLisener ycIdentifyCodeViewLisener) {
        this.ycIdentifyCodeViewLisener = ycIdentifyCodeViewLisener;
    }


    public void StartCountDown(int millisecond) {
        isGetVertifyCode = true;
        btn_yzm.setEnabled(false);
        btn_yzm.setTextColor(getResources().getColor(R.color.txt_9));
        countDownTimer = new CountDownTimer(millisecond, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                btn_yzm.setText(millisUntilFinished / 1000 + "秒重新获取");
            }

            @Override
            public void onFinish() {
                btn_yzm.setEnabled(true);
                btn_yzm.setText("获取验证码");
                btn_yzm.setTextColor(getResources().getColor(R.color.white));
            }

        };
        countDownTimer.start();
    }

    public void setEt_tel(EditText et_tel) {
        this.et_tel = et_tel;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
        ycDialogUtils = new YCDialogUtils(activity);
    }

    public boolean isGetVertifyCode() {
        return isGetVertifyCode;
    }


    public interface YCIdentifyCodeViewLisener {
        void onYCIdentifyCodeViewClickLisener();
    }

    private YCIdentifyCodeViewLisener ycIdentifyCodeViewLisener;

    private Activity activity;

    public YCIdentifyCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialView(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initialView(Context context) {
        View.inflate(context, R.layout.yc_vertifycode_layout, this);
        btn_yzm = (Button) findViewById(R.id.btn_commom);
        btn_yzm.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (null != ycIdentifyCodeViewLisener) {
            if (et_tel != null) {
                String tel = et_tel.getText().toString().trim();
                if (StrUtil.isEmpty(tel)) {
                    ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), "手机号码不能为空!", new YCDialogUtils.MySingleBtnclickLisener() {
                        @Override
                        public void onBtnClick(View v) {
                            ycDialogUtils.DismissMyDialog();
                        }
                    }, true);
                } else if (!StrUtil.checkMobile(tel)) {
                    ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), "请输入正确的手机号码!", new YCDialogUtils.MySingleBtnclickLisener() {
                        @Override
                        public void onBtnClick(View v) {
                            ycDialogUtils.DismissMyDialog();
                        }
                    }, true);
                } else {
                    ycIdentifyCodeViewLisener.onYCIdentifyCodeViewClickLisener();
                }
            } else {
                ycIdentifyCodeViewLisener.onYCIdentifyCodeViewClickLisener();
            }
        }

    }
}
