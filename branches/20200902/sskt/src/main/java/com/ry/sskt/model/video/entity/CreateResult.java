package com.ry.sskt.model.video.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 课程附件
 */
@Data
@Accessors(chain = true)
public class CreateResult extends  VHApiResultObj{
    /// <summary>
    /// 返回的课堂Id
    /// </summary>
    @JSONField(name="sMeetingId")
    private String sMeetingId;
}
