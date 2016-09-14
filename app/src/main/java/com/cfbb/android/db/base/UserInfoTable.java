package com.cfbb.android.db.base;

/**
 * @author MrChang45
 * @time 2016/4/25
 * @desc 用户信息表
 */
public  class UserInfoTable {


    public static final String TABLE_NAME = "UserInfo";

    /***
     * 真实姓名
     */
    public static String trueName = "userName";
    /**
     * 是否已经认证
     * 1 认证
     * -1 未认证
     */
    public static String isAuth = "isAuth";
    /***
     * 手机号码
     */
    public static String mobile = "mobile";
    /**
     * userId
     */
    public static String uid = "uid";
    /***
     * 头像地址
     */
    public static String img = "img";
    /***
     * 手势密码 已加密
     */
    public static String gesturePassword = "gesturePassword";
    /***
     * 是否 显示资金
     * 1 显示
     * -1 不显示
     */
    public static String openEye = "openEye";
    /***
     * 是否 打开手势密码
     * 1 打开
     * -1 不打开
     */
    public static String openGesture = "openGesture";

}
