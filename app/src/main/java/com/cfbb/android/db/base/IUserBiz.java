package com.cfbb.android.db.base;

import com.cfbb.android.protocol.bean.UserBean;

/**
 * @author MrChang45
 * @time 2016/7/27
 * @desc 用户表操作接口
 */
public interface IUserBiz {

    void StoreUserInfo(UserBean bean,String gesturePsw,int gestureState);

    String GetUserId();

    String GetUserName();

    String GetUserMobile();

    String GetUserImgAddr();

    void UpdateUserImgAddr(String imgAddr);

    String GetUserGesturePassword();

    void UpdateUserGesturePassword(String gesturePassword);

    void UpdateUserMoenyShowState(int state);

    int Is_Show_Money();

    String Is_Setted_Gesture();

    int Is_Open_Gesture();

    void UpdateUserGestureState(int state);

    void CleanUserTable();



}
