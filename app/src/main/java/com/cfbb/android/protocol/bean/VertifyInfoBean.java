package com.cfbb.android.protocol.bean;

import java.util.List;

/**
 * Created by MrChang45 on 2016/3/25.
 * 产品详情 审核信息
 */
public class VertifyInfoBean {


    public List<VertifyGroupInfo> vertifyResultlGroup;
    public List<VertifyImageGroupInfo> relativeData;

    public class VertifyGroupInfo {
        public String vertifyName;
        public int vertifyResult;
    }

    public class VertifyImageGroupInfo {
        public String url;
        public String name;
    }
}
