package com.ry.sskt.model.subject.entity;

import com.ry.sskt.model.subject.entity.view.SubjectStatisticsInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 获取课程列表班级信息详情
 */
@Data
@Accessors(chain = true)
public class GetSubjectListStatisticsDetail extends SubjectStatisticsInfo {
    /// <summary>
    /// 课程名称
    /// </summary>
    private String BeginTime;

    public GetSubjectListStatisticsDetail()
    {
        super();
    }
}
