package com.ry.sskt.model.common.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: 幸仁强
 * @Date: 2019/7/16 15:34
 * @Description: 评价类型枚举
 */
public enum EvaluateTypeEnum {

    /// <summary>
    /// 1学生对课程的评价
    /// </summary>
    StudentEvaluateSubject(1),
    /// <summary>
    /// 2助教对课程的评价
    /// </summary>
    TeacherEvaluateSubject(2),
    /// <summary>
    /// 3家长对课程的评价
    /// </summary>
    ParentEvaluateSubject(3),
    /// <summary>
    /// 4老师对学生的评价
    /// </summary>
    TeacherEvaluateStudent(4);

    @Getter
    @Setter
    private int Code;

    // 构造方法
    EvaluateTypeEnum(int Code) {
        this.Code = Code;
    }

}
