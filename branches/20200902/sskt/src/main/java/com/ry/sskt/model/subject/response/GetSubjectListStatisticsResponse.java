package com.ry.sskt.model.subject.response;

import com.ry.sskt.model.subject.entity.GetSubjectListStatisticsDetail;
import com.ry.sskt.model.subject.entity.SubjectRoomDetail;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 【课堂管理】获取课程详情 输出参数
 */
@Data
@Accessors(chain = true)
public class GetSubjectListStatisticsResponse {
    /// <summary>
    /// 课程数组
    /// </summary>
    private List<GetSubjectListStatisticsDetail> SubjectList;

    /// <summary>
    /// 构造函数
    /// </summary>
    public GetSubjectListStatisticsResponse() {
        SubjectList = new LinkedList<GetSubjectListStatisticsDetail>();
    }
}
