package com.ry.sskt.model.common.constant;

import lombok.Getter;

/**
 * 更新课程列表状态
 */
public enum UpdateSubjectStatusEnum {
    //删除
    Delete(0),
    //上架
    Up(1),
    //下架
    Down(2);

    @Getter
    private Integer index;


    private UpdateSubjectStatusEnum(int index) {
        this.index = index;
    }
}
