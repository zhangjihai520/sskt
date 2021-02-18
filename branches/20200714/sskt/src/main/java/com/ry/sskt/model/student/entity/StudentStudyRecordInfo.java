package com.ry.sskt.model.student.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/// <summary>
/// 轨迹信息
/// </summary>
@Data
@Accessors(chain = true)
public class StudentStudyRecordInfo {
    /// <summary>
    /// 时间
    /// </summary>
    private String CreateDateTime;

    /// <summary>
    /// 事件名称
    /// </summary>
    private String Content;

    /// <summary>
    /// 事件类型 <see cref="VideoClass.Model.Enum.StudyRecordTypeEnum">
    /// </summary>
    private int StudyRecordTypeId;

    /// <summary>
    /// 作业ID,加密
    /// </summary>
    private String ExamSetId;
}
