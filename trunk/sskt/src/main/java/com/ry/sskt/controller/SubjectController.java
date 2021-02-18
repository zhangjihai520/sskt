package com.ry.sskt.controller;


import com.alibaba.fastjson.JSON;
import com.ry.sskt.controller.action.ISubjectActionService;
import com.ry.sskt.core.annotation.AllowUserType;
import com.ry.sskt.core.annotation.AlwaysAccessible;
import com.ry.sskt.core.utils.ExcelUtils;
import com.ry.sskt.model.common.constant.UserTypeEnum;
import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.subject.request.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/Subject")
@AllowUserType(userType = UserTypeEnum.All)
@Api(value = "登录模块接口", tags = {"登录模块接口"})
@Slf4j
public class SubjectController {
    @Autowired
    ISubjectActionService subjectActionService;

    @ApiOperation("【课表管理】获取课程列表")
    @ApiImplicitParam(name = "request", dataType = "GetSubjectListApiRequest", paramType = "body", required = true, value = "【课表管理】获取课程列表入参实体")
    @PostMapping("/GetSubjectList")
    public ResponseBase GetSubjectList(@RequestBody GetSubjectListApiRequest request) {
        try {
            return subjectActionService.GetSubjectList(request);
        } catch (
                Exception ex) {
            log.error(String.format("【课表管理】获取课程列表报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【课表管理】下架/上架/删除课程")
    @ApiImplicitParam(name = "request", dataType = "UpdateSubjectStatusApiRequest", paramType = "body", required = true, value = "【课表管理】下架/上架/删除课程入参实体")
    @PostMapping("/UpdateSubjectStatus")
    public ResponseBase UpdateSubjectStatus(@RequestBody UpdateSubjectStatusApiRequest request) {
        try {
            return subjectActionService.UpdateSubjectStatus(request);
        } catch (Exception ex) {
            log.error(String.format("【课表管理】下架/上架/删除课程报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【课堂管理】导出课程数据")
    @PostMapping("/ExportSubjectList")
    public void ExportSubjectList(HttpServletRequest httprequest, HttpServletResponse response, @RequestParam Map<String, String> params) {
        try {
            subjectActionService.ExportSubjectList(httprequest, response, params);
        } catch (Exception ex) {
            log.error(String.format("【课堂管理】导出课程数据报错，参数：%s", JSON.toJSONString(params)), ex);
            ExcelUtils.responseError(response);
        }
    }

    @ApiOperation("【课表管理】添加课程")
    @ApiImplicitParam(name = "request", dataType = "EditSubjectApiRequest", paramType = "body", required = true, value = "【课堂管理】导出课程数据入参实体")
    @PostMapping("/EditSubject")
    public ResponseBase EditSubject(@RequestBody EditSubjectApiRequest request) {
        try {
            return subjectActionService.EditSubject(request);
        } catch (Exception ex) {
            log.error(String.format("【课堂管理】导出课程数据报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【课堂管理】获取课程详情")
    @ApiImplicitParam(name = "request", dataType = "GetSubjectInfoApiRequest", paramType = "body", required = true, value = "【课堂管理】获取课程详情入参实体")
    @PostMapping("/GetSubjectInfo")
    public ResponseBase GetSubjectInfo(@RequestBody GetSubjectInfoApiRequest request) {
        try {
            return subjectActionService.GetSubjectInfo(request);
        } catch (Exception ex) {
            log.error(String.format("【课堂管理】获取课程详情报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【课程统计/学情监控】获取课程人数详情图表")
    @ApiImplicitParam(name = "request", dataType = "GetSubjectListStatisticsApiRequest", paramType = "body", required = true, value = "【课程统计/学情监控】获取课程人数详情图表入参实体")
    @PostMapping("/GetSubjectListStatistics")
    public ResponseBase GetSubjectListStatistics(@RequestBody GetSubjectListStatisticsApiRequest request) {
        try {
            return subjectActionService.GetSubjectListStatistics(request);
        } catch (Exception ex) {
            log.error(String.format("【课程统计/学情监控】获取课程人数详情图表报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【课程统计/学情监控】获取热门数据")
    @ApiImplicitParam(name = "request", dataType = "GetHotSubjectListApiRequest", paramType = "body", required = true, value = "【课程统计/学情监控】获取热门数据入参实体")
    @PostMapping("/GetHotSubjectList")
    public ResponseBase GetHotSubjectList(@RequestBody GetHotSubjectListApiRequest request) {
        try {
            return subjectActionService.GetHotSubjectList(request);
        } catch (Exception ex) {
            log.error(String.format("【课程统计/学情监控】获取热门数据报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【学情监控】获取学生学校占数比")
    @ApiImplicitParam(name = "request", dataType = "GetStudentSchoolShareApiRequest", paramType = "body", required = true, value = "【学情监控】获取学生学校占数比入参实体")
    @PostMapping("/GetStudentSchoolShare")
    public ResponseBase GetStudentSchoolShare(@RequestBody GetStudentSchoolShareApiRequest request) {
        try {
            return subjectActionService.GetStudentSchoolShare(request);
        } catch (Exception ex) {
            log.error(String.format("【学情监控】获取学生学校占数比报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("H5获取时间段内的学生开课情况")
    @ApiImplicitParam(name = "request", dataType = "GetUserSubjectListH5ApiRequest", paramType = "body", required = true, value = "H5获取时间段内的学生开课情况入参实体")
    @PostMapping("/GetUserSubjectListH5")
    public ResponseBase GetUserSubjectListH5(@RequestBody GetUserSubjectListH5ApiRequest request) {
        try {
            return subjectActionService.GetUserSubjectListH5(request);
        } catch (Exception ex) {
            log.error(String.format("H5获取时间段内的学生开课情况报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("H5获取我的学生课程列表")
    @ApiImplicitParam(name = "request", dataType = "GetStudentSubjectListH5ApiRequest", paramType = "body", required = true, value = "获取我的学生课程列表入参实体")
    @PostMapping("/GetStudentSubjectListH5")
    public ResponseBase GetStudentSubjectListH5(@RequestBody GetStudentSubjectListH5ApiRequest request) {
        try {
            return subjectActionService.GetStudentSubjectListH5(request);
        } catch (Exception ex) {
            log.error(String.format("获取我的学生课程列表报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【学生端】课程管理-课程列表-所有课程")
    @ApiImplicitParam(name = "request", dataType = "GetStudentSubjectListApiRequest", paramType = "body", required = true, value = "【学生端】课程管理-课程列表-所有课程入参实体")
    @PostMapping("/GetStudentSubjectList")
    @AlwaysAccessible
    public ResponseBase GetStudentSubjectList(@RequestBody GetStudentSubjectListApiRequest request) {
        try {
            return subjectActionService.GetStudentSubjectList(request);
        } catch (Exception ex) {
            log.error(String.format("【学生端】课程管理-课程列表-所有课程报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【学生端】课程管理-课程列表-我的课程")
    @ApiImplicitParam(name = "request", dataType = "GetStudentSubjectListApiRequest", paramType = "body", required = true, value = "【学生端】课程管理-课程列表-我的课程入参实体")
    @PostMapping("/GetStudentSubjectListLogin")
    public ResponseBase GetStudentSubjectListLogin(@RequestBody GetStudentSubjectListApiRequest request) {
        try {
            return subjectActionService.GetStudentSubjectList(request);
        } catch (Exception ex) {
            log.error(String.format("【学生端】课程管理-课程列表-我的课程报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("获取教师的课程总数")
    @ApiImplicitParam(name = "request", dataType = "CurrentUserApiRequest", paramType = "body", required = true, value = "获取教师的课程总数入参实体")
    @PostMapping("/GetSubjectListCount")
    public ResponseBase GetSubjectListCount(@RequestBody CurrentUserApiRequest request) {
        try {
            return subjectActionService.GetSubjectListCount(request);
        } catch (Exception ex) {
            log.error(String.format("获取教师的课程总数报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("获取H5端教师的课堂数据")
    @ApiImplicitParam(name = "request", dataType = "GetTeacherSubjectDataH5ApiRequest", paramType = "body", required = true, value = "课堂数据入参实体")
    @PostMapping("/GetTeacherSubjectDataH5")
    public ResponseBase GetTeacherSubjectDataH5(@RequestBody GetTeacherSubjectDataH5ApiRequest request) {
        try {
            return subjectActionService.GetTeacherSubjectDataH5(request);
        } catch (
                Exception ex) {
            log.error(String.format("课堂数据报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("搜索课程")
    @ApiImplicitParam(name = "request", dataType = "SearchStudentSubjectApiRequest", paramType = "body", required = true, value = "搜索课程入参实体")
    @PostMapping("/SearchStudentSubject")
    public ResponseBase SearchStudentSubject(@RequestBody SearchStudentSubjectApiRequest request) {
        try {
            return subjectActionService.SearchStudentSubject(request);
        } catch (Exception ex) {
            log.error(String.format("搜索课程报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【课程详情】获取课程详情列表")
    @ApiImplicitParam(name = "request", dataType = "GetSubjectInfoForShowApiRequest", paramType = "body", required = true, value = "【课程详情】获取课程详情列表入参实体")
    @PostMapping("/GetSubjectInfoForShow")
    public ResponseBase GetSubjectInfoForShow(@RequestBody GetSubjectInfoForShowApiRequest request) {
        try {
            return subjectActionService.GetSubjectInfoForShow(request);
        } catch (Exception ex) {
            log.error(String.format("【课程详情】获取课程详情列表报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【课程管理】资料或作业删除")
    @ApiImplicitParam(name = "request", dataType = "DeleteExamSetOrFileApiRequest", paramType = "body", required = true, value = "【课程管理】资料或作业删除入参实体")
    @PostMapping("/DeleteExamSetOrFile")
    public ResponseBase DeleteExamSetOrFile(@RequestBody DeleteExamSetOrFileApiRequest request) {
        try {
            return subjectActionService.DeleteExamSetOrFile(request);
        } catch (Exception ex) {
            log.error(String.format("【课程管理】资料或作业删除报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【课程详情】获取课堂学生列表")
    @ApiImplicitParam(name = "request", dataType = "GetStudentListApiRequest", paramType = "body", required = true, value = "【课程详情】获取课堂学生列表入参实体")
    @PostMapping("/GetStudentList")
    public ResponseBase GetStudentList(@RequestBody GetStudentListApiRequest request) {
        try {
            return subjectActionService.GetStudentList(request);
        } catch (Exception ex) {
            log.error(String.format("【课程详情】获取课堂学生列表报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【课件资料】课件资料列表")
    @ApiImplicitParam(name = "request", dataType = "GetSubjectFileListApiRequest", paramType = "body", required = true, value = "【课件资料】课件资料列表入参实体")
    @PostMapping("/GetSubjectFileList")
    public ResponseBase GetSubjectFileList(@RequestBody GetSubjectFileListApiRequest request) {
        try {
            return subjectActionService.GetSubjectFileList(request);
        } catch (Exception ex) {
            log.error(String.format("【课件资料】课件资料列表报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【课件资料】保存资料")
    @ApiImplicitParam(name = "request", dataType = "SaveSubjectFileApiRequest", paramType = "body", required = true, value = "【课件资料】保存资料入参实体")
    @PostMapping("/SaveSubjectFile")
    public ResponseBase SaveSubjectFile(@RequestBody SaveSubjectFileApiRequest request) {
        try {
            return subjectActionService.SaveSubjectFile(request);
        } catch (Exception ex) {
            log.error(String.format("【课件资料】保存资料报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【学生端】报名课程")
    @ApiImplicitParam(name = "request", dataType = "RegistSubjectApiRequest", paramType = "body", required = true, value = "【学生端】报名课程")
    @PostMapping("/RegistSubject")
    public ResponseBase RegistSubject(@RequestBody RegistSubjectApiRequest request) {
        try {
            return subjectActionService.RegistSubject(request);
        } catch (Exception ex) {
            log.error(String.format("【学生端】报名课程报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【学生端】下载课程附件列表")
    @ApiImplicitParam(name = "request", dataType = "StudentGetSubjectFileListApiRequest", paramType = "body", required = true, value = "【学生端】下载课程附件列表入参实体")
    @PostMapping("/StudentGetSubjectFileList")
    public ResponseBase StudentGetSubjectFileList(@RequestBody StudentGetSubjectFileListApiRequest request) {
        try {
            return subjectActionService.StudentGetSubjectFileList(request);
        } catch (Exception ex) {
            log.error(String.format("【学生端】下载课程附件列表报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("教师首页获取当天课程通知")
    @ApiImplicitParam(name = "request", dataType = "CurrentUserApiRequest", paramType = "body", required = true, value = "教师首页获取当天课程通知入参实体")
    @PostMapping("/GetCurrentSubjectName")
    public ResponseBase GetCurrentSubjectName(@RequestBody CurrentUserApiRequest request) {
        try {
            return subjectActionService.GetCurrentSubjectName(request);
        } catch (Exception ex) {
            log.error(String.format("教师首页获取当天课程通知报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("获取课程录像信息")
    @ApiImplicitParam(name = "request", dataType = "GetSubjectVideoApiRequest", paramType = "body", required = true, value = "获取课程录像信息入参实体")
    @PostMapping("/GetSubjectVideo")
    public ResponseBase GetSubjectVideo(@RequestBody GetSubjectVideoApiRequest request) {
        try {
            return subjectActionService.GetSubjectVideo(request);
        } catch (Exception ex) {
            log.error(String.format("获取课程录像信息报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【学生端】直播课上课状态修改")
    @ApiImplicitParam(name = "request", dataType = "UpdateSubjectStudentStatusApiRequest", paramType = "body", required = true, value = "【学生端】直播课上课状态修改入参实体")
    @PostMapping("/UpdateSubjectStudentStatus")
    public ResponseBase UpdateSubjectStudentStatus(@RequestBody UpdateSubjectStudentStatusApiRequest request) {
        try {
            return subjectActionService.UpdateSubjectStudentStatus(request);
        } catch (Exception ex) {
            log.error(String.format("【学生端】直播课上课状态修改报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("给硬件用的修改课程状态")
    @ApiImplicitParam(name = "request", dataType = "UpdateSubjectStatusOfAnyApiRequest", paramType = "body", required = true, value = "给硬件用的修改课程状态入参实体")
    @PostMapping("/UpdateSubjectStatusOfAny")
    @AlwaysAccessible
    public ResponseBase UpdateSubjectStatusOfAny(@RequestBody UpdateSubjectStatusOfAnyApiRequest request) {
        try {
            log.info(String.format("收到硬件访问，Request：%s", JSON.toJSONString(request)));
            return subjectActionService.UpdateSubjectStatusOfAny(request);
        } catch (Exception ex) {
            log.error(String.format("给硬件用的修改课程状态报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("获取硬件的视频房间列表")
    @ApiImplicitParam(name = "request", dataType = "RequestBase", paramType = "body", required = true, value = "获取硬件的视频房间列表入参实体")
    @PostMapping("/GetDeviceList")
    @AlwaysAccessible
    public ResponseBase GetDeviceList(@RequestBody RequestBase request) {
        try {
            return subjectActionService.GetDeviceList(request);
        } catch (Exception ex) {
            log.error(String.format("获取硬件的视频房间列表报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("获取授权号码")
    @ApiImplicitParam(name = "request", dataType = "GetAuthCodeApiRequest", paramType = "body", required = true, value = "获取授权号码入参实体")
    @PostMapping("/GetAuthCode")
    public ResponseBase GetAuthCode(@RequestBody GetAuthCodeApiRequest request) {
        try {
            return subjectActionService.GetAuthCode(request);
        } catch (Exception ex) {
            log.error(String.format("获取授权号码报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("创建视频直播")
    @ApiImplicitParam(name = "request", dataType = "CreateVideoLiveApiRequest", paramType = "body", required = true, value = "创建视频直播入参实体")
    @PostMapping("/CreateVideoLive")
    public ResponseBase CreateVideoLive(@RequestBody CreateVideoLiveApiRequest request) {
        try {
            return subjectActionService.CreateVideoLive(request);
        } catch (Exception ex) {
            log.error(String.format("创建视频直播报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("获取分组学生列表")
    @ApiImplicitParam(name = "request", dataType = "GetGroupByStudentListRequest", paramType = "body", required = true, value = "获取分组学生列表入参实体")
    @PostMapping("/GetGroupByStudentList")
    public ResponseBase GetGroupByStudentList(@RequestBody GetGroupByStudentListRequest request) {
        try {
            return subjectActionService.GetGroupByStudentList(request);
        } catch (Exception ex) {
            log.error(String.format("获取分组学生列表报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("获取教室列表")
    @ApiImplicitParam(name = "request", dataType = "GetSubjectRoomListRequest", paramType = "body", required = true, value = "获取教室列表入参实体")
    @PostMapping("/GetSubjectRoomList")
    public ResponseBase GetSubjectRoomList(@RequestBody GetSubjectRoomListRequest request) {
        try {
            return subjectActionService.GetSubjectRoomList(request);
        } catch (Exception ex) {
            log.error(String.format("获取教室列表报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("保存学生分组")
    @ApiImplicitParam(name = "request", dataType = "SaveGroupByStudentListRequest", paramType = "body", required = true, value = "保存学生分组入参实体")
    @PostMapping("/SaveGroupByStudentList")
    public ResponseBase SaveGroupByStudentList(@RequestBody SaveGroupByStudentListRequest request) {
        try {
            return subjectActionService.SaveGroupByStudentList(request);
        } catch (Exception ex) {
            log.error(String.format("保存学生分组报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("搜索课程")
    @ApiImplicitParam(name = "request", dataType = "SearchSubjectRequest", paramType = "body", required = true, value = "搜索课程入参实体")
    @PostMapping("/SearchSubject")
    public ResponseBase SearchSubject(@RequestBody SearchSubjectRequest request) {
        try {
            return subjectActionService.SearchSubject(request);
        } catch (Exception ex) {
            log.error(String.format("搜索课程报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("获取公益课列表")
    @ApiImplicitParam(name = "request", dataType = "GetPublicBenefitSubjectListApiRequest", paramType = "body", required = true, value = "获取公益课列表入参实体")
    @PostMapping("/GetPublicBenefitSubjectList")
    public ResponseBase GetPublicBenefitSubjectList(@RequestBody GetPublicBenefitSubjectListApiRequest request) {
        try {
            return subjectActionService.GetPublicBenefitSubjectList(request);
        } catch (Exception ex) {
            log.error(String.format("获取公益课列表报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("获取课程播放情况统计")
    @ApiImplicitParam(name = "request", dataType = "GetPublicBenefitStatisticsApiRequest", paramType = "body", required = true, value = "获取课程播放情况统计入参实体")
    @PostMapping("/GetPublicBenefitStatistics")
    @AllowUserType(userType = UserTypeEnum.Agent)
    public ResponseBase GetPublicBenefitStatistics(@RequestBody GetPublicBenefitStatisticsApiRequest request) {
        try {
            return subjectActionService.GetPublicBenefitStatistics(request);
        } catch (Exception ex) {
            log.error(String.format("获取课程播放情况统计报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }
}
