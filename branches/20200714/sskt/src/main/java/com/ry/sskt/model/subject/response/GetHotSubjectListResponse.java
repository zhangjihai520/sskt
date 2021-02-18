package com.ry.sskt.model.subject.response;

import com.ry.sskt.model.subject.entity.GetHotSubjectListDetail;
import com.ry.sskt.model.subject.entity.SubjectRoomDetail;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * 【课程统计/学情监控】获取热门数据 输出参数
 */
@Data
@Accessors(chain = true)
public class GetHotSubjectListResponse {
    /// <summary>
    /// 总条数
    /// </summary>
    private int Count;

    /// <summary>
    /// 平均上课人次
    /// </summary>
    private int ClassAttendanceTotal;

    /// <summary>
    /// 平均出勤率
    /// </summary>
    private BigDecimal AverageAttendanceRate;

    /// <summary>
    /// 平均好评度
    /// </summary>
    private BigDecimal AveragePraise;

    /// <summary>
    /// 详情
    /// </summary>
    private List<GetHotSubjectListDetail> Details;

    public GetHotSubjectListResponse() {
        Details = new LinkedList<>();
    }
}
