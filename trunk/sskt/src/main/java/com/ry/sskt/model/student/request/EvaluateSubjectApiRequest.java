package com.ry.sskt.model.student.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 【学生端】向老师提问Request
 */
@Data
@Accessors(chain = true)
public class EvaluateSubjectApiRequest extends RequestBase {

    /// <summary>
    /// 评星
    /// </summary>
    private int EvaluateLevel;

    /// <summary>
    /// 评价内容
    /// </summary>
    private String EvaluateComment;

    /// <summary>
    /// 课程Id，加密
    /// </summary>
    private String SubjectId;

    /// <summary>
    /// 教室Id，加密
    /// </summary>
    private String SubjectRoomId;

    /// <summary>
    /// 主讲老师Id，加密
    /// </summary>
    private String TeacherId;
}
