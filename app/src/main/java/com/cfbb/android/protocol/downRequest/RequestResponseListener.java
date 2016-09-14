package com.cfbb.android.protocol.downRequest;

/**
 * @author MrChang
 *         created  at  2016/3/1.
 * @description 对外暴露接口
 */
public interface RequestResponseListener {
    void inProgress(long bytesRead, long contentLength, boolean done);
    void onStart();
    void onRequestFailed(Exception e);
}
