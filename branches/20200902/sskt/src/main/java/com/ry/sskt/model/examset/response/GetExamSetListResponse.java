package com.ry.sskt.model.examset.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.examset.entity.GetExamSetListDetail;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class GetExamSetListResponse {


    /// <summary>
    /// 总数
    /// </summary>
    @JSONField(name = "Count")
    private int count;

    /// <summary>
    /// 作业列表
    /// </summary>
    @JSONField(name = "ExamSetLists")
    private List<GetExamSetListDetail> examSetLists;
}
