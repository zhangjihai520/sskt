package com.ry.sskt.model.subject.request;

import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 当前用户 输入参数
 */
@Data
@Accessors(chain = true)
public class DeleteExamSetOrFileApiRequest extends RequestBase {
    /// <summary>
    /// 作业Id列表
    /// </summary>
    private List<String> ExamSetIdList;

    /// <summary>
    /// 题集附件Id列表
    /// </summary>
    private List<String> SubjectFileIdList;
}
