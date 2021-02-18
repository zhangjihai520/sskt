package com.ry.sskt.model.subject.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 获取课程列表班级信息详情
 */
@Data
@Accessors(chain = true)
public class GetSubjectListDetail {
    /// <summary>
    /// 课程加密Id
    /// </summary>
    private String SubjectId;

    /// <summary>
    /// 课程类型 1、直播室直播课程 2、公益课点播课程'  3在线直播课
    /// </summary>
    private int SubjectGenreId;

    /// <summary>
    /// 课程名称
    /// </summary>
    private String SubjectName;

    /// <summary>
    /// 图片地址
    /// </summary>
    private String ImagePath;

    /// <summary>
    /// 学段学科
    /// </summary>
    private String CourseName;

    /// <summary>
    /// 学段短码学科
    /// </summary>
    private String ShortCode;

    /// <summary>
    /// 关键字查询
    /// </summary>
    private List<String> TeacherNameList;

    /// <summary>
    /// 年级名称
    /// </summary>
    private String GradeName;

    /// <summary>
    /// 课程展示状态  枚举 1：待上架 2：待报名 3：报名中 4:待上课 5：已结课
    /// <see cref="SubjectStatusEnum"/>
    /// </summary>
    private int StatusFlag;

    /// <summary>
    /// 报名截止时间
    /// </summary>
    private String RegistEndTime;

    /// <summary>
    /// 创建时间
    /// </summary>
    private String CreateDateTime;

    /// <summary>
    /// 课程开始时间
    /// </summary>
    private String BeginTime;

    /// <summary>
    /// 是否为主讲教师
    /// </summary>
    private boolean IsLecturer;

    /// <summary>
    /// 教室加密ID，仅助教需要
    /// </summary>
    private String SubjectRoomId;

    /// <summary>
    /// 设备Id
    /// </summary>
    private String DeviceId;
}
