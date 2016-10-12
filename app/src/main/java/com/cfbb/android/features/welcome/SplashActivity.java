package com.cfbb.android.features.welcome;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.baidu.mobstat.StatService;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.config.Const;
import com.cfbb.android.commom.state.MainFragmentEnum;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.commom.utils.base.AppUtils;
import com.cfbb.android.commom.utils.image.ImageWithGlideUtils;
import com.cfbb.android.commom.utils.others.ArithUtil;
import com.cfbb.android.commom.utils.others.SPUtils;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.features.main.MainActivity;
import com.cfbb.android.features.update.UpdateAppService;
import com.cfbb.android.features.update.UpdateDialog;
import com.cfbb.android.features.webview.OtherActivity;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.APIService;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.BaseResultBean;
import com.cfbb.android.protocol.bean.ProductTypeBean;
import com.cfbb.android.protocol.bean.UnsupportedBankCardBean;
import com.cfbb.android.protocol.bean.UpdateVersionBean;
import com.cfbb.android.protocol.bean.WelcomeInfoBean;
import com.cfbb.android.protocol.downRequest.IProgressLisenr;
import com.cfbb.android.widget.dialog.YCDialogUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/***
 * 启动页。引导页
 */
public class SplashActivity extends BaseActivity implements View.OnClickListener, IProgressLisenr {

    private ImageView iv_bg;
    private CountDownTimer countDownTimer;
    private CountDownTimer countDownTimerTwo;
    private static final long LAEST_TIME = 7000;
    private static final long SHOW_PICLAEST_TIME = 5000;
    private ViewPager viewpager;
    private String currenVersionCode;
    private String preVersionCode;
    private YCDialogUtils ycDialogUtils;

    //引导页
    private int[] imagIds = {R.mipmap.one, R.mipmap.two,
            R.mipmap.three};

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
        setBackEnable(false);
    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash);
        currenVersionCode = AppUtils.getAppInfo(this).getVersionCode() + "";
        preVersionCode = (String) SPUtils.get(this, Const.IS_FIRST_START, "");
    }

    @Override
    public void setUpViews() {
        ycDialogUtils = new YCDialogUtils(this);
        viewpager = (ViewPager) findViewById(R.id.viewPager);
        iv_bg = (ImageView) findViewById(R.id.iv_splash);
    }

    @Override
    public void setUpLisener() {
        iv_bg.setOnClickListener(this);
    }

    @Override
    public void getDataOnCreate() {

//
//        BaseResultBean<UnsupportedBankCardBean> bankCardBeanBaseResultBean = new BaseResultBean<>();
//        bankCardBeanBaseResultBean.code = APIService.OK_CODE;
//        UnsupportedBankCardBean unsupportedBankCardBean = new UnsupportedBankCardBean();
//        unsupportedBankCardBean.content = "sssss";
//        bankCardBeanBaseResultBean.data = unsupportedBankCardBean;
//

        RetrofitClient.getVersionInfoRequest(null, this, new YCNetSubscriber<UpdateVersionBean>(this) {

            @Override
            public void onYCError(APIException e) {
                ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), "无法检测版本信息，请检查网络环境后重新打开应用！", new YCDialogUtils.MySingleBtnclickLisener() {
                    @Override
                    public void onBtnClick(View v) {
                        ycDialogUtils.DismissMyDialog();
                        finish();
                        //android.os.Process.killProcess(android.os.Process.myPid());
                    }
                }, false);
            }

            @Override
            public void onYcNext(UpdateVersionBean updateVersionBean) {


                SplashActivity.this.updateVersionBean = updateVersionBean;
                int currentVersionCode = AppUtils.getAppInfo(getApplicationContext()).getVersionCode();
                if (currentVersionCode < updateVersionBean.version_code) {
                    //需要更新版本
                    final String downUrl = updateVersionBean.url;
                    updateDialog = new UpdateDialog(SplashActivity.this);
                    updateDialog.showDialog(updateVersionBean.version_desc, new UpdateDialog.MyBtnclickLisener() {
                        @Override
                        public void onOkClick(View v) {
                            updateDialog.setBtnEnable(false);
                            updateDialog.showPrograss();
                            intent = new Intent(SplashActivity.this, UpdateAppService.class);
                            bindService(intent, new ServiceConnection() {
                                @Override
                                public void onServiceConnected(ComponentName name, IBinder service) {
                                    myBuinder = (UpdateAppService.MyBuinder) service;
                                    myBuinder.setProgressLisener(SplashActivity.this);
                                    myBuinder.startDown(downUrl);
                                }

                                @Override
                                public void onServiceDisconnected(ComponentName name) {

                                }
                            }, BIND_AUTO_CREATE);
                        }

                        @Override
                        public void onCancel(View v) {
                            //广告执行完毕 点击关闭按钮直接跳转首页
                            isVersionOk = true;
                            if (isADShowOk) {
                                TurnToMainActivity();
                            }
                        }
                    }, false, updateVersionBean.force_update == 1 ? true : false);

                } else {
                    //不需要更新
                    isVersionOk = true;
                    if (isADShowOk) {
                        TurnToMainActivity();
                    }
                }
            }
        });

        //只要preVersionCode 和当前code不一致 就显示引导页,一致就显示启动页
        if (currenVersionCode.equals(preVersionCode)) {

            //显示启动页
            viewpager.setVisibility(View.GONE);
            countDownTimer = new CountDownTimer(LAEST_TIME, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    isADShowOk = true;
                    if (isVersionOk) {
                        if (null != countDownTimerTwo) {
                            countDownTimerTwo.cancel();
                        }
                        TurnToMainActivity();
                    }
                }
            };
            countDownTimer.start();

            //TestResultUtils.getSussefulResult30()
            RetrofitClient.GetSplashInfo(null, this, new YCNetSubscriber<WelcomeInfoBean>(this) {

                @Override
                public void onYcNext(WelcomeInfoBean model) {
                    mWelcomeInfo = model;
                    if (!StrUtil.isEmpty(mWelcomeInfo.img)) {
                        btn_jump = (Button) findViewById(R.id.btn_jump);
                        ImageWithGlideUtils.lodeFromUrl(mWelcomeInfo.img, iv_bg, SplashActivity.this, new RequestListener() {

                            @Override
                            public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
                                LoadOk();
                                return false;
                            }

                        });
                    } else {
                        iv_bg.setImageResource((R.mipmap.start_bg));
                    }
                }

                @Override
                public void onYCError(APIException e) {
                    iv_bg.setImageResource((R.mipmap.start_bg));
                }

            });


        } else {

            SPUtils.put(this, Const.IS_FIRST_START, currenVersionCode + "");
            //显示引导页
            List<ImageView> datas = new ArrayList<>();
            for (int i = 0; i < imagIds.length; i++) {
                ImageView iv = new ImageView(this);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                iv.setImageResource(imagIds[i]);
                datas.add(iv);
            }
            MyViewPagerAdapor myViewPagerAdapor = new MyViewPagerAdapor(datas);
            viewpager.setAdapter(myViewPagerAdapor);
            viewpager.setVisibility(View.VISIBLE);
        }


        //TestResultUtils.getSussefulResult26()
        RetrofitClient.getProductTypesRequest(null, this, new YCNetSubscriber<List<ProductTypeBean>>(this) {

            @Override
            public void onYCError(APIException e) {
                //    super.onYCError(e);
                SPUtils.put(SplashActivity.this, Const.PRODUCT_STR, "");
            }

            @Override
            public void onYcNext(List<ProductTypeBean> model) {
                Gson gson = new Gson();
                SPUtils.put(SplashActivity.this, Const.PRODUCT_STR, gson.toJson(model));
            }

        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }


    private WelcomeInfoBean mWelcomeInfo;
    private Button btn_jump;

    private boolean isVersionOk = false;
    private boolean isADShowOk = false;

    private UpdateVersionBean updateVersionBean;
    private UpdateAppService.MyBuinder myBuinder;
    private Intent intent;

    private UpdateDialog updateDialog;


    private void TurnToMainActivity() {
        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.SHOW_FRAGMENT_INDEX, MainFragmentEnum.HOME.getValue());
        JumpCenter.JumpActivity(SplashActivity.this, MainActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, false);

    }

    private void LoadOk() {

        countDownTimer.cancel();

        btn_jump.setOnClickListener(SplashActivity.this);
        btn_jump.setText(SHOW_PICLAEST_TIME / 1000 + "  " + getResources().getString(R.string.jump));
        btn_jump.setVisibility(View.VISIBLE);
        countDownTimerTwo = new CountDownTimer(SHOW_PICLAEST_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (null != btn_jump) {
                    btn_jump.setText(millisUntilFinished / 1000 + "  " + getResources().getString(R.string.jump));
                }
            }

            @Override
            public void onFinish() {
                btn_jump.setText(0 + "  " + getResources().getString(R.string.jump));
                isADShowOk = true;
                if (isVersionOk) {
                    if (null != countDownTimer) {
                        countDownTimer.cancel();
                    }
                    TurnToMainActivity();
                }
            }
        };
        countDownTimerTwo.start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_splash:
                if (updateVersionBean != null && updateVersionBean.force_update != 1) {
                    if (mWelcomeInfo != null) {
                        if (!StrUtil.isEmpty(mWelcomeInfo.clickurl)) {
                            if (null != countDownTimerTwo) {
                                countDownTimerTwo.cancel();
                            }
                            if (null != countDownTimer) {
                                countDownTimer.cancel();
                            }

                            //不为空跳转网页
                            Bundle bundle = new Bundle();
                            bundle.putString(OtherActivity.BACK_STR, getResources().getString(R.string.nav_home));
                            bundle.putString(OtherActivity.URL, mWelcomeInfo.clickurl);
                            bundle.putSerializable(OtherActivity.TURN_TO_ACTIVITY_CLASS, MainActivity.class);
                            JumpCenter.JumpActivity(this, OtherActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, false);

                        }
                    }
                }
                break;
            case R.id.btn_jump:
            case R.id.btn_ok:
                if (updateVersionBean != null && updateVersionBean.force_update != 1) {
                    if (null != countDownTimerTwo) {
                        countDownTimerTwo.cancel();
                    }
                    if (null != countDownTimer) {
                        countDownTimer.cancel();
                    }
                    TurnToMainActivity();
                }
                break;

        }
    }

    @Override
    public void inProgress(long bytesRead, long contentLength) {
        int prograss = (int) (ArithUtil.div(bytesRead, contentLength, 2) * 100);
        updateDialog.setPrograss(prograss);
        if (prograss == 100) {
            if (updateVersionBean.force_update == 0) {
                TurnToMainActivity();
            }
        }

    }

    // 适配器
    private class MyViewPagerAdapor extends PagerAdapter {

        private List<ImageView> mList = new ArrayList<>();

        public MyViewPagerAdapor(List<ImageView> data) {
            mList = data;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        /**
         * 获得相应位置上的view container view的容器，其实就是viewpager自身 position 相应的位置
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mList.get(position);
            // 到最后一个了。
            if ((position == getCount() - 1) && getCount() > 1) {
                view.setId(R.id.btn_ok);
                view.setOnClickListener(SplashActivity.this);
            }

            // 给 container 添加一个view
            container.addView(mList.get(position));

            // 返回一个和该view相对的object
            return mList.get(position);
        }

        /**
         * 判断 view和object的对应关系
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;// 官方提示这样写
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mList.get(position));
        }
    }

}
