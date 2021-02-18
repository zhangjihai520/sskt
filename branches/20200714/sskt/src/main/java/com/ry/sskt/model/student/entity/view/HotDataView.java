package com.ry.sskt.model.student.entity.view;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/// <summary>
/// 热门数据
/// </summary>
@Data
@Accessors(chain = true)
public class HotDataView extends HotDataTotalView {
    /// <summary>
    /// 用户名称
    /// </summary>
    private String Name;

    /// <summary>
    /// 课程id
    /// </summary>
    private int SubjectId;

    /// <summary>
    /// 课程学科Id
    /// </summary>
    private int CourseId;

    /// <summary>
    /// 课程开始时间
    /// </summary>
    private LocalDateTime BeginTime;
}
