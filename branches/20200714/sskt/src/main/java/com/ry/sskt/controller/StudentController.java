package com.ry.sskt.controller;


import com.alibaba.fastjson.JSON;
import com.ry.sskt.controller.action.IStudentActionService;
import com.ry.sskt.core.annotation.AllowUserType;
import com.ry.sskt.model.common.constant.UserTypeEnum;
import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.common.request.RequestPageBase;
import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.student.request.*;
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
@RequestMapping("/Student")
@AllowUserType(userType = UserTypeEnum.All)
@Api(value = "学生相关接口", tags = {"学生相关接口"})
@Slf4j
public class StudentController {
    @Autowired
    IStudentActionService studentActionService;

    @ApiOperation("【学生端】修改个人信息")
    @ApiImplicitParam(name = "request", dataType = "EditStudentInfoApiRequest", paramType = "body", required = true, value = "【学生端】修改个人信息入参实体")
    @PostMapping("/EditStudentInfo")
    public ResponseBase EditStudentInfo(@RequestBody EditStudentInfoApiRequest request) {
        try {
            return studentActionService.EditStudentInfo(request);
        } catch (Exception ex) {
            log.error(String.format("【学生端】修改个人信息报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }


    @ApiOperation("【学生端】提交答案")
    @ApiImplicitParam(name = "request", dataType = "SubmitExamSetApiRequest", paramType = "body", required = true, value = "【学生端】提交答案入参实体")
    @PostMapping("/SubmitExamSet")
    public ResponseBase SubmitExamSet(@RequestBody SubmitExamSetApiRequest request) {
        try {
            return studentActionService.SubmitExamSet(request);
        } catch (Exception ex) {
            log.error(String.format("【学生端】提交答案报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }


    @ApiOperation("【学生端】向老师提问")
    @ApiImplicitParam(name = "request", dataType = "AskQuestionApiRequest", paramType = "body", required = true, value = "【学生端】向老师提问入参实体")
    @PostMapping("/AskQuestion")
    public ResponseBase AskQuestion(@RequestBody AskQuestionApiRequest request) {
        try {
            return studentActionService.AskQuestion(request);
        } catch (Exception ex) {
            log.error(String.format("【学生端】向老师提问报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }


    @ApiOperation("【学生端】发布评价")
    @ApiImplicitParam(name = "request", dataType = "EvaluateSubjectApiRequest", paramType = "body", required = true, value = "【学生端】发布评价入参实体")
    @PostMapping("/EvaluateSubject")
    public ResponseBase EvaluateSubject(@RequestBody EvaluateSubjectApiRequest request) {
        try {
            return studentActionService.EvaluateSubject(request);
        } catch (Exception ex) {
            log.error(String.format("【学生端】发布评价报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("获取学生轨迹")
    @ApiImplicitParam(name = "request", dataType = "GetStudentStudyRecordsApiRequest", paramType = "body", required = true, value = "获取学生轨迹入参实体")
    @PostMapping("/GetStudentStudyRecords")
    public ResponseBase GetStudentStudyRecords(@RequestBody GetStudentStudyRecordsApiRequest request) {
        try {
            return studentActionService.GetStudentStudyRecords(request);
        } catch (Exception ex) {
            log.error(String.format("获取学生轨迹报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("学生-评价管理-我的评价")
    @ApiImplicitParam(name = "request", dataType = "PageApiRequest", paramType = "body", required = true, value = "学生-评价管理-我的评价入参实体")
    @PostMapping("/GetSubjectEvaluateList")
    public ResponseBase GetSubjectEvaluateList(@RequestBody RequestPageBase request) {
        try {
            return studentActionService.GetSubjectEvaluateList(request);
        } catch (Exception ex) {
            log.error(String.format("学生-评价管理-我的评价报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }


    @ApiOperation("学生-评价管理-对我的评价")
    @ApiImplicitParam(name = "request", dataType = "PageApiRequest", paramType = "body", required = true, value = "学生-评价管理-对我的评价入参实体")
    @PostMapping("/GetTeacherToStudentEvaluateList")
    public ResponseBase GetTeacherToStudentEvaluateList(@RequestBody RequestPageBase request) {
        try {
            return studentActionService.GetTeacherToStudentEvaluateList(request);
        } catch (Exception ex) {
            log.error(String.format("学生-评价管理-对我的评价报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【消息】获取未读消息数量")
    @ApiImplicitParam(name = "request", dataType = "ApiRequestBase", paramType = "body", required = true, value = "【消息】获取未读消息数量入参实体")
    @PostMapping("/GetUnReadMessageNumber")
    public ResponseBase GetUnReadMessageNumber(@RequestBody RequestBase request) {
        try {
            return studentActionService.GetUnReadMessageNumber(request);
        } catch (Exception ex) {
            log.error(String.format("【消息】获取未读消息数量报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }
}
