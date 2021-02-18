package com.ry.sskt.model.common.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: 幸仁强
 * @Date: 2019/7/16 15:34
 * @Description: 用户类型
 */
public enum GetMySubjectDataSortEnum {

    /**
     * 上课时间倒序
     */
    BeginTimeDesc(0),

    /**
     * 上课人数倒序
     */
    ClassAttendanceDesc(1),

    /**
     * 好评度倒序
     */
    PraiseDesc(2);

    @Getter
    @Setter
    private Integer Code;

    // 构造方法
    GetMySubjectDataSortEnum(int Code) {
        this.Code = Code;
    }

}
