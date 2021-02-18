package com.ry.sskt.model.subject.request;

import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 获取我的学生课程列表 输入参数
 */
@Data
@Accessors(chain = true)
public class GetStudentSubjectListH5ApiRequest extends RequestBase {
    /// <summary>
    /// 学科Id,0为全部学科
    /// </summary>
    private int CourseId;

}
