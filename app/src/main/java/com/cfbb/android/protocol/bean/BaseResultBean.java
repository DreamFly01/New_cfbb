package com.cfbb.android.protocol.bean;

/**
 * @author MrChang
 *         created  at  2015/12/9.
 * @description 返回json对象基类
 */
public class BaseResultBean<T> {

    public BaseResultBean()
    {

    }

    public BaseResultBean(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    /**
     * 返回结果 1为成功 其余为失败
     * true 返回成功
     * false 返回失败
     */
    public int code;

    /**
     * 服务器返回信息
     */
    public String msg;

    public T data;


}
