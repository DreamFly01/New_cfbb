package com.cfbb.android.commom.state;

/**
 * Created by MrChang45 on 2016/4/7.
 * 自动投标状态
 */
public enum AutoInvestStateEnum {

    OPEN(1, "开启"),
    CLOSE(-1, "关闭");
    private int value;
    private String name;

    AutoInvestStateEnum(int value, String name) {
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
