package com.cfbb.android.commom.state;

/**
 * @author MrChang45
 * @time 2016/4/26
 * @desc 发送短信验证type值
 */
public enum VertifyCodeEnum {
    REGISTER_CODE("1", "注册"),
    WITHDRAW_CODE("2", "提现"),
    MOFIDY_PASSWORD_CODE("20", "修改密码"),
    MODIFY_PHONE_CODE("21", "修改手机号"),
    MODIFY_EMAIL_CODE("22", "修改邮箱"),
    FIND_PASSWORD_CODE("23", "找回密码"),
    SET_NEW_PHONE_CODE("24", "设置新手机"),
    INVEST_BID_CODE("26", "会员投资"),
    REQUEST_SMS_CODE("28","请求手机验证码");

    private String value;
    private String name;

    VertifyCodeEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }


    public String getName() {
        return name;
    }
}
