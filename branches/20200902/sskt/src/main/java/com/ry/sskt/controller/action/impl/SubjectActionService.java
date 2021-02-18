package com.ry.sskt.controller.action.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ry.sskt.controller.action.ISubjectActionService;
import com.ry.sskt.core.config.CommonConfig;
import com.ry.sskt.core.utils.*;
import com.ry.sskt.model.common.constant.*;
import com.ry.sskt.model.common.entity.Course;
import com.ry.sskt.model.common.entity.CourseType;
import com.ry.sskt.model.common.entity.FourthTuple;
import com.ry.sskt.model.common.entity.Grade;
import com.ry.sskt.model.common.entity.cache.AuthCodeCache;
import com.ry.sskt.model.common.entity.view.UserLoginTotalView;
import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.discuss.entity.cache.UserSubjectRoomCouchBaseObject;
import com.ry.sskt.model.examset.entity.ExamSet;
import com.ry.sskt.model.examset.view.ExamSetListView;
import com.ry.sskt.model.student.entity.StudentStudyRecord;
import com.ry.sskt.model.student.entity.SubjectRoomStudent;
import com.ry.sskt.model.student.entity.view.*;
import com.ry.sskt.model.subject.entity.*;
import com.ry.sskt.model.subject.entity.view.SubjectListByFilterView;
import com.ry.sskt.model.subject.entity.view.SubjectRoomTeacherNameView;
import com.ry.sskt.model.subject.request.*;
import com.ry.sskt.model.subject.response.*;
import com.ry.sskt.model.video.entity.VHVideoView;
import com.ry.sskt.service.*;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
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
public class SubjectActionService implements ISubjectActionService {
    @Autowired
    IUserService userService;
    @Autowired
    ISubjectService subjectService;
    @Autowired
    ISubjectRoomService subjectRoomService;
    @Autowired
    ISubjectRoomStudentService subjectRoomStudentService;
    @Autowired
    IGradeCourseService gradeCourseService;
    @Autowired
    ISubjectFileService subjectFileService;
    @Autowired
    IExamSetService examSetService;
    @Autowired
    IStudentAnswerService studentAnswerService;
    @Autowired
    IStudentStudyRecordService studentStudyRecordService;
    @Autowired
    IUserLoginInfoService userLoginInfoService;
    @Autowired
    IWeiKeService weiKeService;
    @Autowired
    IVideoHardwareService videoHardwareService;
    @Autowired
    IVideoPlayService videoPlayService;
    @Autowired
    ITalkMessageService talkMessageService;

    @Override
    public ResponseBase GetSubjectList(GetSubjectListApiRequest request) {
        var total = 0;
        var tulp = CommonUtil.GetFieldNames(request.getStatusFlag());
        if (request.getUserRole() == UserRoleEnum.Teacher.getCode()) {
            tulp = new FourthTuple(SubjectStatusFlagEnum.ToBeRegistered, tulp.second, tulp.third, tulp.fourth);
        } else if (request.getUserRole() == UserRoleEnum.SchoolManager.getCode()) {
        } else {
            return ResponseBase.GetValidateErrorResponse("用户角色错误");
        }
        if (!request.getEndTime().isEqual(DateUtil.LocalDateTimeMIN())) {
            request.setEndTime(request.getEndTime().plusDays(1).plusSeconds(-1));
        }
        if (!request.getBeginTimeMax().isEqual(DateUtil.LocalDateTimeMIN())) {
            request.setBeginTimeMax(request.getBeginTimeMax().plusDays(1).plusSeconds(-1));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("teacherId", request.isIsOnlySelf() ? request.getUserId() : 0);
        map.put("key", request.getKey());
        map.put("courseId", request.getCourseId());
        map.put("gradeId", request.getGradeId());
        map.put("maxTimeField", tulp.second);
        map.put("minTimeField", tulp.third);
        map.put("beginTime", DateUtil.LocalDateTimeMIN().isEqual(request.getBeginTime()) ? "" : request.getBeginTime().format(CommonConst.datefomat_nomal));
        map.put("endTime", DateUtil.LocalDateTimeMIN().isEqual(request.getEndTime()) ? "" : request.getEndTime().format(CommonConst.datefomat_nomal));
        map.put("beginTimeMax", DateUtil.LocalDateTimeMIN().isEqual(request.getBeginTimeMax()) ? "" : request.getBeginTimeMax().format(CommonConst.datefomat_nomal));
        map.put("beginTimeMin", DateUtil.LocalDateTimeMIN().isEqual(request.getBeginTimeMin()) ? "" : request.getBeginTimeMin().format(CommonConst.datefomat_nomal));
        map.put("status", tulp.first.getIndex());
        map.put("classState", tulp.fourth.getIndex());
        map.put("pageSkip", request.getPageSkip());
        map.put("pageSize", request.getPageSize());
        map.put("isOnline", request.getSystemTypeId());
        map.put("subjectGenreId", request.getSubjectGenreId());
        if (request.getOrderId() == SubjectListOrderFiledEnum.BeginTime_Desc.getCode()) {
            map.put("OrderByField", SubjectListOrderFiledEnum.BeginTime_Desc.getMsg());
        } else if (request.getOrderId() == SubjectListOrderFiledEnum.CreateDateTime_Desc.getCode()) {
            map.put("OrderByField", SubjectListOrderFiledEnum.CreateDateTime_Desc.getMsg());
        }
        map.put("totalCount", 0);
        var subjectList = subjectService.GetListByFilter(map);
        var gradeList = gradeCourseService.getAllGrade();
        var courseList = gradeCourseService.getAllCourse(true);
        var result = new GetSubjectListResponse();
        result.setCount(Integer.parseInt(map.get("totalCount").toString()));
        if (request.isIsGroupByDate()) {
            Map<String, List<SubjectListByFilterView>> groupMap = subjectList.stream().collect(Collectors.groupingBy(SubjectListByFilterView::BeginTimeFomatToLocalDate));
            for (Map.Entry<String, List<SubjectListByFilterView>> d : groupMap.entrySet()) {
                var groupDetail = new DateGroupDetail().setDate(d.getKey());
                for (SubjectListByFilterView c : d.getValue()) {
                    groupDetail.getGroupClassInfoList().add(CreateDetail(c, courseList, gradeList, request));
                }
                result.getDateGroup().add(groupDetail);
            }
        } else {
            subjectList.forEach(d -> {
                result.getClassInfoList().add(CreateDetail(d, courseList, gradeList, request));
            });
        }
        return ResponseBase.GetSuccessResponse(result);
    }

    /// <summary>
    /// 获取返回实例
    /// </summary>
    /// <param name="d"></param>
    /// <param name="courseList"></param>
    /// <param name="gradeList"></param>
    /// <param name="apiRequest"></param>
    /// <returns></returns>
    private GetSubjectListDetail CreateDetail(SubjectListByFilterView d, List<Course> courseList, List<Grade> gradeList, GetSubjectListApiRequest apiRequest) {
        Course course = courseList.stream().filter(g -> g.getCourseId() == d.getCourseId()).findFirst().orElse(new Course());
        Grade grade = gradeList.stream().filter(g -> g.getGradeId() == d.getGradeId()).findFirst().orElse(new Grade());
        return new GetSubjectListDetail()
                .setSubjectGenreId(d.getSubjectGenreId())
                .setCourseName(course.getCourseName())
                .setShortCode(course.getShortCode())
                .setCreateDateTime(d.getCreateDateTime().format(CommonConst.datefomat_nomal))
                .setGradeName(grade.getGradeName())
                .setImagePath(d.getImagePath())
                .setRegistEndTime(d.getRegistEndTime().format(CommonConst.datefomat_nomal))
                .setBeginTime(d.getBeginTime().format(CommonConst.datefomat_nomal))
                .setStatusFlag(apiRequest.getStatusFlag() == 0 ? CommonUtil.GetSubjectStatusEnum(d).getIndex() : apiRequest.getStatusFlag())
                .setSubjectId(UrlUtil.encrypt(d.getSubjectId()))
                .setSubjectName(d.getSubjectName())
                .setTeacherNameList(Arrays.stream(d.getTeacherNameList().split(",")).filter(x -> StringUtils.isNotBlank(x)).distinct().collect(Collectors.toList()))
                .setIsLecturer(d.getTeacherId() == apiRequest.getUserId())
                .setSubjectRoomId(UrlUtil.encrypt(d.getSubjectRoomId()))
                .setDeviceId(StringUtils.isBlank(d.getVideoRoom()) ? StringUtils.EMPTY : d.getVideoRoom().split("_")[0]);
    }

    @Override
    public ResponseBase UpdateSubjectStatus(UpdateSubjectStatusApiRequest request) {
        var subjectId = UrlUtil.decrypt(request.getSubjectId(), Integer.class);
        if (request.getStatusFlag() == UpdateSubjectStatusEnum.Up.getIndex()) {
            subjectService.UpdateStatusFlag(subjectId, SubjectStatusFlagEnum.ToBeRegistered);
        } else if (request.getStatusFlag() == UpdateSubjectStatusEnum.Down.getIndex()) {
            subjectService.UpdateStatusFlag(subjectId, SubjectStatusFlagEnum.Invalid);
        } else if (request.getStatusFlag() == UpdateSubjectStatusEnum.Delete.getIndex()) {
            subjectService.UpdateStatusFlag(subjectId, SubjectStatusFlagEnum.Delete);
        }
        return ResponseBase.GetSuccessResponse();
    }

    @Override
    public void ExportSubjectList(HttpServletRequest httprequest, HttpServletResponse response, Map<String, String> params) throws Exception {
        List<Integer> ids = new LinkedList<>();
        //value
        for (String x : params.values()) {
            ids.add(UrlUtil.decrypt(x, Integer.class));
        }
        var subjectViewList = subjectService.GetSubjectListBySubjectIds(ids);
        subjectViewList.sort(Comparator.comparing(ExportSubjectListView::getCreateDateTime));
        if (CollectionUtils.isEmpty(subjectViewList)) {
            throw new Exception();
        }
        String fileName = "课程信息-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        var courseList = gradeCourseService.getAllCourse(true);
        var gradeList = gradeCourseService.getAllGrade();
        List<SubjectExport> list = new LinkedList<>();
        subjectViewList.forEach(x -> {
            Course course = courseList.stream().filter(g -> g.getCourseId() == x.getCourseId()).findFirst().orElse(new Course());
            Grade grade = gradeList.stream().filter(g -> g.getGradeId() == x.getGradeId()).findFirst().orElse(new Grade());
            list.add(new SubjectExport(x.getSubjectName(), grade.getGradeName(), course.getCourseName(), x.getTeacherNameList(), x.getBeginTime().format(CommonConst.datefomat_nomal), SubjectStatusEnum.getValueByIndex(x.getStatusFlag())));
        });
        ExcelUtils.writeExcel(httprequest, response, list, fileName, SubjectExport.class);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase EditSubject(EditSubjectApiRequest request) throws Exception {
        if (CollectionUtils.isEmpty(request.getSubjectRooms())
                || (request.getSubjectRooms().stream().anyMatch(x -> x.getMaxRegisterNumber() <= 0) && request.getSystemTypeId() == 1)
                || request.getRegistEndTime().isAfter(request.getBeginTime()) || request.getRegistEndTime().isEqual(request.getBeginTime())
                || (request.getEndTime() != null && request.getEndTime().isBefore(request.getBeginTime()))
        ) //必须输入最大报名数
        {
            return ResponseBase.GetSuccessResponse(0);
        }

        if (request.getSystemTypeId() == 1 && request.getSubjectRooms().stream().mapToInt(p -> p.getMaxRegisterNumber()).sum() > CommonConfig.getMaxRegisterNumber()) //人数限制累加超过500
        {
            return ResponseBase.GetSuccessResponse(2);
        }

        var allCourse = gradeCourseService.getAllCourse(true);
        var obj = new Subject();
        obj.setSubjectId(UrlUtil.decrypt(request.getSubjectId(), Integer.class));

        List<Integer> oldSRoomIds = new LinkedList<Integer>();
        if (obj.getSubjectId() > 0) {
            obj = subjectService.getByKey(obj.getSubjectId(), true);
            oldSRoomIds = subjectRoomService.getSubjectRoomList(obj.getSubjectId()).stream().map(SubjectRoomTeacherNameView::getSubjectRoomId).collect(Collectors.toList());
        } else {
            obj.setStatusFlag(SubjectStatusEnum.Invalid.getIndex());
            obj.setVideoRoom(StringUtils.EMPTY);
            obj.setFilePath(StringUtils.EMPTY);
            obj.setPptFilePath(request.getSubjectGenreId() != SubjectGenreEnum.PublicBenefit.getIndex()
                    ? StringUtils.EMPTY
                    : (StringUtils.isBlank(request.getVideoUrl())
                    ? StringUtils.EMPTY
                    : VHVideoView.GetPublicBenefitInstanceByJson(request.getVideoUrl(), allCourse.stream().filter(d -> d.getCourseId() == request.getCourseId()).findFirst().orElse(new Course()).getCourseName(), null)));
        }
        obj.setSubjectTypeId(request.getSystemTypeId());
        obj.setBeginTime(request.getBeginTime());
        obj.setRegistBeginTime(request.getRegistBeginTime());
        obj.setRegistEndTime(request.getRegistEndTime().plusDays(1).plusSeconds(-1));
        obj.setSubjectName(request.getSubjectName());
        obj.setImagePath(StringUtils.isBlank(request.getImagePath()) ? StringUtils.EMPTY : request.getImagePath());
        obj.setComment(StringUtils.isBlank(request.getComment()) ? StringUtils.EMPTY : request.getComment());
        obj.setCourseId(request.getCourseId());
        obj.setGradeId(request.getGradeId());
        obj.setTeacherId(UrlUtil.decrypt(request.getTeacherId(), Integer.class));
        obj.setEndTime(request.getEndTime() == null ? request.getBeginTime() : request.getEndTime());
        obj.setSubjectGenreId(request.getSubjectGenreId() == 0 ? 1 : request.getSubjectGenreId());
        obj.setSubjectId(subjectService.Save(obj));
        if (obj.getSubjectId() == 0) {
            log.error(String.format("保存课程数据失败，参数：%s", JSON.toJSONString(request)));
            return ResponseBase.GetErrorResponse();
        }
        for (var subjectRoom : request.getSubjectRooms()) {
            var room = new SubjectRoom();
            room.setSubjectRoomId(UrlUtil.decrypt(subjectRoom.getSubjectRoomId(), Integer.class));
            if (room.getSubjectRoomId() > 0) {
                room = subjectRoomService.getByKey(room.getSubjectRoomId());
                oldSRoomIds.remove(room.getSubjectRoomId());
            }
            room.setMaxRegisterNumber(subjectRoom.getMaxRegisterNumber());
            room.setSubjectId(obj.getSubjectId());
            room.setTeacherId(UrlUtil.decrypt(subjectRoom.getHelpTeacherId(), Integer.class));
            room.setSchoolName(StringUtils.isBlank(subjectRoom.getSchoolName()) ? StringUtils.EMPTY : subjectRoom.getSchoolName());
            room.setStatusFlag(1);
            room.setSubjectRoomName(StringUtils.isBlank(subjectRoom.getSubjectRoomName()) ? StringUtils.EMPTY : subjectRoom.getSubjectRoomName());
            subjectRoomService.save(room);
        }
        if (CollectionUtils.isNotEmpty(oldSRoomIds)) {
            subjectRoomService.deleteRooms(oldSRoomIds);
        }
        return ResponseBase.GetSuccessResponse();
    }

    @Override
    public ResponseBase GetSubjectInfo(GetSubjectInfoApiRequest request) {
        int subjectId = UrlUtil.decrypt(request.getSubjectId(), Integer.class);
        var subject = subjectService.getByKey(subjectId, true);
        if (subject == null) {
            return ResponseBase.GetValidateErrorResponse("找不到该课程，获取失败");
        }
        var subjectRooms = subjectRoomService.getSubjectRoomList(subjectId);
        var teacher = userService.getByKey(subject.getTeacherId(), true);
        var result = new GetSubjectInfoResponse().setSubjectId(request.getSubjectId()).setSubjectName(subject.getSubjectName()).setRegistBeginTime(subject.getRegistBeginTime().format(CommonConst.datefomat_nomal2))
                .setRegistEndTime(subject.getRegistEndTime().format(CommonConst.datefomat_nomal2)).setBeginTime(subject.getBeginTime().format(CommonConst.datefomat_nomal2)).setGradeId(subject.getGradeId())
                .setCourseId(subject.getCourseId()).setTeacherId(UrlUtil.encrypt(subject.getTeacherId())).setTeacherName(StringUtils.isBlank(teacher.getUserTrueName()) ? teacher.getUserName() : teacher.getUserTrueName())
                .setImagePath(subject.getImagePath()).setComment(subject.getComment()).setEndTime(subject.getEndTime().format(CommonConst.datefomat_nomal2)).setSubjectGenreId(subject.getSubjectGenreId())
                .setVideoUrl(subject.getSubjectGenreId() == SubjectGenreEnum.PublicBenefit.getIndex() ? VHVideoView.GetPublicBenefitUrl(subject.getPptFilePath()) : "").setSubjectRooms(GetSubjectInfoForShowDetails(subjectRooms));
        return ResponseBase.GetSuccessResponse(result);
    }

    /// <summary>
    /// 获取教室列表
    /// </summary>
    /// <param name="subjectRooms"></param>
    /// <returns></returns>
    private List<SubjectRoomDetail> GetSubjectInfoForShowDetails(List<SubjectRoomTeacherNameView> subjectRooms) {
        var result = new LinkedList<SubjectRoomDetail>();
        for (var room : subjectRooms) {
            result.add(new SubjectRoomDetail()
                    .setSubjectRoomId(UrlUtil.encrypt(room.getSubjectRoomId()))
                    .setSubjectRoomName(room.getSubjectRoomName())
                    .setSchoolName(room.getSchoolName()).setHelpTeacherId(UrlUtil.encrypt(room.getTeacherId())).setHelpTeacherName(room.getUserTrueName()).setMaxRegisterNumber(room.getMaxRegisterNumber()));
        }
        return result;
    }

    @Override
    public ResponseBase GetSubjectListStatistics(GetSubjectListStatisticsApiRequest request) {
        var date = LocalDate.now().plusDays(1);
        List<SubjectStudentCountView> subjectList;
        if (request.getUserRole() == UserRoleEnum.SchoolManager.getCode())
            subjectList = subjectRoomStudentService.GetStudentCountByTeacherId(0, date.plusDays(-1 * request.getDays()), date, request.getSystemTypeId());
        else if (request.getUserRole() == UserRoleEnum.Teacher.getCode()) {
            subjectList = subjectRoomStudentService.GetStudentCountByTeacherId(request.getUserId(), date.plusDays(-1 * request.getDays()), date, request.getSystemTypeId());
        } else {
            return ResponseBase.GetValidateErrorResponse("用户验证错误");
        }
        var result = new GetSubjectListStatisticsResponse();
        Map<String, List<SubjectStudentCountView>> groupMap = subjectList.stream().collect(Collectors.groupingBy(SubjectStudentCountView::BeginTimeFomatToMMdd));
        for (Map.Entry<String, List<SubjectStudentCountView>> d : groupMap.entrySet()) {
            BigDecimal attendanceRate = new BigDecimal(0);
            if (d.getValue().stream().anyMatch(n -> n.getTotalCount() > 0)) {
                int absentCountSum = d.getValue().stream().mapToInt(p -> p.getAbsentCount()).sum();
                int totalCountSum = d.getValue().stream().mapToInt(p -> p.getTotalCount()).sum();
                attendanceRate = new BigDecimal((absentCountSum / totalCountSum) * 100.0).setScale(0);
            }
            GetSubjectListStatisticsDetail detail = new GetSubjectListStatisticsDetail();
            detail.setBeginTime(d.getKey()).setClassAttendance(d.getValue().stream().mapToInt(p -> p.getAbsentCount()).sum()).setAttendanceRate(attendanceRate);
            result.getSubjectList().add(detail);
        }
        Collections.sort(result.getSubjectList(), (a, b) -> b.getBeginTime().compareTo(a.getBeginTime()));
        return ResponseBase.GetSuccessResponse(result);
    }

    @Override
    public ResponseBase GetHotSubjectList(GetHotSubjectListApiRequest request) {
        var teacherId = request.getUserId();
        if (request.getUserRole() == UserRoleEnum.Teacher.getCode()) {
        } else if (request.getUserRole() == UserRoleEnum.SchoolManager.getCode()) {
            teacherId = 0;
        } else {
            return ResponseBase.GetValidateErrorResponse("用户角色错误");
        }
        List<HotDataView> dataList;
        HotDataTotalView total;
        int count = 0;
        Map<String, Object> map = new HashMap<>();
        map.put("pageSkip", request.getPageSkip());
        map.put("pageSize", request.getPageSize());
        map.put("isOnline", request.getSystemTypeId());
        map.put("totalCount", 0);
        if (request.getTypeEnum() == HotSubjectEnum.Subject.getCode()) {
            map.put("teacherId", teacherId);
            dataList = subjectService.GetHotSubjectList(map);
            total = subjectService.GetHotSubjectTotal(teacherId, request.getSystemTypeId());
        } else if (request.getTypeEnum() == HotSubjectEnum.School.getCode()) {
            dataList = subjectService.GetHotSchoolList(map);
            total = subjectService.GetHotSchoolTotal(request.getSystemTypeId());
        } else {
            dataList = subjectService.GetHotTeacherList(map);
            total = subjectService.GetHotTeacherTotal(request.getSystemTypeId());
        }
        count = Integer.parseInt(map.get("totalCount").toString());
        var courseList = gradeCourseService.getAllCourse(true);
        var result = new GetHotSubjectListResponse();
        dataList.forEach(d -> {
            Course course = courseList.stream().filter(g -> g.getCourseId() == d.getCourseId()).findFirst().orElse(new Course());
            GetHotSubjectListDetail detail = new GetHotSubjectListDetail();
            detail.setName(d.getName()).setAttendanceRate(d.getAttendanceRate()).setPraise(d.getPraise()).setClassAttendance(d.getClassAttendanceTotal());
            detail.setSubjectId(UrlUtil.encrypt(d.getSubjectId())).setBeginTime(d.getBeginTime().format(CommonConst.datefomat_nomal2)).setShortCode(course.getShortCode());
            result.getDetails().add(detail);
        });
        if (total == null) {
            total = new HotDataTotalView();
        }
        result.setCount(count);
        result.setClassAttendanceTotal(total.getClassAttendanceTotal());
        result.setAverageAttendanceRate(total.getAttendanceRate());
        result.setAveragePraise(total.getPraise());
        return ResponseBase.GetSuccessResponse(result);
    }

    @Override
    public ResponseBase GetStudentSchoolShare(GetStudentSchoolShareApiRequest request) {
        var list = subjectService.GetSchoolStudentCountView(request.getUserId(), request.getTypeEnum(), request.getSystemTypeId());
        list.sort(Comparator.comparing(SchoolStudentCountView::getTotalCount));
        var limitList = list.stream().limit(5).collect(Collectors.toList());
        var skipList = list.stream().skip(5).collect(Collectors.toList());
        var result = new GetStudentSchoolShareResponse().setValueCount(list.stream().mapToInt(p -> p.getTotalCount()).sum());
        limitList.forEach(x -> {
            result.getPieData().add(new PieDataDetail().setName(x.getName()).setValue(x.getTotalCount()));
        });
        result.getPieData().add(new PieDataDetail().setName("其他").setValue(skipList.stream().mapToInt(p -> p.getTotalCount()).sum()));
        return ResponseBase.GetSuccessResponse(result);
    }

    @Override
    public ResponseBase GetUserSubjectListH5(GetUserSubjectListH5ApiRequest request) {
        if (request.getSubjectGenreId() == SubjectGenreEnum.Other.getIndex()) {
            request.setSubjectGenreId(SubjectGenreEnum.Common.getIndex());
        }
        var userInfo = userService.getByKey(request.getUserId(), true);
        var beginMonth = StringUtils.isNotBlank(request.getSelectedMonth()) ? LocalDate.parse(request.getSelectedMonth() + "-01", CommonConst.Datefomat_yyyyMMdd) : DateUtil.LocalDateMIN();
        var endMonth = beginMonth.plusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        var beginDay = StringUtils.isNotBlank(request.getSelectedDate()) ? LocalDate.parse(request.getSelectedDate(), CommonConst.Datefomat_yyyyMMdd) : DateUtil.LocalDateMIN();
        var endDay = beginDay.plusDays(1);
        List<GetUserSubjectListH5View> views = new LinkedList<GetUserSubjectListH5View>();
        if (userInfo.getUserTypeId() == UserTypeEnum.Student.getCode()) {
            views = request.getTypeEnum() == GetUserSubjectListH5Enum.Mine.getCode() ?
                    subjectService.GetUserSubjectListH5(beginMonth, endMonth, beginDay, endDay, 0, userInfo.getUserId(), request.getSystemTypeId(), request.getSubjectGenreId())
                    : subjectService.GetALLSubjectListH5(beginMonth, endMonth, beginDay, endDay, request.getSystemTypeId(), request.getUserId(), request.getSubjectGenreId());
        } else {
            views = subjectService.GetUserSubjectListH5(beginMonth, endMonth, beginDay, endDay, request.getTypeEnum() == GetUserSubjectListH5Enum.Mine.getCode() ? userInfo.getUserId() : 0, 0, request.getSystemTypeId(), request.getSubjectGenreId());
        }
        var result = new GetUserSubjectListH5Response();
        Map<String, List<GetUserSubjectListH5View>> groupMap = views.stream().collect(Collectors.groupingBy(GetUserSubjectListH5View::BeginTimeFomatToyyyyMMdd));
        var sortMap = sortByKey(groupMap);
        for (Map.Entry<String, List<GetUserSubjectListH5View>> d : sortMap.entrySet()) {
            var subjectListH5ByDay = new GetUserSubjectListH5ByDay().setBeginDate(d.getKey());
            d.getValue().sort(Comparator.comparing(GetUserSubjectListH5View::getBeginTime));
            d.getValue().forEach(s -> {
                var detail = new GetUserSubjectH5Detail().setSubjectId(UrlUtil.encrypt(s.getSubjectId()))
                        .setSubjectRoomName(String.format("%s %s", s.getSchoolName(), s.getSubjectRoomName()))
                        .setBeginTime(s.getBeginTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                        .setSubjectName(s.getSubjectName())
                        .setTeacherName(userService.getByKey(s.getTeacherId(), true).getUserTrueName())
                        .setIsMaxRegisterNumber((s.getRealRegisterNumber() >= s.getMaxRegisterNumber() || s.getRealRegisterNumber() >= CommonConfig.getMaxRegisterNumber()))
                        .setStatusFlag(CommonUtil.GetSubjectStatusEnum(s).getIndex()).setRegistEndTime(s.getRegistEndTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")));
                if (request.getTypeEnum() == GetUserSubjectListH5Enum.Mine.getCode()) {
                    detail.setIsRegister(true);
                } else {
                    detail.setIsRegister(s.isIsRegister());
                }

                subjectListH5ByDay.getSubjectList().add(detail);
            });
            result.getDateList().add(subjectListH5ByDay);
        }
        return ResponseBase.GetSuccessResponse(result);
    }

    public <K extends Comparable<? super K>, V> Map<K, V> sortByKey(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();

        map.entrySet().stream()
                .sorted(Map.Entry.<K, V>comparingByKey()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

    @Override
    public ResponseBase GetStudentSubjectListH5(GetStudentSubjectListH5ApiRequest request) {
        var list = subjectService.GetStudentSubjectListH5(request.getUserId(), request.getSystemTypeId());
        var result = new GetStudentSubjectListH5Response();
        Map<Integer, List<GetStudentSubjectListH5View>> groupMap = list.stream().collect(Collectors.groupingBy(GetStudentSubjectListH5View::getSubjectId));
        for (Map.Entry<Integer, List<GetStudentSubjectListH5View>> m : groupMap.entrySet()) {
            var mySubjectH5Detail = new GetMySubjectH5Detail();
            mySubjectH5Detail.setSubjectName(m.getValue().get(0).getSubjectName()).setSubjectId(UrlUtil.encrypt(m.getValue().get(0).getSubjectId())).setCourseId(m.getValue().get(0).getCourseId());
            m.getValue().forEach(s -> {
                mySubjectH5Detail.getExamSetList().add(new SubjectExamSetH5().setExamSetId(UrlUtil.encrypt(s.getExamSetId())).setExamSetName(s.getExamSetName()).setExamSetTypeId(s.getExamSetTypeId()).setIsFinish(s.getIsFinish() > 0 ? true : false));
            });
            result.getSubjectList().add(mySubjectH5Detail);
        }
        return ResponseBase.GetSuccessResponse(result);
    }

    @Override
    public ResponseBase GetStudentSubjectList(GetStudentSubjectListApiRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("studentId", request.getUserId());
        map.put("courseId", request.getCourseId());
        map.put("gradeId", request.getGradeId());
        map.put("isEnd", request.getTypeEnum());
        map.put("isMine", request.getIsMySubject());
        map.put("pageSkip", request.getPageSkip());
        map.put("pageSize", request.getPageSize());
        map.put("isOnline", request.getSystemTypeId());
        map.put("subjectGenreId", request.getSubjectGenreId());
        map.put("keyWord", StringUtils.isBlank(request.getKeyword()) ? StringUtils.EMPTY : request.getKeyword());
        map.put("totalCount", 0);
        var list = subjectService.GetStudentSubjectList(map);
        var courses = gradeCourseService.getAllCourse(true);
        var result = new GetStudentSubjectListResponse();
        result.setCount(Integer.parseInt(map.get("totalCount").toString()));
        list.forEach(d -> {
            Course course = courses.stream().filter(x -> x.getCourseId() == d.getCourseId()).findFirst().orElse(new Course());
            result.getSubjectList().add(new GetStudentSubjectList()
                    .setBeginTime(d.getBeginTime().format(CommonConst.Datefomat_yyyyMMddHHmm))
                    .setCourseShortCode(course.getShortCode())
                    .setImagePath(d.getImagePath())
                    .setIsRegister(d.getIsRegister())
                    .setMaxRegisterNumber(d.getMaxRegisterNumber())
                    .setRegistEndTime(d.getRegistEndTime().format(CommonConst.Datefomat_yyyyMMddHHmm))
                    .setRegisterNumber(d.getRegisterNumber() >= CommonConfig.getMaxRegisterNumber() ? d.getMaxRegisterNumber() : d.getRegisterNumber())
                    .setSchoolName(d.getSchoolName())
                    .setStatusFlag(CommonUtil.GetSubjectStatusEnum(d).getIndex())
                    .setSubjectId(UrlUtil.encrypt(d.getSubjectId()))
                    .setSubjectName(d.getSubjectName())
                    .setTeacherName(d.getUserTrueName()));
        });
        if (request.getIsMySubject() == 1) {
            var subjectIdList = list.stream().map(GetStudentSubjectListView::getSubjectId).collect(Collectors.toList());
            var examSetList = examSetService.getExamSetList(subjectIdList);
            var studentAnswerList = studentAnswerService.getStudentAnswerBySubjectId(request.getUserId(), StringUtils.join(subjectIdList, ","));
            var subjectFileList = subjectFileService.GetBySubjectIds(subjectIdList);
            for (var subject : result.getSubjectList()) {
                examSetList.stream().filter(d -> UrlUtil.encrypt(d.getSubjectId()).equalsIgnoreCase(subject.getSubjectId())).forEach(d -> {
                    subject.getExamSetList().add(new StudentExamSetDetail()
                            .setExamSetId(UrlUtil.encrypt(d.getExamSetId()))
                            .setExamSetTypeId(d.getExamSetTypeId())
                            .setIsFinish(studentAnswerList.stream().anyMatch(x -> x.getExamSetId() == d.getExamSetId())));
                });
                subjectFileList.stream().filter(d -> UrlUtil.encrypt(d.getSubjectId()).equalsIgnoreCase(subject.getSubjectId())).forEach(d ->
                {
                    subject.getSubjectFileList().add(new StudentSubjectFileDetail().setSubjectFileName(d.getSubjectFileName()).setFilePath(d.getFilePath()));
                });
            }
        }
        return ResponseBase.GetSuccessResponse(result);
    }

    @Override
    public ResponseBase GetSubjectListCount(CurrentUserApiRequest request) {
        var weekFirstDay = LocalDateTime.now().with(DayOfWeek.MONDAY);
        var subjectList = subjectService.GetTeacherSubjectCount(request.getUserId(), weekFirstDay.plusDays(7), weekFirstDay, request.getSystemTypeId());
        var result = new GetSubjectListCountResponse().setTotal(subjectList.getTotal()).setTotalWeek(subjectList.getTotalWeek()).setCompletedTotalWeek(subjectList.getCompletedTotalWeek());
        return ResponseBase.GetSuccessResponse(result);
    }

    @Override
    public ResponseBase GetTeacherSubjectDataH5(GetTeacherSubjectDataH5ApiRequest request) {
        var subjectList = subjectService.GetListByTeacherId(request.getUserId(), request.getSystemTypeId());
        var result = new GetTeacherSubjectDataH5Response();
        if (subjectList.size() > 0) {
            var subjectIds = subjectList.stream().map(Subject::getSubjectId).collect(Collectors.toList());
            var classAttendanceList = subjectService.GetClassAttendance(subjectIds);
            var praiseList = subjectService.GetPraise(subjectIds);
            var tacherSubjectDetailJoinObjs = new LinkedList<GetTeacherSubjectDetailJoinObj>();
            for (var subject : subjectList) {
                for (var classAttendance : classAttendanceList) {
                    if (subject.getSubjectId() == classAttendance.getSubjectId()) {
                        tacherSubjectDetailJoinObjs.add(new GetTeacherSubjectDetailJoinObj()
                                .setSubjectId(subject.getSubjectId())
                                .setBeginTime(subject.getBeginTime().format(CommonConst.Datefomat_yyyyMMdd2))
                                .setSubjectName(subject.getSubjectName())
                                .setClassAttendance(classAttendance.getClassAttendance()));
                    }
                }
            }
            for (var joinObj : tacherSubjectDetailJoinObjs) {
                for (var praise : praiseList) {
                    if (joinObj.getSubjectId() == praise.getSubjectId()) {
                        result.getSubjectList().add(new GetTeacherSubjectDataH5Detail()
                                .setSubjectId(UrlUtil.encrypt(joinObj.getSubjectId()))
                                .setBeginTime(joinObj.getBeginTime())
                                .setSubjectName(joinObj.getSubjectName())
                                .setClassAttendance(joinObj.getClassAttendance())
                                .setPraise(praise.getTheSum().divide(new BigDecimal(praise.getTheCount()), 1, RoundingMode.HALF_UP).setScale(1)));
                    }
                }
            }
            if (request.getSortId() == GetMySubjectDataSortEnum.BeginTimeDesc.getCode()) {
                result.getSubjectList().sort(Comparator.comparing(GetTeacherSubjectDataH5Detail::getBeginTime).reversed());
            } else if (request.getSortId() == GetMySubjectDataSortEnum.ClassAttendanceDesc.getCode()) {
                result.getSubjectList().sort(Comparator.comparing(GetTeacherSubjectDataH5Detail::getClassAttendance).reversed());
            } else if (request.getSortId() == GetMySubjectDataSortEnum.PraiseDesc.getCode()) {
                result.getSubjectList().sort(Comparator.comparing(GetTeacherSubjectDataH5Detail::getPraise).reversed());
            }
        }
        return ResponseBase.GetSuccessResponse(result);
    }

    @Override
    public ResponseBase SearchStudentSubject(SearchStudentSubjectApiRequest request) {
        var subjectList = subjectService.SearchStudentSubject(request.getKey(), request.getUserId(), LocalDateTime.now(), request.getSystemTypeId());
        var result = new SearchStudentSubjectResponse();
        subjectList.sort(Comparator.comparing(SearchStudentSubjectView::getBeginTime));
        subjectList.forEach(d -> {
            result.getSubjectList().add(new GetUserSubjectH5Detail(d.getSubjectId(), d.getSubjectRoomName(), d.getSchoolName(), d.getBeginTime(), d.getUserTrueName(), d.getSubjectName(), false, DateUtil.LocalDateTimeMIN(), false, 0));
        });
        return ResponseBase.GetSuccessResponse(result);
    }

    @Override
    public ResponseBase GetSubjectInfoForShow(GetSubjectInfoForShowApiRequest request) {
        int subjectId = UrlUtil.decrypt(request.getSubjectId(), Integer.class);
        var subJect = subjectService.getByKey(subjectId, true);
        if (subJect == null) {
            return ResponseBase.GetValidateErrorResponse("找不到该课程，获取失败");
        }
        var subjectRooms = subjectRoomService.getSubjectRoomList(subjectId);
        var examSets = examSetService.getExamSetListForTeacher(subjectId, 0, StringUtils.EMPTY, 0, 1000, 1);
        var allCourse = gradeCourseService.getAllCourse(true);
        var allGrade = gradeCourseService.getAllGrade();
        var result = ConvertResponse(subJect, allCourse, allGrade, subjectRooms, examSets.second, request);
        return ResponseBase.GetSuccessResponse(result);
    }

    /// <summary>
    /// 转换输出对象
    /// </summary>
    /// <param name="subject"></param>
    /// <param name="allCourse"></param>
    /// <param name="allGrade"></param>
    /// <param name="subjectRooms"></param>
    /// <param name="examSetListViews"></param>
    /// <param name="request"></param>
    /// <returns></returns>
    private GetSubjectInfoForShowResponse ConvertResponse(
            Subject subject,
            List<Course> allCourse,
            List<Grade> allGrade,
            List<SubjectRoomTeacherNameView> subjectRooms,
            List<ExamSetListView> examSetListViews,
            GetSubjectInfoForShowApiRequest request) {
        var teacher = userService.getByKey(subject.getTeacherId(), true);
        Course course = allCourse.stream().filter(g -> g.getCourseId() == subject.getCourseId()).findFirst().orElse(new Course());
        Grade grade = allGrade.stream().filter(g -> g.getGradeId() == subject.getGradeId()).findFirst().orElse(new Grade());
        var result = new GetSubjectInfoForShowResponse()
                .setSubjectId(UrlUtil.encrypt(subject.getSubjectId()))
                .setSubjectName(subject.getSubjectName())
                .setBeginTime(subject.getBeginTime().format(CommonConst.datefomat_nomal2))
                .setGradeName(grade.getGradeName())
                .setCourseName(course.getCourseName())
                .setStatusFlag(CommonUtil.GetSubjectStatusEnum(subject).getIndex())
                .setTeacherId(UrlUtil.encrypt(subject.getTeacherId()))
                .setIsShowNumber(subject.getTeacherId() == request.getUserId())
                .setRegisterTotalCount(subjectRooms.stream().mapToInt(p -> p.getRealRegisterNumber()).sum())
                .setTeacherName(StringUtils.isBlank(teacher.getUserTrueName()) ? teacher.getUserName() : teacher.getUserTrueName())
                .setSubjectRooms(GetSubjectInfoForShowDetails(subjectRooms, request))
                .setExamSetList(GetSubjectExamSetDetails(examSetListViews));
        return result;
    }

    /// <summary>
    /// 获取教室列表
    /// </summary>
    /// <param name="subjectRooms"></param>
    /// <param name="request"></param>
    /// <returns></returns>
    private List<SubjectInfoForShowDetail> GetSubjectInfoForShowDetails(List<SubjectRoomTeacherNameView> subjectRooms, GetSubjectInfoForShowApiRequest request) {
        var result = new LinkedList<SubjectInfoForShowDetail>();
        for (var room : subjectRooms) {
            result.add(new SubjectInfoForShowDetail()
                    .setSubjectRoomId(UrlUtil.encrypt(room.getSubjectRoomId()))
                    .setSubjectRoomName(room.getSubjectRoomName())
                    .setSchoolName(room.getSchoolName())
                    .setTeacherId(UrlUtil.encrypt(room.getTeacherId()))
                    .setTeacherName(room.getUserTrueName())
                    .setRegisterNumber(room.getRealRegisterNumber())
                    .setClassAttendance(room.getAttendNumber())
                    .setIsShowRoomNumber(room.getTeacherId() == request.getUserId()));
        }
        return result;
    }

    /// <summary>
    /// 获取作业列表
    /// </summary>
    /// <param name="examSetListViews"></param>
    /// <returns></returns>
    private List<SubjectExamSetDetail> GetSubjectExamSetDetails(List<ExamSetListView> examSetListViews) {
        var result = new LinkedList<SubjectExamSetDetail>();
        for (var item : examSetListViews) {
            result.add(new SubjectExamSetDetail()
                    .setExamSetId(UrlUtil.encrypt(item.getExamSetId()))
                    .setExamSetName(item.getExamSetName())
                    .setExamSetTypeId(item.getExamSetTypeId())
                    .setAnswerNumber(item.getAnswerCount()));

        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase DeleteExamSetOrFile(DeleteExamSetOrFileApiRequest request) {
        int result = 0;
        if (CollectionUtils.isNotEmpty(request.getExamSetIdList())) {
            List<Integer> ids = new LinkedList<>();
            request.getExamSetIdList().forEach(x -> {
                ids.add(UrlUtil.decrypt(x, Integer.class));
            });
            result += examSetService.deleteExamSetList(ids);
        }
        if (CollectionUtils.isNotEmpty(request.getSubjectFileIdList())) {
            List<Integer> ids = new LinkedList<>();
            request.getSubjectFileIdList().forEach(x -> {
                ids.add(UrlUtil.decrypt(x, Integer.class));
            });
            result += this.subjectFileService.DeleteSubjectFile(ids);
        }
        return ResponseBase.GetSuccessResponse(result > 0 ? 1 : 0);
    }

    @Override
    public ResponseBase GetStudentList(GetStudentListApiRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("teacherId", request.getUserId());
        map.put("subjectId", UrlUtil.decrypt(request.getSubjectId(), Integer.class));
        map.put("subjectRoomId", UrlUtil.decrypt(request.getSubjectRoomId(), Integer.class));
        map.put("searchKey", request.getStudentName());
        map.put("isGetAttendance", request.getTypeEnum() == GetStudentListEnum.AttendNumber.getCode() ? 1 : 0);
        map.put("pageSkip", request.getPageSkip());
        map.put("pageSize", request.getPageSize());
        map.put("totalCount", 0);
        var viewList = subjectRoomStudentService.GetStudentList(map);
        var gradeList = gradeCourseService.getAllGrade();
        var result = new GetStudentListResponse().setCount(Integer.parseInt(map.get("totalCount").toString()));
        viewList.forEach(d -> {
            Grade grade = gradeList.stream().filter(g -> g.getGradeId() == d.getGradeId()).findFirst().orElse(null);
            result.getStudentList().add(new GetStudentDetail().setCreateDateTime(d.getCreateDateTime().format(CommonConst.datefomat_nomal2))
                    .setGradeName(grade == null ? "--" : grade.getGradeName()).setHasEvaluateRecord(d.isHasEvaluateRecord())
                    .setSchoolName(d.getSchoolName()).setStudentId(UrlUtil.encrypt(d.getUserId())).setStudentName(d.getUserTrueName()).setUserFace(d.getUserFace()));
        });
        return ResponseBase.GetSuccessResponse(result);
    }

    @Override
    public ResponseBase GetSubjectFileList(GetSubjectFileListApiRequest request) {
        List<SubjectFile> filtersList = new LinkedList<>();
        var list = subjectFileService.GetBySubjectId(UrlUtil.decrypt(request.getSubjectId(), Integer.class));
        if (StringUtils.isNotBlank(request.getFileName())) {
            list = list.stream().filter(m -> m.getSubjectFileName().contains(request.getFileName())).collect(Collectors.toList());
        }
        if (CollectionUtils.isNotEmpty(request.getFileType())) {
            for (var m : list) {
                for (var n : request.getFileType()) {
                    if (m.getFileType().replace(".", "").trim().toLowerCase().equals(n)) {
                        filtersList.add(m);
                    }
                }
            }
            list = filtersList;
        }
        var result = new GetSubjectFileListResponse().setCount(list.size());
        list = list.stream().skip((request.getPageIndex() - 1) * request.getPageSize()).limit(request.getPageSize()).collect(Collectors.toList());
        list.forEach(m -> {
            result.getFileList().add(new SubjectFileDetail()
                    .setFilePath(m.getFilePath())
                    .setFileName(m.getSubjectFileName())
                    .setFileId(UrlUtil.encrypt(m.getSubjectFileId()))
                    .setFileType(m.getFileType())
                    .setUpdateDateTime(m.getUpdateDateTime().format(CommonConst.Datefomat_yyyyMMdd2)));
        });
        return ResponseBase.GetSuccessResponse(result);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase SaveSubjectFile(SaveSubjectFileApiRequest request) throws Exception {
        var now = LocalDateTime.now();
        var fileType = request.getFilePath().substring(request.getFilePath().lastIndexOf(".")).replace(".", "").trim().toLowerCase();
        var result = subjectFileService.Save(new SubjectFile()
                .setCreateDateTime(LocalDateTime.now())
                .setFilePath(request.getFilePath())
                .setStatusFlag(1)
                .setSubjectId(UrlUtil.decrypt(request.getSubjectId(), Integer.class))
                .setFileType(fileType).setUpdateDateTime(now).setSubjectFileName(request.getFileName()).setSubjectFileId(0));
        return ResponseBase.GetSuccessResponse(result > 0 ? 1 : 0);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase RegistSubject(RegistSubjectApiRequest request) {
        var subjectId = UrlUtil.decrypt(request.getSubjectId(), Integer.class);
        var subject = subjectService.getByKey(subjectId, true);
        if (subject == null) {
            return ResponseBase.GetValidateErrorResponse("找不到该课程，获取失败");
        }
        if (LocalDateTime.now().isAfter(subject.getRegistEndTime())) {
            return ResponseBase.GetSuccessResponse(1);
        }
        return ResponseBase.GetSuccessResponse(subjectService.studentRegistSubject(subjectId, request.getUserId(), true) > 0 ? 1 : 0);
    }

    @Override
    public ResponseBase StudentGetSubjectFileList(StudentGetSubjectFileListApiRequest request) {
        var subjectId = UrlUtil.decrypt(request.getSubjectId(), Integer.class);
        var list = subjectFileService.GetBySubjectId(subjectId);
        var result = new GetSubjectFileListResponse().setCount(list.size());
        list.forEach(m -> {
            result.getFileList().add(new SubjectFileDetail()
                    .setFilePath(m.getFilePath())
                    .setFileName(m.getSubjectFileName())
                    .setFileType(m.getFileType())
                    .setFileId(UrlUtil.encrypt(m.getSubjectFileId()))
                    .setUpdateDateTime(m.getUpdateDateTime().format(CommonConst.Datefomat_yyyyMMdd2)));
        });
        return ResponseBase.GetSuccessResponse(result);
    }

    @Override
    public ResponseBase GetCurrentSubjectName(CurrentUserApiRequest request) {
        var beginTime = LocalDate.now();
        var subjectList = subjectService.GetTeacherSubjectByDate(request.getUserId(), request.getUserId(), beginTime, beginTime.plusDays(1), request.getSystemTypeId());
        var result = new GetCurrentSubjectNameResponse();
        subjectList.forEach(d -> {
            result.getSubjectList().add(new GetCurrentSubjectNameDetail().setSubjectId(UrlUtil.encrypt(d.getSubjectId())).setSubjectName(d.getSubjectName()));
        });
        return ResponseBase.GetSuccessResponse(result);
    }

    @Override
    public ResponseBase GetSubjectVideo(GetSubjectVideoApiRequest request) {
        var subjectId = UrlUtil.decrypt(request.getSubjectId(), Integer.class);
        var subject = subjectService.getByKey(subjectId, true);
        int userType = request.getUserTypeId();
        if (subject == null) {
            return ResponseBase.GetValidateErrorResponse("找不到该课程，获取失败");
        }
        var teacher = userService.getByKey(subject.getTeacherId(), true);
        var examSets = examSetService.getExamSetList(subject.getSubjectId());
        var examSet = examSets.stream().filter(m -> m.getExamSetTypeId() == ExamSetTypeEnum.AlongClass.getIndex()).findFirst().orElse(null);
        var videoList = GetVideoList(subject, 1);
        var pptVideoList = GetVideoList(subject, 2);
        var result = new GetSubjectVideoResponse()
                .setPPTVideoUrl(pptVideoList)
                .setSubjectId(UrlUtil.encrypt(subjectId))
                .setSubjectName(subject.getSubjectName())
                .setExamSetId(examSet == null ? StringUtils.EMPTY : UrlUtil.encrypt(examSet.getExamSetId()))
                .setTeacherName(teacher.getUserTrueName())
                .setTeacherId(teacher.getUserId())
                .setIsFinish(subject.getClassStateId() == 3)
                .setVideoUrl(videoList).setUid(request.getUserId());
        result.setSubjectStatus(CommonUtil.GetSubjectStatusEnum(subject).getIndex());
        var user = userService.getByKey(request.getUserId(), true);
        if (Arrays.stream(user.getUserRoles().split(",")).mapToInt(Integer::valueOf).anyMatch(d -> d == UserRoleEnum.Student.getCode())) {
            var subroom = subjectRoomService.getSubjectRoomByStudentId(subjectId, request.getUserId());
            if (subroom != null) {
                result.setHelperTeacherId(subroom.getTeacherId()).setChannel(subroom.getSubjectRoomId());
            } else {
                UserSubjectRoomCouchBaseObject UserSubjectRoom = talkMessageService.getUserSubjectRoomInfo(request.getUserId(), user.getUserTypeId(), subjectId);
                if (UserSubjectRoom != null) {
                    result.setChannel(UserSubjectRoom.getSubjectRoomId());
                }
            }
            result.setIsFinish(GetExamSetIsFinish(examSet, request.getUserId()));
        } else {
            UserSubjectRoomCouchBaseObject userSubjectRoom = talkMessageService.getUserSubjectRoomInfo(request.getUserId(), user.getUserTypeId(), subjectId);
            if (userSubjectRoom == null) {
                if (subject.getTeacherId() == request.getUserId()) {
                    List<SubjectRoom> subjectRooms = subjectRoomService.getSubjectRoomBySubjectId(subjectId);
                    if (CollectionUtils.isNotEmpty(subjectRooms)) {
                        result.setChannel(subjectRooms.get(0).getSubjectRoomId());
                    }
                }
            } else {
                result.setChannel(userSubjectRoom.getSubjectRoomId());
            }
        }
        if (subject.getClassStateId() == 2 && (result.getChannel() == null || result.getChannel().intValue() <= 0)) {
            var subjectRoomId = 0;
            if (userType == UserTypeEnum.Teacher.getCode()) {
                subjectRoomId = subjectService.teacherRegistSubject(subjectId, request.getUserId(), false);
                if (subjectRoomId <= 0) {
                    return ResponseBase.GetValidateErrorResponse("");
                }
                subjectRoomService.clearSubjectRoomUserListCache(subjectRoomId);
            } else if (userType == UserTypeEnum.Student.getCode()) {
                if (subjectService.getByKey(subjectId, true).getSubjectGenreId() == SubjectGenreEnum.PublicBenefit.getIndex()) {
                    subjectRoomId = subjectService.studentRegistSubject(subjectId, request.getUserId(), false);
                    if (subjectRoomId <= 0) {
                        return ResponseBase.GetValidateErrorResponse(ErrorMessageStudent);
                    }
                    subjectRoomService.clearSubjectRoomUserListCache(subjectRoomId);
                } else {
                    return ResponseBase.GetValidateErrorResponse(ErrorMessageUnregistered);
                }
            } else {
                return ResponseBase.GetValidateErrorResponse(ErrorMessageUnregistered);
            }
            result.setChannel(subjectRoomId);
        }
        return ResponseBase.GetSuccessResponse(result);
    }

    /// <summary>
    /// 获取学生作业是否完成
    /// </summary>
    /// <param name="examSet"></param>
    /// <param name="userId"></param>
    /// <returns></returns>
    private boolean GetExamSetIsFinish(ExamSet examSet, int userId) {
        if (examSet != null) {
            var studentAnswer = studentAnswerService.getByKey(examSet.getExamSetId(), userId);
            if (studentAnswer == null) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /// <summary>
    /// 获取硬件厂家的视频播放地址
    /// </summary>
    /// <param name="subject"></param>
    /// <param name="type">1主路 2ppt线路</param>
    /// <returns></returns>
    private List<String> GetVideoList(Subject subject, int type) {
        var result = new LinkedList<String>();
        List<VHVideoView> list = JSONArray.parseArray(type == 1 ? subject.getFilePath() : subject.getPptFilePath(), VHVideoView.class);
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        int index = 1;
        for (var item : list) {
            result.add(videoHardwareService.GetVideoPath(item.getUrl(), subject.getSubjectId(), true, index++, type));
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase UpdateSubjectStudentStatus(UpdateSubjectStudentStatusApiRequest request) {
        var userId = UrlUtil.decrypt(request.getStudentId(), Integer.class);
        if (userId <= 0) {
            userId = request.getUserId();
        }
        var subject = subjectService.getByKey(UrlUtil.decrypt(request.getSubjectId(), Integer.class), true);
        if (subjectRoomStudentService.UpdateSubjectStudentStatus(subject.getSubjectId(), userId, request.StatusFlag == 1 ? 1 : 0)) {
            studentStudyRecordService.Save(new StudentStudyRecord()
                    .setContent(String.format("完成%s课程", subject.getSubjectName()))
                    .setCreateDateTime(LocalDateTime.now())
                    .setExamSetId(0)
                    .setStudyRecordTypeId(StudyRecordTypeEnum.Subject.getCode())
                    .setSubjectId(subject.getSubjectId())
                    .setUpdateDateTime(LocalDateTime.now())
                    .setUserId(userId));
        }
        return ResponseBase.GetSuccessResponse();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase UpdateSubjectStatusOfAny(UpdateSubjectStatusOfAnyApiRequest request) {
        int id = 0;
        boolean error = false;
        boolean isSubject = false;
        var subjectid = UrlUtil.decrypt(request.getSConfid(), String.class);
        if (StringUtils.isBlank(subjectid)) {
            error = true;
            isSubject = false;
        } else {
            var strs = subjectid.split("_");
            if (strs.length != 2) {
                error = true;
                isSubject = false;
            }
            id = Integer.parseInt(strs[1]);
            isSubject = "sub".equals(strs[0]);
        }

        var list = request.getVideos();//VideosFormatHelp.VideosFormat(apiRequest.Videos);
        if (error || CollectionUtils.isEmpty(list)) {
            log.info(MessageFormat.format("请求参数错误，硬件调用修改课程状态接口:{0}", JSON.toJSONString(request)));
            return ResponseBase.GetValidateErrorResponse();
        }
        var result = isSubject ? SaveSubject(id, request, list) : SaveWeiKe(id, request, list);
        return ResponseBase.GetSuccessResponse(result > 0 ? 1 : 0);
    }

    /// <summary>
    /// 保存课程信息
    /// </summary>
    /// <param name="id"></param>
    /// <param name="apiRequest"></param>
    /// <param name="list"></param>
    /// <returns></returns>
    private int SaveSubject(int id, UpdateSubjectStatusOfAnyApiRequest request, List<VHVideoView> list) {
        var subject = subjectService.getByKey(id, false);
        if (subject == null) {
            log.info(MessageFormat.format("找不到课程信息，硬件调用修改课程状态接口:{0}", JSON.toJSONString(request)));
            return -1;
        }
        var pptlist = list.stream().filter(x -> x.getVideostype() == 1).sorted(Comparator.comparing(VHVideoView::getFilename)).collect(Collectors.toList());
        var filelist = list.stream().filter(x -> x.getVideostype() == 0).sorted(Comparator.comparing(VHVideoView::getFilename)).collect(Collectors.toList());
        subject.setClassStateId(request.getStatusFlag() + 1);

        /**if (subject.getClassStateId() == 3) {
         removeAliLiveConfig(subject);
         }**/

        if (CollectionUtils.isNotEmpty(pptlist)) {
            if (request.getStatusFlag() == 1) {
                //pptlist.forEach(x -> x.setUrl(x.getUrl().replace(CommonConst.AVCON_PUSH_PORT_1935, CommonConst.AVCON_HLS_PROT_8299).replace("rtmp://", "https://").replace("/live/", "/hls/") + CommonConst.PINGOS_HLS_STUFF));
                /** pptlist.forEach(x -> {
                 x.setUrl(String.format("https://%s:%s%s", CommonConst.DOMAIN, CommonConst.AVCON_HLS_PROT_8299, x.getUrl().substring(x.getUrl().indexOf("/live/"))));
                 });*/

                pptlist.forEach(x -> {
                    x.setUrl(String.format("https://%s:%s%s%s", CommonConst.DOMAIN, CommonConst.AVCON_HLS_PROT_8299, x.getUrl().substring(x.getUrl().indexOf("/live/")).replace("/live/", "/hls/"), CommonConst.PINGOS_HLS_STUFF));
                });
            }
            var pptListString = JSONArray.toJSONString(pptlist);
            subject.setPptFilePath(pptListString);
            subject.setRealBeginTimeZone(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
            subjectService.UpdatePPTFilePath(subject);
        }
        if (CollectionUtils.isNotEmpty(filelist)) {
            if (request.getStatusFlag() == 1) {
                //filelist.forEach(x -> x.setUrl(x.getUrl().replace(CommonConst.AVCON_PUSH_PORT_1935, CommonConst.AVCON_HLS_PROT_8299).replace("rtmp://", "https://").replace("/live/", "/hls/") + CommonConst.PINGOS_HLS_STUFF));
                /**filelist.forEach(x -> {
                 x.setUrl(String.format("https://%s:%s%s", CommonConst.DOMAIN, CommonConst.AVCON_HLS_PROT_8299, x.getUrl().substring(x.getUrl().indexOf("/live/"))));
                 });*/
                filelist.forEach(x -> {
                    x.setUrl(String.format("https://%s:%s%s%s", CommonConst.DOMAIN, CommonConst.AVCON_HLS_PROT_8299, x.getUrl().substring(x.getUrl().indexOf("/live/")).replace("/live/", "/hls/"), CommonConst.PINGOS_HLS_STUFF));
                });
            }
            var fileListString = JSONArray.toJSONString(filelist);
            subject.setFilePath(fileListString);
            subject.setRealBeginTimeZone(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
            subjectService.UpdateFilePath(subject);
        }
        //更新缓存
        subjectService.getByKey(id, false);
        return 1;
    }

    /// <summary>
    /// 删除阿里云直播拉流配置
    /// </summary>
    /// <param name="subject"></param>
    private void removeAliLiveConfig(Subject subject) {
        var filePathList = JSONArray.parseArray(subject.getFilePath(), VHVideoView.class);
        var pptFilePathlist = JSONArray.parseArray(subject.getPptFilePath(), VHVideoView.class);
        if (filePathList != null) {
            for (var item : filePathList) {
                videoHardwareService.RemAliLiveConfigs(subject.getSubjectId(), item.getUrl(), 1);
            }
        }
        if (pptFilePathlist != null) {
            for (var item : pptFilePathlist) {
                videoHardwareService.RemAliLiveConfigs(subject.getSubjectId(), item.getUrl(), 2);
            }
        }
    }


    /// <summary>
    /// 保存微课信息
    /// </summary>
    /// <param name="id"></param>
    /// <param name="apiRequest"></param>
    /// <param name="list"></param>
    /// <returns></returns>
    int SaveWeiKe(int id, UpdateSubjectStatusOfAnyApiRequest request, List<VHVideoView> list) {
        var weike = weiKeService.GetByKey(id);
        if (weike == null) {
            log.info(MessageFormat.format("找不到微课信息，硬件调用修改课程状态接口:{0}", JSON.toJSONString(request)));
            return -1;
        }
        var pptlist = list.stream().filter(x -> x.getVideostype() == 1).sorted(Comparator.comparing(VHVideoView::getFilename)).collect(Collectors.toList());
        var filelist = list.stream().filter(x -> x.getVideostype() == 0).sorted(Comparator.comparing(VHVideoView::getFilename)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(pptlist)) {
            weiKeService.UpdatePPTFilePath(id, JSONArray.toJSONString(pptlist));
        }
        if (CollectionUtils.isNotEmpty(filelist)) {
            weiKeService.UpdateFilePath(id, JSONArray.toJSONString(filelist));
        }
        return 1;
    }

    @Override
    public ResponseBase GetDeviceList(RequestBase request) throws Exception {
        var list = videoHardwareService.GetDeviceList();
        var result = new GetDeviceListResponse();
        result.setDeviceList(list);
        return ResponseBase.GetSuccessResponse(result);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase CreateVideoLive(CreateVideoLiveApiRequest request) throws Exception {
        var result = request.getIsSubject() == 1 ? SaveSubject(request) : SaveWeiKe(request);
        return ResponseBase.GetSuccessResponse(result);
    }

    /// <summary>
    /// 保存课程信息
    /// </summary>
    /// <param name="request"></param>
    /// <returns></returns>
    private int SaveSubject(CreateVideoLiveApiRequest request) throws Exception {
        var subject = subjectService.getByKey(UrlUtil.decrypt(request.getId(), Integer.class), true);
        if (subject == null) {
            return -1;
        }
        if (subject.getClassStateId() == 3 || subject.getClassStateId() == 4) {
            return 2;
        }
        var vhResult = videoHardwareService.createConf(subject.getSubjectName(), UrlUtil.encrypt(String.format("%s_%s", "sub", subject.getSubjectId())), request.getDiviceId(), StringUtils.isBlank(subject.getVideoRoom()) ? 1 : 2);
        if (vhResult.getNResult() == 1) {
            if (StringUtils.isNotBlank(vhResult.getSMeetingId())) {
                subject.setVideoRoom(String.format("%s_%s", request.getDiviceId(), vhResult.getSMeetingId()));
            } else {
                subject.setVideoRoom(String.format("%s_%s", request.getDiviceId(), StringUtils.EMPTY));
            }
            subjectService.Save(subject);
            return 1;
        }
        return 0;
    }

    /// <summary>
    /// 保存微课信息
    /// </summary>
    /// <param name="request"></param>
    /// <returns></returns>
    private int SaveWeiKe(CreateVideoLiveApiRequest request) throws Exception {
        var weike = weiKeService.GetByKey(UrlUtil.decrypt(request.getId(), Integer.class));
        if (weike == null) {
            return -1;
        }
        if (StringUtils.isNotBlank(weike.getFilePath()) && weike.getFilePath().length() > 2) //微课不限制
        {
            return 2;
        }
        var vhResult = videoHardwareService.createConf(weike.getWeiKeName(), UrlUtil.encrypt(String.format("%s_%s", "weike", weike.getWeiKeId())), request.getDiviceId(), StringUtils.isBlank(weike.getSourceRoomId()) ? 1 : 2);
        if (vhResult.getNResult() == 1) {
            if (StringUtils.isNotBlank(vhResult.getSMeetingId())) {
                weike.setSourceRoomId(String.format("%s_%s", request.getDiviceId(), vhResult.getSMeetingId()));
            } else {
                weike.setSourceRoomId(String.format("%s_%s", request.getDiviceId(), StringUtils.EMPTY));
            }
            weiKeService.Save(weike);
            return 1;
        }
        return 0;
    }

    @Override
    public ResponseBase GetGroupByStudentList(GetGroupByStudentListRequest request) {
        var subjectId = UrlUtil.decrypt(request.getSubjectId(), Integer.class);
        var subjectRoomId = UrlUtil.decrypt(request.getSubjectRoomId(), Integer.class);
        Map<String, Object> map = new HashMap<>();
        map.put("subjectId", subjectId);
        map.put("subjectRoomId", subjectRoomId);
        map.put("pageSkip", request.getPageSkip());
        map.put("pageSize", request.getPageSize());
        map.put("totalCount", 0);
        var list = this.subjectRoomStudentService.GetByGroupViews(map);
        var result = new GetGroupByStudentListResponse().setTotalCount(Integer.parseInt(map.get("totalCount").toString()));
        list.forEach(x -> {
            result.getStudents().add(new StudentListInfo(x));
        });
        return ResponseBase.GetSuccessResponse(result);
    }

    @Override
    public ResponseBase GetSubjectRoomList(GetSubjectRoomListRequest request) {
        var result = new GetSubjectRoomListResponse();
        int subjectId = UrlUtil.decrypt(request.getSubjectId(), Integer.class);
        var subjectRooms = subjectRoomService.getSubjectRoomList(subjectId);
        subjectRooms.forEach(x -> {
            result.getSubjectRooms().add(new SubjectRoomInfo(x));
        });
        return ResponseBase.GetSuccessResponse(result);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase SaveGroupByStudentList(SaveGroupByStudentListRequest request) throws Exception {
        var result = new SaveGroupByStudentListResponse();
        var subjectRoomId = UrlUtil.decrypt(request.getSubjectRoomId(), Integer.class);
        var subjectId = UrlUtil.decrypt(request.getSubjectId(), Integer.class);
        var subjectRoom = subjectRoomService.getByKey(subjectRoomId);
        var students = subjectRoomStudentService.GetAllBySubjectId(subjectId);
        if (subjectRoom == null || subjectRoom.getSubjectRoomId() <= 0 || CollectionUtils.isEmpty(students)) {
            return ResponseBase.GetValidateErrorResponse("数据有误");
        }
        var studentIds = students.stream().filter(x -> x.getSubjectRoomId() == subjectRoomId).map(SubjectRoomStudent::getUserId).collect(Collectors.toList());
        var newStudents = request.getStudents().parallelStream().filter(x -> !studentIds.contains(x.getUserId())).collect(Collectors.toList());
        if (newStudents.size() <= 0) {
            return ResponseBase.GetSuccessResponse(new SaveGroupByStudentListResponse().setStatusFlag(1));
        }
        var num = subjectRoom.getMaxRegisterNumber() - subjectRoom.getRealRegisterNumber() - newStudents.size();
        if (num < 0) {
            result.setStatusFlag(2);
            result.setNumber(subjectRoom.getMaxRegisterNumber() - subjectRoom.getRealRegisterNumber());
            return ResponseBase.GetSuccessResponse(result);
        }
        result.setStatusFlag(SaveData(newStudents, students, subjectRoomId, subjectId, subjectRoom));
        return ResponseBase.GetSuccessResponse(result);
    }

    /// <summary>
    /// 保存数据
    /// </summary>
    /// <param name="newStudent"></param>
    /// <param name="students"></param>
    /// <param name="subjectRoomId"></param>
    /// <param name="subjectId"></param>
    /// <param name="subjectRoom"></param>
    /// <returns></returns>
    private int SaveData(List<StudentRoom> newStudent, List<SubjectRoomStudent> students, int subjectRoomId, int subjectId, SubjectRoom subjectRoom) throws Exception {
        int count = 0;
        var subjectRooms = new LinkedList<SubjectRoom>();
        newStudent.stream().map(StudentRoom::getSubjectRoomId).distinct().forEach(x -> {
            subjectRooms.add(subjectRoomService.getByKey(UrlUtil.decrypt(x, Integer.class)));
        });

        for (var student : newStudent) {
            for (var x : students) {
                if (x.getUserId() == student.getUserId()) {
                    var index = subjectRooms.indexOf(subjectRooms.stream().filter(y -> y.getSubjectRoomId() == UrlUtil.decrypt(student.getSubjectRoomId(), Integer.class)).findFirst());
                    if (index > -1) {
                        subjectRooms.get(index).setRealRegisterNumber(subjectRooms.get(index).getRealRegisterNumber() - 1);
                    }
                    x.setSubjectRoomId(subjectRoomId);
                    count++;
                }
            }
        }
        subjectRoom.setRealRegisterNumber(subjectRoom.getRealRegisterNumber() + count);
        this.subjectRoomStudentService.DeleteAllBySubjectId(subjectId);
        this.subjectRoomService.save(subjectRoom);
        for (var x : subjectRooms) {
            subjectRoomService.save(x);
        }
        return this.subjectRoomStudentService.BatchInsertSubjectRoomStudent(students) > 0 ? 1 : 0;
    }

    @Override
    public ResponseBase SearchSubject(SearchSubjectRequest request) {
        var result = new SearchSubjectResponse();
        var list = subjectService.SearchSubject(LocalDateTime.now(), request.getKey(), request.getSystemTypeId(), request.getUserId());
        list.forEach(x -> {
            result.getSubjectList().add(new GetUserSubjectH5Detail(x.getSubjectId(), StringUtils.EMPTY, StringUtils.EMPTY, x.getBeginTime(), x.getTeacherName(), x.getSubjectName(), x.getRealRegisterNumber() >= x.getMaxRegisterNumber() || x.getRealRegisterNumber() >= CommonConfig.getMaxRegisterNumber(), x.getRegistEndTime(), x.getIsRegister(), CommonUtil.GetSubjectStatusEnum(x).getIndex()));
        });
        return ResponseBase.GetSuccessResponse(result);
    }

    @Override
    public ResponseBase GetPublicBenefitSubjectList(GetPublicBenefitSubjectListApiRequest request) {
        var result = new GetPublicBenefitSubjectListResponse();
        var gradeList = gradeCourseService.getAllGrade();
        var courseList = gradeCourseService.getAllCourse(true);
        var h5DisableCourseId = courseList.stream().filter(d -> "体育锻炼".equals(d.getCourseName()) || "红色文化教育".equals(d.getCourseName()) || "艺术欣赏".equals(d.getCourseName()) || "健康教育".equals(d.getCourseName()) || "上课前".equals(d.getCourseName()) || "自主作业".equals(d.getCourseName())).map(Course::getCourseId).collect(Collectors.toList());
        var allPublicBenefitSubject = subjectService.getAllPublicBenefitSubject(true);

        if (request.getGradeId() > 0) {
            allPublicBenefitSubject = allPublicBenefitSubject.stream().filter(d -> d.getGradeId() == request.getGradeId()).collect(Collectors.toList());
        }
        if (request.getCourseId() > 0) {
            allPublicBenefitSubject = allPublicBenefitSubject.stream().filter(d -> d.getCourseId() == request.getCourseId()).collect(Collectors.toList());
        }
        if (!request.isIsWeb()) {
            allPublicBenefitSubject = allPublicBenefitSubject.stream().filter(d -> !h5DisableCourseId.contains(d.getCourseId())).collect(Collectors.toList());
        }
        var dateSubjectList = allPublicBenefitSubject.stream().filter(d -> d.getBeginTime().toLocalDate().equals(request.getDate().toLocalDate())).sorted(Comparator.comparing(SubjectListByFilterView::getBeginTime)).collect(Collectors.toList());

        Map<Boolean, List<SubjectListByFilterView>> groupMap = dateSubjectList.stream().collect(Collectors.groupingBy(SubjectListByFilterView::isForenoon));
        for (Map.Entry<Boolean, List<SubjectListByFilterView>> d : groupMap.entrySet()) {
            var groupDetail = new PublicBenefitDateGroupDetail().setDate(d.getKey() ? "上午(9:00-12:00)" : "下午(14:00-17:00)");
            for (SubjectListByFilterView c : d.getValue()) {
                groupDetail.getGroupClassInfoList().add(CreateDetail(c, courseList, gradeList));
            }
            result.getDateGroup().add(groupDetail);
        }
        return ResponseBase.GetSuccessResponse(result);
    }

    private GetPublicBenefitSubjectListDetail CreateDetail(SubjectListByFilterView d, List<Course> courseList, List<Grade> gradeList) {
        var vHVideoView = new VHVideoView();
        if (StringUtils.isNotBlank(d.getPPTFilePath())) {
            var videoViews = JSONArray.parseArray(d.getPPTFilePath(), VHVideoView.class);
            if (CollectionUtils.isNotEmpty(videoViews)) {
                vHVideoView = videoViews.get(0);
            }
        }
        var course = courseList.stream().filter(m -> m.getCourseId() == d.getCourseId()).findFirst().orElse(new Course());
        var grade = gradeList.stream().filter(m -> m.getGradeId() == d.getGradeId()).findFirst().orElse(new Grade());
        GetPublicBenefitSubjectListDetail restult = new GetPublicBenefitSubjectListDetail()
                .setOperationUrl(vHVideoView.getUrl())
                .setPopupUrl(vHVideoView.getVideostype() == PublicBenefitUrlTypeEnum.NewPageRedirect.getCode() ? vHVideoView.getUrl() : StringUtils.EMPTY)
                .setFileDownUrl(vHVideoView.getVideostype() == PublicBenefitUrlTypeEnum.OtherPageRedirect.getCode() ? vHVideoView.getUrl() : StringUtils.EMPTY)
                .setPPTVideoUrl(vHVideoView.getVideostype() == PublicBenefitUrlTypeEnum.OpenUrl.getCode() ? vHVideoView.getUrl() : StringUtils.EMPTY)
                .setCourseName(course.getCourseName())
                .setShortCode(course.getShortCode())
                .setCreateDateTime(d.getCreateDateTime().format(CommonConst.datefomat_nomal))
                .setGradeName(grade.getGradeName())
                .setImagePath(d.getImagePath())
                .setRegistEndTime(d.getRegistEndTime().format(CommonConst.datefomat_nomal))
                .setBeginTime(d.getBeginTime().format(CommonConst.datefomat_nomal))
                .setEndTime(d.getBeginTime() == d.getEndTime() ? "" : d.getEndTime().format(CommonConst.datefomat_nomal))
                .setStatusFlag(CommonUtil.GetSubjectStatusEnum(d).getIndex())
                .setSubjectId(UrlUtil.encrypt(d.getSubjectId()))
                .setSubjectName(d.getSubjectName())
                .setSubjectRoomId(UrlUtil.encrypt(d.getSubjectRoomId()))
                .setDeviceId(StringUtils.isBlank(d.getVideoRoom()) ? StringUtils.EMPTY : d.getVideoRoom().split("_")[0]);
        GenerateOperationMode(restult, vHVideoView);
        return restult;

    }

    private void GenerateOperationMode(GetPublicBenefitSubjectListDetail restult, VHVideoView vhVideoView) {
        if ("自主作业".equals(restult.getCourseName())) {
            restult.setIsZzzy(true);
            restult.setOperationMode(OperationModeEnum.NoUrl.getCode());
        } else {
            restult.setIsZzzy(false);
            if (vhVideoView.getVideostype() == PublicBenefitUrlTypeEnum.NewPageRedirect.getCode()) {
                restult.setOperationMode(OperationModeEnum.PopupPage.getCode());
            } else if (vhVideoView.getVideostype() == PublicBenefitUrlTypeEnum.OpenUrl.getCode()) {
                restult.setOperationMode(OperationModeEnum.Play.getCode());
            } else if (vhVideoView.getVideostype() == PublicBenefitUrlTypeEnum.OtherPageRedirect.getCode()) {
                restult.setOperationMode(OperationModeEnum.SimulationExercise.getCode());
            }
        }
    }

    @Override
    public ResponseBase GetPublicBenefitStatistics(GetPublicBenefitStatisticsApiRequest request) {
        var loginUserGradeTotalList = userLoginInfoService.getGradeTotal(request.getCurrentDate());
        var videoPlayTotalList = videoPlayService.getVideoPlayTotal(request.getCurrentDate());
        var grade = gradeCourseService.getAllGrade();
        var courseTypes = gradeCourseService.getAllCourseType();
        var highCourseType = courseTypes.stream().filter(c -> "高中".equals(c.getCourseTypeName())).findFirst().orElse(new CourseType());
        var juniorCourseType = courseTypes.stream().filter(c -> "初中".equals(c.getCourseTypeName())).findFirst().orElse(new CourseType());
        var highGrade = grade.stream().filter(d -> d.getCourseTypeId() == highCourseType.getCourseTypeId()).collect(Collectors.toList());
        var juniorGrade = grade.stream().filter(d -> d.getCourseTypeId() == juniorCourseType.getCourseTypeId()).collect(Collectors.toList());

        var result = new GetPublicBenefitStatisticsResponse().setHeadcount(loginUserGradeTotalList.stream().mapToInt(UserLoginTotalView::getGradeCount).sum())
                .setHighHeadcount(loginUserGradeTotalList.stream().filter(d -> highGrade.stream().anyMatch(h -> h.getGradeId() == d.getGradeId())).mapToInt(UserLoginTotalView::getGradeCount).sum())
                .setJuniorHeadcount(loginUserGradeTotalList.stream().filter(d -> juniorGrade.stream().anyMatch(h -> h.getGradeId() == d.getGradeId())).mapToInt(UserLoginTotalView::getGradeCount).sum());
        videoPlayTotalList.forEach(d -> {
            result.getVideoPlayInfoList().add(new VideoPlayCountInfo()
                    .setBeginTime(d.getBeginTime().format(CommonConst.datefomat_nomal))
                    .setPlayCount(d.getPlayCount())
                    .setSubjectId(UrlUtil.encrypt(d.getSubjectId())).setSubjectName(d.getSubjectName()));
        });
        return ResponseBase.GetSuccessResponse(result);
    }

    @Override
    public ResponseBase GetAuthCode(GetAuthCodeApiRequest request) {
        int subjectId = UrlUtil.decrypt(request.getSubjectId(), Integer.class);
        var subject = subjectService.getByKey(subjectId, true);

        AuthCodeCache authCodeCache = new AuthCodeCache().setTeacherId(request.getUserId()).setSubjectId(subjectId);
        var cache = RedisUtil.getObj(authCodeCache.getKey(), AuthCodeCache.class);
        if (cache == null) {
            cache = new AuthCodeCache();
            String code = CommonUtil.getRandomString(5);
            cache.setTeacherId(request.getUserId()).setSubjectId(subjectId).setCode(code);
            RedisUtil.setObj(cache, CommonConst.MINUTE_10);
        }
        var result = new GetAuthCodeResponse();
        String authStr = String.format("%s/%s/%s", request.getUserId(), subjectId, cache.getCode());
        result.setAuthCode(String.format("R_%s%s?auth=%s", subject.getBeginTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")), request.getUserId(), UrlUtil.encrypt(authStr)));
        return ResponseBase.GetSuccessResponse(result);
    }

    /// <summary>
/// 老师听课自动报名失败
/// </summary>
    public static final String ErrorMessageTeacher = "老师自动报名失败，您不能在该讨论区发言!";

    /// <summary>
/// 学生自动报名失败
/// </summary>
    public static final String ErrorMessageStudent = "自动报名失败，您不能在该讨论区发言!";

    /// <summary>
/// 未报名
/// </summary>
    public static final String ErrorMessageUnregistered = "您不能在该讨论区发言";
}
