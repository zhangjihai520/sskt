package com.ry.sskt.model.common.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 根据教材获取课本列表
 */
@Data
@Accessors(chain = true)
public class GetCourseMappingListApiRequest extends RequestBase {
    @JSONField(name = "BookVersionId")
    @NotNull(message = "bookVersionId不能为空")
    private int bookVersionId;
}
