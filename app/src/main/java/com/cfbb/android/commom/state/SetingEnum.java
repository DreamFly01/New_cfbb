package com.cfbb.android.commom.state;

/**
 * Created by MrChang45 on 2016/3/29.
 * 设置状态
 */
public enum SetingEnum {
    SETED_STATE(1, "已设置"),
    UNSETED_STATE(-1, "未设置");

    private int value;
    private String name;

    SetingEnum(int value, String name) {
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
