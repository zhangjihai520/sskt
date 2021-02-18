package com.ry.sskt.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ry.sskt.controller.action.ILoginActionService;
import com.ry.sskt.controller.action.IRtmpNotifyActionService;
import com.ry.sskt.controller.action.ISubjectActionService;
import com.ry.sskt.core.utils.UrlUtil;
import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.common.response.RtmpResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping(value = "/rtmp")
public class RtmpNotifyController extends AbstractController {

    @Autowired
    IRtmpNotifyActionService rtmpNotifyActionService;
    /**
     * 推流通知
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping(value = "/publish")
    public String publish(HttpServletRequest request, HttpServletResponse response) {
        var params = getAllRequestParam(request);
        try {
            if (rtmpNotifyActionService.pushAuth(params)) {
                response.setStatus(HttpServletResponse.SC_OK);
                return RtmpResponse.GetSuccessResponse();
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            log.error(String.format("推流异常，参数%s", JSON.toJSONString(params)), e);
            return RtmpResponse.GetErrorResponse();
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        log.error(String.format("推流异常，参数%s", JSON.toJSONString(params)));
        return RtmpResponse.GetValidateErrorResponse();
    }


    private String debug(HttpServletRequest request, String action) {
        String str = action + ": " + request.getRequestURI() + " " + request.getQueryString();
        System.out.println(str);
        return str;
    }
}