package com.ry.sskt.model.subject.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class GetPublicBenefitSubjectListDetail {
    /// <summary>
    /// 操作Url
    /// </summary>
    private String OperationUrl;

    /// <summary>
    /// 操作类型 0为视频本地播放 1为模拟练习（原FileDownUrl操作类型） 2为跳转其他网页（原PopupUrl操作类型） 3为自主作业
    /// </summary>
    private int OperationMode;

    /// <summary>
    /// 是否自助作业
    /// </summary>
    private boolean IsZzzy;

    /// <summary>
    /// 弹出窗口地址
    /// </summary>
    private String PopupUrl;

    /// <summary>
    /// 练习文件地址
    /// </summary>
    private String FileDownUrl;

    /// <summary>
    /// 视频播放地址
    /// </summary>
    private String PPTVideoUrl;

    /// <summary>
    /// 课程加密Id
    /// </summary>
    private String SubjectId;

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
    /// 年级名称
    /// </summary>
    private String GradeName;

    /// <summary>
    /// 课程展示状态  枚举 1：待上架 2：待报名 3：报名中 4:待上课 5：已结课
    /// <see cref="VideoClass.WebApi.Models.Enum.SubjectStatusEnum"/>
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
    /// 课程结束时间
    /// </summary>
    private String EndTime;

    /// <summary>
    /// 教室加密ID，仅助教需要
    /// </summary>
    private String SubjectRoomId;

    /// <summary>
    /// 设备Id
    /// </summary>
    private String DeviceId;
}
