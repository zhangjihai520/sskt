package com.ry.sskt.model.examset.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.examset.entity.ExamSetStatistDetail;
import com.ry.sskt.model.examset.entity.GetExamSetListDetail;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
/// <summary>
/// 【课堂作业】获取作业统计列表 输出对象
/// </summary>
@Data
@Accessors(chain = true)
public class GetExamSetStatistListResponse {


    /// <summary>
    /// 总数
    /// </summary>
    @JSONField(name = "Count")
    private int count;

    /// <summary>
    /// 作业统计列表
    /// </summary>
    @JSONField(name = "ExamSetStatistLists")
    private List<ExamSetStatistDetail> examSetStatistLists;
}
