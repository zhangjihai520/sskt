package com.ry.sskt.model.openapi.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.openapi.entity.StudentQuestion;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// <summary>
/// 获取题目请求参数
/// </summary>
@Data
@Accessors(chain = true)
public class GetQuestionRequest extends OpenApiRequestBase {

    /// <summary>
    /// 题目Id集合
    /// </summary>
    @JSONField(name = "QuestionIds")
    private List<Integer> questionIds;

    public GetQuestionRequest() {
    }

    public GetQuestionRequest(String key) {
        super(key);
    }
}
