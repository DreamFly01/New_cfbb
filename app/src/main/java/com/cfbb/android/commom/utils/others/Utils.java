package com.cfbb.android.commom.utils.others;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cfbb.android.BuildConfig;
import com.cfbb.android.R;
import com.cfbb.android.commom.config.Const;
import com.cfbb.android.commom.state.InvestPeriodEnum;
import com.cfbb.android.commom.state.RepaymentTypeEnum;
import com.cfbb.android.commom.utils.base.AppUtils;

import java.io.InputStream;
import java.lang.reflect.Field;


/**
 * @author MrChang
 *         created  at  2015/12/28.
 * @description
 */
public class Utils {


    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        return "V" + AppUtils.getAppInfo(context).getVersionName();
    }




    /**
     * 从Resources中获取图片资源，转化为Bitmap格式返回
     *
     * @param c
     * @param res
     * @return
     */
    public static Bitmap decodeCustomRes(Context c, int res) {
        InputStream is = c.getResources().openRawResource(res);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 1;//原尺寸加载图片
        Bitmap bmp = BitmapFactory.decodeStream(is, null, options);
        return bmp;
    }


    /***
     * @param iv_flag
     * @param repanymentWay
     */
    public static void FillterReturnWay(ImageView iv_flag, int repanymentWay) {

        if (repanymentWay == RepaymentTypeEnum.DISPOSABLE.getValue()) {
            iv_flag.setImageResource(R.mipmap.return_type_four);
        }
        if (repanymentWay == RepaymentTypeEnum.SERVICE_MATURITY_PRINCIPAL.getValue()) {
            iv_flag.setImageResource(R.mipmap.return_type_two);
        }
        if (repanymentWay == RepaymentTypeEnum.EQUAL_INSTALLMENT_PAYMENT.getValue()) {
            iv_flag.setImageResource(R.mipmap.return_type_three);
        }
        if (repanymentWay == RepaymentTypeEnum.MONTHLY_PAYMENT_DUE.getValue()) {
            iv_flag.setImageResource(R.mipmap.return_type_one);
        }
        if (repanymentWay == RepaymentTypeEnum.INTEREST_PAYMENT_BY_QUARTER.getValue()) {
            iv_flag.setImageResource(R.mipmap.return_type_five);
        }
        if (repanymentWay == RepaymentTypeEnum.INTEREST_PER_ANNUM.getValue()) {
            iv_flag.setImageResource(R.mipmap.return_type_six);
        }
    }

    /***
     * 根据flag 返回还款方式名称
     *
     * @param repanymentWay
     * @return
     */
    public static String getReturnWayNameByType(int repanymentWay) {
        String result = "";
        if (repanymentWay == RepaymentTypeEnum.DISPOSABLE.getValue()) {
            result = RepaymentTypeEnum.DISPOSABLE.getName();
        }
        if (repanymentWay == RepaymentTypeEnum.SERVICE_MATURITY_PRINCIPAL.getValue()) {
            result = RepaymentTypeEnum.SERVICE_MATURITY_PRINCIPAL.getName();
        }
        if (repanymentWay == RepaymentTypeEnum.EQUAL_INSTALLMENT_PAYMENT.getValue()) {
            result = RepaymentTypeEnum.EQUAL_INSTALLMENT_PAYMENT.getName();
        }
        if (repanymentWay == RepaymentTypeEnum.MONTHLY_PAYMENT_DUE.getValue()) {
            result = RepaymentTypeEnum.MONTHLY_PAYMENT_DUE.getName();
        }
        if (repanymentWay == RepaymentTypeEnum.INTEREST_PAYMENT_BY_QUARTER.getValue()) {
            result = RepaymentTypeEnum.INTEREST_PAYMENT_BY_QUARTER.getName();
        }
        if (repanymentWay == RepaymentTypeEnum.INTEREST_PER_ANNUM.getValue()) {
            result = RepaymentTypeEnum.INTEREST_PER_ANNUM.getName();
        }
        return result;
    }

    /***
     * 根据还款方式名称 返回flag
     *
     * @param repanymentWayName
     * @return
     */
    public static int getReturnWayTypeByName(String repanymentWayName) {
        int result = -99;
        if (repanymentWayName.equals(RepaymentTypeEnum.DISPOSABLE.getName())) {
            result = RepaymentTypeEnum.DISPOSABLE.getValue();
        }
        if (repanymentWayName.equals(RepaymentTypeEnum.SERVICE_MATURITY_PRINCIPAL.getName())) {
            result = RepaymentTypeEnum.SERVICE_MATURITY_PRINCIPAL.getValue();
        }
        if (repanymentWayName.equals(RepaymentTypeEnum.EQUAL_INSTALLMENT_PAYMENT.getName())) {
            result = RepaymentTypeEnum.EQUAL_INSTALLMENT_PAYMENT.getValue();
        }
        if (repanymentWayName.equals(RepaymentTypeEnum.MONTHLY_PAYMENT_DUE.getName())) {
            result = RepaymentTypeEnum.MONTHLY_PAYMENT_DUE.getValue();
        }
        if (repanymentWayName.equals(RepaymentTypeEnum.INTEREST_PAYMENT_BY_QUARTER.getName())) {
            result = RepaymentTypeEnum.INTEREST_PAYMENT_BY_QUARTER.getValue();
        }
        if (repanymentWayName.equals(RepaymentTypeEnum.INTEREST_PER_ANNUM.getName())) {
            result = RepaymentTypeEnum.INTEREST_PER_ANNUM.getValue();
        }
        return result;
    }


    /***
     * 根据flag 返回投资期限单位 个月/天
     *
     * @param flag
     * @return
     */
    public static String getInvestUnitNameByFlag(int flag) {
        String result = "";
        if (flag == InvestPeriodEnum.DAY.getValue()) {
            result = InvestPeriodEnum.DAY.getName();
        }
        if (flag == InvestPeriodEnum.YUE.getValue()) {
            result = InvestPeriodEnum.YUE.getName();
        }
        return result;
    }

    /***
     * 根据金额，填充正确单位 和金额
     * 目前规则
     * 一万为分割，低于一万显示元，高于一万显示万
     *
     * @param tv_money
     * @param tv_unit
     * @param moeny
     */
    public static void FillMoenyAndUnit(TextView tv_money, TextView tv_unit, String moeny) {
        try {
            double remainsMoney = Double.parseDouble(moeny);
            if (remainsMoney < 10000) {
                tv_money.setText(remainsMoney + "");
                tv_unit.setText(Const.YUAN_STR);
            } else {
                remainsMoney = remainsMoney / 10000;
                tv_money.setText(StrUtil.moenyToDH(remainsMoney + ""));
                tv_unit.setText(Const.WAN_STR);
            }
        } catch (Exception e) {
            //转换出错
            tv_money.setText("--");
            tv_unit.setText("-");
        }

    }


    public static void SetFullScreen(Activity mActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            mActivity.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            mActivity.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }



}
