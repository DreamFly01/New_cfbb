package com.cfbb.android.widget.dialog;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.utils.others.StrUtil;

/***
 * 对话框帮助类
 *
 * @author MrChang
 */
public class YCDialogUtils {

    private Dialog mDialog;
    private Activity mActivity;
    private MyTwoBtnclickLisener myTwoBtnclickLisener;
    private MySingleBtnclickLisener mySingleBtnclickLisener;
    private MySMSclickLisener mySmsClickLisener;
    private OnButtomCickLisener onButtomClick;

    public YCDialogUtils(Activity activity) {
        mActivity = activity;
    }


    public interface MyTwoBtnclickLisener {

        void onFirstBtnClick(View v);

        void onSecondBtnClick(View v);

    }

    public interface MySingleBtnclickLisener {

        void onBtnClick(View v);
    }

    public interface MySMSclickLisener {

        void onFirstbtnClick(View v);

        void onSecondBtnClick(String code);

    }

    public interface OnButtomCickLisener {
        void sendSMS(CountDownTimer v);
    }


    public void hideDialog() {
        if (null != mDialog) {
            mDialog.hide();
        }
    }

    public void showDialog(String title, String showMsg, MyTwoBtnclickLisener myTwoBtnclickLisener,
                           boolean isAutoCanlce) {
        if (null != this.mActivity && !mActivity.isFinishing()) {
            this.myTwoBtnclickLisener = myTwoBtnclickLisener;
            Create(title, showMsg, isAutoCanlce);
        }
    }

    public void showSingleDialog(String title, String showMsg, MySingleBtnclickLisener mySingleBtnclickLisener,
                                 boolean isAutoCanlce) {

        if (null != this.mActivity && !mActivity.isFinishing()) {
            this.mySingleBtnclickLisener = mySingleBtnclickLisener;
            CreateSingle(title, showMsg, isAutoCanlce);
        }

    }

    public void showSingle2Dialog(String title, String showMsg, MySingleBtnclickLisener mySingleBtnclickLisener,
                                 boolean isAutoCanlce) {

        if (null != this.mActivity && !mActivity.isFinishing()) {
            this.mySingleBtnclickLisener = mySingleBtnclickLisener;
            CreateSingle2(title, showMsg, isAutoCanlce);
        }

    }

    public void showCallDialog(String showMsg, MyTwoBtnclickLisener myTwoBtnclickLisener,
                               boolean isAutoCanlce) {
        if (null != this.mActivity && !mActivity.isFinishing()) {
            this.myTwoBtnclickLisener = myTwoBtnclickLisener;
            CreateCall(showMsg, isAutoCanlce);
        }
    }

    public void showSMSDialog(String showMsg, int millisecond, MySMSclickLisener mysms, OnButtomCickLisener onButtomClick,
                              boolean isAutoCanlce) {
        if (null != this.mActivity && !mActivity.isFinishing()) {
            this.mySmsClickLisener = mysms;
            this.onButtomClick = onButtomClick;
            CreateSMS(showMsg, millisecond, isAutoCanlce);
        }
    }


    public void showAuthenticationDialog( String showMsg, MyTwoBtnclickLisener myTwoBtnclickLisener,
                           boolean isAutoCanlce) {
        if (null != this.mActivity && !mActivity.isFinishing()) {
            this.myTwoBtnclickLisener = myTwoBtnclickLisener;
            CreateNoAuthentication( showMsg, isAutoCanlce);
        }
    }

    private Button btn_ok;
    private Button btn_cancle;

    private void Create(String title, String showMsg, boolean c) {

        if (null != this.mActivity && !this.mActivity.isFinishing()) {

            // 构建对话框对象
            Builder builder = new Builder(this.mActivity);
            mDialog = builder.create();
            // 是否点击屏幕外面自动消失
            mDialog.setCanceledOnTouchOutside(c);
            mDialog.setCancelable(c);

            mDialog.show();

            // 如果要得到这个布局上的控件的话，就用 dialog.findViewById()
            // 要特别注意的是,dialog.show(),一定要放在dialog.setContentView()的前面
            View view = ((LayoutInflater) this.mActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.commom_dialog_layout, null);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            TextView tv_content = (TextView) view.findViewById(R.id.tv_02);
            btn_ok = (Button) view.findViewById(R.id.btn_ok);
            btn_cancle = (Button) view.findViewById(R.id.btn_cancel);
            tv_title.setText(title);
            tv_content.setText(Html.fromHtml(showMsg));
            btn_ok.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    myTwoBtnclickLisener.onFirstBtnClick(v);
                }
            });
            btn_cancle.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    myTwoBtnclickLisener.onSecondBtnClick(v);
                }
            });
            mDialog.setContentView(view);
        }
    }

    private void CreateSingle(String title, String showMsg, boolean c) {

        if (null != this.mActivity && !this.mActivity.isFinishing()) {

            // 构建对话框对象
            Builder builder = new Builder(this.mActivity);
            mDialog = builder.create();
            // 是否点击屏幕外面自动消失
            mDialog.setCanceledOnTouchOutside(c);
            mDialog.setCancelable(c);

            mDialog.show();

            // 如果要得到这个布局上的控件的话，就用 dialog.findViewById()
            // 要特别注意的是,dialog.show(),一定要放在dialog.setContentView()的前面
            View view = ((LayoutInflater) this.mActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.commom_single_dialog_layout, null);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            TextView tv_content = (TextView) view.findViewById(R.id.tv_02);

            btn_ok = (Button) view.findViewById(R.id.btn_ok);
            tv_title.setText(title);
            tv_content.setText(Html.fromHtml(showMsg));
            btn_ok.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mySingleBtnclickLisener.onBtnClick(v);
                }
            });
            mDialog.setContentView(view);
        }
    }
    private void CreateSingle2(String title, String showMsg, boolean c) {

        if (null != this.mActivity && !this.mActivity.isFinishing()) {

            // 构建对话框对象
            Builder builder = new Builder(this.mActivity);
            mDialog = builder.create();
            // 是否点击屏幕外面自动消失
            mDialog.setCanceledOnTouchOutside(c);
            mDialog.setCancelable(c);

            mDialog.show();

            // 如果要得到这个布局上的控件的话，就用 dialog.findViewById()
            // 要特别注意的是,dialog.show(),一定要放在dialog.setContentView()的前面
            View view = ((LayoutInflater) this.mActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.commom_single_dialog_layout, null);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            TextView tv_content = (TextView) view.findViewById(R.id.tv_02);

            btn_ok = (Button) view.findViewById(R.id.btn_ok);
            tv_title.setText(title);
            tv_content.setGravity(Gravity.LEFT|Gravity.CENTER);
            tv_content.setText(Html.fromHtml(showMsg));
            btn_ok.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mySingleBtnclickLisener.onBtnClick(v);
                }
            });
            mDialog.setContentView(view);
        }
    }
    private void CreateCall(String showMsg, boolean c) {

        if (null != this.mActivity && !this.mActivity.isFinishing()) {

            // 构建对话框对象
            Builder builder = new Builder(this.mActivity);
            mDialog = builder.create();
            // 是否点击屏幕外面自动消失
            mDialog.setCanceledOnTouchOutside(c);
            mDialog.setCancelable(c);

            mDialog.show();

            // 如果要得到这个布局上的控件的话，就用 dialog.findViewById()
            // 要特别注意的是,dialog.show(),一定要放在dialog.setContentView()的前面
            View view = ((LayoutInflater) this.mActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.commom_call_dialog_layout, null);
            TextView tv_content = (TextView) view.findViewById(R.id.tv_02);
            btn_ok = (Button) view.findViewById(R.id.btn_ok);
            btn_cancle = (Button) view.findViewById(R.id.btn_cancel);
            tv_content.setText(showMsg);
            btn_ok.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    myTwoBtnclickLisener.onSecondBtnClick(v);
                }
            });
            btn_cancle.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    myTwoBtnclickLisener.onFirstBtnClick(v);
                }
            });
            mDialog.setContentView(view);
        }
    }

    private void CreateSMS(String showMsg, long millisecond, boolean c) {

        if (null != this.mActivity && !this.mActivity.isFinishing()) {

            // 构建对话框对象
            Builder builder = new Builder(this.mActivity);
            mDialog = builder.create();
            // 是否点击屏幕外面自动消失
            mDialog.setCanceledOnTouchOutside(c);
            mDialog.setCancelable(c);

            mDialog.show();

            // 如果要得到这个布局上的控件的话，就用 dialog.findViewById()
            // 要特别注意的是,dialog.show(),一定要放在dialog.setContentView()的前面
            View view = ((LayoutInflater) this.mActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.commom_dialog_invest, null);
            final Button tv_get_agagin = (Button) view.findViewById(R.id.btn_commom);
            TextView tv_content = (TextView) view.findViewById(R.id.tv_02);
            final EditText et_sms = (EditText) view.findViewById(R.id.et_01);
            final CountDownTimer countDownTimer;
            btn_ok = (Button) view.findViewById(R.id.btn_ok);
            btn_cancle = (Button) view.findViewById(R.id.btn_cancel);
            tv_content.setText(showMsg);
            btn_ok.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String code = et_sms.getText().toString().trim();
                    if (!StrUtil.isEmpty(code)) {
                        mySmsClickLisener.onSecondBtnClick(code);
                    }

                }
            });
            btn_cancle.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mySmsClickLisener.onFirstbtnClick(v);
                }
            });
            mDialog.setContentView(view);
            tv_get_agagin.setEnabled(false);
            countDownTimer = new CountDownTimer(millisecond, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                    tv_get_agagin.setText(millisUntilFinished / 1000 + "秒");
                }

                @Override
                public void onFinish() {
                    tv_get_agagin.setEnabled(true);
                    tv_get_agagin.setText("重新获取");
                }

            };
            countDownTimer.start();

            tv_get_agagin.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtomClick.sendSMS(countDownTimer);
                }
            });
        }
    }


    private void CreateNoAuthentication(String showMsg, boolean c) {

        if (null != this.mActivity && !this.mActivity.isFinishing()) {

            // 构建对话框对象
            Builder builder = new Builder(this.mActivity);
            mDialog = builder.create();
            // 是否点击屏幕外面自动消失
            mDialog.setCanceledOnTouchOutside(c);
            mDialog.setCancelable(c);

            mDialog.show();

            // 如果要得到这个布局上的控件的话，就用 dialog.findViewById()
            // 要特别注意的是,dialog.show(),一定要放在dialog.setContentView()的前面
            View view = ((LayoutInflater) this.mActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.commom_dialog_layout, null);
           // TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            TextView tv_content = (TextView) view.findViewById(R.id.tv_02);

            btn_ok = (Button) view.findViewById(R.id.btn_ok);
            btn_cancle = (Button) view.findViewById(R.id.btn_cancel);

            btn_ok.setText(R.string.waitStr);
            btn_cancle.setText(R.string.goAuthentication);

            //tv_title.setText(R.string.Badly);

            tv_content.setText(Html.fromHtml(showMsg));

            btn_ok.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    myTwoBtnclickLisener.onFirstBtnClick(v);
                }
            });
            btn_cancle.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    myTwoBtnclickLisener.onSecondBtnClick(v);
                }
            });
            mDialog.setContentView(view);
        }
    }





    public void DismissMyDialog() {

        if (null != mDialog && mDialog.isShowing() && !mActivity.isFinishing()) {
            mDialog.dismiss();
        }
    }

}
