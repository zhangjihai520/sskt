package com.ry.sskt.model.subject.request;

import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 当前用户 输入参数
 */
@Data
@Accessors(chain = true)
public class GetTeacherSubjectDataH5ApiRequest extends RequestBase {
    /// <summary>
    /// 排序方式： 0 = 上课日期时间排序（倒序）， 1 = 上课人数排序（倒序）， 2 = 好评度排序（倒序）
    /// </summary>
    private int SortId;
}