package com.ry.sskt.model.student.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.student.entity.QuestionAnswer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 【学生端】向老师提问Request
 */
@Data
@Accessors(chain = true)
public class AskQuestionApiRequest extends RequestBase {
    /// <summary>
    /// 助教老师id，加密
    /// </summary>
    @JSONField(name = "TeacherId")
    private String teacherId;

    /// <summary>
    /// 提问内容
    /// </summary>
    @JSONField(name = "Content")
    private String content;
}
