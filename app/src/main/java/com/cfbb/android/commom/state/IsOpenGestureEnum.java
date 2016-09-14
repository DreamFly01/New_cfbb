package com.cfbb.android.commom.state;

/**
 * Created by MrChang45 on 2016/4/14.
 * 是否打开手势密码
 */
public enum IsOpenGestureEnum {
    CLOSE(-1, "关闭"),
    OPEN(1, "打开"),
    UN_KONW_STATE(99, "未知状态");
    private int value;
    private String name;

    IsOpenGestureEnum(int value, String name) {
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
