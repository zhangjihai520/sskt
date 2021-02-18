package com.ry.sskt.model.subject.response;

import com.ry.sskt.model.subject.entity.DateGroupDetail;
import com.ry.sskt.model.subject.entity.GetSubjectListDetail;
import com.ry.sskt.model.subject.entity.SubjectRoomDetail;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 【课堂管理】获取课程详情 输出参数
 */
@Data
@Accessors(chain = true)
public class GetSubjectInfoResponse {

    /// <summary>
    /// 课程加密Id
    /// </summary>
    private String SubjectId;

    /// <summary>
    /// 课程加密Id,可以为空
    /// </summary>
    private String SubjectName;

    /// <summary>
    /// 报名开始时间
    /// </summary>
    private String RegistBeginTime;

    /// <summary>
    /// 报名开始时间
    /// </summary>
    private String RegistEndTime;

    /// <summary>
    /// 课程开始时间
    /// </summary>
    private String BeginTime;

    /// <summary>
    /// 所属学科枚举值
    /// </summary>
    private int CourseId;

    /// <summary>
    /// 年级枚举值
    /// </summary>
    private int GradeId;

    /// <summary>
    /// 教师Id
    /// </summary>
    private String TeacherId;

    /// <summary>
    /// 教师名称
    /// </summary>
    private String TeacherName;

    /// <summary>
    /// 图片地址
    /// </summary>
    private String ImagePath;

    /// <summary>
    /// 备注
    /// </summary>
    private String Comment;


    /// <summary>
    /// 公益课录像地址
    /// </summary>
    private String VideoUrl;

    /// <summary>
    /// 课程类型
    /// </summary>
    private int SubjectGenreId;

    /// <summary>
    /// 课程结束时间
    /// </summary>
    private String EndTime;

    /// <summary>
    /// 教室列表
    /// </summary>
    private List<SubjectRoomDetail> SubjectRooms;
}
