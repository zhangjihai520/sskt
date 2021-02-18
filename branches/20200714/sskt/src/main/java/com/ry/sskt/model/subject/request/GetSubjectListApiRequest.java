package com.ry.sskt.model.subject.request;

import com.ry.sskt.core.utils.DateUtil;
import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.common.request.RequestPageBase;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 【课表管理】获取课程列表
 */
@Data
@Accessors(chain = true)
public class GetSubjectListApiRequest extends RequestPageBase {

    /// <summary>
    /// 所属学科枚举值
    /// </summary>
    private int CourseId;

    /// <summary>
    /// 年级枚举值
    /// </summary>
    private int GradeId;

    /// <summary>
    /// 课程状态枚举值,0为全部
    /// </summary>
    private int StatusFlag;

    /// <summary>
    /// 关键字查询
    /// </summary>
    private String Key;

    /// <summary>
    /// 创建开始时间
    /// </summary>
    private LocalDateTime BeginTime= DateUtil.LocalDateTimeMIN();

    /// <summary>
    /// 创建结束时间
    /// </summary>
    private LocalDateTime EndTime= DateUtil.LocalDateTimeMIN();

    /// <summary>
    /// 是否仅获取本身课程数据
    /// </summary>
    private boolean IsOnlySelf;

    /// <summary>
    /// 用户角色
    /// </summary>
    private int UserRole;

    /// <summary>
    /// 用户角色
    /// </summary>
    private int SubjectGenreId;

    /// <summary>
    /// 是否分组
    /// </summary>
    private boolean IsGroupByDate;

    /// <summary>
    /// 创建开始时间
    /// </summary>
    private LocalDateTime BeginTimeMax= DateUtil.LocalDateTimeMIN();

    /// <summary>
    /// 创建结束时间
    /// </summary>
    private LocalDateTime BeginTimeMin= DateUtil.LocalDateTimeMIN();

    /// <summary>
    /// 排序字段
    /// </summary>
    private int OrderId;

}
