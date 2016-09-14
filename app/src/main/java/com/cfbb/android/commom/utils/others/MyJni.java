package com.cfbb.android.commom.utils.others;

/**
 * @author MrChang
 *         created  at  2015/12/21.
 * @description jni调用
 */
public class MyJni {

    static {
        System.loadLibrary("cfbb");
    }

    private static MyJni instance = null;

    public static MyJni getInstance() {

        if (instance == null)
            instance = new MyJni();
        return instance;
    }


    /***
     * 获取到加密Key
     * @return
     */
    public native String  getSecretStr();

    /***
     * 获取到主机地址
     * @return
     */
    public native String  getHostAdd();

    /***
     * 获取3des加密密钥
     * @return
     */
    public native String getSignStrForSecure();

    /***
     * 加签算法
     * @param waitSignStr
     * @return
     */
    public native String  createSign(String waitSignStr);

}
