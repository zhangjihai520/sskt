package com.ry.sskt.model.subject.request;

import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 创建视频直播（硬件是创建会议）请求参数
 */
@Data
@Accessors(chain = true)
public class GetSubjectRoomListRequest extends RequestBase {
    /// <summary>
    /// 课程加密ID
    /// </summary>
    private String SubjectId;
}
