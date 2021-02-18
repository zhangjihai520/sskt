package com.ry.sskt.model.student.entity.view;

import com.ry.sskt.model.subject.entity.Subject;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

/// <summary>
/// 学校学生占比
/// </summary>
@Data
@Accessors(chain = true)
public class SchoolStudentCountView {
    /// <summary>
    /// 学校名称
    /// </summary>
    private String SchoolName;

    /// <summary>
    /// 总人数
    /// </summary>
    private int TotalCount;

    /// <summary>
    /// 年级名称
    /// </summary>
    private String GradeName;

    /// <summary>
    /// 学校名称或年级名称，只会有一个
    /// </summary>
    private String Name;

    public String getName() {
        return StringUtils.isBlank(SchoolName) ? GradeName : SchoolName;
    }
}
