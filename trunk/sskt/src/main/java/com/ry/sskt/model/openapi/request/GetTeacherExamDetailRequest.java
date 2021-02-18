package com.ry.sskt.model.openapi.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetTeacherExamDetailRequest extends OpenApiRequestBase {
    /// <summary>
    /// 题集唯一标识
    /// </summary>
    @JSONField(name = "TeacherExamId")
    public String teacherExamId;

    public GetTeacherExamDetailRequest(String appKey, String nonceStr, String teacherExamId) {
        super(appKey, nonceStr);
        this.teacherExamId = teacherExamId;
    }

    public GetTeacherExamDetailRequest(String key) {
        super(key);
    }

    public GetTeacherExamDetailRequest() {

    }
}
