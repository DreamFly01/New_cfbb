package com.cfbb.android.commom.state;

/**
 * Created by MrChang45 on 2016/4/5.
 * 红包状态
 */
public enum RedPaperStateEnum {

    UNUSER_REDPAPER(0, "未使用"),
    OVERDUE_REDPAPER(1, "已过期"),
    OVERDUE_USED(2, "已使用");

    private int value;
    private String name;

    RedPaperStateEnum(int value, String name) {
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
