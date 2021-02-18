package com.ry.sskt.controller.action.impl;

import com.alibaba.fastjson.JSONArray;
import com.ry.sskt.controller.action.ILoginActionService;
import com.ry.sskt.controller.action.IWeiKeActionService;
import com.ry.sskt.core.config.CommonConfig;
import com.ry.sskt.core.dledc.request.CheckUserAccountRequest;
import com.ry.sskt.core.dledc.response.CheckUserAccountResponse;
import com.ry.sskt.core.utils.JwtUtil;
import com.ry.sskt.core.utils.UrlUtil;
import com.ry.sskt.model.common.constant.*;
import com.ry.sskt.model.common.entity.ChapterSection;
import com.ry.sskt.model.common.entity.IdName;
import com.ry.sskt.model.common.entity.User;
import com.ry.sskt.model.common.entity.UserLoginInfo;
import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.openapi.entity.BookVersion;
import com.ry.sskt.model.openapi.entity.CourseMapping;
import com.ry.sskt.model.subject.entity.WeiKe;
import com.ry.sskt.model.user.request.LoginApiRequest;
import com.ry.sskt.model.user.response.LoginResponse;
import com.ry.sskt.model.video.entity.VHVideoView;
import com.ry.sskt.model.weike.entity.GetWeiKeListResponseItem;
import com.ry.sskt.model.weike.request.GetWeiKeInfoRequest;
import com.ry.sskt.model.weike.request.GetWeiKeListRequest;
import com.ry.sskt.model.weike.request.SetWeiKeInfoRequest;
import com.ry.sskt.model.weike.request.UpdateWeiKeStatusRequest;
import com.ry.sskt.model.weike.response.GetWeiKeInfoResponse;
import com.ry.sskt.model.weike.response.GetWeiKeListResponse;
import com.ry.sskt.model.weike.response.GetWeiKeVideoResponse;
import com.ry.sskt.service.*;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 消息 服务实现类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
@Slf4j
@Service
public class WeiKeActionService implements IWeiKeActionService {
    @Autowired
    IUserService userService;

    @Autowired
    IGradeCourseService gradeCourseService;

    @Autowired
    IWeiKeService weiKeService;

    @Autowired
    IOpenApiService openApiService;

    @Autowired
    IVideoHardwareService videoHardwareService;

    @Override
    public ResponseBase GetWeiKeInfo(GetWeiKeInfoRequest request) throws Exception {
        var weike = weiKeService.GetByKey(UrlUtil.decrypt(request.getWeiKeId(), Integer.class));
        var teacher = userService.getByKey(weike.getTeacherId(), true);
        if (teacher != null) {
            weike.setTeacherName(teacher.getUserTrueName());
        }
        var result = new GetWeiKeInfoResponse(weike, GetVideoList(weike, 1), GetVideoList(weike, 2));
        var chapterSection = openApiService.getChapterSections(new int[]{result.getCourseMappingId()}).stream().filter(d -> d.getNodeId() == result.getSectionId()).findFirst().orElse(new ChapterSection());
        result.setSectionName(chapterSection.getNodeName());
        return ResponseBase.GetSuccessResponse(result);
    }

    @Override
    public ResponseBase GetWeiKeList(GetWeiKeListRequest request) throws Exception {
        var userInfo = new User().setUserTypeId(UserTypeEnum.Student.getCode());
        if (request.getUserId() > 0) {
            userInfo = userService.getByKey(request.getUserId(), true);
            if (userInfo == null || userInfo.getUserId() == 0) {
                return ResponseBase.GetValidateErrorResponse("用户不存在");
            }
            //Arrays.stream(exam.getQuestionIds().split(",")).map(Integer::parseInt).collect(Collectors.toList()
            var userRoles = Arrays.stream(userInfo.getUserRoles().split(",")).map(Integer::parseInt).collect(Collectors.toList());
            if (userRoles.size() > 0 && request.getUserRole() > 0) {
                if (!userRoles.contains(request.getUserRole())) {
                    return ResponseBase.GetValidateErrorResponse("用户类型不匹配");
                }
            } else {
                request.setUserRole(userInfo.getUserTypeId());
            }
        } else {
            request.setUserRole(UserRoleEnum.Student.getCode());
        }
        var teacherId = 0;
        var statusFlagSymbol = "";
        if (request.getUserRole() == UserRoleEnum.Student.getCode()) {
            statusFlagSymbol = "=";
            request.setStatusFlag(WeiKeStatusFlagEnum.AuditPass.getIndex());
        } else if (request.getUserRole() == UserRoleEnum.Teacher.getCode()) {
            teacherId = userInfo.getUserId();
            statusFlagSymbol = request.getStatusFlag() > 0 ? "=" : ">=";
            request.setStatusFlag(request.getStatusFlag() > 0 ? request.getStatusFlag() : WeiKeStatusFlagEnum.ToBeShelves.getIndex());
        } else if (request.getUserRole() == UserRoleEnum.SchoolManager.getCode()) {
            statusFlagSymbol = request.getStatusFlag() > 0 ? "=" : ">=";
            request.setStatusFlag(request.getStatusFlag() > 0 ? request.getStatusFlag() : WeiKeStatusFlagEnum.ToBeAudited.getIndex());
        } else {
            return ResponseBase.GetValidateErrorResponse("用户类型错误");
        }
        if (request.getEndTime() != null) {
            request.setEndTime(request.getEndTime().plusDays(1).plusSeconds(-1));
        }
        var weike = weiKeService.GetListBySearch(request.getStatusFlag(), request.getGradeId(), request.getCourseId(), request.getBeginTime(), request.getEndTime(), request.getKeyword(), teacherId, statusFlagSymbol, request.getPageIndex(), request.getPageSize());
        var bookVersions = openApiService.getBookVersions(weike.second.stream().mapToInt(WeiKe::getCourseId).distinct().toArray());
        var courseMappings = openApiService.getCourseMappings(bookVersions.stream().mapToInt(BookVersion::getBookVersionId).distinct().toArray());
        var chapterSections = openApiService.getChapterSections(courseMappings.stream().mapToInt(CourseMapping::getCourseMappingId).distinct().toArray());
        var allCourse = gradeCourseService.getAllCourse(true);
        var allGrade = gradeCourseService.getAllGrade();
        var bookVersionsIdNames = new LinkedList<IdName>();
        var courseMappingsIdNames = new LinkedList<IdName>();
        var chapterSectionsIdNames = new LinkedList<IdName>();
        bookVersions.forEach(m -> bookVersionsIdNames.add(new IdName(m.getBookVersionId() + "", m.getBookVersionName())));
        courseMappings.forEach(m -> courseMappingsIdNames.add(new IdName(m.getCourseMappingId() + "", m.getCourseMappingName())));
        chapterSections.forEach(m -> chapterSectionsIdNames.add(new IdName(m.getNodeId() + "", m.getNodeName())));
        GetWeiKeListResponse result = new GetWeiKeListResponse();
        // GetWeiKeListResponse result = new GetWeiKeListResponse(weike.first, weike.second, bookVersionsIdNames, courseMappingsIdNames, chapterSectionsIdNames, allCourse, allGrade);
        result.setTotalCount(weike.first);
        weike.second.forEach(m -> {
            result.getWeiKeList().add(new GetWeiKeListResponseItem(m, bookVersionsIdNames, courseMappingsIdNames, chapterSectionsIdNames, allCourse, allGrade));
        });
        return ResponseBase.GetSuccessResponse(result);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase SetWeiKeInfo(SetWeiKeInfoRequest request) throws Exception {
        var oldMod = weiKeService.GetByKey(UrlUtil.decrypt(request.getWeiKeId(), Integer.class));
        var weike = request.ConvertToWeiKe(oldMod);
        var result = weiKeService.Save(weike);
        return ResponseBase.GetSuccessResponse(result > 0 ? 1 : 0);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase UpdateWeiKeStatus(UpdateWeiKeStatusRequest request) {
        var ids = new LinkedList<Integer>();
        request.getUpdateIds().forEach(x -> {
            ids.add(UrlUtil.decrypt(x, Integer.class));
        });
        var result = weiKeService.UpdateWeiKeStatus(ids, request.getStatusFlag());
        return ResponseBase.GetSuccessResponse(result ? 1 : 0);
    }

    @Override
    public ResponseBase GetWeiKeVideo(GetWeiKeInfoRequest request) {
        var weike = weiKeService.GetByKey(UrlUtil.decrypt(request.getWeiKeId(), Integer.class));
        var user = userService.getByKey(weike.getTeacherId(), true);
        if (user == null) {
            user = new User();
        }
        var result = new GetWeiKeVideoResponse()
                .setWeiKeId(UrlUtil.encrypt(weike.getWeiKeId()))
                .setCreateUserName(user.getUserTrueName())
                .setVideoUrl(GetVideoList2(weike, 1))
                .setPPTVideoUrl(GetVideoList2(weike, 2))
                .setOverViewUrl(weike.getOverViewUrl())
                .setWeiKeName(weike.getWeiKeName());
        return ResponseBase.GetSuccessResponse(result);
    }

    /// <summary>
    /// 获取硬件厂家的视频播放地址
    /// </summary>
    /// <param name="weike"></param>
    /// <param name="type">1主路 2ppt线路</param>
    /// <returns></returns>
    private List<VHVideoView> GetVideoList(WeiKe weike, int type) {
        var result = new LinkedList<VHVideoView>();
        var list = JSONArray.parseArray(type == 1 ? weike.getFilePath() : weike.getPptFilePath(), VHVideoView.class);
        if (list == null) {
            return result;
        }
        return list;
    }

    /// <summary>
    /// 获取硬件厂家的视频播放地址
    /// </summary>
    /// <param name="weike"></param>
    /// <param name="type">1主路 2ppt线路</param>
    /// <returns></returns>
    private List<String> GetVideoList2(WeiKe weike, int type) {
        var result = new LinkedList<String>();
        var list = JSONArray.parseArray(type == 1 ? weike.getFilePath() : weike.getPptFilePath(), VHVideoView.class);
        if (list == null) {
            return result;
        }
        int index = 1;
        for (var item : list) {
            result.add(this.videoHardwareService.GetVideoPath(item.getUrl(), weike.getWeiKeId(), false, index++, type));
        }
        return result;
        //TODO 未完成
    }
}
