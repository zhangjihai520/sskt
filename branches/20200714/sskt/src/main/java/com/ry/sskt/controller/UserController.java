package com.ry.sskt.controller;


import com.alibaba.fastjson.JSON;
import com.ry.sskt.controller.action.IUserActionService;
import com.ry.sskt.core.annotation.AllowUserType;
import com.ry.sskt.model.common.constant.UserTypeEnum;
import com.ry.sskt.model.common.entity.User;
import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.subject.request.CurrentUserApiRequest;
import com.ry.sskt.model.user.request.SignReadMessageApiRequest;
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
@RequestMapping("/User")
@AllowUserType(userType = UserTypeEnum.All)
@Api(value = "用户相关接口", tags = {"用户相关接口"})
@Slf4j
public class UserController extends AbstractController {
    @Autowired
    IUserActionService userActionService;

    @ApiOperation("【用户信息】获取用户基本信息")
    @ApiImplicitParam(name = "request", dataType = "RequestBase", paramType = "body", required = true, value = "【用户信息】获取用户基本信息入参实体")
    @PostMapping("/GetUserBaseInfo")
    public ResponseBase GetUserBaseInfo(HttpServletRequest httpRequest, @RequestBody RequestBase request) {
        try {
            User user = getUserInfo(httpRequest);
            if (user == null) {
                throw new NullPointerException("用户信息为空");
            }
            return userActionService.GetUserBaseInfo(user);
        } catch (Exception ex) {
            log.error(String.format("【用户信息】获取用户基本信息报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("获取个人信息")
    @ApiImplicitParam(name = "request", dataType = "RequestBase", paramType = "body", required = true, value = "获取个人信息入参实体")
    @PostMapping("/GetUserInfo")
    public ResponseBase GetUserInfo(HttpServletRequest httpRequest, @RequestBody CurrentUserApiRequest request) {
        try {
            User user = getUserInfo(httpRequest);
            if (user == null) {
                throw new NullPointerException("用户信息为空");
            }
            return userActionService.GetUserInfo(user);
        } catch (Exception ex) {
            log.error(String.format("获取个人信息报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【消息】标记已读消息")
    @ApiImplicitParam(name = "request", dataType = "SignReadMessageApiRequest", paramType = "body", required = true, value = "【消息】标记已读消息入参实体")
    @PostMapping("/SignReadMessage")
    public ResponseBase SignReadMessage(@RequestBody SignReadMessageApiRequest request) {
        try {
            return userActionService.SignReadMessage(request);
        } catch (Exception ex) {
            log.error(String.format("【消息】标记已读消息报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }
}
