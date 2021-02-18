package com.ry.sskt.model.subject.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/// <summary>
/// 学生附件详情
/// </summary>
@Data
@Accessors(chain = true)
public class StudentSubjectFileDetail {
    /// <summary>
    /// 课程附件名称
    /// </summary>
    private String SubjectFileName;

    /// <summary>
    /// 文件下载地址
    /// </summary>
    private String FilePath;
}
