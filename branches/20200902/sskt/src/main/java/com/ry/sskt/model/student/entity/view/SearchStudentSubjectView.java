package com.ry.sskt.model.student.entity.view;

import com.ry.sskt.model.subject.entity.Subject;
import lombok.Data;
import lombok.experimental.Accessors;

/// <summary>
/// 获取上课人数
/// </summary>
@Data
@Accessors(chain = true)
public class SearchStudentSubjectView extends Subject {
    /// <summary>
    /// 主讲教师名称
    /// </summary>
    private String UserTrueName;

    /// <summary>
    /// 学校名称
    /// </summary>
    private String SchoolName;

    /// <summary>
    /// 教室名称
    /// </summary>
    private String SubjectRoomName;
}
