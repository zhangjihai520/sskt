package com.ry.sskt.model.subject.request;

import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 当前用户 输入参数
 */
@Data
@Accessors(chain = true)
public class RegistSubjectApiRequest extends RequestBase {
    /// <summary>
    /// 课程Id
    /// </summary>
    private String SubjectId;
}
