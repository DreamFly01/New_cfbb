package com.cfbb.android.features.aboutus;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.config.Const;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.commom.utils.base.AppUtils;
import com.cfbb.android.commom.utils.base.PhoneUtils;
import com.cfbb.android.commom.utils.others.ArithUtil;
import com.cfbb.android.commom.utils.others.SPUtils;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.commom.utils.others.Utils;
import com.cfbb.android.features.update.UpdateAppService;
import com.cfbb.android.features.update.UpdateDialog;
import com.cfbb.android.features.webview.OtherActivity;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.UpdateVersionBean;
import com.cfbb.android.protocol.downRequest.IProgressLisenr;
import com.cfbb.android.widget.dialog.YCDialogUtils;

/***
 * 关于我们
 */
public class AboutUsActivity extends BaseActivity implements IProgressLisenr {

    public static final String ABOUT_US_URL = "about_us_url";

    private TextView tv_back;
    private TextView tv_title;

    private String tel;
    private String url;
    private TextView tv_tel;
    private TextView tv_version;

    private RelativeLayout rl_01;
    private RelativeLayout rl_02;
    private RelativeLayout rl_03;
    private UpdateAppService.MyBuinder myBuinder;

    private YCDialogUtils ycDialogUtils;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_about_us);
    }

    @Override
    public void setUpViews() {

        ycDialogUtils = new YCDialogUtils(this);
        rl_01 = (RelativeLayout) findViewById(R.id.rl_01);
        rl_02 = (RelativeLayout) findViewById(R.id.rl_02);
        rl_03 = (RelativeLayout) findViewById(R.id.rl_03);
        tv_version = (TextView) findViewById(R.id.tv_02);
        tv_tel = (TextView) findViewById(R.id.tv_04);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_back.setVisibility(View.VISIBLE);
        tv_back.setText(getResources().getString(R.string.account_set_str));
        tv_title.setText(getResources().getString(R.string.about_us_str));
        tv_version.setText(Utils.getVersionName(this));

    }

    @Override
    public void setUpLisener() {

        rl_01.setOnClickListener(this);
        rl_02.setOnClickListener(this);
        rl_03.setOnClickListener(this);
        tv_back.setOnClickListener(this);

    }

    @Override
    public void getDataOnCreate() {
        mIntent = getIntent();
        if (mIntent != null) {
            url = mIntent.getExtras().getString(ABOUT_US_URL);
            tel = (String) SPUtils.get(this, Const.CUSTOM_TEL, "");
            tv_tel.setText(tel);
        }
    }

    private UpdateDialog updateDialog;

    @Override
    public void onUserClick(View v) {
        super.onUserClick(v);
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            //版本更新检测
            case R.id.rl_01:

                RetrofitClient.getVersionInfoRequest(null, AboutUsActivity.this, new YCNetSubscriber<UpdateVersionBean>(this, true) {


                    @Override
                    public void onYcNext(UpdateVersionBean updateVersionBean) {

                        int currentVersionCode = AppUtils.getAppInfo(AboutUsActivity.this).getVersionCode();
                        if (currentVersionCode < updateVersionBean.version_code) {
                            //需要更新版本
                            final String downUrl = updateVersionBean.url;
                            updateDialog = new UpdateDialog(AboutUsActivity.this);
                            updateDialog.showDialog(updateVersionBean.version_desc, new UpdateDialog.MyBtnclickLisener() {
                                @Override
                                public void onOkClick(View v) {
                                    updateDialog.setBtnEnable(false);
                                    updateDialog.showPrograss();
                                    mIntent = new Intent(AboutUsActivity.this, UpdateAppService.class);
                                    bindService(mIntent, new ServiceConnection() {
                                        @Override
                                        public void onServiceConnected(ComponentName name, IBinder service) {
                                            myBuinder = (UpdateAppService.MyBuinder) service;
                                            myBuinder.setProgressLisener(AboutUsActivity.this);
                                            myBuinder.startDown(downUrl);
                                        }

                                        @Override
                                        public void onServiceDisconnected(ComponentName name) {

                                        }
                                    }, BIND_AUTO_CREATE);
                                }

                                @Override
                                public void onCancel(View v) {

                                }
                            }, false, updateVersionBean.force_update == 1 ? true : false);

                        } else {
                            //不需要更新
                            showShortToast(getString(R.string.is_latest_app));
                        }
                    }
                });

                break;
            //拨打客服电话
            case R.id.rl_02:
                if (!StrUtil.isEmpty(tel)) {
                    ycDialogUtils.showCallDialog(tel, new YCDialogUtils.MyTwoBtnclickLisener() {
                        @Override
                        public void onFirstBtnClick(View v) {
                            ycDialogUtils.DismissMyDialog();
                        }

                        @Override
                        public void onSecondBtnClick(View v) {
                            ycDialogUtils.DismissMyDialog();
                            PhoneUtils.callDierct(AboutUsActivity.this, tel);
                        }
                    }, true);
                }
                break;
            //关于我们
            case R.id.rl_03:
                Bundle bundle = new Bundle();
                bundle.putString(OtherActivity.BACK_STR, getResources().getString(R.string.about_us_str));
                bundle.putString(OtherActivity.URL, url);
                JumpCenter.JumpActivity(this, OtherActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
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

    @Override
    public void inProgress(long bytesRead, long contentLength) {
        int prograss = (int) (ArithUtil.div(bytesRead, contentLength, 2) * 100);
        updateDialog.setPrograss(prograss);
    }
}
