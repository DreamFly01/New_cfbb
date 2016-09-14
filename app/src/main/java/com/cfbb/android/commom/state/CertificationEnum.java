package com.cfbb.android.commom.state;

/**
 * Created by MrChang45 on 2016/3/29.
 * 是否实名认证认证
 */
public enum CertificationEnum {
    PASS_CERTIFICATION(1, "已认证"),
    UNPASS_CERTIFICATION(-1, "未认证");

    private int value;
    private String name;

    CertificationEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }


    public String getName() {
        return name;
    }

}
