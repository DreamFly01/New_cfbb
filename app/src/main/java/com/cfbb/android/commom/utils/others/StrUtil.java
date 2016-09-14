package com.cfbb.android.commom.utils.others;

import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yu on 14-1-8.
 */
public class StrUtil {

    public static boolean isEmpty(String str) {
        return (str == null) || str.trim().equalsIgnoreCase("null")
                || (str.trim().length() < 1) || str.equals("");
    }

    /**
     * 验证手机号码的合法性 支持+86,0+，170 14
     *
     * @param mobile
     * @return
     */
    public static boolean checkMobile(String mobile) {

        if (StrUtil.isEmpty(mobile)) {
            return false;
        }
        if (!StrUtil.isNumeric(mobile)) {
            return false;
        }
        if (mobile.length() < 9 && mobile.length() > 13) {
            return false;
        }
        return true;
    }

    /***
     * 判断是不是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /***
     * 将传入的金额转化为以逗号 3个为分割 保留两位小数
     *
     * @param str
     * @return
     */
    public static String moenyToDH(String str) {

        Double dou = Double.parseDouble(str);
        return new DecimalFormat("###,##0.00").format(dou);

    }

    /***
     * @param remiansDay 秒数
     * @return 0天0小时0分0秒
     */
    public static String getTimeDiff(Long remiansDay) {
        try {
            Long day = remiansDay / 86400000;
            Long hour = (remiansDay % 86400000) / 3600000;
            Long min = (remiansDay % 86400000 % 3600000) / 60000;
            Long second = (remiansDay % 86400000 % 3600000 % 60000 / 1000);
            if (remiansDay <= 0) {
                return "已结束";
            } else {
                return day + "天" + hour + "小时" + min + "分" + second + "秒";
            }
        } catch (Exception e) {

        }
        return "---";

    }

    /***
     * 截断字符串，默认加三个点
     *
     * @param str
     * @return
     */
    public static String cuttOffStr(String str, int length, String suffixs) {
        if (str.length() > length) {
            return str.substring(0, length) + suffixs;
        } else {
            return str;
        }
    }

    /***
     * 是否是中文 不含空格
     *
     * @param str
     * @return
     */
    public static Boolean isChinese(String str) {
        String regex = "^[\\u4e00-\\u9fa5]*$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
