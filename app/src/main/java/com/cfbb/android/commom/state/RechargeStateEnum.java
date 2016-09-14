package com.cfbb.android.commom.state;

/**
 * Created by MrChang45 on 2016/4/6.
 * 是否首次充值
 */
public enum RechargeStateEnum {
    FIRST_RECHARGE(0, "未绑定银行卡"),
    NOT_FRIST_RECHAGRE(1, "已绑定银行卡");
    private int value;
    private String name;

    RechargeStateEnum(int value, String name) {
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
