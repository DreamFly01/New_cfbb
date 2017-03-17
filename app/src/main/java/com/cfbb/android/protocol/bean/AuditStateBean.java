package com.cfbb.android.protocol.bean;

import java.util.List;

/**
 * @author MrChang45
 * @time 2016/11/10
 * @desc
 */
public class AuditStateBean {

    public String failureDescribe;
    public String loanSateText;
    public String fundraisFailReason;
    public AuditInfo products;

    public class AuditInfo {
        public String productName;
        public String yearOfRate;
        public String investDate;
        public String remiansMoeny;
        public String loanTypeId;
        public String prodcutId;
        public int unit;
        public String btntxt;
        public int canInvest;
    }
}
