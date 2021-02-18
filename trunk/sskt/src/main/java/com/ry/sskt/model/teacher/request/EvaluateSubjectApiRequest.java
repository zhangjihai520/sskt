package com.ry.sskt.model.teacher.request;

import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 【教学管理】助教 -评价课程Request
 */
@Data
@Accessors(chain = true)
public class EvaluateSubjectApiRequest extends RequestBase {

    /// <summary>
    /// 课程id，加密
    /// </summary>
    private String SubjectId;

    /// <summary>
    /// 学生ID，加密
    /// </summary>
    private String TeacherId;

    /// <summary>
    /// 评星
    /// </summary>
    private int EvaluateLevel;

    /// <summary>
    /// 评价内容
    /// </summary>
    private String EvaluateComment;

    /// <summary>
    /// 回复状态  1:已回复，0:未回复
    /// </summary>
    private int IsReply;

}
