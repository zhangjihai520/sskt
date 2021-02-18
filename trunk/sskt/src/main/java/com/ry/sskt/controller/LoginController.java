package com.ry.sskt.controller;


import com.alibaba.fastjson.JSON;
import com.ry.sskt.controller.action.ILoginActionService;
import com.ry.sskt.core.annotation.AllowUserType;
import com.ry.sskt.core.annotation.AlwaysAccessible;
import com.ry.sskt.model.common.constant.UserTypeEnum;
import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.user.request.LoginApiRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Login")
@AllowUserType(userType = UserTypeEnum.All)
@AlwaysAccessible
@Api(value = "登录模块接口", tags = {"登录模块接口"})
@Slf4j
public class LoginController {
    @Autowired
    ILoginActionService loginActionService;

    @ApiOperation("登录")
    @ApiImplicitParam(name = "request", dataType = "LoginApiRequest", paramType = "body", required = true, value = "登录入参实体")
    @PostMapping("/LoginWithPassword")
    public ResponseBase LoginWithPassword(@RequestBody LoginApiRequest request) {
        try {
            return loginActionService.LoginWithPassword(request);
        } catch (Exception ex) {
            log.error(String.format("登录报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }
}
