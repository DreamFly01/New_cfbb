package com.cfbb.android.commom.state;

/**
 * Created by MrChang45 on 2016/3/25.
 * 审核状态
 */
public enum VertifyStateEnum {
    PASS(1, "通过"),
    NO_PASS(-1, "未通过");

    private int value;
    private String name;

    VertifyStateEnum(int value, String name) {
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
