package com.cfbb.android.commom.state;

/**
 * Created by MrChang on 2016/4/12.
 * <p/>
 * 显示或者隐藏 资金显示
 */
public enum IsHidenMoneyShowEnum {

    HIDDEN(-1, "隐藏"),
    SHOW(1, "显示"),
    UN_KONW_STATE(99, "未知状态");
    private int value;
    private String name;

    IsHidenMoneyShowEnum(int value, String name) {
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
