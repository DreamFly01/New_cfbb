package com.cfbb.android.commom.utils.secure;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.cfbb.android.commom.utils.base.PhoneUtils;


/**
 * @author MrChang
 *         created  at  2016/3/1.
 * @description
 */
public class CheckDebugableFlag {

    /***
     * 使用此方法时必须预先在AndroidManifest.xml设置android:debuggable=”false”，
     * 攻击者要尝试调试应用时很有可能去修改该参数，因而此手法可用于做动态反调试检测。
     *
     * @param context
     */
    public static void checkeDebug(Context context) {
        if ((context.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            PhoneUtils.killSelf();
        }
    }

}
