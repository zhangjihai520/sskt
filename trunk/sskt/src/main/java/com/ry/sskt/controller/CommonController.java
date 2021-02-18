package com.ry.sskt.controller;

import com.alibaba.fastjson.JSON;
import com.ry.sskt.controller.action.ICommonActionService;
import com.ry.sskt.core.annotation.AlwaysAccessible;
import com.ry.sskt.core.config.CommonConfig;
import com.ry.sskt.model.common.constant.SubjectStatusEnum;
import com.ry.sskt.model.common.constant.WeiKeStatusFlagEnum;
import com.ry.sskt.model.common.entity.IdName;
import com.ry.sskt.model.common.request.*;
import com.ry.sskt.model.common.response.ResponseBase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 公共接口控制器
 * </p>
 *
 * @author xrq
 * @since 2020-04-16
 */
@RestController
@RequestMapping("/Common")
@AlwaysAccessible
@Api(value = "公共接口", tags = {"公共接口"})
@Slf4j
public class CommonController extends AbstractController {
    @Autowired
    ICommonActionService commonActionService;

    @ApiOperation("获取教材版本")
    @ApiImplicitParam(name = "request", dataType = "GetBookVersionListApiRequest", paramType = "body", required = true, value = "获取教材版本入参实体")
    @PostMapping("/GetBookVersionList")
    public ResponseBase GetBookVersionList(@Valid @RequestBody GetBookVersionListApiRequest request, BindingResult result) {
        try {
            return commonActionService.GetBookVersionList(request);
        } catch (Exception e) {
            log.error(String.format("获取教材版本出错，参数：%s", JSON.toJSONString(request)), e);
        }
        return ResponseBase.GetErrorResponse();
    }

    @ApiOperation("根据教材获取课本列表")
    @ApiImplicitParam(name = "request", dataType = "GetCourseMappingListApiRequest", paramType = "body", required = true, value = "根据教材获取课本列表")
    @PostMapping("/GetCourseMappingList")
    public ResponseBase GetCourseMappingList(@Valid @RequestBody GetCourseMappingListApiRequest request, BindingResult result) {
        try {
            return commonActionService.GetCourseMappingList(request);
        } catch (Exception e) {
            log.error(String.format("根据教材获取课本列表出错，参数：%s", JSON.toJSONString(request)), e);
        }
        return ResponseBase.GetErrorResponse();
    }

    @ApiOperation("根据课本获取章节列表")
    @ApiImplicitParam(name = "request", dataType = "GetChapterSectionListApiRequest", paramType = "body", required = true, value = "根据课本获取章节列表")
    @PostMapping("/GetChapterSectionList")
    public ResponseBase GetChapterSectionList(@Valid @RequestBody GetChapterSectionListApiRequest request, BindingResult result) {
        try {
            return commonActionService.GetChapterSectionList(request);
        } catch (Exception e) {
            log.error(String.format("根据课本获取章节列表出错，参数：%s", JSON.toJSONString(request)), e);
        }
        return ResponseBase.GetErrorResponse();
    }

    @ApiOperation("获取课程基础信息-课程状态")
    @PostMapping("/GetSubjectStatusList")
    public ResponseBase GetSubjectStatusList() {
        try {
            List<IdName> idNames = new LinkedList<>();
            for (SubjectStatusEnum e : SubjectStatusEnum.values()) {
                idNames.add(new IdName(e.getIndex() + "", e.getValue()));
            }
            return ResponseBase.GetSuccessResponse(idNames);
        } catch (Exception e) {
            log.error("获取课程基础信息-课程状态出错,参数：无", e);
        }
        return ResponseBase.GetErrorResponse();
    }

    @ApiOperation("获取微课基础信息-状态")
    @PostMapping("/GetWeiKeStatusList")
    public ResponseBase GetWeiKeStatusList() {
        try {
            List<IdName> idNames = new LinkedList<>();
            for (WeiKeStatusFlagEnum e : WeiKeStatusFlagEnum.values()) {
                idNames.add(new IdName(e.getIndex() + "", e.getValue()));
            }
            return ResponseBase.GetSuccessResponse(idNames);
        } catch (Exception e) {
            log.error("获取微课基础信息-状态出错,参数：无", e);
        }
        return ResponseBase.GetErrorResponse();
    }

    @ApiOperation("获取所有年级等信息")
    @PostMapping("/GetGradeList")
    public ResponseBase GetGradeList() {
        try {
            return commonActionService.GetGradeList();
        } catch (Exception e) {
            log.error("获取所有年级等信息出错,参数：无", e);
        }
        return ResponseBase.GetErrorResponse();
    }

    @ApiOperation("获取视频地址配置信息")
    @PostMapping("/GetVideoHost")
    public ResponseBase GetVideoHost() {
        try {
            return ResponseBase.GetSuccessResponse(CommonConfig.getVideoHost());
        } catch (Exception e) {
            log.error("获取视频地址配置信息出错,参数：无", e);
        }
        return ResponseBase.GetErrorResponse();
    }

    @ApiOperation("获取公益课直播时间差")
    @ApiImplicitParam(name = "request", dataType = "CurrentTimeApiRequest", paramType = "body", required = false, value = "获取公益课直播时间差")
    @PostMapping("/CurrentTime")
    public ResponseBase CurrentTime(@RequestBody(required = false) CurrentTimeApiRequest request) {
        try {
            return commonActionService.CurrentTime(request);
        } catch (Exception e) {
            log.error(String.format("获取公益课直播时间差列表出错，参数：%s", request == null ? "" : JSON.toJSONString(request)), e);
        }
        return ResponseBase.GetErrorResponse();
    }

    @ApiOperation("记录日志")
    @ApiImplicitParam(name = "request", dataType = "RecordLogApiRequest", paramType = "body", required = true, value = "记录日志")
    @PostMapping("/RecordLog")
    public ResponseBase RecordLog(@RequestBody RecordLogApiRequest request) {
        try {
            return commonActionService.RecordLog(request);
        } catch (Exception e) {
            log.error(String.format("记录日志出错，参数：%s", JSON.toJSONString(request)), e);
        }
        return ResponseBase.GetErrorResponse();
    }

    @ApiOperation("附件上传")
    @PostMapping(value = "/UploadFile", consumes = "multipart/*", headers = "content-type=multipart/form-data")
    public ResponseBase uploadFile(@ApiParam(name = "file", required = true) MultipartFile[] file, @ApiParam(name = "param", required = false) String param) {
        try {
            return commonActionService.uploadFile(file, param);
        } catch (Exception e) {
            log.error(String.format("附件上传出错，参数：%s", param, e));
            return ResponseBase.GetErrorResponse();
        }
    }
}
