package com.ry.sskt.model.openapi.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 题集详细信息
 */
@Data
@Accessors(chain = true)
public class TeacherExamDetailModel {
    /// <summary>
    /// 题集id，加密
    /// </summary>
    @JSONField(name="TeacherExamId")
    private String teacherExamId;

    /// <summary>
    /// 题集名称
    /// </summary>
    @JSONField(name="TeacherExamName")
    private String teacherExamName;

    /// <summary>
    /// 题集创建时间
    /// 格式：yyyy/MM/dd HH:mm:ss
    /// </summary>
    @JSONField(name="CreateDateTime")
    private String createDateTime;

    /// <summary>
    /// 题集下的题目信息
    /// </summary>
    @JSONField(name="Questions")
    private List<TeacherExamQuestionModel> questions;
}
