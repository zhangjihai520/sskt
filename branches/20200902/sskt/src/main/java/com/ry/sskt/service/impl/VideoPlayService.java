package com.ry.sskt.service.impl;

import com.ry.sskt.mapper.VideoPlayMapper;
import com.ry.sskt.model.common.entity.VideoPlayInfo;
import com.ry.sskt.model.common.entity.VideoPlayTotalView;
import com.ry.sskt.service.IVideoPlayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class VideoPlayService implements IVideoPlayService {

    @Autowired
    VideoPlayMapper videoPlayMapper;

    @Override
    public void save(VideoPlayInfo videoPlayInfo) {
        videoPlayMapper.save(videoPlayInfo);
    }

    @Override
    public List<VideoPlayTotalView> getVideoPlayTotal(LocalDate date) {
        Map<String, Object> map = new HashMap();
        map.put("loginDateMin", date);
        map.put("loginDateMax", date.plusDays(1));
        return videoPlayMapper.getVideoPlayTotal(map);
    }
}
