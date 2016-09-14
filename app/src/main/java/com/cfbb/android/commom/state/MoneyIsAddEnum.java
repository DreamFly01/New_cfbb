package com.cfbb.android.commom.state;

/**
 * Created by MrChang45 on 2016/3/31.
 * 资金流入流出状态
 */
public enum MoneyIsAddEnum {
    IS_ADD_MOENY(1, "流入"),
    NOT_ADD_MOENY(-1, "流出");

    private int value;
    private String name;

    MoneyIsAddEnum(int value, String name) {
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
