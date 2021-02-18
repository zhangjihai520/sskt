package com.ry.sskt.model.student.entity.view;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/// <summary>
/// 获取用户对主讲老师的评价 输出对象
/// </summary>
@Data
@Accessors(chain = true)
public class GetSubjectEvaluateView {
    /// <summary>
    /// 用户名称
    /// </summary>
    private String UserTrueName;

    /// <summary>
    /// 用户头像
    /// </summary>
    private String UserFace;

    /// <summary>
    /// 评价内容
    /// </summary>
    private String EvaluateComment;

    /// <summary>
    /// 评价星级
    /// </summary>
    private int EvaluateLevel;
}
