package com.ry.sskt.model.student.entity.view;

import com.ry.sskt.model.common.entity.User;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/// <summary>
/// 课程学生Count
/// </summary>
@Data
@Accessors(chain = true)
public class GetStudentListView extends User {
    /// <summary>
    /// 是否有评价，仅获取上课学生、且课堂加密类型有
    /// </summary>
    private boolean HasEvaluateRecord;
}
