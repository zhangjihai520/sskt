package com.ry.sskt.model.common.constant;

import lombok.Getter;

public enum SubjectStatusFlagEnum {
    Delete(0,"删除"),
    Invalid(1, "待上架"),
    ToBeRegistered(2, "已上架");

    @Getter
    private Integer index;

    @Getter
    private String value;

    private SubjectStatusFlagEnum(int index, String value) {
        this.index = index;
        this.value = value;
    }
}
