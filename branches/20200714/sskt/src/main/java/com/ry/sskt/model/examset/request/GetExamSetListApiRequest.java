package com.ry.sskt.model.examset.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.common.request.RequestPageBase;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 【课堂作业】获取课堂作业列表
 */
@Data
@Accessors(chain = true)
public class GetExamSetListApiRequest extends RequestPageBase {
    /// <summary>
    /// 课程加密Id
    /// </summary>
    @JSONField(name = "SubjectId")
    private String subjectId;

    /// <summary>
    /// 学科Id 非必填
    /// </summary>
    @JSONField(name = "CourseId")
    private int courseId;

    /// <summary>
    /// 作业名称 非必填
    /// </summary>
    @JSONField(name = "ExamSetName")
    private String examSetName;

    /// <summary>
    /// 状态，0删除，1待上架，2已上架  非必填
    /// </summary>
    @JSONField(name = "StatusFlag")
    private int statusFlag;

}
