package com.cfbb.android.commom.state;

/**
 * Created by MrChang45 on 2016/4/7.
 * 自动投标 投资
 */
public enum AutoInvestTypeEnum {
    AVAILABLE_BALANCE(0, "按账户可用余额"),
    FIXED_AMOUNT(1, "按固定金额"),
    PROPORTION_OF_TOTAL_BORROWINGS(2, "按借款标总额比例"),
    ACCOUNT_BALANCE_RATIO(3, "按账户保留余额");
    private int value;
    private String name;

    AutoInvestTypeEnum(int value, String name) {
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
