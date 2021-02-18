package com.ry.sskt.model.subject.response;

import com.ry.sskt.model.subject.entity.GetHotSubjectListDetail;
import com.ry.sskt.model.subject.entity.GetStudentDetail;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * 【课程详情】获取课堂学生列表 输出参数
 */
@Data
@Accessors(chain = true)
public class GetStudentListResponse {
    // <summary>
    /// 总数
    /// </summary>
    private int Count;

    /// <summary>
    /// 学生列表
    /// </summary>
    private List<GetStudentDetail> StudentList;

    public GetStudentListResponse() {
        StudentList = new LinkedList<>();
    }
}
