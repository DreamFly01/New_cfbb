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
import com.cfbb.android.db.user.UserBiz;
import com.cfbb.android.features.gesture.GestureVerifyActivity;
import com.cfbb.android.features.main.MainActivity;
import com.cfbb.android.features.update.UpdateAppService;
import com.cfbb.android.features.update.UpdateDialog;
import com.cfbb.android.features.webview.OtherActivity;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.ProductTypeBean;
import com.cfbb.android.protocol.bean.UpdateVersionBean;
import com.cfbb.android.protocol.bean.WelcomeInfoBean;
import com.cfbb.android.protocol.downRequest.IProgressLisenr;
import com.cfbb.android.widget.dialog.YCDialogUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/***
 * 启动页。引导页
 * <p/>
 * 1.进行系统版本检测
 * 2.获取广告页
 * 3.获取借款产品列表
 * <p/>
 * 要求，一定要等版本检测完成和广告加载成功 才能跳转。
 */
public class SplashActivity extends BaseActivity implements View.OnClickListener, IProgressLisenr {

    private static final long SHOW_PICLAEST_TIME = 5000;
    private static int[] imagIds = {R.mipmap.one, R.mipmap.two,
            R.mipmap.three};

    private boolean isVersionOk = false;
    private boolean isADShowOk = false;

    private ImageView iv_bg;
    private ViewPager viewpager;
    private Button btn_jump;

    private String currenVersionCode;
    private String preVersionCode;
    private YCDialogUtils ycDialogUtils;

    private CountDownTimer countDownTimer;
    private CountDownTimer countDownTimerTwo;
    private WelcomeInfoBean mWelcomeInfo;
    private UpdateVersionBean updateVersionBean;
    private UpdateAppService.MyBuinder myBuinder;
    private Intent intent;
    private UpdateDialog updateDialog;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash);
        currenVersionCode = AppUtils.getAppInfo(this).getVersionCode() + "";
        preVersionCode = (String) SPUtils.get(this, Const.IS_FIRST_START, "");
        SPUtils.put(this, Const.IS_FIRST_START, currenVersionCode + "");
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

        RetrofitClient.getVersionInfoRequest(null, this, new YCNetSubscriber<UpdateVersionBean>(this) {

            @Override
            public void onYCError(APIException e) {
                FailToCheckVersion();

//                Bundle bundle = new Bundle();
//                bundle.putInt(MainActivity.SHOW_FRAGMENT_INDEX, MainFragmentEnum.HOME.getValue());
//                JumpCenter.JumpActivity(SplashActivity.this, MainActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, false);

            }

            @Override
            public void onYcNext(UpdateVersionBean updateVersionBean) {

                CheckVersionOk(updateVersionBean);
            }

        });

        RetrofitClient.getProductTypesRequest(null, this, new YCNetSubscriber<List<ProductTypeBean>>(this) {

            @Override
            public void onYCError(APIException e) {
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
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
        setBackEnable(false);
        InitialVersionCheck();
    }

    private void InitialVersionCheck() {

        if (currenVersionCode.equals(preVersionCode)) {

            RetrofitClient.GetSplashInfo(null, this, new YCNetSubscriber<WelcomeInfoBean>(this) {

                @Override
                public void onYcNext(WelcomeInfoBean welcomeInfoBean) {
//                    isADShowOk = true;
                    GetAdOk(welcomeInfoBean);
                }

                @Override
                public void onYCError(APIException e) {
                    iv_bg.setImageResource((R.mipmap.start_bg));
                }

            });
            countDownTimer = new CountDownTimer(SHOW_PICLAEST_TIME, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    isADShowOk = true;
                    TurnToMainActivity();
                }
            };
            countDownTimer.start();
            //显示启动页
            viewpager.setVisibility(View.GONE);

        } else {


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
    }

    private void GetAdOk(WelcomeInfoBean welcomeInfoBean) {
        mWelcomeInfo = welcomeInfoBean;
        if (!StrUtil.isEmpty(mWelcomeInfo.img)) {
            btn_jump = (Button) findViewById(R.id.btn_jump);
//            mWelcomeInfo.img = "";
            if (mWelcomeInfo.img.equals("")) {
                Bundle bundle = new Bundle();
                bundle.putInt(MainActivity.SHOW_FRAGMENT_INDEX, MainFragmentEnum.HOME.getValue());
                JumpCenter.JumpActivity(SplashActivity.this, MainActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, false);
            }
            ImageWithGlideUtils.lodeFromUrl(mWelcomeInfo.img, iv_bg, SplashActivity.this, new RequestListener() {

                @Override
                public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                    TurnToMainActivity();
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

    private void CheckVersionOk(UpdateVersionBean updateVersionBean) {

        SplashActivity.this.updateVersionBean = updateVersionBean;
        int currentVersionCode = Integer.parseInt(currenVersionCode);
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
                    isVersionOk = true;
                    TurnToMainActivity();
                }

            }, false, updateVersionBean.force_update == 1 ? true : false);

        } else {
            //不需要更新
            isVersionOk = true;
            TurnToMainActivity();
        }
    }

    private void FailToCheckVersion() {

        ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), "无法检测版本信息，请检查网络环境后重新打开应用！", new YCDialogUtils.MySingleBtnclickLisener() {
            @Override
            public void onBtnClick(View v) {
                ycDialogUtils.DismissMyDialog();
                finish();
            }
        }, false);

    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    private void TurnToMainActivity() {
        if (null != countDownTimerTwo) {
            countDownTimerTwo.cancel();
        }
        if (null != countDownTimer) {
            countDownTimer.cancel();
        }
        //确保广告请求获取ok 和 版本信息获取Ok

        if (isADShowOk && isVersionOk) {
            if(UserBiz.getInstance(this).Is_Setted_Gesture()&&UserBiz.getInstance(this).Is_Open_Gesture()){
                Bundle bundle = new Bundle();
                bundle.putString("key","key");
                JumpCenter.JumpActivity(this, GestureVerifyActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, false);
            }else{

            Bundle bundle = new Bundle();
            bundle.putInt(MainActivity.SHOW_FRAGMENT_INDEX, MainFragmentEnum.HOME.getValue());
            JumpCenter.JumpActivity(SplashActivity.this, MainActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, false);
            }
        }
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

//                showShortToast("sssssss");
            case R.id.btn_ok:
                isADShowOk = true;
                //if (updateVersionBean != null && isVersionOk) {
                if (null != countDownTimerTwo) {
                    countDownTimerTwo.cancel();
                }
                if (null != countDownTimer) {
                    countDownTimer.cancel();
                }

                //   }
//                JumpCenter.JumpActivity(this, GestureVerifyActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, false);

                TurnToMainActivity();
                break;

        }
    }

    @Override
    public void inProgress(long bytesRead, long contentLength) {
        int prograss = (int) (ArithUtil.div(bytesRead, contentLength, 2) * 100);
        updateDialog.setPrograss(prograss);
        if (prograss == 100) {
            if (updateVersionBean.force_update == -1) {
                TurnToMainActivity();
            }
        }

    }

    private class MyViewPagerAdapor extends PagerAdapter {

        private List<ImageView> mList = new ArrayList<>();

        public MyViewPagerAdapor(List<ImageView> data) {
            mList = data;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mList.get(position);
            if ((position == getCount() - 1) && getCount() > 1) {
                view.setId(R.id.btn_ok);
                view.setOnClickListener(SplashActivity.this);
            }
            container.addView(mList.get(position));
            return mList.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mList.get(position));
        }
    }

}
