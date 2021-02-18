package com.ry.sskt.model.subject.response;

import com.ry.sskt.model.student.entity.GetTeacherToStudentEvaluateListInfo;
import com.ry.sskt.model.subject.entity.DateGroupDetail;
import com.ry.sskt.model.subject.entity.GetSubjectListDetail;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 【课表管理】获取课程列表返回参数列表
 */
@Data
@Accessors(chain = true)
public class GetSubjectListResponse {

    /// <summary>
    /// 总数
    /// </summary>
    private int Count;

    /// <summary>
    /// 详情
    /// </summary>
    private List<GetSubjectListDetail> ClassInfoList;

    private List<DateGroupDetail> DateGroup;

    public GetSubjectListResponse() {
        ClassInfoList = new LinkedList<>();
        DateGroup = new LinkedList<>();
    }
}
