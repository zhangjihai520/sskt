package com.ry.sskt.model.subject.request;

import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 搜索课程 输入参数
 */
@Data
@Accessors(chain = true)
public class SearchStudentSubjectApiRequest extends RequestBase {
    /// <summary>
    /// 关键字 课程名称或老师名称
    /// </summary>
    private String Key;
}
