package com.ry.sskt.model.subject.request;

import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *【课堂管理】获取课程详情 输入参数
 */
@Data
@Accessors(chain = true)
public class GetSubjectInfoApiRequest extends RequestBase {
    /// <summary>
    /// 课程加密Id
    /// </summary>
    private String SubjectId;

}
