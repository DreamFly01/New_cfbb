package com.cfbb.android.protocol.bean;

import java.util.List;

/**
 * @author MrChang45
 * @time 2016/11/11
 * @desc
 */
public class LoanPersonInfo {
    public List<PersonInfo> loanInfos;
    public class PersonInfo{
        public String loanerInfo;
        public String info;
    }
}
