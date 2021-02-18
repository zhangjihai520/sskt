package com.ry.sskt.model.subject.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 课程附件
 */
@Data
@Accessors(chain = true)
public class SubjectFile {
    /// <summary>
    /// 课程附件id
    /// </summary>
    @JSONField(name = "SubjectFileId")
    private int subjectFileId;

    /// <summary>
    /// 课程id
    /// </summary>
    @JSONField(name = "SubjectId")
    private int subjectId;

    /// <summary>
    /// 课程附件名称
    /// </summary>
    @JSONField(name = "SubjectFileName")
    private String subjectFileName;

    /// <summary>
    /// 文件类型
    /// </summary>
    @JSONField(name = "FileType")
    private String fileType;

    /// <summary>
    /// 文件路径
    /// </summary>
    @JSONField(name = "FilePath")
    private String filePath;

    /// <summary>
    /// 状态，0删除，1未删除
    /// </summary>
    @JSONField(name = "StatusFlag")
    private int statusFlag;

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
