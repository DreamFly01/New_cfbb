package com.cfbb.android.protocol.bean;

/**
 * @author MrChang
 *         created  at  2015/12/9.
 * @description 用户对象
 */
public class UserBean {

    //@SerializedName是指定Json格式中的Key名
    //可以不写，则默认采用与变量名一样的Key名
    public String uid;
    public String mobile;
    public int isAuth;
    public String userName;
    public String img;
}
