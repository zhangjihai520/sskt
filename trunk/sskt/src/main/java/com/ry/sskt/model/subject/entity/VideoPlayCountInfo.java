package com.ry.sskt.model.subject.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

@Data
@Accessors(chain = true)
public class VideoPlayCountInfo {
    /// <summary>
    /// 课程ID
    /// </summary>
    private String SubjectId;
    /// <summary>
    /// 课程名称
    /// </summary>
    private String SubjectName;
    /// <summary>
    /// 课程时间
    /// </summary>
    private String BeginTime;
    /// <summary>
    /// 观看次数
    /// </summary>
    private int PlayCount;
}
