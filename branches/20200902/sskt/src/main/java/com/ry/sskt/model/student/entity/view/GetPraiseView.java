package com.ry.sskt.model.student.entity.view;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/// <summary>
/// 课程评价信息
/// </summary>
@Data
@Accessors(chain = true)
public class GetPraiseView {
    /// <summary>
    /// 课程Id
    /// </summary>
    private int SubjectId;

    /// <summary>
    /// 评价总分
    /// </summary>
    private BigDecimal TheSum;

    /// <summary>
    /// 评价总数
    /// </summary>
    private int TheCount;
}
