package com.ry.sskt.model.subject.response;

import com.ry.sskt.model.subject.entity.GetCurrentSubjectNameDetail;
import com.ry.sskt.model.video.entity.view.DeviceView;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 【获取硬件的视频房间列表
 */
@Data
@Accessors(chain = true)
public class GetDeviceListResponse {
    private List<DeviceView> deviceList;
}
