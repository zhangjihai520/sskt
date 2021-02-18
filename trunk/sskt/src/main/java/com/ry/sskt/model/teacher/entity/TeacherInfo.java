package com.ry.sskt.model.teacher.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/// <summary>
/// 教师信息
/// </summary>
@Data
@Accessors(chain = true)
public class TeacherInfo {
    /// <summary>
    /// 教师id
    /// </summary>
    public String Id;

    /// <summary>
    /// 教师名称
    /// </summary>
    public String Name;

    /// <summary>
    /// 学校名称
    /// </summary>
    public String SchoolName;
}
