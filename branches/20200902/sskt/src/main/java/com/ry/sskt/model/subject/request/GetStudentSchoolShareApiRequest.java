package com.ry.sskt.model.subject.request;

import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 【学情监控】获取学生学校占数比 输入参数
 */
@Data
@Accessors(chain = true)
public class GetStudentSchoolShareApiRequest extends RequestBase {
    /// <summary>
    /// 类型 1.学校 2.年级, <see cref="GetStudentSchoolShareEnum"/>
    /// </summary>
    private int TypeEnum;

    /// <summary>
    /// 取前几最大值，剩下为其他，传0为全部
    /// </summary>
    private int PageSize;
}
