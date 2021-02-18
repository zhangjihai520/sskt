package com.ry.sskt.model.student.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.student.entity.QuestionAnswer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 【学生端】提交答案request
 */
@Data
@Accessors(chain = true)
public class SubmitExamSetApiRequest extends RequestBase {
    /// <summary>
    /// 作业加密ID
    /// </summary>
    @JSONField(name = "ExamSetId")
    private String examSetId;

    /// <summary>
    /// 题目列表
    /// </summary>
    @JSONField(name = "QuestionList")
    private List<QuestionAnswer> questionList;
}
