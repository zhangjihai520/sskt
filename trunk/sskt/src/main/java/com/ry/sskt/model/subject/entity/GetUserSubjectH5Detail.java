package com.ry.sskt.model.subject.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ry.sskt.core.utils.UrlUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 获取时间段内的学生开课情况 输出参数
 */
@Data
@Accessors(chain = true)
public class GetUserSubjectH5Detail {
    /// <summary>
    /// 上课地点 XX学校+教室名称
    /// </summary>
    private String SubjectRoomName;

    /// <summary>
    /// 课程加密Id
    /// </summary>
    private String SubjectId;

    /// <summary>
    /// 课程开始时间  YYYY-MM-DD HH:mm
    /// </summary>
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private String BeginTime;

    /// <summary>
    /// 报名截止时间 2019/3/3
    /// </summary>
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private String RegistEndTime;

    /// <summary>
    /// 是否已报名
    /// </summary>
    private boolean IsRegister;

    /// <summary>
    /// 课程状态
    /// </summary>
    private int StatusFlag;

    /// <summary>
    /// 课程名称
    /// </summary>
    private String SubjectName;

    /// <summary>
    /// 教师名称
    /// </summary>
    private String TeacherName;

    /// <summary>
    /// 是否满员
    /// </summary>
    private boolean IsMaxRegisterNumber;

    public GetUserSubjectH5Detail() {
    }

    public GetUserSubjectH5Detail(int subjectId, String subjectRoomName, String schoolName, LocalDateTime beginTime, String teacherName, String subjectName, boolean isMaxRegisterNumber, LocalDateTime registEndTime, boolean isRegister, int statusFlag) {
        SubjectRoomName = String.format("%s %s", schoolName, subjectRoomName);
        BeginTime = beginTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        SubjectId = UrlUtil.encrypt(subjectId);
        SubjectName = subjectName;
        TeacherName = teacherName;
        IsMaxRegisterNumber = isMaxRegisterNumber;
        IsRegister = isRegister;
        RegistEndTime = registEndTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        StatusFlag = statusFlag;
    }
}
