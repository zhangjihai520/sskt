package com.ry.sskt.controller;

import com.alibaba.fastjson.JSON;
import com.ry.sskt.controller.action.IExamSetActionService;
import com.ry.sskt.core.annotation.AllowUserType;
import com.ry.sskt.model.common.constant.UserTypeEnum;
import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.examset.request.*;
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
@RequestMapping("/ExamSet")
@AllowUserType(userType = UserTypeEnum.All)
@Api(value = "作业控制器", tags = {"作业控制器"})
@Slf4j
public class ExamSetController {
    @Autowired
    IExamSetActionService examSetActionService;

    @ApiOperation("【课表管理】获取课程作业列表")
    @ApiImplicitParam(name = "request", dataType = "GetExamSetListApiRequest", paramType = "body", required = true, value = "获取课程作业列表入参实体")
    @PostMapping("/GetExamSetList")
    public ResponseBase GetExamSetList(@RequestBody GetExamSetListApiRequest request) {
        try {
            return examSetActionService.GetExamSetList(request);
        } catch (Exception ex) {
            log.error(String.format("【课表管理】获取课程作业列表报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【课表管理】获取作业统计列表")
    @ApiImplicitParam(name = "request", dataType = "GetExamSetStatistListApiRequest", paramType = "body", required = true, value = "【课表管理】获取作业统计列表入参实体")
    @PostMapping("/GetExamSetStatistList")
    public ResponseBase GetExamSetStatistList(@RequestBody GetExamSetStatistListApiRequest request) {
        try {
            return examSetActionService.GetExamSetStatistList(request);
        } catch (Exception ex) {
            log.error(String.format("【课表管理】获取作业统计列表报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("获取布置新题地址")
    @ApiImplicitParam(name = "request", dataType = "GetAddExamSetUrlApiRequest", paramType = "body", required = true, value = "获取布置新题地址入参实体")
    @PostMapping("/GetAddExamSetUrl")
    public ResponseBase GetAddExamSetUrl(@RequestBody GetAddExamSetUrlApiRequest request) {
        try {
            return examSetActionService.GetAddExamSetUrl(request);
        } catch (Exception ex) {
            log.error(String.format("获取布置新题地址报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("测评结果")
    @ApiImplicitParam(name = "request", dataType = "GetEvaluationResultApiRequest", paramType = "body", required = true, value = "测评结果入参实体")
    @PostMapping("/GetEvaluationResult")
    public ResponseBase GetEvaluationResult(@RequestBody GetEvaluationResultApiRequest request) {
        try {
            return examSetActionService.GetEvaluationResult(request);
        } catch (Exception ex) {
            log.error(String.format("测评结果报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("作业上架")
    @ApiImplicitParam(name = "request", dataType = "SetExamSetOnlineApiRequest", paramType = "body", required = true, value = "作业上架入参实体")
    @PostMapping("/SetExamSetOnline")
    public ResponseBase SetExamSetOnline(@RequestBody SetExamSetOnlineApiRequest request) {
        try {
            return examSetActionService.SetExamSetOnline(request);
        } catch (Exception ex) {
            log.error(String.format("作业上架报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【课堂作业】作业预览")
    @ApiImplicitParam(name = "request", dataType = "GetExamSetPreviewApiRequest", paramType = "body", required = true, value = "【课堂作业】作业预览入参实体")
    @PostMapping("/GetExamSetPreview")
    public ResponseBase GetExamSetPreview(@RequestBody GetExamSetPreviewApiRequest request) {
        try {
            return examSetActionService.GetExamSetPreview(request);
        } catch (Exception ex) {
            log.error(String.format("【课堂作业】作业预览报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【课堂作业】保存作业")
    @ApiImplicitParam(name = "request", dataType = "SaveExamSetApiRequest", paramType = "body", required = true, value = "【课堂作业】保存作业入参实体")
    @PostMapping("/SaveExamSet")
    public ResponseBase SaveExamSet(@RequestBody SaveExamSetApiRequest request) {
        try {
            return examSetActionService.SaveExamSet(request);
        } catch (Exception ex) {
            log.error(String.format("【课堂作业】保存作业报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【课堂作业】获取作业信息")
    @ApiImplicitParam(name = "request", dataType = "GetExamSetInfoApiRequest", paramType = "body", required = true, value = "【课堂作业】获取作业信息入参实体")
    @PostMapping("/GetExamSetInfo")
    //TODO 加解密
    public ResponseBase GetExamSetInfo(@RequestBody GetExamSetInfoApiRequest request) {
        try {
            return examSetActionService.GetExamSetInfo(request);
        } catch (Exception ex) {
            log.error(String.format("【课堂作业】获取作业信息报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

}
