package com.ry.sskt.controller;


import com.alibaba.fastjson.JSON;
import com.ry.sskt.controller.action.IWeiKeActionService;
import com.ry.sskt.core.annotation.AllowUserType;
import com.ry.sskt.core.annotation.AlwaysAccessible;
import com.ry.sskt.model.common.constant.UserTypeEnum;
import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.weike.request.GetWeiKeInfoRequest;
import com.ry.sskt.model.weike.request.GetWeiKeListRequest;
import com.ry.sskt.model.weike.request.SetWeiKeInfoRequest;
import com.ry.sskt.model.weike.request.UpdateWeiKeStatusRequest;
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
@RequestMapping("/WeiKe")
@AllowUserType(userType = UserTypeEnum.All)
@Api(value = "微课相关接口", tags = {"微课相关接口"})
@Slf4j
public class WeiKeController {
    @Autowired
    IWeiKeActionService weiKeActionService;

    @ApiOperation("【课程管理】获取微课详情")
    @ApiImplicitParam(name = "request", dataType = "GetWeiKeInfoRequest", paramType = "body", required = true, value = "【课程管理】获取微课详情入参实体")
    @PostMapping("/GetWeiKeInfo")
    public ResponseBase GetWeiKeInfo(@RequestBody GetWeiKeInfoRequest request) {
        try {
            return weiKeActionService.GetWeiKeInfo(request);
        } catch (Exception ex) {
            log.error(String.format("【课程管理】获取微课详情报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }


    @ApiOperation("获取在线微课列表")
    @ApiImplicitParam(name = "request", dataType = "GetWeiKeListRequest", paramType = "body", required = true, value = "获取在线微课列表入参实体")
    @PostMapping("/GetWeiKeList")
    @AlwaysAccessible
    public ResponseBase GetWeiKeList(@RequestBody GetWeiKeListRequest request) {
        try {
            return weiKeActionService.GetWeiKeList(request);
        } catch (Exception ex) {
            log.error(String.format("获取在线微课列表报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【课程管理】修改微课详情")
    @ApiImplicitParam(name = "request", dataType = "SetWeiKeInfoRequest", paramType = "body", required = true, value = "【课程管理】修改微课详情入参实体")
    @PostMapping("/SetWeiKeInfo")
    public ResponseBase SetWeiKeInfo(@RequestBody SetWeiKeInfoRequest request) {
        try {
            return weiKeActionService.SetWeiKeInfo(request);
        } catch (Exception ex) {
            log.error(String.format("【课程管理】修改微课详情报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【课程管理】修改微课状态(上架/下架/删除)")
    @ApiImplicitParam(name = "request", dataType = "UpdateWeiKeStatusRequest", paramType = "body", required = true, value = "【课程管理】修改微课状态(上架/下架/删除)入参实体")
    @PostMapping("/UpdateWeiKeStatus")
    public ResponseBase UpdateWeiKeStatus(@RequestBody UpdateWeiKeStatusRequest request) {
        try {
            return weiKeActionService.UpdateWeiKeStatus(request);
        } catch (Exception ex) {
            log.error(String.format("【课程管理】修改微课状态(上架/下架/删除)报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【学生端】微课视频播放")
    @ApiImplicitParam(name = "request", dataType = "GetWeiKeInfoRequest", paramType = "body", required = true, value = "【学生端】微课视频播放入参实体")
    @PostMapping("/GetWeiKeVideo")
    public ResponseBase GetWeiKeVideo(@RequestBody GetWeiKeInfoRequest request) {
        try {
            return weiKeActionService.GetWeiKeVideo(request);
        } catch (Exception ex) {
            log.error(String.format("【学生端】微课视频播放报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }


}
