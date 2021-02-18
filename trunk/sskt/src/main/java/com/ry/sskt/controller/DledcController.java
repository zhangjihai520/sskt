package com.ry.sskt.controller;

import com.alibaba.fastjson.JSON;
import com.ry.sskt.controller.action.IDledcActionService;
import com.ry.sskt.core.annotation.AlwaysAccessible;
import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.dledc.request.DledcConfigApiRequest;
import com.ry.sskt.model.dledc.request.DledcLoginApiRequest;
import com.ry.sskt.model.dledc.request.DledcMobileLoginApiRequest;
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
@RequestMapping("/Dledc")
@AlwaysAccessible
@Api(value = "南昌市教育资源云平台模块接口", tags = {"南昌市教育资源云平台模块接口"})
@Slf4j
public class DledcController {
    @Autowired
    IDledcActionService dledcActionService;

    @ApiOperation("南昌市教育资源云平台登录")
    @ApiImplicitParam(name = "request", dataType = "DledcLoginApiRequest", paramType = "body", required = true, value = "南昌市教育资源云平台登录入参实体")
    @PostMapping("/DledcLogin")
    public ResponseBase DledcLogin(@RequestBody DledcLoginApiRequest request) {
        try {
            return dledcActionService.DledcLogin(request);
        } catch (Exception ex) {
            log.error(String.format("南昌市教育资源云平台登录报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }


    @ApiOperation("南昌市教育资源云平台移动端登录")
    @ApiImplicitParam(name = "request", dataType = "DledcMobileLoginApiRequest", paramType = "body", required = true, value = "南昌市教育资源云平台移动端登录入参实体")
    @PostMapping("/DledcMobileLogin")
    public ResponseBase DledcMobileLogin(@RequestBody DledcMobileLoginApiRequest request) {
        try {
            return dledcActionService.DledcMobileLogin(request);
        } catch (Exception ex) {
            log.error(String.format("南昌市教育资源云平台移动端登录报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }


    @ApiOperation("获取南昌市教育资源云平台对接参数")
    @ApiImplicitParam(name = "request", dataType = "DledcConfigApiRequest", paramType = "body", required = true, value = "获取南昌市教育资源云平台对接参数入参实体")
    @PostMapping("/DledcConfig")
    public ResponseBase DledcConfig(@RequestBody DledcConfigApiRequest request) {
        try {
            return dledcActionService.DledcConfig(request);
        } catch (Exception ex) {
            log.error(String.format("获取南昌市教育资源云平台对接参数报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }
}
