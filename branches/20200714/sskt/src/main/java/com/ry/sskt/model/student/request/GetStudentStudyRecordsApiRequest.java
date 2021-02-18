package com.ry.sskt.model.student.request;

import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.common.request.RequestPageBase;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 【学生端】向老师提问Request
 */
@Data
@Accessors(chain = true)
public class GetStudentStudyRecordsApiRequest extends RequestPageBase {

    /// <summary>
    /// 评星
    /// </summary>
    private String StudentId;


}
