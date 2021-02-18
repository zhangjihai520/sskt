package com.ry.sskt.model.common.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 根据课本获取章节列表
 */
@Data
@Accessors(chain = true)
public class GetChapterSectionListApiRequest extends RequestBase {
    @JSONField(name = "CourseMappingId")
    @NotNull(message = "courseMappingId不能为空")
    private Integer courseMappingId;
}
