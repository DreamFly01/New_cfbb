package com.cfbb.android.commom.utils.secure;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

/**
 * @author MrChang
 *         created  at  2016/3/1.
 * @description
 */
public class CheckSign {

    private static int getSignature(PackageManager pm, String packageName) {
        PackageInfo pi = null;
        int sig = 0;
        Signature[] s = null;
        try {
            pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            s = pi.signatures;
            sig = s[0].hashCode();//s[0]是签名证书的公钥，此处获取hashcode方便对比
        } catch (Exception e) {
            //     Utils.killSelf();//出现异常则强制程序退出
        }
        return sig;
    }

    public static void checkSign(int rightFeatureCode, PackageManager pm) {
        int featureCode = getSignature(pm, "com.cfbb.android");
        if (rightFeatureCode != featureCode) {
           //    Utils.killSelf();//不一致则强制程序退出
        }
    }

}
