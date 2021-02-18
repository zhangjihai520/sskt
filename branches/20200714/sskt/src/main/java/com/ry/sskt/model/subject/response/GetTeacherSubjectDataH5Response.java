package com.ry.sskt.model.subject.response;

import com.ry.sskt.model.subject.entity.GetTeacherSubjectDataH5Detail;
import com.ry.sskt.model.subject.entity.SubjectRoomDetail;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 课堂数据 输出参数
 */
@Data
@Accessors(chain = true)
public class GetTeacherSubjectDataH5Response {
    /// <summary>
    /// 用户课程列表
    /// </summary>
    private List<GetTeacherSubjectDataH5Detail> SubjectList;

    public GetTeacherSubjectDataH5Response() {
        SubjectList = new LinkedList<>();
    }

}
