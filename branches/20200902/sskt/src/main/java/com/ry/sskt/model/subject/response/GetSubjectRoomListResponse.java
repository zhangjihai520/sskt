package com.ry.sskt.model.subject.response;

import com.ry.sskt.model.subject.entity.GetCurrentSubjectNameDetail;
import com.ry.sskt.model.subject.entity.SubjectRoomInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 教室分组
 */
@Data
@Accessors(chain = true)
public class GetSubjectRoomListResponse {
    /// <summary>
    /// 教室列表
    /// </summary>
    private List<SubjectRoomInfo> SubjectRooms;

    public GetSubjectRoomListResponse() {
        SubjectRooms = new LinkedList<>();
    }
}
