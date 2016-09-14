package com.cfbb.android.protocol.downRequest;

/**
 * @author MrChang
 *         created  at  2015/12/29.
 * @description 监听进度service接口
 */
public interface IProgressLisenr {
    /***
     * @param bytesRead     已读取字节数
     * @param contentLength 总的字节数
     */
    void inProgress(long bytesRead, long contentLength);
}
