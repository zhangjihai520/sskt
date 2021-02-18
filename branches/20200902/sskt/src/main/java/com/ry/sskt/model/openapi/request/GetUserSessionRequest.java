package com.ry.sskt.model.openapi.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

/// <summary>
/// API注册用户请求
/// </summary>
@Data
@Accessors(chain = true)
public class GetUserSessionRequest extends OpenApiRequestBase {
    /// <summary>
    /// 用户唯一标识
    /// </summary>
    @JSONField(name = "UserCode")
    private String userCode;

    /// <summary>
    /// 用户的真实姓名(最大长度6位)
    /// </summary>
    @JSONField(name = "UserTrueName")
    private String userTrueName;

    /// <summary>
    /// 用户类型Id 1老师 2学生
    /// </summary>
    @JSONField(name = "UserTypeId")
    private int userTypeId;

    /// <summary>
    /// 学科Id(当UserTypeId为1的时候该字段必传)
    /// </summary>
    @JSONField(name = "CourseId")
    private int courseId;

    /// <summary>
    /// 年级(当UserTypeId为2的时候该字段必传)
    /// </summary>
    @JSONField(name = "GradeId")
    private int gradeId;

    public GetUserSessionRequest() {
    }

    public GetUserSessionRequest(String key) {
        super(key);
    }
}
