package com.cfbb.android.commom.state;

/**
 * Created by MrChang45 on 2016/4/12.
 * <p/>
 * 借款发布状态
 */
public enum LoanReleasedStateEnum {

    RELEASED(1, "已发布"),
    UNRELEASED(-1, "未发布");

    private int value;
    private String name;

    LoanReleasedStateEnum(int value, String name) {
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
