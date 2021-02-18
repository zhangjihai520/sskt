package com.ry.sskt.controller.action.impl;

import com.alibaba.fastjson.JSONArray;
import com.ry.sskt.controller.action.ILoginActionService;
import com.ry.sskt.controller.action.IStudentActionService;
import com.ry.sskt.core.config.CommonConfig;
import com.ry.sskt.core.dledc.request.CheckUserAccountRequest;
import com.ry.sskt.core.dledc.response.CheckUserAccountResponse;
import com.ry.sskt.core.utils.JwtUtil;
import com.ry.sskt.core.utils.UrlUtil;
import com.ry.sskt.model.common.constant.EvaluateTypeEnum;
import com.ry.sskt.model.common.constant.LoginTypeEnum;
import com.ry.sskt.model.common.constant.SourceTypeEnum;
import com.ry.sskt.model.common.constant.SubjectStatusEnum;
import com.ry.sskt.model.common.entity.Course;
import com.ry.sskt.model.common.entity.Grade;
import com.ry.sskt.model.common.entity.User;
import com.ry.sskt.model.common.entity.UserLoginInfo;
import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.common.request.RequestPageBase;
import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.examset.entity.StudentAnswer;
import com.ry.sskt.model.openapi.entity.StudentQuestion;
import com.ry.sskt.model.student.entity.*;
import com.ry.sskt.model.student.entity.view.TeacherToStudentEvaluateView;
import com.ry.sskt.model.student.request.*;
import com.ry.sskt.model.student.response.GetStudentStudyRecordsResponse;
import com.ry.sskt.model.student.response.GetSubjectEvaluateListResponse;
import com.ry.sskt.model.student.response.GetTeacherToStudentEvaluateListResponse;
import com.ry.sskt.model.student.response.GetUnReadMessageNumberResponse;
import com.ry.sskt.model.subject.entity.Subject;
import com.ry.sskt.model.user.request.LoginApiRequest;
import com.ry.sskt.model.user.response.LoginResponse;
import com.ry.sskt.service.*;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
public class StudentActionService implements IStudentActionService {
    @Autowired
    IUserService userService;

    @Autowired
    IMessageService messageService;

    @Autowired
    IStudentAnswerService studentAnswerService;

    @Autowired
    IExamSetService examSetService;

    @Autowired
    IEvaluateRecordService evaluateRecordService;

    @Autowired
    ISubjectRoomStudentService subjectRoomStudentService;

    @Autowired
    ISubjectService subjectService;

    @Autowired
    IStudentStudyRecordService studentStudyRecordService;


    @Autowired
    IGradeCourseService gradeCourseService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase EditStudentInfo(EditStudentInfoApiRequest request) throws Exception {
        var userId = request.getUserId();
        var user = userService.getByKey(userId, true);
        user.setUserTrueName(request.getUserTrueName());
        user.setSchoolName(request.getSchoolName());
        user.setGradeId(request.getGradeId());
        user.setComment(request.getComment());
        user.setUpdateDateTime(LocalDateTime.now());
        userService.save(user);
        return ResponseBase.GetSuccessResponse();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase SubmitExamSet(SubmitExamSetApiRequest request) throws Exception {
        var exam = this.examSetService.getByKey(UrlUtil.decrypt(request.getExamSetId(), Integer.class));
        if (exam == null) {
            return ResponseBase.GetValidateErrorResponse("获取不到作业，保存失败");
        }
        var subject = subjectService.getByKey(exam.getSubjectId());
        if (subject == null) {
            subject = new Subject();
        }
        var subjectRoomStudent = subjectRoomStudentService.GetByUserIdAndSubjectId(subject.getSubjectId(), request.getUserId());
        var answer = GetStudentQuestionAnswer(Arrays.stream(exam.getQuestionIds().split(",")).map(Integer::parseInt).collect(Collectors.toList()), request);
        var studentAnswer = new StudentAnswer()
                .setExamSetId(exam.getExamSetId())
                .setUserId(request.getUserId())
                .setSubjectId(exam.getSubjectId())
                .setScore(new BigDecimal(0))
                .setQuestionResult(JSONArray.toJSONString(answer))
                .setMotkResultInfo(StringUtils.EMPTY)
                .setAnswerNumber(answer.size())
                .setSubjectRoomId(subjectRoomStudent.getSubjectRoomId())
                .setStatusFlag(1)
                .setCreateDateTime(LocalDateTime.now())
                .setUpdateDateTime(LocalDateTime.now());
        var result = this.studentAnswerService.save(studentAnswer);
        String content = String.format("完成%s%s作业", subject.getSubjectName(), exam.getExamSetTypeId() == 1 ? "课前" : exam.getExamSetTypeId() == 2 ? "课堂" : "课后");
        studentStudyRecordService.Save(new StudentStudyRecord()
                .setContent(content)
                .setCreateDateTime(LocalDateTime.now())
                .setExamSetId(exam.getExamSetId())
                .setStudyRecordTypeId(exam.getExamSetTypeId())
                .setSubjectId(exam.getSubjectId())
                .setUpdateDateTime(LocalDateTime.now())
                .setUserId(request.getUserId())
                .setStudyRecordTypeId(0));
        return ResponseBase.GetSuccessResponse(result > 0 ? 1 : 0);
    }

    /// <summary>
    /// 获取学生作答
    /// </summary>
    /// <param name="request"></param>
    /// <returns></returns>
    private List<StudentQuestion> GetStudentQuestionAnswer(List<Integer> questionIds, SubmitExamSetApiRequest request) {
        var result = new LinkedList<StudentQuestion>();
        for (var item : request.getQuestionList()) {
            int questionId = 0;
            questionId = Integer.parseInt(item.getQuestionId());
            int finalQuestionId = questionId;
            if (questionId > 0 && StringUtils.isNotBlank(item.getAnswer()) && questionIds.stream().anyMatch(x -> x == finalQuestionId)) {
                result.add(new StudentQuestion().setQuestionId(questionId).setStudentAnswer(item.getAnswer()));
            }
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase AskQuestion(AskQuestionApiRequest request) throws Exception {
        var teacherId = UrlUtil.decrypt(request.getTeacherId(), Integer.class);
        var message = new Message()
                .setSourseUserId(request.getUserId())
                .setTargetUserId(teacherId)
                .setContent(request.getContent())
                .setCreateDateTime(LocalDateTime.now())
                .setUpdateDateTime(LocalDateTime.now());
        messageService.Save(message);
        return ResponseBase.GetSuccessResponse();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase EvaluateSubject(EvaluateSubjectApiRequest request) throws Exception {
        var subjectId = UrlUtil.decrypt(request.getSubjectId(), Integer.class);
        var teacherId = UrlUtil.decrypt(request.getTeacherId(), Integer.class);
        var subjectRoomId = UrlUtil.decrypt(request.getSubjectRoomId(), Integer.class);
        var evaluateRecord = evaluateRecordService.GetByKey(subjectId, request.getUserId(), teacherId);
        if (evaluateRecord != null) {
            return ResponseBase.GetErrorResponse("已评价此课程");
        }
        var subjectRoomStudent = subjectRoomStudentService.GetByKey(subjectRoomId, request.getUserId());
        subjectRoomStudent.setIsEvaluate(1);
        var addEvaluateRecord = new EvaluateRecord()
                .setSubjectId(subjectId)
                .setSourseUserId(request.getUserId())
                .setTargetUserId(teacherId)
                .setEvaluateTypeId(EvaluateTypeEnum.StudentEvaluateSubject.getCode())
                .setEvaluateLevel(request.getEvaluateLevel())
                .setEvaluateComment(request.getEvaluateComment())
                .setStatusFlag(1).setCreateDateTime(LocalDateTime.now())
                .setUpdateDateTime(LocalDateTime.now());
        evaluateRecordService.Save(addEvaluateRecord);
        subjectRoomStudentService.Save(subjectRoomStudent);
        return ResponseBase.GetSuccessResponse();
    }

    @Override
    public ResponseBase GetStudentStudyRecords(GetStudentStudyRecordsApiRequest request) {
        User userInfo = userService.getByKey(UrlUtil.decrypt(request.getStudentId(), Integer.class), true);
        if (userInfo == null) {
            userInfo = new User();
        }
        var records = studentStudyRecordService.GetListByUserId(userInfo.getUserId(), request.getSystemTypeId());
        if (CollectionUtils.isEmpty(records)) {
            records = new LinkedList<StudentStudyRecord>();
        }

        User finalUserInfo = userInfo;
        var gradeInfo = gradeCourseService.getAllGrade().stream().filter(m -> m.getGradeId() == finalUserInfo.getGradeId()).findFirst().orElse(new Grade());
        var result = new GetStudentStudyRecordsResponse().setUserId(UrlUtil.encrypt(userInfo.getUserId()))
                .setGradeName(gradeInfo.getGradeName()).setSchoolName(userInfo.getSchoolName()).setUserTrueName(userInfo.getUserTrueName()).setUserFace(userInfo.getUserFace());
        DateTimeFormatter dtf3 = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        records.forEach(x -> {
            result.getStudentStudyRecordList().add(new StudentStudyRecordInfo()
                    .setContent(x.getContent())
                    .setCreateDateTime(x.getCreateDateTime().format(dtf3))
                    .setExamSetId(UrlUtil.encrypt(x.getExamSetId()))
                    .setStudyRecordTypeId(x.getStudyRecordTypeId()));
        });
        return ResponseBase.GetSuccessResponse(result);
    }

    @Override
    public ResponseBase GetSubjectEvaluateList(RequestPageBase request) {
        var subjects = evaluateRecordService.GetStudentEvaluateSubjectList(request.getUserId(), request.getPageIndex(), request.getPageSize(), request.getSystemTypeId());
        var allCourses = gradeCourseService.getAllCourse(true);
        var response = new GetSubjectEvaluateListResponse();
        response.setTotalCount(subjects.first);
        DateTimeFormatter dtf3 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        subjects.second.forEach(x -> {
            Course course = allCourses.stream().filter(g -> g.getCourseId() == x.getCourseId()).findFirst().orElse(new Course());
            response.getSubjectList().add(new SubjectInfo()
                    .setSubjectId(UrlUtil.encrypt(x.getSubjectId()))
                    .setSubjectName(x.getSubjectName())
                    .setImagePath(x.getImagePath())
                    .setCourseName(course.getCourseName())
                    .setShortCode(course.getShortCode())
                    .setTeacherId(UrlUtil.encrypt(x.getTeacherId()))
                    .setTeacherName(x.getTeacherName())
                    .setBeginTime(x.getBeginTime().format(dtf3))
                    .setStatusFlag(SubjectStatusEnum.ClosedClass.getIndex())
                    .setIsEvaluate(x.getIsEvaluate())
                    .setSubjectRoomId(UrlUtil.encrypt(x.getSubjectRoomId()))
                    .setEvaluateComment(x.getEvaluateComment())
                    .setEvaluateLevel(x.getEvaluateLevel())
                    .setTeacherFace(x.getTeacherFace())
            );
        });
        return ResponseBase.GetSuccessResponse(response);
    }

    @Override
    public ResponseBase GetTeacherToStudentEvaluateList(RequestPageBase request) {
        var data = this.evaluateRecordService.GetTeacherToStudentEvaluateList(
                request.getUserId(),
                request.getPageIndex(),
                request.getPageSize(),
                request.getSystemTypeId());

        var evaluateList = new LinkedList<GetTeacherToStudentEvaluateListInfo>();
        if (CollectionUtils.isNotEmpty(data.second)) {
            var allCourses = gradeCourseService.getAllCourse(true);

            Map<Integer, List<TeacherToStudentEvaluateView>> map = data.second.stream()
                    .collect(Collectors.groupingBy(TeacherToStudentEvaluateView::getSubjectId));
            for (Map.Entry<Integer, List<TeacherToStudentEvaluateView>> entry : map.entrySet()) {
                var first = true;
                for (var o : entry.getValue()) {
                    if (first) {
                        Course course = allCourses.stream().filter(g -> g.getCourseId() == o.getCourseId()).findFirst().orElse(new Course());
                        evaluateList.add(new GetTeacherToStudentEvaluateListInfo().
                                setBeginTime(o.getBeginTime())
                                .setImagePath(o.getImagePath())
                                .setSubjectName(o.getSubjectName())
                                .setShortCode(course.getShortCode())
                                .setCourseName(course.getCourseName())
                                .setEvaluateInfoList(new LinkedList<>()));
                        first = false;
                    }
                    evaluateList.getLast().getEvaluateInfoList().add(new EvaluateListInfoDetail()
                            .setEvaluateComment(o.getEvaluateComment())
                            .setEvaluateLevel(o.getEvaluateLevel())
                            .setTeacherFace(o.getTeacherFace())
                            .setTeacherName(o.getTeacherName()));
                }
            }
        }
        return ResponseBase.GetSuccessResponse(new GetTeacherToStudentEvaluateListResponse(data.first, evaluateList));
    }

    @Override
    public ResponseBase GetUnReadMessageNumber(RequestBase request) {
        var response = new GetUnReadMessageNumberResponse();
        response.setUnReadMessageNumber(messageService.GetUnReadMessageNumber(request.getUserId()));
        return ResponseBase.GetSuccessResponse(response);
    }
}
