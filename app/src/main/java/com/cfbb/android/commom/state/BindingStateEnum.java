package com.cfbb.android.commom.state;

/**
 * Created by MrChang45 on 2016/3/29.
 * 手机号绑定状态
 */
public enum BindingStateEnum {
    BINDED_MOBILE(1, "已绑定"),
    UNBINDED_MOBILE(-1, "未绑定");

    private int value;
    private String name;

    BindingStateEnum(int value, String name) {
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
