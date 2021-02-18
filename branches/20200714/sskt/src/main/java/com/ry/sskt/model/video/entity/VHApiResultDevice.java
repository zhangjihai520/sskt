package com.ry.sskt.model.video.entity;

import com.ry.sskt.model.video.entity.view.DeviceView;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 硬件返回的视频列表对象
 */
@Data
@Accessors(chain = true)
public class VHApiResultDevice extends  VHApiResultObj{
    /// <summary>
    /// 返回的课堂Id
    /// </summary>
    private List<DeviceView> deviceList;
}
