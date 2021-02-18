package com.ry.sskt.model.student.response;

import com.ry.sskt.model.student.entity.GetTeacherToStudentEvaluateListInfo;
import com.ry.sskt.model.student.entity.SubjectInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 学生-评价管理-我的评价Response
 */
@Data
@Accessors(chain = true)
public class GetTeacherToStudentEvaluateListResponse {

    /// <summary>
    /// 总条数
    /// </summary>
    private int TotalCount;

    /// <summary>
    /// 课程列表
    /// </summary>
    private List<GetTeacherToStudentEvaluateListInfo> EvaluateList;

    public GetTeacherToStudentEvaluateListResponse() {
        EvaluateList = new LinkedList<>();
    }

    public GetTeacherToStudentEvaluateListResponse(int totalCount, List<GetTeacherToStudentEvaluateListInfo> evaluateList) {
        TotalCount = totalCount;
        EvaluateList = evaluateList;
    }
}
