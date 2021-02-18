package com.ry.sskt.model.student.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 学生轨迹表
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
@Data
@Accessors(chain = true)
public class StudentStudyRecord {

    /// <summary>
    /// 轨迹id
    /// </summary>
    @JSONField(name = "StudentStudyRecordId")
    private int studentStudyRecordId;

    /// <summary>
    /// 学生id
    /// </summary>
    @JSONField(name = "UserId")
    private int userId;

    /// <summary>
    /// 作业id
    /// </summary>
    @JSONField(name = "ExamSetId")
    private int examSetId;

    /// <summary>
    /// 课程id
    /// </summary>
    @JSONField(name = "SubjectId")
    private int subjectId;

    /// <summary>
    /// 轨迹类型，1:课前作业,2:随堂练习,3:课后作业,4:课程
    /// </summary>
    @JSONField(name = "StudyRecordTypeId")
    private int studyRecordTypeId;

    /// <summary>
    /// 轨迹信息
    /// </summary>
    @JSONField(name = "Content")
    private String content;

    /// <summary>
    /// 创建时间
    /// </summary>
    @JSONField(name = "CreateDateTime")
    private LocalDateTime createDateTime;

    /// <summary>
    /// 更新时间
    /// </summary>
    @JSONField(name = "UpdateDateTime")
    private LocalDateTime updateDateTime;

}
