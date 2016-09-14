package com.cfbb.android.commom.state;

/**
 * Created by MrChang45 on 2016/3/30.
 * <p/>
 * 我的投资状态
 */
public enum MyInvestStateEnum {
    ALL_INVEST(0, "默认"),
    INVESTING(3, "投标中"),
    HOLDING(1, "持有中"),
    COMPLETE(2, "已完成");
    private int value;
    private String name;

    MyInvestStateEnum(int value, String name) {
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
