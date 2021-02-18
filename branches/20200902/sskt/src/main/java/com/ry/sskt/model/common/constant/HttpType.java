package com.ry.sskt.model.common.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: 幸仁强
 * @Date: 2019/7/16 15:34
 * @Description: 用户类型
 */
public enum HttpType {
    PUT(0),
    GET(1),
    POST(2),
    DELETE(3);
    @Getter
    @Setter
    private int Code;

    // 构造方法
    HttpType(int Code) {
        this.Code = Code;
    }

}
