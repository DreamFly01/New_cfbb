package com.cfbb.android.commom.state;

/**
 * @author MrChang45
 * @time 2016/6/3
 * @desc
 */
public enum AccountTurnWhereEnum {
    NO_JUMP(0, "不跳转"),
    JUMP_MY_RED_PAPAER(1, "跳转我的红包"),
    JUMP_MY_GIFT_PAPAER(2, "跳转我的礼品"),;
    private int value;
    private String name;

    AccountTurnWhereEnum(int value, String name) {
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
