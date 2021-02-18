package com.ry.sskt.model.weike.response;

import com.ry.sskt.core.utils.UrlUtil;
import com.ry.sskt.model.subject.entity.WeiKe;
import com.ry.sskt.model.video.entity.VHVideoView;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 播放微课
 */
@Data
@Accessors(chain = true)
public class GetWeiKeVideoResponse {
    /// <summary>
    /// 微课的Id
    /// </summary>
    public String WeiKeId;

    /// <summary>
    /// 微课名称
    /// </summary>
    public String WeiKeName;

    /// <summary>
    /// 缩略图
    /// </summary>
    public String OverViewUrl;

    /// <summary>
    /// 文件路径，json格式存储["",""] 这样的格式
    /// </summary>
    public List<String> VideoUrl;

    /// <summary>
    /// 播放地址，json格式存储["",""] 这样的格式
    /// </summary>
    public List<String> PPTVideoUrl;


    /// <summary>
    /// 创建人名称
    /// </summary>
    public String CreateUserName;

    public GetWeiKeVideoResponse() {
        VideoUrl = new LinkedList<String>();
    }
}
