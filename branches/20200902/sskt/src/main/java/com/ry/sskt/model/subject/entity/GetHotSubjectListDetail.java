package com.ry.sskt.model.subject.entity;

import com.ry.sskt.model.subject.entity.view.SubjectStatisticsInfo;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 获取课程列表班级信息详情
 */
@Data
@Accessors(chain = true)
public class GetHotSubjectListDetail extends SubjectStatisticsInfo {
    /// <summary>
    /// 课程名称
    /// </summary>
    private String Name;

    /// <summary>
    /// 课程id
    /// </summary>
    private String SubjectId;

    /// <summary>
    /// 课程开始时间
    /// </summary>
    private String BeginTime;

    /// <summary>
    /// 学段短码学科
    /// </summary>
    private String ShortCode;
    public GetHotSubjectListDetail()
    {
        super();
    }
}
