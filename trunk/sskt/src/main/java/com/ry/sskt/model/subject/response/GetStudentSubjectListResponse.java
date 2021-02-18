package com.ry.sskt.model.subject.response;

import com.ry.sskt.model.subject.entity.GetMySubjectH5Detail;
import com.ry.sskt.model.subject.entity.GetStudentSubjectList;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 【学生端】课程管理-课程列表 输出参数
 */
@Data
@Accessors(chain = true)
public class GetStudentSubjectListResponse {
    /// <summary>
    /// 总数
    /// </summary>
    private int Count;

    /// <summary>
    /// 课程列表
    /// </summary>
    private List<GetStudentSubjectList> SubjectList;

    public GetStudentSubjectListResponse() {
        SubjectList = new LinkedList<>();
    }
}
