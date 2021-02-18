package com.ry.sskt.model.subject.response;

import com.ry.sskt.model.subject.entity.GetHotSubjectListDetail;
import com.ry.sskt.model.subject.entity.SubjectFileDetail;
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
public class GetSubjectFileListResponse {
    /// <summary>
    /// 总数
    /// </summary>
    private int Count;

    /// <summary>
    /// 课件资料列表
    /// </summary>
    private List<SubjectFileDetail> FileList;

    /// <summary>
    /// 构造函数
    /// </summary>
    public GetSubjectFileListResponse() {
        FileList = new LinkedList<SubjectFileDetail>();
    }
}
