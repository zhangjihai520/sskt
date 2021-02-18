package com.ry.sskt.model.teacher.response;

import com.ry.sskt.model.teacher.entity.TeacherInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/// <summary>
/// 【课程管理】我的课程-导入学生Response
/// </summary>
@Data
@Accessors(chain = true)
public class ImportStudentResponse {

    /// <summary>
    /// 教师列表
    /// </summary>
    private List<String> MessageList;

    public ImportStudentResponse() {
        MessageList = new LinkedList<>();
    }
}
