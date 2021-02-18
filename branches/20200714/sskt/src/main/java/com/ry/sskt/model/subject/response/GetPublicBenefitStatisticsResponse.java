package com.ry.sskt.model.subject.response;

import com.ry.sskt.model.subject.entity.GetCurrentSubjectNameDetail;
import com.ry.sskt.model.subject.entity.VideoPlayCountInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 获取课程播放情况统计 输出参数
 */
@Data
@Accessors(chain = true)
public class GetPublicBenefitStatisticsResponse {

    /// <summary>
    /// 总人数
    /// </summary>
    private int Headcount;

    /// <summary>
    /// 初中人数
    /// </summary>
    private int JuniorHeadcount;
    /// <summary>
    /// 高中人数
    /// </summary>
    private int HighHeadcount;
    /// <summary>
    /// 公益课播放信息列表
    /// </summary>
    private List<VideoPlayCountInfo> VideoPlayInfoList;

    public GetPublicBenefitStatisticsResponse() {
        VideoPlayInfoList = new LinkedList<>();
    }
}
