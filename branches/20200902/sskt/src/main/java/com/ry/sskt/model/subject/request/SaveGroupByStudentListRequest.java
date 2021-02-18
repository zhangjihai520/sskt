package com.ry.sskt.model.subject.request;

import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.subject.entity.StudentRoom;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 创建视频直播（硬件是创建会议）请求参数
 */
@Data
@Accessors(chain = true)
public class SaveGroupByStudentListRequest extends RequestBase {
    /// <summary>
    /// 课程加密ID
    /// </summary>
    private String SubjectId;

    /// <summary>
    /// 教室加密ID
    /// </summary>
    private String SubjectRoomId;

    /// <summary>
    /// 学生集合
    /// </summary>
    private List<StudentRoom> Students;
}
