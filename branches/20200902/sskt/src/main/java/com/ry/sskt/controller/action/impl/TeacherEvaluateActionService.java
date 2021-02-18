package com.ry.sskt.controller.action.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ry.sskt.controller.action.ITeacherActionService;
import com.ry.sskt.controller.action.ITeacherEvaluateActionService;
import com.ry.sskt.core.utils.ExcelUtils;
import com.ry.sskt.core.utils.UrlUtil;
import com.ry.sskt.model.common.constant.EvaluateTypeEnum;
import com.ry.sskt.model.common.constant.SubjectStatusEnum;
import com.ry.sskt.model.common.entity.Course;
import com.ry.sskt.model.common.entity.Grade;
import com.ry.sskt.model.common.entity.ThreeTuple;
import com.ry.sskt.model.common.entity.User;
import com.ry.sskt.model.common.request.RequestPageBase;
import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.student.entity.EvaluateRecord;
import com.ry.sskt.model.student.entity.Message;
import com.ry.sskt.model.student.entity.SubjectRoomStudent;
import com.ry.sskt.model.teacher.entity.MessageInfo;
import com.ry.sskt.model.teacher.entity.SubjectEvaluateInfo;
import com.ry.sskt.model.teacher.entity.SubjectInfo;
import com.ry.sskt.model.teacher.entity.TeacherInfo;
import com.ry.sskt.model.teacher.request.*;
import com.ry.sskt.model.teacher.response.*;
import com.ry.sskt.service.*;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
public class TeacherEvaluateActionService implements ITeacherEvaluateActionService {
    @Autowired
    IMessageService messageService;

    @Autowired
    IGradeCourseService gradeCourseService;

    @Autowired
    IEvaluateRecordService evaluateRecordService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase EvaluateSubject(EvaluateSubjectApiRequest request) throws Exception {
        var subjectId = UrlUtil.decrypt(request.getSubjectId(), Integer.class);
        var teacherId = UrlUtil.decrypt(request.getTeacherId(), Integer.class);
        var evaluateRecord = evaluateRecordService.GetByKey(subjectId, request.getUserId(), teacherId);
        if (evaluateRecord != null) {
            return ResponseBase.GetErrorResponse("已评价此课程");
        }
        var addEvaluateRecord = new EvaluateRecord()
                .setSubjectId(subjectId)
                .setSourseUserId(request.getUserId())
                .setTargetUserId(teacherId)
                .setEvaluateTypeId(EvaluateTypeEnum.TeacherEvaluateSubject.getCode())
                .setEvaluateLevel(request.getEvaluateLevel())
                .setEvaluateComment(request.getEvaluateComment())
                .setStatusFlag(1).setCreateDateTime(LocalDateTime.now()).setUpdateDateTime(LocalDateTime.now());
        evaluateRecordService.Save(addEvaluateRecord);
        return ResponseBase.GetSuccessResponse();
    }

    @Override
    public ResponseBase GetSubjectEvaluateList(RequestPageBase request) {
        var subjects = evaluateRecordService.GetTeacherEvaluateSubjectList(request.getUserId(), request.getPageIndex(), request.getPageSize(), request.getSystemTypeId());
        var allCourses = gradeCourseService.getAllCourse(true);
        var allGrades = gradeCourseService.getAllGrade();
        var response = new GetSubjectEvaluateListResponse();
        response.setTotalCount(subjects.first);
        DateTimeFormatter dtf3 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        subjects.second.forEach(x -> {
            Course course = allCourses.stream().filter(g -> g.getCourseId() == x.getCourseId()).findFirst().orElse(new Course());
            Grade grade = allGrades.stream().filter(g -> g.getGradeId() == x.getGradeId()).findFirst().orElse(new Grade());
            response.getSubjectList().add(new SubjectInfo()
                    .setSubjectId(UrlUtil.encrypt(x.getSubjectId()))
                    .setSubjectName(x.getSubjectName())
                    .setImage(x.getImagePath())
                    .setCourseName(course.getCourseName())
                    .setGradeName(grade.getGradeName())
                    .setTeacherId(UrlUtil.encrypt(x.getTeacherId()))
                    .setTeacherName(x.getTeacherName())
                    .setBeginTime(x.getBeginTime().format(dtf3))
                    .setStatusFlag(SubjectStatusEnum.ClosedClass.getIndex())
                    .setIsEvaluate(x.getIsEvaluate()));
        });
        return ResponseBase.GetSuccessResponse(response);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase ReplayMessage(ReplayMessageApiRequest request) throws Exception {
        var parentMessageId = UrlUtil.decrypt(request.getMessageId(), Integer.class);
        var targetUserId = UrlUtil.decrypt(request.getTargetUserId(), Integer.class);
        var parentMessage = messageService.GetByKey(parentMessageId);
        if (parentMessage.getIsReply() == 1) {
            return ResponseBase.GetErrorResponse("已回复此消息");
        }
        var message = new Message().setSourseUserId(request.getUserId()).setTargetUserId(targetUserId).setContent(request.getContent()).setParentId(parentMessageId).setCreateDateTime(LocalDateTime.now()).setUpdateDateTime(LocalDateTime.now());
        parentMessage.setIsReply(1);
        parentMessage.setUpdateDateTime(LocalDateTime.now());
        messageService.Save(message);
        messageService.Save(parentMessage);
        return ResponseBase.GetSuccessResponse(1);
    }

    @Override
    public ResponseBase GetMessageList(GetMessageListApiRequest request) {
        var messages = messageService.GetMessageList(request.getUserId(), request.getMessageTypeId(), request.getPageIndex(), request.getPageSize());
        var response = new GetMessageListResponse();
        response.setTotalCount(messages.first);
        DateTimeFormatter dtf3 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        messages.second.forEach(x -> {
            response.getMessageList().add(new MessageInfo()
                    .setMessageId(UrlUtil.encrypt(x.getMessageId()))
                    .setUserId(UrlUtil.encrypt(x.getSourseUserId()))
                    .setUserTrueName(x.getUserTrueName())
                    .setUserFace(x.getUserFace())
                    .setContent(x.getContent())
                    .setIsReply(x.getIsReply())
                    .setStatusFlag(x.getStatusFlag())
                    .setUserTypeId(x.getUserTypeId()).setCreateDateTime(x.getCreateDateTime().format(dtf3)));
        });
        return ResponseBase.GetSuccessResponse(response);
    }

    @Override
    public ResponseBase GetAllSubjectEvaluate(RequestPageBase request) {
        var data = evaluateRecordService.GetAllSubjectEvaluate(
                request.getUserId(),
                request.getPageIndex(),
                request.getPageSize(),
                request.getSystemTypeId());
        List<SubjectEvaluateInfo> list = new LinkedList<>();
        data.forEach(x -> {
            list.add(new SubjectEvaluateInfo(x));
        });
        return ResponseBase.GetSuccessResponse(new GetAllSubjectEvaluateResponse().setSubjectEvaluateList(list));
    }

    @Override
    public ResponseBase GetSubjectEvaluate(GetSubjectEvaluateApiRequest request) {
        var data = this.evaluateRecordService.GetSubjectEvaluate(
                request.getUserId(),
                UrlUtil.decrypt(request.getSubjectId(), Integer.class),
                request.getEvaluateTypeId(),
                request.getPageIndex(),
                request.getPageSize(),
                request.getSystemTypeId());
        return ResponseBase.GetSuccessResponse(new GetSubjectEvaluateResponse(data.first, data.second));
    }
}
