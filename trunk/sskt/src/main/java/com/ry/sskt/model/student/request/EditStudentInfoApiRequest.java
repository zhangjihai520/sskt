package com.ry.sskt.model.student.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 【学生端】修改个人信息Request
 */
@Data
@Accessors(chain = true)
public class EditStudentInfoApiRequest extends RequestBase {

    /// <summary>
    /// 姓名
    /// </summary>
    @JSONField(name = "UserTrueName")
    private String userTrueName;

    /// <summary>
    /// 学校名称
    /// </summary>
    @JSONField(name = "SchoolName")
    private String schoolName;

    /// <summary>
    /// 年级Id
    /// </summary>
    @JSONField(name = "GradeId")
    private int gradeId;

    /// <summary>
    /// 备注
    /// </summary>
    @JSONField(name = "Comment")
    private String comment;
}
