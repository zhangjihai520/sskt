package com.ry.sskt.model.common.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: 幸仁强
 * @Date: 2019/7/16 15:34
 * @Description: 轨迹类型枚举
 */
public enum StudyRecordTypeEnum {

    /**
     * 1课前作业
     */
    BeforeClass(1),

    /**
     * 2随堂练习
     */
    AlongClass(2),

    /**
     * 3课后作业
     */
    AfterClass(3),

    /**
     * 课程
     */
    Subject (4);

    @Getter
    @Setter
    private Integer Code;

    // 构造方法
    StudyRecordTypeEnum(int Code) {
        this.Code = Code;
    }

}
