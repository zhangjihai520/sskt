package com.ry.sskt.model.common.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: 幸仁强
 * @Date: 2020/4/21 15:34
 * @Description: 发生文件类型
 */
public enum SendFileResultEnum {

    /**
     * 成功
     */
    Success(1),

    /**
     * 失败
     */
    Fail(2),

    /**
     * 讨论区关闭
     */
    Closed(3),

    /**
     * 文件过大
     */
    OverSize(4);

    @Getter
    @Setter
    private int Code;

    // 构造方法
    SendFileResultEnum(int Code) {
        this.Code = Code;
    }

}
