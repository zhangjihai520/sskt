package com.ry.sskt.model.subject.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 饼图详情
 */
@Data
@Accessors(chain = true)
public class PieDataDetail {
    /// <summary>
    /// 名称
    /// </summary>
    private String Name;
    /// <summary>
    /// 值
    /// </summary>
    private int Value;
}
