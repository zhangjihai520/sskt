package com.ry.sskt.model.subject.response;

import com.ry.sskt.model.subject.entity.GetCurrentSubjectNameDetail;
import com.ry.sskt.model.subject.entity.GetHotSubjectListDetail;
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
public class GetCurrentSubjectNameResponse {
    /// <summary>
    /// 详情
    /// </summary>
    private List<GetCurrentSubjectNameDetail> SubjectList;

    public GetCurrentSubjectNameResponse() {
        SubjectList = new LinkedList<>();
    }
}
