package com.cfbb.android.db.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cfbb.android.commom.config.Const;
import com.cfbb.android.commom.state.IsHidenMoneyShowEnum;
import com.cfbb.android.commom.state.IsOpenGestureEnum;
import com.cfbb.android.commom.state.MainFragmentEnum;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.commom.utils.others.SPUtils;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.db.base.IUserBiz;
import com.cfbb.android.db.base.UserBizHelper;
import com.cfbb.android.features.main.MainActivity;
import com.cfbb.android.protocol.bean.UserBean;

/**
 * Created by MrChang45 on 2016/3/23.
 */
public class UserBiz {

    private static UserBiz instance = null;
    private IUserBiz userBizHelper;
    private Context context;

    protected UserBiz(Context context) {
        userBizHelper = new UserBizHelper(context);
        this.context = context;
    }

    public static UserBiz getInstance(Context context) {
        if (instance == null) {
            instance = new UserBiz(context);
        }
        return instance;
    }


    /***
     * 检测是否登录
     *
     * @return
     */
    public boolean CheckLoginState() {
        if (StrUtil.isEmpty(userBizHelper.GetUserId())) {
            return false;
        } else {
            return true;
        }
    }

    /***
     * 清楚登录状态
     *
     * @return
     */
    public void ClearLoginState() {
        userBizHelper.CleanUserTable();
    }

    /***
     * 检测是否设置手势密码
     *
     * @return
     */
    public boolean CheckGesturePassword() {
        if (StrUtil.isEmpty(userBizHelper.GetUserGesturePassword())) {
            return false;
        } else {
            return true;
        }
    }


    /***
     * 获得手势密码
     *
     * @return
     */
    public String GetGesturePassWord() {
        return userBizHelper.GetUserGesturePassword();
    }

    /***
     * 退出登录
     * 默认跳转 首页
     *
     * @return
     */
    public void ExitLogin() {

        ClearLoginState();
        SPUtils.put(context, Const.GESTURE_SHOW_TIME,9999999999999L);

    }

    /***
     * 获得用户名
     *
     * @return
     */
    public String GetUserName() {
        return userBizHelper.GetUserName();
    }

    /***
     * 获得用户ID
     *
     * @return
     */
    public String GetUserId() {
        return userBizHelper.GetUserId();
    }

    /***
     * 获得用户手机号码
     *
     * @return
     */
    public String GetUserMobile() {
        return userBizHelper.GetUserMobile();
    }

    /***
     * 获得用户头像地址
     *
     * @return
     */
    public String GetUserImg() {
        return userBizHelper.GetUserImgAddr();
    }

    /***
     * 更新用户手势密码
     *
     * @return
     */
    public void UpdateUserGesturePassword(String password) {
        userBizHelper.UpdateUserGesturePassword(password);
    }

    /***
     * 更新用户头像地址
     *
     * @return
     */
    public void UpdateUserPhotoAddr(String imgAddr) {
        userBizHelper.UpdateUserImgAddr(imgAddr);
    }

    /***
     * 更新用户资金显示状态
     *
     * @return
     */
    public void UpdateUserMoenyShowState(int state) {
        userBizHelper.UpdateUserMoenyShowState(state);
    }


    /***
     * 获得用户资金显示状态
     *
     * @return
     */
    public boolean Is_Show_Money() {
        return userBizHelper.Is_Show_Money() == IsHidenMoneyShowEnum.SHOW.getValue() ? true : false;
    }


    /***
     * 是否设置手势密码
     *
     * @return
     */
    public boolean Is_Setted_Gesture() {
        return !StrUtil.isEmpty(userBizHelper.Is_Setted_Gesture()) ? true : false;
    }


    /***
     * 是否打开手势密码
     *
     * @return
     */
    public boolean Is_Open_Gesture() {
        return userBizHelper.Is_Open_Gesture() == IsOpenGestureEnum.OPEN.getValue() ? true : false;
    }

    /***
     * 更新手势密码打开关闭状态
     *
     * @return
     */
    public void UpdateUserGestureState(int state) {
        userBizHelper.UpdateUserGestureState(state);
    }


    public void StoreUserInfo(UserBean bean, String gesturePsw, int gestureState) {
        userBizHelper.StoreUserInfo(bean, gesturePsw, gestureState);
    }
}
