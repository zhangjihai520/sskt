package com.ry.sskt.service;


/**
 * 错误码接口
 */
public interface IRespCode {
    /**
     * 获取错误编码
     *
     * @return 结果 错误编码
     */
    int getResultType();

    /**
     * 获取异常名称
     *
     * @return 结果 异常名称
     */
    String name();

    /**
     * 获取异常消息
     *
     * @return 结果 异常消息
     */
    String getMessage();


}
