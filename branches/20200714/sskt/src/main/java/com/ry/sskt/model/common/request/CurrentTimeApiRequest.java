package com.ry.sskt.model.common.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 获取公益课直播时间差
 */
@Data
@Accessors(chain = true)
public class CurrentTimeApiRequest extends RequestBase {
    @JSONField(name = "InputDate")
    private LocalDateTime inputDate;
    @JSONField(name = "BeginTime")
    private LocalDateTime beginTime;

}
