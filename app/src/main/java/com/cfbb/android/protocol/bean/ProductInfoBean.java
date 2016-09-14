package com.cfbb.android.protocol.bean;

import java.util.List;

/**
 * Created by MrChang45 on 2016/3/21.
 * <p/>
 * 标的列表
 */
public class ProductInfoBean {

    public String showType;
    public String showProductTypeId;
    public List<ProductInfo> products;

    public class ProductInfo {
        public String prodcutId;
        public String productName;
        public String yearOfRate;
        public String investDate;
        public int unit;
        public String remiansMoeny;
        public String btntxt;
        public int canInvest;
        public String flagIcon;
        public String loanTypeId;
        public int loanStatus;
    }

}
