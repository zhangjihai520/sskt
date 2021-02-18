package com.ry.sskt.model.examset.view;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 获取作业统计列表
 */
@Data
@Accessors(chain = true)
public class GetStatistListView {
    /// <summary>
    /// 用户ID
    /// </summary>
    @JSONField(name = "UserId")
    private int userId;

    /// <summary>
    /// 学生姓名
    /// </summary>
    @JSONField(name = "UserTrueName")
    private String userTrueName;

    /// <summary>
    /// 学生得分
    /// </summary>
    @JSONField(name = "Score")
    private BigDecimal score;

    /// <summary>
    /// 创建时间
    /// </summary>
    @JSONField(name = "CreateDateTime")
    private LocalDateTime createDateTime;
}
