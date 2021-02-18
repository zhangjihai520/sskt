package com.ry.sskt.model.teacher.response;

import com.ry.sskt.model.teacher.entity.TeacherInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/// <summary>
/// 模糊搜索所有教师名称Response
/// </summary>
@Data
@Accessors(chain = true)
public class SearchTeacherInfoResponse {

    /// <summary>
    /// 教师列表
    /// </summary>
    private List<TeacherInfo> TeacherList;

    public SearchTeacherInfoResponse() {
        TeacherList = new LinkedList<>();
    }
}
