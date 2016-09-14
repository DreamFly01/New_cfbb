package com.cfbb.android.commom.state;

/**
 * Created by MrChang45 on 2016/3/23.
 *
 * 代表三个tab页
 */
public enum MainFragmentEnum {
    HOME(0, "首页"),
    INVEST(1, "投资"),
    ACCOUNT(2, "我的账户"),
    INVALID(-1, "无效");
    private int value;
    private String name;

    MainFragmentEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
