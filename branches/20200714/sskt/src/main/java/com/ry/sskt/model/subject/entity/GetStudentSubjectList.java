package com.ry.sskt.model.subject.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 课程管理-课程列表
 */
@Data
@Accessors(chain = true)
public class GetStudentSubjectList {
    /// <summary>
    /// 课程加密Id
    /// </summary>
    private String SubjectId;

    /// <summary>
    /// 课程名称
    /// </summary>
    private String SubjectName;

    /// <summary>
    /// 学科名称 yw
    /// </summary>
    private String CourseShortCode;

    /// <summary>
    /// 课程图片
    /// </summary>
    private String ImagePath;

    /// <summary>
    /// 首个教师学校名称
    /// </summary>
    private String SchoolName;

    /// <summary>
    /// 课程名称
    /// </summary>
    private String TeacherName;

    /// <summary>
    /// 报名人数
    /// </summary>
    private int RegisterNumber;

    /// <summary>
    /// 课程图片
    /// </summary>
    private int MaxRegisterNumber;

    /// <summary>
    /// 报名截止时间 2019/3/3
    /// </summary>
    private String RegistEndTime;

    /// <summary>
    /// 开始上课时间 "2019/3/3 00:00"
    /// </summary>
    private String BeginTime;

    /// <summary>
    /// 是否已报名
    /// </summary>
    private boolean IsRegister;

    /// <summary>
    /// 课程状态
    /// </summary>
    private int StatusFlag;

    /// <summary>
    /// 作业列表
    /// </summary>
    private List<StudentExamSetDetail> ExamSetList;

    /// <summary>
    /// 附件列表
    /// </summary>
    private List<StudentSubjectFileDetail> SubjectFileList;

    public GetStudentSubjectList() {
        ExamSetList = new LinkedList<>();
        SubjectFileList = new LinkedList<>();
    }
}
