package com.ry.sskt.model.examset.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 作业统计信息
 */
@Data
@Accessors(chain = true)
public class ExamSetStatistDetail {
    /// <summary>
    ///学生Id 加密
    /// </summary>
    @JSONField(name = "StudentId")
    private String studentId;

    /// <summary>
    /// 学生名称
    /// </summary>
    @JSONField(name = "StudentName")
    private String studentName;

    /// <summary>
    /// 得分
    /// </summary>
    @JSONField(name = "Score")
    private String score;

    /// <summary>
    /// 提交时间
    /// </summary>
    @JSONField(name = "UpdateDateTime")
    private String updateDateTime;

}
