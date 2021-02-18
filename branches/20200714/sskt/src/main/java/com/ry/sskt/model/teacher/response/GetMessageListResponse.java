package com.ry.sskt.model.teacher.response;

import com.ry.sskt.model.teacher.entity.MessageInfo;
import com.ry.sskt.model.teacher.entity.SubjectInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/// <summary>
/// 【消息】教师、学生消息列表Response
/// </summary>
@Data
@Accessors(chain = true)
public class GetMessageListResponse {
    /// <summary>
    /// 总数
    /// </summary>
    private int TotalCount;

    /// <summary>
    /// 课程列表
    /// </summary>
    private List<MessageInfo> MessageList;

    public GetMessageListResponse() {
        MessageList = new LinkedList<>();
    }

}
