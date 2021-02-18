package com.ry.sskt.service;

import com.ry.sskt.model.common.entity.VideoPlayInfo;
import com.ry.sskt.model.common.entity.VideoPlayTotalView;
import com.ry.sskt.model.subject.entity.view.SubjectListByFilterView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 表Videoplayinfo的接口
 */
public interface IVideoPlayService {

    /// <summary>
    /// 新增或者更新一条Videoplayinfo表数据
    /// </summary>
    /// <param name="dataModel">实体类</param>
    /// <returns>新增或更新后的主键ID</returns>
    void save(VideoPlayInfo videoPlayInfo);

    /// <summary>
    /// 获取某天课程播放次数
    /// </summary>
    /// <param name="dateTime"></param>
    List<VideoPlayTotalView> getVideoPlayTotal(LocalDate date);
}
