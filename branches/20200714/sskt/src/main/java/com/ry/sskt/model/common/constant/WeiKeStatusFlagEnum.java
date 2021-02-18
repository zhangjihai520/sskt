package com.ry.sskt.model.common.constant;

import lombok.Getter;

public enum WeiKeStatusFlagEnum {
    Deleted(0, "已删除"),
    ToBeShelves(1, "待上架"),
    ToBeAudited(2, "待审核"),
    AuditPass(3, "已上架"),
    AuditFailed(4, "已驳回");

    @Getter
    private int index;

    @Getter
    private String value;

    private WeiKeStatusFlagEnum(int index, String value) {
        this.index = index;
        this.value = value;
    }
}
