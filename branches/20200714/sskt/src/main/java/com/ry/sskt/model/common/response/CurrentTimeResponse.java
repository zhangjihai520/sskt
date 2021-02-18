package com.ry.sskt.model.common.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.entity.BookVersionInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 获取服务端时间 输出参数
 */
@Data
@Accessors(chain = true)
public class CurrentTimeResponse {

    /// <summary>
    /// 公益课上课时间最大日期
    /// </summary>

    @JSONField(name = "MaxDate")
    private String maxDate;

    /// <summary>
    /// 公益课上课时间最小日期
    /// </summary>
    @JSONField(name = "MinDate")
    public String minDate;

    /// <summary>
    /// 当前日期
    /// </summary>
    @JSONField(name = "CurrentDate")
    public String currentDate;

    /// <summary>
    /// 当前时间
    /// </summary>
    @JSONField(name = "CurrentTime")
    public String currentTime;

    /// <summary>
    /// 与输入时间的间隔秒数，暂用于已播放时间
    /// </summary>
    @JSONField(name = "SpaceOfTime")
    public Integer spaceOfTime;

    public CurrentTimeResponse() {
    }

    public CurrentTimeResponse(String maxDate, String minDate, String currentDate, String currentTime, Integer spaceOfTime) {
        this.maxDate = maxDate;
        this.minDate = minDate;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.spaceOfTime = spaceOfTime;
    }
}
