package com.ry.sskt.controller.action.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ry.sskt.controller.action.IRtmpNotifyActionService;
import com.ry.sskt.core.utils.RedisUtil;
import com.ry.sskt.core.utils.UrlUtil;
import com.ry.sskt.model.common.constant.CommonConst;
import com.ry.sskt.model.common.entity.cache.AuthCodeCache;
import com.ry.sskt.model.video.entity.VHVideoView;
import com.ry.sskt.service.ISubjectService;
import com.ry.sskt.service.IUserLoginInfoService;
import com.ry.sskt.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 消息 服务实现类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
@Slf4j
@Service
public class RtmpNotifyActionService implements IRtmpNotifyActionService {
    @Autowired
    IUserService userService;

    @Autowired
    ISubjectService subjectService;


    @Autowired
    IUserLoginInfoService userLoginInfoService;

    @Override
    public boolean pushAuth(Map<String, String> params) throws Exception {
        var act = params.get("act");
        String auth = params.get("auth");
        if (StringUtils.isNotBlank(auth)) {
            String authStr = UrlUtil.decrypt(auth, String.class);
            String[] auths = authStr.split("/");
            var teacherId = Integer.parseInt(auths[0]);
            var subjectId = Integer.parseInt(auths[1]);
            var authCode = auths[2];
            var domain = params.get("domain");
            var name = params.get("name");
            var app = params.get("app");
            if ("done".equals(act)) {
                log.info(String.format("结束推流，参数%s", JSON.toJSONString(params)));
                List<VHVideoView> pptlist = new ArrayList<>();
                pptlist.add(new VHVideoView().setVideostype(1).setFilename(name).setUrl(String.format("https://%s:%s/%s.mp4", CommonConst.DOMAIN, CommonConst.PINGOS_RECORD_PORT_8899, name)));
                subjectService.closeSubject(subjectId, JSONArray.toJSONString(pptlist));
                //更新缓存
                subjectService.getByKey(subjectId, false);
                return true;
            } else {
                log.info(String.format("开始推流，参数%s", JSON.toJSONString(params)));
                if (null != auths && auths.length == 3) {
                    AuthCodeCache authCodeCache = new AuthCodeCache().setTeacherId(teacherId).setSubjectId(subjectId);
                    var cache = RedisUtil.getObj(authCodeCache.getKey(), AuthCodeCache.class);
                    if (cache != null && cache.getCode().equals(authCode)) {
                        List<VHVideoView> pptlist = new ArrayList<>();
                        //pptlist.add(new VHVideoView().setVideostype(1).setFilename(name).setUrl(String.format("rtmp://%s:%s/%s/%s", domain, CommonConst.PINGOS_PULL_1938, app, name)));
                        //pptlist.add(new VHVideoView().setVideostype(1).setFilename(name).setUrl(String.format("https://%s:%s/%s/%s%s", domain, CommonConst.PINGOS_HLS_PROT_8999, CommonConst.HLS_APP, name, CommonConst.PINGOS_HLS_STUFF)));
                        pptlist.add(new VHVideoView().setVideostype(1).setFilename(name).setUrl(String.format("https://%s:%s/%s/%s%s", domain, CommonConst.PINGOS_HLS_PROT_8999, "hls", name, CommonConst.PINGOS_HLS_STUFF)));
                        var realBeginTimeZone = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
                        subjectService.attendSubject(subjectId, realBeginTimeZone,JSONArray.toJSONString(pptlist));
                        //更新缓存
                        subjectService.getByKey(subjectId, false);
                        return true;
                    }
                }
            }
        }
        return false;
    }


}
