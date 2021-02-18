package com.ry.sskt.model.subject.request;

import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.common.request.RequestPageBase;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 获取学生分组列表
 */
@Data
@Accessors(chain = true)
public class GetGroupByStudentListRequest extends RequestPageBase {
    /// <summary>
    /// 课程教室加密ID
    /// </summary>
    private String SubjectRoomId;

    /// <summary>
    /// 课程加密ID
    /// </summary>
    private String SubjectId;
}
