package com.ry.sskt.model.teacher.response;

import com.ry.sskt.model.teacher.entity.SubjectInfo;
import com.ry.sskt.model.teacher.entity.TeacherInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/// <summary>
/// 【教学管理】助教 - 课程评价列表Response
/// </summary>
@Data
@Accessors(chain = true)
public class GetSubjectEvaluateListResponse {

    /// <summary>
    /// 总数
    /// </summary>
    private int TotalCount;

    /// <summary>
    /// 课程列表
    /// </summary>
    private List<SubjectInfo> SubjectList;

    public GetSubjectEvaluateListResponse() {
        SubjectList = new LinkedList<>();
    }
}
