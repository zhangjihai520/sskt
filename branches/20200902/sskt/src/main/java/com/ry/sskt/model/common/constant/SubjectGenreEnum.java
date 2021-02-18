package com.ry.sskt.model.common.constant;

import lombok.Getter;

/**
 * 课程类型 1、普通课程 2、在线互动实验室'
 */
public enum SubjectGenreEnum {
    Other(0,"全部"),
    Common(1, "普通课程"),
    PublicBenefit(2, "公益课课程");

    @Getter
    private Integer index;

    @Getter
    private String value;

    private SubjectGenreEnum(int index, String value) {
        this.index = index;
        this.value = value;
    }
}
