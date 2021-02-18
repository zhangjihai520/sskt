package com.ry.sskt.model.common.constant;

import lombok.Getter;

public enum SubjectClassStateEnum {
    All(0,"全部"),
    WaitingForClass(1, "待上课"),
    InClass(2, "上课中"),
    ClosedClass(3, "已结课"),
    ManualClosedClass(4, "手动已结课");

    @Getter
    private Integer index;

    @Getter
    private String value;

    private SubjectClassStateEnum(int index, String value) {
        this.index = index;
        this.value = value;
    }
}
