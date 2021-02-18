package com.ry.sskt.core.dledc.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ClassInfo {
    /// <summary>
    /// 班级id
    /// </summary>
    @JSONField(name = "DepartmentId")
    private String departmentId;
    /// <summary>
    /// 班级名称
    /// </summary>
    @JSONField(name = "DepartmentName")
    private String departmentName;
    /// <summary>
    /// 年级code,1-12对应12个年级
    /// </summary>
    @JSONField(name = "GradeCode")
    private String gradeCode;
}
