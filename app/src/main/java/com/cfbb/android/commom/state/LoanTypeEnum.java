package com.cfbb.android.commom.state;

/**
 * Created by MrChang45 on 2016/4/22.
 * 借款方式
 */
public enum LoanTypeEnum {

    LOAN_DAY(1, "按天借款"),
    LOAN_MOENTH(3, "按月借款");

    private int value;
    private String name;

    LoanTypeEnum(int value, String name) {
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
