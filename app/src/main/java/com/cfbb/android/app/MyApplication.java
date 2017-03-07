package com.cfbb.android.app;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

import com.baidu.mobstat.StatService;
import com.cfbb.android.BuildConfig;
import com.cfbb.android.features.gesture.YCSystemReceiver;

import cn.sharesdk.framework.ShareSDK;

import static com.cfbb.android.commom.utils.secure.CheckDebugableFlag.checkeDebug;

/**
 * @author MrChang
 *         created  at  2015/12/31.
 * @description
 */
public final class MyApplication extends Application {

    private static MyApplication myApplication;
    //是否可以弹出手势密码
    private boolean mGestureCanShow = true;

    public static MyApplication getInstance() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        doForUser();
        safeCheck();
        ShareSDK.initSDK(this);
        // LeakCanary.install(this);
    }

    private void doForUser() {

        if (BuildConfig.DEBUG) {
            StatService.setDebugOn(true);
        }
        final IntentFilter homeFilter = new IntentFilter(
                Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(new YCSystemReceiver(), homeFilter);

    }

    private void safeCheck() {
        checkeDebug(getApplicationContext());
    }

    public boolean isGestureCanShow() {
        return mGestureCanShow;
    }

    public void setGestureCanShow(boolean mGestureCanShow) {
        this.mGestureCanShow = mGestureCanShow;
    }


}
