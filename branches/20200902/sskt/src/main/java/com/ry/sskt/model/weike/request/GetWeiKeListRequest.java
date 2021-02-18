package com.ry.sskt.model.weike.request;

import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.common.request.RequestPageBase;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 读取微课列表
 */
@Data
@Accessors(chain = true)
public class GetWeiKeListRequest extends RequestPageBase {
    /// <summary>
    /// 题目状态枚举,传0为全部
    /// </summary>
    private int StatusFlag;

    /// <summary>
    /// 课程年级,可以穿0
    /// </summary>
    private int GradeId;

    /// <summary>
    /// 学科,可以传0
    /// </summary>
    private int CourseId;

    /// <summary>
    /// 创建时间日期
    /// </summary>
    private LocalDateTime BeginTime;

    /// <summary>
    /// 创建时间日期
    /// </summary>
    private LocalDateTime EndTime;

    /// <summary>
    /// 搜索关键字
    /// </summary>
    private String Keyword;

    /// <summary>
    /// 用户角色
    /// </summary>
    private int UserRole;
}
