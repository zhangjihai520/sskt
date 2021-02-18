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
public class SCPExamDataRequest extends OpenApiRequestBase {
     /// <summary>
    /// 学生Id
    /// </summary>
    @JSONField(name = "SCPStudentId")
    private String scpStudentId;

    /// <summary>
    /// 评试卷Id
    /// </summary>
    @JSONField(name = "ExaminationId")
    private int examinationId;

    /// <summary>
    /// 年级Id
    /// </summary>
    @JSONField(name = "GradeId")
    private int gradeId;

    /// <summary>
    /// 总机构编码，可以没有
    /// </summary>
    @JSONField(name = "SCPOrganizationCode")
    private String scpOrganizationCode;

    /// <summary>
    /// 做题结果
    /// </summary>
    @JSONField(name = "StudentQuestions")
    private List<StudentQuestion> studentQuestions;

    public SCPExamDataRequest() {
    }

    public SCPExamDataRequest(String key) {
        super(key);
    }
}
