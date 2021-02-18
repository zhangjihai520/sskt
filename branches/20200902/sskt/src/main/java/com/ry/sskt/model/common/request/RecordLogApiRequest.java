package com.ry.sskt.model.common.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 获取公益课直播时间差
 */
@Data
@Accessors(chain = true)
public class RecordLogApiRequest extends RequestBase {
    @JSONField(name = "ObjectKey")
    private String objectKey;

    @JSONField(name = "ObjectType")
    private int objectType;

    //0视频播放
    @JSONField(name = "RecordLogType")
    private int recordLogType;

}
