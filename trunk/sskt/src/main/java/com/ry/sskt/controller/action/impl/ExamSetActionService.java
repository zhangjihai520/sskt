package com.ry.sskt.controller.action.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.ry.sskt.controller.action.IExamSetActionService;
import com.ry.sskt.core.config.CommonConfig;
import com.ry.sskt.core.utils.UrlUtil;
import com.ry.sskt.model.common.entity.*;
import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.discuss.entity.cache.UserSubjectRoomCouchBaseObject;
import com.ry.sskt.model.examset.entity.*;
import com.ry.sskt.model.examset.request.*;
import com.ry.sskt.model.examset.response.*;
import com.ry.sskt.model.examset.view.ExamSetListView;
import com.ry.sskt.model.examset.view.GetStatistListView;
import com.ry.sskt.model.examset.view.SCPEvaluationResultView;
import com.ry.sskt.model.examset.view.SCPQuestionDetailView;
import com.ry.sskt.model.openapi.entity.StudentQuestion;
import com.ry.sskt.model.openapi.entity.TeacherExamDetailModel;
import com.ry.sskt.model.openapi.entity.TeacherExamQuestionModel;
import com.ry.sskt.model.openapi.request.GetUserSessionRequest;
import com.ry.sskt.model.openapi.response.ApiResultEntity;
import com.ry.sskt.model.openapi.response.UserBindResponse;
import com.ry.sskt.model.subject.entity.Subject;
import com.ry.sskt.model.subject.entity.view.SubjectRoomTeacherNameView;
import com.ry.sskt.service.*;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
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
public class ExamSetActionService implements IExamSetActionService {
    @Autowired
    IGradeCourseService gradeCourseService;
    @Autowired
    IExamSetService examSetService;
    @Autowired
    ISubjectRoomService subjectRoomService;
    @Autowired
    IStudentAnswerService studentAnswerService;
    @Autowired
    IUserService userService;
    @Autowired
    IOpenApiService openApiService;
    @Autowired
    ITalkMessageService talkMessageService;

    @Override
    public ResponseBase GetExamSetList(GetExamSetListApiRequest request) {
        TwoTuple<Integer, List<ExamSetListView>> obj = examSetService.getExamSetListForTeacher(UrlUtil.decrypt(request.getSubjectId(), Integer.class), request.getCourseId(), request.getExamSetName(), request.getStatusFlag(), request.getPageSize(), request.getPageIndex());
        GetExamSetListResponse result = new GetExamSetListResponse();
        List<Course> allCourse = gradeCourseService.getAllCourse(true);
        List<Grade> allGrade = gradeCourseService.getAllGrade();
        result.setCount(obj.first);
        List<GetExamSetListDetail> list = new LinkedList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        obj.second.stream().forEach(x -> {
            list.add(new GetExamSetListDetail()
                    .setExamSetId(UrlUtil.encrypt(x.getExamSetId()))
                    .setExamSetName(x.getExamSetName())
                    .setCourseName(allCourse.stream().filter(m -> m.getCourseId() == x.getCourseId()).findFirst().orElse(new Course()).getCourseName())
                    .setGradeName(allGrade.stream().filter(m -> m.getGradeId() == x.getGradeId()).findFirst().orElse(new Grade()).getGradeName())
                    .setExamSetTypeId(x.getExamSetTypeId())
                    .setQuestionCount(x.getQuestionNumber())
                    .setStatusFlag(x.getStatusFlag())
                    .setCreateTime(x.getCreateDateTime().format(formatter))
                    .setAnswerCount(x.getAnswerCount()));
        });
        result.setExamSetLists(list);
        return ResponseBase.GetSuccessResponse(result);
    }

    @Override
    public ResponseBase GetExamSetStatistList(GetExamSetStatistListApiRequest request) {
        ExamSet examSet = examSetService.getByKey(UrlUtil.decrypt(request.getExamSetId(), Integer.class));
        if (examSet == null) {
            return ResponseBase.GetValidateErrorResponse("没有该作业");
        }
        List<SubjectRoomTeacherNameView> subjectRooms = subjectRoomService.getSubjectRoomList(examSet.getSubjectId());
        int subjectRoomId = 0;
        if (CollectionUtils.isNotEmpty(subjectRooms)) {
            var subjectRoom = subjectRooms.stream().filter(x -> x.getTeacherId() == request.getUserId()).findFirst().orElse(null);
            if (subjectRoom != null) {
                subjectRoomId = subjectRoom.getSubjectRoomId();
            }
        }
        int score = examSet.getQuestionNumber();
        TwoTuple<Integer, List<GetStatistListView>> obj = examSetService.getStatistList(examSet.getExamSetId(), subjectRoomId, request.getStudentName(), request.getPageSize(), request.getPageIndex());
        GetExamSetStatistListResponse result = new GetExamSetStatistListResponse();
        result.setCount(obj.first);
        List<ExamSetStatistDetail> list = new LinkedList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        obj.second.stream().forEach(x -> {
            list.add(new ExamSetStatistDetail()
                    .setStudentId(UrlUtil.encrypt(x.getUserId()))
                    .setStudentName(x.getUserTrueName())
                    .setScore(String.format("%S/%S", x.getScore().setScale(0, BigDecimal.ROUND_DOWN).intValue(), score))
                    .setUpdateDateTime(x.getCreateDateTime().format(formatter)));
        });
        result.setExamSetStatistLists(list);
        return ResponseBase.GetSuccessResponse(result);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase GetAddExamSetUrl(GetAddExamSetUrlApiRequest request) throws Exception {
        User user = userService.getByKey(request.getUserId(), null);
        if (user == null) {
            return ResponseBase.GetValidateErrorResponse("获取用户失败");
        }
        GetUserSessionRequest requestAPI = new GetUserSessionRequest(CommonConfig.getOpenAPIId())
                .setUserTypeId(1).setCourseId(request.getCourseId()).setUserCode(String.format("%s_%s", user.getUserId(), request.getCourseId())).setUserTrueName(user.getUserTrueName() + "");
        UserBindResponse data = openApiService.getUserSession(requestAPI);
        if (data == null) {
            return ResponseBase.GetValidateErrorResponse("获取MOTK API 失败");
        }
        GetAddExamSetUrlResponse result = new GetAddExamSetUrlResponse()
                .setAddExamSetUrl(String.format("%s/SelectQuestionForAlliance/ChapterSection?psid=%s&backurl=%s", CommonConfig.getMotkUrl(), data.getSessionId(), String.format("%s_%s_%s", request.getSubjectId(), request.getCourseId(), request.getOnline())));
        return ResponseBase.GetSuccessResponse(result);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase GetEvaluationResult(GetEvaluationResultApiRequest request) throws Exception {
        ExamSet exam = examSetService.getByKey(UrlUtil.decrypt(request.getExamSetId(), Integer.class));
        if (exam == null) {
            return ResponseBase.GetValidateErrorResponse("获取失败，找不到该作业");
        }
        StudentAnswer studentAnswer = studentAnswerService.getByKey(exam.getExamSetId(), UrlUtil.decrypt(request.getStudentId(), Integer.class));
        if (studentAnswer == null) {
            return ResponseBase.GetValidateErrorResponse("获取失败，找不到该学生的作业");
        }
        List<StudentQuestion> studentQuestionAnswers = JSONArray.parseArray(studentAnswer.getQuestionResult(), StudentQuestion.class);
        SCPEvaluationResultView evaluationResult = getEvaluationResultView(exam, studentAnswer, studentQuestionAnswers);
        if (evaluationResult == null) {
            return ResponseBase.GetValidateErrorResponse("获取评价失败");
        }
        List<Course> allCourse = gradeCourseService.getAllCourse(true);
        User user = userService.getByKey(studentAnswer.getUserId(), true);
        return ResponseBase.GetSuccessResponse(converData(evaluationResult, exam, studentAnswer, user, allCourse, studentQuestionAnswers));
    }

    @Override
    public ResponseBase GetAnswersStatistics(GetAnswersStatisticsApiRequest request) throws Exception {
        GetAnswersStatisticsResponse response = new GetAnswersStatisticsResponse();
        int userTypeId = request.getUserTypeId();
        int examSetId = UrlUtil.decrypt(request.getExamSetId(), Integer.class);
        var examSet = examSetService.getByKey(examSetId);
        UserSubjectRoomCouchBaseObject userSubjectRoom = talkMessageService.getUserSubjectRoomInfo(request.getUserId(), userTypeId, examSet.getSubjectId());
        List<StudentAnswer> studentAnswers = new ArrayList<>();
        if (userSubjectRoom.getRoomUserType() == 1) {
            studentAnswers = studentAnswerService.getStudentAnswerBySubjectRoomId(examSetId, userSubjectRoom.getSubjectRoomId());
            var subjectRoom = subjectRoomService.getByKey(userSubjectRoom.getSubjectRoomId());
            if (subjectRoom != null) {
                response.setStudentCount(subjectRoom.getRealRegisterNumber());
            }
            //助教
        } else if (userSubjectRoom.getRoomUserType() == 3) {
            //主讲
            studentAnswers = studentAnswerService.getStudentAnswerBySubjectId(examSetId, userSubjectRoom.getSubjectId());
            var subjectRooms = subjectRoomService.getSubjectRoomBySubjectId(userSubjectRoom.getSubjectId());
            if (CollectionUtils.isNotEmpty(subjectRooms)) {
                response.setStudentCount(subjectRooms.stream().mapToInt(p -> p.getRealRegisterNumber()).sum());
            }
        }
        if (CollectionUtils.isNotEmpty(studentAnswers)) {
            response.setAnswerCount(studentAnswers.size());
            response.setUnAnswerCount(response.getStudentCount() - studentAnswers.size());
        }
        List<SCPEvaluationResultView> evaluationResults = new ArrayList<>();
        for (var studentAnswer : studentAnswers) {
            List<StudentQuestion> studentQuestionAnswers = JSONArray.parseArray(studentAnswer.getQuestionResult(), StudentQuestion.class);
            SCPEvaluationResultView evaluationResult = getEvaluationResultView(examSet, studentAnswer, studentQuestionAnswers);
            evaluationResults.add(evaluationResult);
        }
        var questionDetail = openApiService.getQuestionDetail(examSet);
        int[] questionIds = Arrays.stream(examSet.getQuestionIds().split(",")).mapToInt(Integer::valueOf).toArray();
        int index = 1;
        for (int id : questionIds) {
            var question = questionDetail.getQuestions().stream().filter(x -> x.getQuestionId() == id).findFirst().orElse(null);
            if (question != null) {
                question.getOptions().sort(Comparator.comparing(SCPQuestionOption::getOrderIndex));
                QuestionAnswerStatisticsResult answerStatisticsResult = new QuestionAnswerStatisticsResult();
                answerStatisticsResult
                        .setQuestionId(id)
                        .setOrder(index)
                        .setQuestionContent(question.getQuestionContent())
                        .setAnswer(question.getAnswer())
                        .setAnalysis(question.getAnalysis())
                        .setOptions(question.getOptions());
                response.getQuestionResults().add(answerStatisticsResult);
                for (var evaluationResult : evaluationResults) {
                    var currentAnswerResult = evaluationResult.getQuestionResult().stream().filter(x -> x.getQuestionId() == id).findFirst().orElse(new SCPQuestionResult());
                    if (currentAnswerResult.isCorrect()) {
                        answerStatisticsResult.setCorrectNum(answerStatisticsResult.getCorrectNum() + 1);
                    } else {
                        answerStatisticsResult.setErrorNum(answerStatisticsResult.getErrorNum() + 1);
                    }
                }
                index++;
            }
        }
        return ResponseBase.GetSuccessResponse(response);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase SetExamSetOnline(SetExamSetOnlineApiRequest request) {
        if (request.getStatusFlag() == 2) {
            if (this.examSetService.getCountForTypeId(UrlUtil.decrypt(request.getSubjectId(), Integer.class), request.getExamSetTypeId()) > 0) {
                return ResponseBase.GetSuccessResponse(2);
            }
        }
        ExamSet exam = this.examSetService.getByKey(UrlUtil.decrypt(request.getExamSetId(), Integer.class));
        if (exam == null) {
            return ResponseBase.GetValidateErrorResponse("获取不到作业，保存失败");
        }
        exam.setStatusFlag(request.getStatusFlag());
        exam.setUpdateDateTime(new Date());
        int result = this.examSetService.save(exam);
        return ResponseBase.GetSuccessResponse(result > 0 ? 1 : 0);
    }

    @Override
    public ResponseBase GetExamSetPreview(GetExamSetPreviewApiRequest request) throws Exception {
        ExamSet exam = this.examSetService.getByKey(UrlUtil.decrypt(request.getExamSetId(), Integer.class));
        if (exam == null) {
            return ResponseBase.GetValidateErrorResponse("获取不到作业，获取失败");
        }
        var question = openApiService.getQuestionDetail(exam);
        if (question == null || CollectionUtils.isEmpty(question.getQuestions())) {
            return ResponseBase.GetValidateErrorResponse("获取不到题目，获取失败");
        }
        return ResponseBase.GetSuccessResponse(this.coverResponse(exam, question));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase SaveExamSet(SaveExamSetApiRequest request) {
        ExamSet exam = this.examSetService.getByKey(UrlUtil.decrypt(request.getExamSetId(), Integer.class));
        if (exam == null) {
            return ResponseBase.GetValidateErrorResponse("获取不到作业，保存失败");
        }
        exam.setExamSetName(request.getExamSetName());
        exam.setGradeId(request.getGradeId());
        exam.setExamSetTypeId(request.getExamSetTypeId());
        exam.setUpdateDateTime(new Date());
        int result = this.examSetService.save(exam);
        return ResponseBase.GetSuccessResponse(result > 0 ? 1 : 0);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase GetExamSetInfo(GetExamSetInfoApiRequest request) throws Exception {
        String[] strs = UrlUtil.getURLDecoderString(request.getBackurl()).split("_");
        List<Course> allCourse = gradeCourseService.getAllCourse(true);
        ExamSet exam = examSetService.getByMotkExamSetID(UrlUtil.getURLDecoderString(request.getTeid()));
        if (exam != null) {
            String courseName = allCourse.stream().filter(m -> m.getCourseId() == exam.getCourseId()).findFirst().orElse(new Course()).getCourseName();
            return ResponseBase.GetSuccessResponse(new GetExamSetInfoResponse().setExamSetId(UrlUtil.encrypt(exam.getExamSetId()))
                    .setExamSetName(exam.getExamSetName())
                    .setCourseName(courseName)
                    .setCourseId(exam.getCourseId())
                    .setGradeId(exam.getGradeId())
                    .setQuestionCount(exam.getQuestionNumber())
                    .setStatusFlag(1));
        }
        TeacherExamDetailModel examDeatil = openApiService.getTeacherExamDetail(UrlUtil.getURLDecoderString(request.getTeid()));
        if (examDeatil == null) {
            return ResponseBase.GetValidateErrorResponse("获取MOTK API 失败");
        }
        List<Integer> questionIds;
        double score = 0;
        questionIds = new LinkedList<Integer>();
        score = 0;
        examDeatil.getQuestions().sort(Comparator.comparing(TeacherExamQuestionModel::getOrder));
        for (TeacherExamQuestionModel item : examDeatil.getQuestions()) {
            score += item.getScore();
            questionIds.add(item.getQuestion().getQuestionId());
        }
        ApiResultEntity check = openApiService.checkQuestion(questionIds);
        if (check == null || check.getApiResultType() != 1) {
            return ResponseBase.GetSuccessResponse(new GetExamSetInfoResponse()
                    .setExamSetId("0")
                    .setExamSetName(examDeatil.getTeacherExamName())
                    .setCourseId(Integer.parseInt(strs[1]))
                    .setGradeId(0)
                    .setQuestionCount(questionIds.size())
                    .setStatusFlag(0));
        }
        ExamSet examSet = getExamSetInstance(request, questionIds, score, examDeatil.getTeacherExamName(), strs);
        examSet.setExamSetId(examSetService.save(examSet));
        var result = new GetExamSetInfoResponse().setExamSetId(UrlUtil.encrypt(examSet.getExamSetId()))
                .setExamSetName(examSet.getExamSetName())
                .setCourseName(allCourse.stream().filter(m -> m.getCourseId() == examSet.getCourseId()).findFirst().orElse(new Course()).getCourseName())
                .setCourseId(examSet.getCourseId())
                .setGradeId(examSet.getGradeId())
                .setQuestionCount(examSet.getQuestionNumber())
                .setStatusFlag(1);
        return ResponseBase.GetSuccessResponse(result);
    }


    /// <summary>
    /// 转换为输出对象
    /// </summary>
    /// <param name="evaluationResult"></param>
    /// <param name="exam"></param>
    /// <param name="studentAnswer"></param>
    /// <param name="user"></param>
    /// <param name="courses"></param>
    /// <param name="studentQuestionAnswers"></param>
    /// <returns></returns>
    private GetEvaluationResultResponse converData(SCPEvaluationResultView evaluationResult, ExamSet exam, StudentAnswer studentAnswer, User user, List<Course> courses, List<StudentQuestion> studentQuestionAnswers) {
        GetEvaluationResultResponse result = new GetEvaluationResultResponse();
        result.setExamSetName(String.format("%s-%s-%s", StringUtils.isBlank(user.getUserTrueName()) ? user.getUserName() : user.getUserTrueName(), (courses.stream().filter(m -> m.getCourseId() == exam.getCourseId()).findFirst().orElse(new Course()).getCourseName()), exam.getExamSetName()));
        result.setQuestionCount(exam.getQuestionNumber());
        result.setCorrectCount(studentAnswer.getScore().setScale(0, BigDecimal.ROUND_DOWN).intValue());
        result.setKnowledgePointMasterAnalysis(evaluationResult.getKnowledgePointMasterAnalysis());
        result.setKnowledgePointScores(evaluationResult.getKnowledgePointScores()); //evaluationResult.KnowledgePointScores.Select(x => new SCPEvaluationKnowledgePoint() { KnowledgePointId = x.KnowledgePointId, KnowledgePointName = x.KnowledgePointName, Score = Math.Round(x.Score,1) }).ToList();
        result.setQuestionResult(getStudentAnswerResult(evaluationResult, Arrays.stream(exam.getQuestionIds().split(",")).mapToInt(Integer::valueOf).toArray(), studentQuestionAnswers));
        return result;
    }

    /// <summary>
    /// 获取学生做题结果
    /// </summary>
    /// <param name="evaluationResult"></param>
    /// <param name="questionIds"></param>
    /// <param name="studentQuestionAnswers"></param>
    /// <returns></returns>
    private List<StudentAnswerResult> getStudentAnswerResult(SCPEvaluationResultView evaluationResult, int[] questionIds, List<StudentQuestion> studentQuestionAnswers) {
        List<StudentAnswerResult> result = new LinkedList<StudentAnswerResult>();
        for (int questionId : questionIds) {
            SCPQuestionResult questionInfo = evaluationResult.getQuestionResult().stream().filter(x -> x.getQuestionId() == questionId).findFirst().orElse(new SCPQuestionResult().setQuestionId(questionId));
            StudentQuestion studentQuestionAnswer = studentQuestionAnswers.stream().filter(x -> x.getQuestionId() == questionId).findFirst().orElse(null);
            result.add(new StudentAnswerResult(questionInfo, studentQuestionAnswer == null ? StringUtils.EMPTY : studentQuestionAnswer.getStudentAnswer()));
        }
        return result;
    }

    /// <summary>
    /// 计算学生答对了多少题目
    /// </summary>
    /// <param name="studentQuestionAnswers"></param>
    /// <param name="evaluationResult"></param>
    /// <returns></returns>
    private int getStudentCorrectCount(List<StudentQuestion> studentQuestionAnswers, SCPEvaluationResultView evaluationResult) {
        int result = 0;
        for (StudentQuestion item : studentQuestionAnswers) {
            SCPQuestionResult question = evaluationResult.getQuestionResult().stream().filter(x -> x.getQuestionId() == item.getQuestionId()).findFirst().orElse(null);
            if (question != null) {
                result += question.isCorrect() ? 1 : 0;
            }
        }
        return result;
    }

    /// <summary>
    /// 获取学生测评
    /// </summary>
    /// <param name="exam"></param>
    /// <param name="studentAnswer"></param>
    /// <param name="studentQuestionAnswers"></param>
    /// <returns></returns>
    private SCPEvaluationResultView getEvaluationResultView(ExamSet exam, StudentAnswer studentAnswer, List<StudentQuestion> studentQuestionAnswers) throws Exception {
        SCPEvaluationResultView result = null;
        if (StringUtils.isBlank(studentAnswer.getMotkResultInfo())) {
            result = openApiService.submitExamResult(studentAnswer.getUserId() + "", exam.getGradeId(), studentQuestionAnswers, exam.getExamSetId());
            if (result == null) {
                return null;
            }
            studentAnswer.setMotkResultInfo(JSON.toJSONString(result));
            studentAnswer.setScore(new BigDecimal(getStudentCorrectCount(studentQuestionAnswers, result)));
            this.studentAnswerService.save(studentAnswer);
        } else {
            result = JSONObject.parseObject(studentAnswer.getMotkResultInfo(), new TypeReference<SCPEvaluationResultView>() {
            }.getType());
        }
        return result;
    }

    /// <summary>
    /// 转换输出对象
    /// </summary>
    /// <param name="exam"></param>
    /// <param name="questionDetail"></param>
    /// <returns></returns>
    public GetExamSetPreviewResponse coverResponse(ExamSet exam, SCPQuestionDetailView questionDetail) {
        GetExamSetPreviewResponse result = new GetExamSetPreviewResponse().setQuestions(new LinkedList<QuestionDetail>());
        result.setExamSetName(exam.getExamSetName());
        result.setStatusFlag(exam.getStatusFlag());
        int[] questionIds = Arrays.stream(exam.getQuestionIds().split(",")).mapToInt(Integer::valueOf).toArray();
        int index = 1;
        for (int id : questionIds) {
            SCPQuestionDetail question = questionDetail.getQuestions().stream().filter(x -> x.getQuestionId() == id).findFirst().orElse(null);
            if (question != null) {
                question.getOptions().sort(Comparator.comparing(SCPQuestionOption::getOrderIndex));
                result.getQuestions().add(new QuestionDetail().setQuestionId(id).setOrder(index).setQuestionContent(question.getQuestionContent()).setAnswer(question.getAnswer()).setAnalysis(question.getAnalysis())
                        .setOptions(question.getOptions()));
                index++;
            }
        }
        return result;
    }

    /// <summary>
    /// 获取ExamSet 实例
    /// </summary>
    /// <param name="request"></param>
    /// <param name="questionIds"></param>
    /// <param name="score"></param>
    /// <param name="examSetName"></param>
    /// <param name="strs"></param>
    /// <returns></returns>
    private ExamSet getExamSetInstance(GetExamSetInfoApiRequest request, List<Integer> questionIds, double score, String examSetName, String[] strs) {
        return new ExamSet().setSubjectId(UrlUtil.decrypt(strs[0], Integer.class)).setExamSetName(examSetName).setCourseId(Integer.parseInt(strs[1]))
                .setGradeId(0)
                .setExamSetTypeId(0)
                .setQuestionNumber(questionIds.size())
                .setScore(new BigDecimal(score))
                .setMotkExamSetId(request.getTeid())
                .setQuestionIds(StringUtils.join(questionIds, ","))
                .setStatusFlag(1)
                .setCreateDateTime(new Date())
                .setUpdateDateTime(new Date());
    }
}
