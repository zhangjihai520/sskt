package com.ry.sskt.model.subject.request;

import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.subject.entity.SubjectRoomDetail;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 【课程统计/学情监控】获取课程人数详情图表 输入参数
 */
@Data
@Accessors(chain = true)
public class GetSubjectListStatisticsApiRequest extends RequestBase {
    /// <summary>
    /// 创建时间 最小时间
    /// </summary>
    private int Days;

    /// <summary>
    /// 数据类型 1学生，2老师，3管理员，4家长
    /// </summary>
    private int UserRole;
}
