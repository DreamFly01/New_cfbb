package com.cfbb.android.protocol;

/**
 * Created by MrChang45 on 2016/3/21.
 */
public class APIException extends RuntimeException {

    public int code;
    public String msg;


    public APIException(String erroMsg) {
        super(erroMsg);
    }

    public APIException(String erroMsg,int code) {
        super(erroMsg);
        this.code =code;
        this.msg = erroMsg;
    }
}
