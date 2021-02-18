package com.ry.sskt.model.subject.response;

import com.ry.sskt.model.subject.entity.PublicBenefitDateGroupDetail;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 【课程统计/学情监控】获取热门数据 输出参数
 */
@Data
@Accessors(chain = true)
public class GetPublicBenefitSubjectListResponse {
    /// <summary>
    /// 总数
    /// </summary>
    private int Count;

    private List<PublicBenefitDateGroupDetail> DateGroup;

    public GetPublicBenefitSubjectListResponse() {
        DateGroup = new LinkedList<>();
    }
}
