package com.ry.sskt.model.subject.request;

import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 更新学生上课状态
 */
@Data
@Accessors(chain = true)
public class UpdateSubjectStudentStatusApiRequest extends RequestBase {
    /// <summary>
    /// 加密课程Id
    /// </summary>
    private String SubjectId;

    /// <summary>
    /// 加密的学生Id,可以不传(不传则使用登录人的Id)
    /// </summary>
    private String StudentId;

    /// <summary>
    /// 上课状态,0到席，1缺席
    /// </summary>
    public int StatusFlag;
}
