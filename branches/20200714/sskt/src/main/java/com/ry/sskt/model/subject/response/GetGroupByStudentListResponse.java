package com.ry.sskt.model.subject.response;

import com.ry.sskt.model.subject.entity.StudentListInfo;
import com.ry.sskt.model.video.entity.view.DeviceView;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 学生分组输出对象
 */
@Data
@Accessors(chain = true)
public class GetGroupByStudentListResponse {
    /// <summary>
    /// 总数
    /// </summary>
    private int TotalCount;

    /// <summary>
    /// 学生列表
    /// </summary>
    private List<StudentListInfo> Students;

    public GetGroupByStudentListResponse() {
        Students = new LinkedList<>();
    }
}
