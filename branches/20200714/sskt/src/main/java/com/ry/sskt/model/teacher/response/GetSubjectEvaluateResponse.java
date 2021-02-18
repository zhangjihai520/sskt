package com.ry.sskt.model.teacher.response;

import com.ry.sskt.model.student.entity.view.GetSubjectEvaluateView;
import com.ry.sskt.model.teacher.entity.MessageInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/// <summary>
/// 获取用户对主讲老师的评价 输出对象
/// </summary>
@Data
@Accessors(chain = true)
public class GetSubjectEvaluateResponse {
    /// <summary>
    /// 总分页数
    /// </summary>
    private int PageCount;

    /// <summary>
    /// 课程列表
    /// </summary>
    private List<GetSubjectEvaluateView> EvaluateList;

    public GetSubjectEvaluateResponse() {
    }

    public GetSubjectEvaluateResponse(int pageCount, List<GetSubjectEvaluateView> evaluateList) {
        PageCount = pageCount;
        EvaluateList = evaluateList;
    }
}
