package com.ry.sskt.model.common.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: 幸仁强
 * @Date: 2020/4/21 15:34
 * @Description: 发生文件类型
 */
public enum SendMessageResultEnum {

    /**
     * 成功
     */
    Sucess(1),

    /**
     * 讨论区关闭
     */
    Close(2);

    @Getter
    @Setter
    private int Code;

    // 构造方法
    SendMessageResultEnum(int Code) {
        this.Code = Code;
    }

}
