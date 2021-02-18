package com.ry.sskt.model.subject.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 获取我的学生课程列表 输出参数
 */
@Data
@Accessors(chain = true)
public class GetMySubjectH5Detail {
    /// <summary>
    /// 课程名称
    /// </summary>
    private String SubjectName;

    /// <summary>
    /// 课程加密Id
    /// </summary>
    private String SubjectId;

    /// <summary>
    /// 学科ID
    /// </summary>
    private int CourseId;

    /// <summary>
    /// 课程开始时间  YYYY-MM-DD HH:mm
    /// </summary>
    private List<SubjectExamSetH5> ExamSetList;

    public GetMySubjectH5Detail() {
        ExamSetList = new LinkedList<>();
    }

}
