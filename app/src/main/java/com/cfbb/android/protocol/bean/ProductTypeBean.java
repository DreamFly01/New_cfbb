package com.cfbb.android.protocol.bean;

import java.io.Serializable;

/**
 * Created by dell on 2016/3/21.
 */
public class ProductTypeBean implements Serializable{

    public String prodcutTypeId;
    public String productTypeName;
    //1 借款标  5 债权投资
    public int loanTypeId;
}
