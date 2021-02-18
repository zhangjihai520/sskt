package com.ry.sskt.model.examset.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.request.RequestPageBase;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 获取作业统计列表 请求参数
 */
@Data
@Accessors(chain = true)
public class GetExamSetStatistListApiRequest extends RequestPageBase {
    /// <summary>
    /// 作业加密Id
    /// </summary>
    @JSONField(name = "ExamSetId")
    private String examSetId;

    /// <summary>
    /// 学生名称 非必填
    /// </summary>
    @JSONField(name = "StudentName")
    private String studentName;

}
