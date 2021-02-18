package com.ry.sskt.model.common.constant;

import lombok.Getter;

/**
 * 类型名称
 */
public enum SubjectListOrderFiledEnum {
    /**
     * 目录
     */
    CreateDateTime_Desc(0, "CreateDateTime Desc"),
    /**
     * 菜单
     */
    BeginTime_Desc(1, "BeginTime Desc");
    @Getter
    private Integer Code;
    @Getter
    private String Msg;

    SubjectListOrderFiledEnum(Integer Code, String Msg) {
        this.Code = Code;
        this.Msg = Msg;
    }
}
