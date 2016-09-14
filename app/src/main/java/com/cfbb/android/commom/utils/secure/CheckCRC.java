package com.cfbb.android.commom.utils.secure;

import android.content.Context;

import com.cfbb.android.commom.utils.base.PhoneUtils;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author MrChang
 *         created  at  2016/3/1.
 * @description
 */
public class CheckCRC {


    /***
     * 重新编译程序classes.dex文件肯定会变，因此可在程序运行时对比签名或CRC值的方法对抗重打包
     *
     * @param crc     classes.dex的crc值
     * @param context
     */
    public static void checkCRC(long crc, Context context) {

        // long crc = Long.parseLong(getString(R.string.crc));//获取字符资源中预埋的crc值
        ZipFile zf;
        try {
            String path = context.getApplicationContext().getPackageCodePath();//获取apk安装路径
            zf = new ZipFile(path);//将apk封装成zip对象
            ZipEntry ze = zf.getEntry("classes.dex");//获取apk中的classes.dex
            long CurrentCRC = ze.getCrc();//计算当前应用classes.dex的crc值
            if (CurrentCRC != crc) {//crc值对比
                PhoneUtils.killSelf();
            }
        } catch (IOException e) {
            PhoneUtils.killSelf();
        }
    }
}
