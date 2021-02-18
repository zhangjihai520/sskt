package com.ry.sskt.model.common.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: 幸仁强
 * @Date: 2020/4/16 15:34
 * @Description: 公共常量类
 */
public enum GetMessageTypeEnum {
    /**
     * 获取某Id之前的数据
     */
    OldMessage(1),
    /**
     * 获取某Id之后的数据
     */
    NewMessage(2),
    /**
     * 获取当前时间之前的数据
     */
    CurrentMessage(3);

    @Getter
    @Setter
    private int Code;

    GetMessageTypeEnum(Integer Code) {
        this.Code = Code;
    }
}
