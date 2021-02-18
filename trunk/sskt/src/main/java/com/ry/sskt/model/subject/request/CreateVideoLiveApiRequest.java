package com.ry.sskt.model.subject.request;

import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.video.entity.VHVideoView;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 创建视频直播（硬件是创建会议）请求参数
 */
@Data
@Accessors(chain = true)
public class CreateVideoLiveApiRequest extends RequestBase {
    /// <summary>
    /// 房间ID（选择的设备ID）
    /// </summary>
    private String diviceId;

    /// <summary>
    /// 课程加密Id或者微课加密Id
    /// </summary>
    private String Id;

    /// <summary>
    /// 1课程 2微课
    /// </summary>
    private int IsSubject;
}
