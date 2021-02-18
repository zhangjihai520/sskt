package com.ry.sskt.model.openapi.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.openapi.entity.StudentQuestion;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// <summary>
/// 提交做题数据请求对象
/// </summary>
@Data
@Accessors(chain = true)
public class CheckQuestionRequest extends OpenApiRequestBase {
    /// <summary>
    /// 题目Id集合
    /// </summary>
    @JSONField(name = "QuestionIds")
    private List<Integer> questionIds;


    public CheckQuestionRequest() {
    }

    public CheckQuestionRequest(String key) {
        super(key);
    }
}
