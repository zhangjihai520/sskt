package com.ry.sskt.model.common.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class VideoPlayInfo {
    /// <summary>
    /// 课程Id
    /// </summary>
    private Integer subjectId;

    /// <summary>
    /// 播放地址
    /// </summary>
    private String videoUrl;

    /// <summary>
    /// 播放时间
    /// </summary>
    private Date playDateTime;

    /// <summary>
    /// 播放者Id
    /// </summary>
    private Integer playerId;

    /// <summary>
    /// 0为普通播放 1为手机播放
    /// </summary>
    private Integer playTypeId;

    public VideoPlayInfo() {
        this.videoUrl = "";
    }

    public VideoPlayInfo(Integer subjectId, String videoUrl, Date playDateTime, Integer playerId, Integer playTypeId) {
        this.subjectId = subjectId;
        this.videoUrl = videoUrl;
        this.playDateTime = playDateTime;
        this.playerId = playerId;
        this.playTypeId = playTypeId;
    }
}
