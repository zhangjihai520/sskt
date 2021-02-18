package com.ry.sskt.model.video.entity;

import com.ry.sskt.model.video.entity.view.DeviceView;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 根据课堂ID获取录播地址接口返回对象
 */
@Data
@Accessors(chain = true)
public class QueryConfRecordResult extends  VHApiResultObj{
    /// <summary>
    /// 课堂ID （软云提供）
    /// </summary>
    private String config;

    /// <summary>
    /// 课堂名称
    /// </summary>
    private String confsubject;

    /// <summary>
    /// 课堂录像地址及文件名列表
    /// </summary>
    private List<VHVideoView> videos;
}
