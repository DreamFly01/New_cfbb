package com.cfbb.android.protocol.bean;


import java.util.List;

/**
 * Created by MrChang45 on 2016/3/18.
 */
public class HomeInfoBean  {


    public List<AdsBean> ads;
    public List<NoticeBean> notice;
    public HotrecommendBean hotrecommend;
    public List<OtherproductsBean> otherproducts;
    public otherHomeInfo otherHomeInfo;

    public class otherHomeInfo
    {
        public String helperUrl;
        public String messageUrl;
        public String unReadMsgNum;
    }

    public class AdsBean {
        public String textIndex;
        public String imageUrl;
        public String clickurl;
    }

    public class NoticeBean {
        public String content;
        public String clickurl;
    }

    public class HotrecommendBean {
        public String prodcutId;
        public String productName;
        public String loanTypeId;
        public String yearOfRate;
        public String investTatoalMoeny;
        public String startMoeny;
        public String investDate;
        public int unit;
        public int repaymentType;
        public String btntxt;
        public int canInvest;
        public String flagIconUrl;
    }

    public class OtherproductsBean {
        public String prodcutId;
        public String productName;
        public String loanTypeId;
        public String yearOfRate;
        public String investDate;
        public int unit;
        public String prograss;
        public String btntxt;
        public int canInvest;
        public String flagIconUrl;
    }

}
