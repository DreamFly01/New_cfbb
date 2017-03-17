package com.cfbb.android.protocol.bean;

import java.util.List;

/**
 * @author MrChang45
 * @time 2016/11/10
 * @desc
 */
public class MyLoanDetailsBean {
    public int loanStats;
    public String loanStatName;
    public String loanTime;
    public String loanSateText;
    public String productName;
    public String loanMoeny;
    public String loanDate;
    public String returnWay;
    public String loanDescrib;
    public String bindingDays;
    public String loanerInfo;
    public List<ImageInfo> relativeData;

    public class ImageInfo{
        public String url;
        public String name;
    }

}
