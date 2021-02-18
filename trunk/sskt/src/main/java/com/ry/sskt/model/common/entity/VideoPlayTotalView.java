package com.ry.sskt.model.common.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Accessors(chain = true)
public class VideoPlayTotalView {
    /// <summary>
    /// 课程Id
    /// </summary>
    private Integer subjectId;

    /// <summary>
    /// 课程名称
    /// </summary>
    private String subjectName;

    /// <summary>
    /// 播放次数
    /// </summary>
    private Integer playCount;

    /// <summary>
    /// 课程开始时间
    /// </summary>
    private LocalDateTime beginTime;

    /// <summary>
    /// 构造函数
    /// </summary>
    public VideoPlayTotalView()
    {
    }
}
