package com.ry.sskt.model.common.entity.view;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserLoginTotalView {
    /// <summary>
    /// 学生年级ID
    /// </summary>
    @JSONField(name = "GradeId")
    private int gradeId;

    /// <summary>
    /// 学生年级总数
    /// </summary>
    @JSONField(name = "GradeCount")
    private int gradeCount;
}
