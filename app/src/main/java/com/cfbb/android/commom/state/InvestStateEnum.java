package com.cfbb.android.commom.state;

/**
 * Created by MrChang45 on 2016/3/23.
 *
 * 标的投资状态
 */
public enum InvestStateEnum {

    ENABLE_INVEST(1, "可投资"),
    DISABLE_INVEST(-1, "不可投资");

    private int value;
    private String name;

    InvestStateEnum(int value, String name) {
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
