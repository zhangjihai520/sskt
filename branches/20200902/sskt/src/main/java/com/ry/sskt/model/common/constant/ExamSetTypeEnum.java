package com.ry.sskt.model.common.constant;

import lombok.Getter;

/**
 * 课程类型 1、普通课程 2、在线互动实验室'
 */
public enum ExamSetTypeEnum {
    BeforeClass(1,"课前作业"),
    AlongClass(2, "随堂练习"),
    AfterClass(3, "课后作业");

    @Getter
    private Integer index;

    @Getter
    private String value;

    private ExamSetTypeEnum(int index, String value) {
        this.index = index;
        this.value = value;
    }
}
