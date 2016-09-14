package com.cfbb.android.features.gesture;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cfbb.android.app.MyApplication;
import com.cfbb.android.commom.config.Const;
import com.cfbb.android.commom.utils.others.SPUtils;
import com.cfbb.android.db.user.UserBiz;

/***
 * 监听锁屏以及Home键状态，更改手势密码状态
 *
 * @author MrChang
 */
public class YCSystemReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = "HomeReceiver";
    private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
    private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    private static final String SYSTEM_DIALOG_REASON_LOCK = "lock";
    private static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (UserBiz.getInstance(context).CheckLoginState()) {

            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
                    // 短按Home键
                    MyApplication.getInstance().setGestureCanShow(true);
                    SPUtils.put(context, Const.GESTURE_SHOW_TIME, System.currentTimeMillis());
                } else if (SYSTEM_DIALOG_REASON_RECENT_APPS.equals(reason)) {
                    // 长按Home键 或者 activity切换键
                    MyApplication.getInstance().setGestureCanShow(true);
                    SPUtils.put(context, Const.GESTURE_SHOW_TIME, System.currentTimeMillis());
                } else if (SYSTEM_DIALOG_REASON_LOCK.equals(reason)) {
                    // 锁屏
                    MyApplication.getInstance().setGestureCanShow(true);
                    SPUtils.put(context, Const.GESTURE_SHOW_TIME, System.currentTimeMillis());
                } else if (SYSTEM_DIALOG_REASON_ASSIST.equals(reason)) {
                    // samsung 长按Home键
                    MyApplication.getInstance().setGestureCanShow(true);
                    SPUtils.put(context, Const.GESTURE_SHOW_TIME, System.currentTimeMillis());
                }
            }
            else
            {
                SPUtils.put(context, Const.GESTURE_SHOW_TIME,9999999999999L);
            }

        }
    }

}
