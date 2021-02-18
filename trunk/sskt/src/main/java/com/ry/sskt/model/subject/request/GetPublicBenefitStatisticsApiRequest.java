package com.ry.sskt.model.subject.request;

import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 获取课程播放情况统计请求参数
 */
@Data
@Accessors(chain = true)
public class GetPublicBenefitStatisticsApiRequest extends RequestBase {
    /// <summary>
    /// 筛选时间
    /// </summary>
    private LocalDate CurrentDate;
}
