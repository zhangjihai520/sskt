package com.ry.sskt.model.teacher.response;

import com.ry.sskt.model.teacher.entity.SubjectEvaluateInfo;
import com.ry.sskt.model.teacher.entity.SubjectInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// <summary>
/// 获取所有课程的评价输出对象
/// </summary>
@Data
@Accessors(chain = true)
public class GetAllSubjectEvaluateResponse {

    /// <summary>
    /// 集合
    /// </summary>
    private List<SubjectEvaluateInfo> SubjectEvaluateList;
}
