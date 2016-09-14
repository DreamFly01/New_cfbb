package com.cfbb.android.commom.state;

/**
 * Created by MrChang45 on 2016/3/22.
 * <p/>
 * 还款方式
 * DISPOSABLE,一次性
 * SERVICE_MATURITY_PRINCIPAL,先付息到期还本
 * EQUAL_INSTALLMENT_PAYMENT,按月等额本息
 * MONTHLY_PAYMENT_DUE,按月付息到期还本
 * INTEREST_PAYMENT_BY_QUARTER, 按季付息
 * INTEREST_PER_ANNUM,  每半年付息
 */
public enum RepaymentTypeEnum {

    DISPOSABLE(4,"一次性"),
    SERVICE_MATURITY_PRINCIPAL(3,"先付息到期还本"),
    EQUAL_INSTALLMENT_PAYMENT(5,"按月等额本息"),
    MONTHLY_PAYMENT_DUE(11,"按月付息到期还本"),
    INTEREST_PAYMENT_BY_QUARTER(12,"按季付息"),
    INTEREST_PER_ANNUM(13,"每半年付息");

    private int value;
    private String name;

    RepaymentTypeEnum(int value, String name) {
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
