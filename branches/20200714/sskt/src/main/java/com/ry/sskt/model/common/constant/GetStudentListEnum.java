package com.ry.sskt.model.common.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: 幸仁强
 * @Date: 2019/7/16 15:34
 * @Description: 发言类型
 */
public enum GetStudentListEnum {

    /**
     * 纯文本
     */
    RegisterNumber(0),

    /**
     * 图片
     */
    AttendNumber(1);

    @Getter
    @Setter
    private Integer Code;

    // 构造方法
    GetStudentListEnum(int Code) {
        this.Code = Code;
    }

}
