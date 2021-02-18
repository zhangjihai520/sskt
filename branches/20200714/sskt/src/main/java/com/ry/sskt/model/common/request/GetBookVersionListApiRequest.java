package com.ry.sskt.model.common.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 根据课程ID获取课本列表
 */
@Data
@Accessors(chain = true)
public class GetBookVersionListApiRequest extends RequestBase {
    @JSONField(name = "CourseId")
    @NotNull(message = "courseId不能为空")
    private int courseId;
}
