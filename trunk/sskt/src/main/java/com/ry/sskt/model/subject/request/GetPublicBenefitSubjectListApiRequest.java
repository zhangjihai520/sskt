package com.ry.sskt.model.subject.request;

import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 获取公益课列表 输入参数
 */
@Data
@Accessors(chain = true)
public class GetPublicBenefitSubjectListApiRequest extends RequestBase {
    /// <summary>
    /// 当前日期
    /// </summary>
    private LocalDateTime Date;

    /// <summary>
    ///
    /// </summary>
    private int GradeId;

    /// <summary>
    ///
    /// </summary>
    private int CourseId;

    /// <summary>
    ///
    /// </summary>

    private boolean IsWeb;

    /// <summary>
    /// 当前用户角色 UserRoleEnum
    /// </summary>
    private int UserUserRoleEnumRole;
}
