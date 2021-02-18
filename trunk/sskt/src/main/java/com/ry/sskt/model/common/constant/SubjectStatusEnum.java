package com.ry.sskt.model.common.constant;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

public enum SubjectStatusEnum {
    All(0, "全部"),
    Invalid(1, "待上架"),
    ToBeRegistered(2, "待报名"),
    Registration(3, "报名中"),
    WaitingForClass(4, "待上课"),
    ClosedClass(5, "已结课"),
    InClass(6, "上课中");

    @Getter
    private int index;

    @Getter
    private String value;

    private SubjectStatusEnum(int index, String value) {
        this.index = index;
        this.value = value;
    }

    public static String getValueByIndex(int index) {
        for (SubjectStatusEnum subjectStatusEnum : SubjectStatusEnum.values()) {
            if (subjectStatusEnum.getIndex() == index) {
                return subjectStatusEnum.getValue();
            }
        }
        return StringUtils.EMPTY;
    }
}
