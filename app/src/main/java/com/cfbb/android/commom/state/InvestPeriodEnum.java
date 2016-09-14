package com.cfbb.android.commom.state;

/**
 * Created by MrChang45 on 2016/3/22.
 * 投资期限单位
 */
public enum InvestPeriodEnum {
    DAY(1, "天"),
    YUE(3, "个月");

    private int value;
    private String name;

    InvestPeriodEnum(int value, String name) {
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
