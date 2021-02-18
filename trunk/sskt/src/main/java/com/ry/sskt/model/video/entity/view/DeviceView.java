package com.ry.sskt.model.video.entity.view;

import com.ry.sskt.model.common.constant.DeviceEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 视频房间信息
 */
@Data
@Accessors(chain = true)
public class DeviceView {
    /// <summary>
    /// ID
    /// </summary>
    private String diviceId;

    /// <summary>
    /// 名称
    /// </summary>
    private String diviceName;

    /// <summary>
    /// 设备状态  1离线/2在线/3在会
    /// </summary>
    private DeviceEnum state;
}
