package com.ry.sskt.model.subject.response;

import com.ry.sskt.model.subject.entity.SubjectRoomDetail;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 获取课程管理列表 输出参数
 */
@Data
@Accessors(chain = true)
public class GetSubjectListCountResponse {
    /// <summary>
    /// 总数
    /// </summary>
    private int Total;

    /// <summary>
    /// 本周数量
    /// </summary>
    private int TotalWeek;

    /// <summary>
    /// 本周完成数量
    /// </summary>
    private int CompletedTotalWeek;

}
