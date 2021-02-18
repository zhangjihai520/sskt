package com.ry.sskt.model.subject.response;

import com.ry.sskt.model.subject.entity.SubjectExamSetDetail;
import com.ry.sskt.model.subject.entity.SubjectInfoForShowDetail;
import com.ry.sskt.model.subject.entity.SubjectRoomDetail;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 【课程详情】获取课程详情列表 输出参数
 */
@Data
@Accessors(chain = true)
public class GetSubjectInfoForShowResponse {
    /// <summary>
    /// 课程加密Id
    /// </summary>
    private String SubjectId;

    /// <summary>
    /// 课程加密Id,可以为空
    /// </summary>
    private String SubjectName;

    /// <summary>
    /// 课程开始时间
    /// </summary>
    private String BeginTime;

    /// <summary>
    /// 学段学科
    /// </summary>
    private String CourseName;

    /// <summary>
    /// 年级
    /// </summary>
    private String GradeName;

    /// <summary>
    /// 教师Id
    /// </summary>
    private String TeacherId;

    /// <summary>
    /// 教师名称
    /// </summary>
    private String TeacherName;

    /// <summary>
    /// 教室列表
    /// </summary>
    private List<SubjectInfoForShowDetail> SubjectRooms;

    /// <summary>
    /// 教室列表
    /// </summary>
    private List<SubjectExamSetDetail> ExamSetList;

    /// <summary>
    /// 课程状态 SubjectStatusEnum
    /// </summary>
    private int StatusFlag;

    /// <summary>
    /// 所有报名人数
    /// </summary>
    private int RegisterTotalCount;

    /// <summary>
    /// 是否显示主讲查看详情
    /// </summary>
    private boolean IsShowNumber;

}
