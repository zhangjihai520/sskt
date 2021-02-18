package com.ry.sskt.model.common.constant;

import lombok.Getter;
import lombok.Setter;

public enum ResponseTypeEnum {

    /**
     * 学生
     */
    Success(1),

    /**
     * 老师
     */
    ValidateError(2),

    /**
     * 家长
     */
    Error (3);

    @Getter
    @Setter
    private int Code;

    // 构造方法
    ResponseTypeEnum(int Code) {
        this.Code = Code;
    }
}
