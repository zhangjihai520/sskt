package com.ry.sskt.model.teacher.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 模糊搜索所有教师名称Request
 */
@Data
@Accessors(chain = true)
public class EvaluateStudentApiRequest extends RequestBase {

    /// <summary>
    /// 课程id，加密
    /// </summary>
    private String SubjectId;

    /// <summary>
    /// 学生ID，加密
    /// </summary>
    private String StudentId;

    /// <summary>
    /// 评星
    /// </summary>
    private int EvaluateLevel;

    /// <summary>
    /// 评价内容
    /// </summary>
    private String EvaluateComment;

}
