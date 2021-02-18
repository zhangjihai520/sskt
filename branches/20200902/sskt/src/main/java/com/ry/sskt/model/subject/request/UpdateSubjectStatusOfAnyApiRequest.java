package com.ry.sskt.model.subject.request;

import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.video.entity.VHVideoView;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 给硬件用的修改课程状态
 */
@Data
@Accessors(chain = true)
public class UpdateSubjectStatusOfAnyApiRequest extends RequestBase {
    /// <summary>
    /// 课程加密Id,可能需要2次处理。比如SUB_123,WEI_123
    /// </summary>
    private String sConfid;

    /// <summary>
    /// 操作状态 1上课中  2已经结课
    /// </summary>
    private int StatusFlag;

    /// <summary>
    /// 视频地址JSON字符串
    /// </summary>
    private List<VHVideoView> Videos;
}
