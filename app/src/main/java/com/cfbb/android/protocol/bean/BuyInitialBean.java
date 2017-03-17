package com.cfbb.android.protocol.bean;

/**
 * Created by MrChang45 on 2016/4/11.
 * <p/>
 * 投资初始化model
 */
public class BuyInitialBean {

    public String accountAmount;//账户可用余额
    public String loanFailingAmount;//剩余可投金额
    public String rate;//年化利率
    public String term;//投资期限
    public int termType;//投资期限单位
    public String phoneNumLast4;//手机尾号
    public String  hint;//输入框内提示语
    public String minInvestMoney;//最小投标金额
    public String interestInfo;//加息劵是否拥有
}
