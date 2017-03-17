package com.cfbb.android.protocol.bean;

import java.util.List;

/**
 * @author MrChang45
 * @time 2017/3/10
 * @desc
 */
public class RatesBean {
    public List<Item1> item1;//加息券集合
    public String item2;//加息劵使用说明

    public class Item1 {
        public String interest_id;//加息券编号
        public String interest_name;//加息劵名称
        public String interest_rate;//加息劵利率
        public String interest_beginTime;//加息劵使用期限(开始时间)
        public String interest_endTime;//加息劵使用期限(结束时间)
    }

}
