package com.cfbb.android.commom.utils.activityJump;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.cfbb.android.db.user.UserBiz;
import com.cfbb.android.features.authentication.LoginActivity;

/**
 * @author MrChang45
 * @time 2016/6/29
 * @desc 跳转控制中心
 */
public class JumpCenter {

    /***
     * 参数跳转
     */
    public static final String TO_ACTIVITY ="to_activity";

    /**
     * 普通开启activity,不接收返回值
     */
    public static final int NORMALL_REQUEST = -100;
    /**
     * 无效的Intent标志位
     */
    public static final int INVAILD_FLAG = -999;

    /***
     * 未登录响应监听
     */
    public interface CheckAuthorityLisener {
        void onFaildCheckAuthority();

    }

    /***
     * 跳转activity，可以指定失败后的方法，不指定默认跳转登录界面
     *
     * @param fromActivity          当前activity
     * @param toActivity            跳转到的activity
     * @param params                携带参数
     * @param checkAuthorityLisener 校验失败执行监听
     * @param reuqestCode           请求码不是NORMALL_REQUEST，就会执行startActivityForResult
     * @param flag_intent           Intent的Flags不是 INVAILD_FLAG，就设置ActivityFlag
     * @param isFinishActivity      开启activity后是否关闭当前activity
     * @param isCheckAuthority      是否校验登录状态
     */
    public static void JumpActivity(Activity fromActivity, Class toActivity, Bundle params, CheckAuthorityLisener checkAuthorityLisener, int reuqestCode, int flag_intent, Boolean isFinishActivity, Boolean isCheckAuthority) {

        if (CheckAuthority(fromActivity, isCheckAuthority)) {
            JumpKernel(fromActivity, toActivity, params, reuqestCode, flag_intent, isFinishActivity);
        } else {
            if (checkAuthorityLisener != null) {
                checkAuthorityLisener.onFaildCheckAuthority();
            } else {
                Intent myIntent = new Intent(fromActivity, LoginActivity.class);
                myIntent.putExtras(params);
                fromActivity.startActivity(myIntent);
            }
        }

    }


    /***
     * 校验是否登录
     *
     * @param context
     * @return
     */
    private static Boolean CheckAuthority(Activity context, Boolean isCheckAuthority) {
        return isCheckAuthority ? UserBiz.getInstance(context).CheckLoginState() : !isCheckAuthority;
    }

    /***
     * 执行跳转
     *
     * @param fromActivity
     * @param toActivity
     * @param params
     * @param requestCode
     * @param activity_flag
     * @param isFinishActivity
     */
    private static void JumpKernel(Activity fromActivity, Class toActivity, Bundle params, int requestCode, int activity_flag, Boolean isFinishActivity) {
        Intent myIntent = new Intent(fromActivity, toActivity);
        if (null != params) {
            myIntent.putExtras(params);
        } else {
            myIntent.putExtras(new Bundle());
        }
        if (activity_flag != INVAILD_FLAG) {
            myIntent.setFlags(activity_flag);
        }
        if (requestCode != NORMALL_REQUEST) {
            fromActivity.startActivityForResult(myIntent, requestCode);
        } else {
            fromActivity.startActivity(myIntent);
        }
        if (isFinishActivity) {
            fromActivity.finish();
        }
    }

}
