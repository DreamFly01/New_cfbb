package com.cfbb.android.commom.state;

/**
 * Created by MrChang45 on 2016/3/22.
 * 1.借款标
 * 3.债权投资
 */
public enum ProductTypeEnum {

    NORAMLL_BID("1"),
    DEBTS("5");

    private String value;

    ProductTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
