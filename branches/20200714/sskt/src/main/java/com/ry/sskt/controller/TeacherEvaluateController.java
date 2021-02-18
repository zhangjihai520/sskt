package com.ry.sskt.controller;


import com.alibaba.fastjson.JSON;
import com.ry.sskt.controller.action.ITeacherEvaluateActionService;
import com.ry.sskt.core.annotation.AllowUserType;
import com.ry.sskt.model.common.constant.UserTypeEnum;
import com.ry.sskt.model.common.request.RequestPageBase;
import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.teacher.request.EvaluateSubjectApiRequest;
import com.ry.sskt.model.teacher.request.GetMessageListApiRequest;
import com.ry.sskt.model.teacher.request.GetSubjectEvaluateApiRequest;
import com.ry.sskt.model.teacher.request.ReplayMessageApiRequest;
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
@RequestMapping("/TeacherEvaluate")
@AllowUserType(userType = UserTypeEnum.All)
@Api(value = "教师端-评估教学管理相关接口", tags = {"教师端-评估教学管理相关接口"})
@Slf4j
public class TeacherEvaluateController {
    @Autowired
    ITeacherEvaluateActionService teacherEvaluateActionService;


    @ApiOperation("【教学管理】助教 -评价课程")
    @ApiImplicitParam(name = "request", dataType = "EvaluateSubjectApiRequest", paramType = "body", required = true, value = "【教学管理】助教 -评价课程入参实体")
    @PostMapping("/EvaluateSubject")
    public ResponseBase EvaluateSubject(@RequestBody EvaluateSubjectApiRequest request) {
        try {
            return teacherEvaluateActionService.EvaluateSubject(request);
        } catch (Exception ex) {
            log.error(String.format("【教学管理】助教 -评价课程报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【教学管理】助教 - 课程评价列表")
    @ApiImplicitParam(name = "request", dataType = "PageApiRequest", paramType = "body", required = true, value = "【教学管理】助教 - 课程评价列表入参实体")
    @PostMapping("/GetSubjectEvaluateList")
    public ResponseBase GetSubjectEvaluateList(@RequestBody RequestPageBase request) {
        try {
            return teacherEvaluateActionService.GetSubjectEvaluateList(request);
        } catch (Exception ex) {
            log.error(String.format("【教学管理】助教 - 课程评价列表报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【教学管理】回复消息")
    @ApiImplicitParam(name = "request", dataType = "ReplayMessageApiRequest", paramType = "body", required = true, value = "【教学管理】回复消息入参实体")
    @PostMapping("/ReplayMessage")
    public ResponseBase ReplayMessage(@RequestBody ReplayMessageApiRequest request) {
        try {
            return teacherEvaluateActionService.ReplayMessage(request);
        } catch (Exception ex) {
            log.error(String.format("【教学管理】回复消息报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【消息】教师、学生消息列表")
    @ApiImplicitParam(name = "request", dataType = "GetMessageListApiRequest", paramType = "body", required = true, value = "【消息】教师、学生消息列表入参实体")
    @PostMapping("/GetMessageList")
    public ResponseBase GetMessageList(@RequestBody GetMessageListApiRequest request) {
        try {
            return teacherEvaluateActionService.GetMessageList(request);
        } catch (Exception ex) {
            log.error(String.format("【消息】教师、学生消息列表报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【评估教学管理】获取所有课程的评价")
    @ApiImplicitParam(name = "request", dataType = "RequestPageBase", paramType = "body", required = true, value = "【评估教学管理】获取所有课程的评价入参实体")
    @PostMapping("/GetAllSubjectEvaluate")
    public ResponseBase GetAllSubjectEvaluate(@RequestBody RequestPageBase request) {
        try {
            return teacherEvaluateActionService.GetAllSubjectEvaluate(request);
        } catch (Exception ex) {
            log.error(String.format("【评估教学管理】获取所有课程的评价报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【评估教学管理】评价管理-主讲获取课程评价")
    @ApiImplicitParam(name = "request", dataType = "GetSubjectEvaluateApiRequest", paramType = "body", required = true, value = "【评估教学管理】评价管理-主讲获取课程评价入参实体")
    @PostMapping("/GetSubjectEvaluate")
    public ResponseBase GetSubjectEvaluate(@RequestBody GetSubjectEvaluateApiRequest request) {
        try {
            return teacherEvaluateActionService.GetSubjectEvaluate(request);
        } catch (Exception ex) {
            log.error(String.format("【评估教学管理】评价管理-主讲获取课程评价报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }
}
