package com.cfbb.android.protocol.bean;

import java.util.List;

/**
 * @author MrChang45
 * @time 2016/11/9
 * @desc
 */
public class MyLoanBean {
    public List<MyLoanArray> myloanarray;

    public class MyLoanArray {
        public String loanTypeId;
        public String prodcutId;
        public String loanMoeny;
        public String loanSateText;
        public String returnWay;
        public String bindingDays;
        public String loanDate;
        public String loanTitle;
    }
}

