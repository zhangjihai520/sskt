package com.ry.sskt.model.subject.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/// <summary>
/// 课件资料详情
/// </summary>
@Data
@Accessors(chain = true)
public class SubjectFileDetail {
    /// <summary>
    /// 课件资料id
    /// </summary>
    private String FileId;

    /// <summary>
    /// 课件资料名称
    /// </summary>
    private String FileName;

    /// <summary>
    /// 课件资料类型
    /// </summary>
    private String FileType;

    /// <summary>
    /// 课件资料下载路径
    /// </summary>
    private String FilePath;

    /// <summary>
    /// 更新时间
    /// </summary>
    private String UpdateDateTime;
}
