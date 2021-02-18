package com.ry.sskt.controller;


import com.alibaba.fastjson.JSON;
import com.ry.sskt.controller.action.IDiscussActionService;
import com.ry.sskt.core.annotation.AllowUserType;
import com.ry.sskt.model.common.constant.SendFileResultEnum;
import com.ry.sskt.model.common.constant.UserTypeEnum;
import com.ry.sskt.model.common.entity.User;
import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.discuss.request.MessageApiRequest;
import com.ry.sskt.model.discuss.request.SendFileApiRequest;
import com.ry.sskt.model.discuss.request.SendMessageApiRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/Discuss")
@AllowUserType(userType = UserTypeEnum.All)
@Api(value = "互动模块接口", tags = {"互动模块接口"})
@Slf4j
public class DiscussController extends AbstractController {
    @Autowired
    IDiscussActionService discussActionService;

    @ApiOperation("发送讨论区消息")
    @ApiImplicitParam(name = "request", dataType = "SendMessageApiRequest", paramType = "body", required = true, value = "发送讨论区消息入参实体")
    @PostMapping("/SendMessage")
    public ResponseBase SendMessage(@RequestBody SendMessageApiRequest request) {
        try {
            return discussActionService.SendMessage(request);
        } catch (Exception ex) {
            log.error(String.format("发送讨论区消息报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("发送讨论区文件")
    @ApiImplicitParam(name = "request", dataType = "SendFileApiRequest", paramType = "body", required = true, value = "发送讨论区文件入参实体")
    @PostMapping("/SendFile")
    public ResponseBase SendFile(HttpServletRequest httprequest, SendFileApiRequest request) {
        try {
            User user = getUserInfo(httprequest);
            if (user != null) {
                request.setUserId(user.getUserId());
                request.setUserTypeId(user.getUserTypeId());
                request.setCurrentUserTrueName(user.getUserTrueName());
                request.setUserFace(user.getUserFace());
            }
            return discussActionService.SendFile(request);
        } catch (Exception ex) {
            log.error(String.format("发送讨论区文件报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetSuccessResponse(SendFileResultEnum.Fail.getCode());
        }
    }

    @ApiOperation("获取讨论区消息")
    @ApiImplicitParam(name = "request", dataType = "MessageApiRequest", paramType = "body", required = true, value = "获取讨论区消息入参实体")
    @PostMapping("/Message")
    public ResponseBase Message(@RequestBody MessageApiRequest request) {
        try {
            return discussActionService.Message(request);
        } catch (Exception ex) {
            log.error(String.format("获取讨论区消息报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }
}
