package com.ry.sskt.model.student.response;

import com.ry.sskt.model.student.entity.SubjectInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 【消息】获取未读消息数量Response
 */
@Data
@Accessors(chain = true)
public class GetUnReadMessageNumberResponse {

    /// <summary>
    /// 总条数
    /// </summary>
    private int UnReadMessageNumber;
}
