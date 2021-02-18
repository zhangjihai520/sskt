package com.ry.sskt.model.teacher.request;

import com.ry.sskt.model.common.request.RequestPageBase;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 【消息】教师、学生消息列表Request
 */
@Data
@Accessors(chain = true)
public class GetSubjectEvaluateApiRequest extends RequestPageBase {
    /// <summary>
    /// 课程加密Id
    /// </summary>
    private String SubjectId;

    /// <summary>
    /// 0 全部， 2 老师 1 学生 3 家长
    /// </summary>
    private int EvaluateTypeId;
}
