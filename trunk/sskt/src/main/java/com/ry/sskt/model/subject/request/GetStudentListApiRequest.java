package com.ry.sskt.model.subject.request;

import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.common.request.RequestPageBase;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 【课程详情】获取课堂学生列表 输入参数
 */
@Data
@Accessors(chain = true)
public class GetStudentListApiRequest extends RequestPageBase {
    /// <summary>
    /// 0=报名,1=上课
    /// </summary>
    private int TypeEnum;

    /// <summary>
    /// 课程教室加密ID 同课程ID二选一必传
    /// </summary>
    private String SubjectRoomId;

    /// <summary>
    /// 课程加密ID 同教室ID二选一必传
    /// </summary>
    private String SubjectId;

    /// <summary>
    /// 学生名称 搜索用，可不传
    /// </summary>
    private String StudentName;
}
