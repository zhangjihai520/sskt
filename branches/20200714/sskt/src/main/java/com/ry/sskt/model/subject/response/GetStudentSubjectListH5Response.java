package com.ry.sskt.model.subject.response;

import com.ry.sskt.model.subject.entity.GetMySubjectH5Detail;
import com.ry.sskt.model.subject.entity.SubjectRoomDetail;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 获取我的学生课程列表 输出参数
 */
@Data
@Accessors(chain = true)
public class GetStudentSubjectListH5Response {
    /// <summary>
    /// 用户课程列表
    /// </summary>
    private List<GetMySubjectH5Detail> SubjectList;

    public GetStudentSubjectListH5Response() {
        SubjectList = new LinkedList<>();
    }
}
