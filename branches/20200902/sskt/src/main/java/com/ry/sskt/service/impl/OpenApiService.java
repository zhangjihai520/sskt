package com.ry.sskt.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ry.sskt.core.config.CommonConfig;
import com.ry.sskt.core.utils.OpenApiHttpUtils;
import com.ry.sskt.core.utils.RedisUtil;
import com.ry.sskt.core.utils.SignedUtils;
import com.ry.sskt.model.common.constant.CommonConst;
import com.ry.sskt.model.common.entity.ChapterSection;
import com.ry.sskt.model.examset.entity.ExamSet;
import com.ry.sskt.model.examset.view.SCPEvaluationResultView;
import com.ry.sskt.model.examset.view.SCPQuestionDetailView;
import com.ry.sskt.model.openapi.entity.BookVersion;
import com.ry.sskt.model.openapi.entity.CourseMapping;
import com.ry.sskt.model.openapi.entity.StudentQuestion;
import com.ry.sskt.model.openapi.entity.TeacherExamDetailModel;
import com.ry.sskt.model.openapi.entity.cache.BookVersionsCache;
import com.ry.sskt.model.openapi.entity.cache.ChapterSectionsCache;
import com.ry.sskt.model.openapi.entity.cache.CourseMappingsCache;
import com.ry.sskt.model.openapi.entity.cache.ExamSetSCPQuestionDetailCache;
import com.ry.sskt.model.openapi.request.*;
import com.ry.sskt.model.openapi.response.ApiResultEntity;
import com.ry.sskt.model.openapi.response.UserBindResponse;
import com.ry.sskt.service.IOpenApiService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
public class OpenApiService implements IOpenApiService {
    @Override
    public List<BookVersion> getBookVersions(int[] courseIds) throws Exception {
        List<BookVersion> result = new LinkedList<BookVersion>();
        for (int courseId : courseIds) {
            List<BookVersion> bookVersions = getBookVersionsByCourseId(courseId);
            if (CollectionUtils.isNotEmpty(bookVersions)) {
                result.addAll(bookVersions);
            }
        }
        return result;
    }


    @Override
    public List<CourseMapping> getCourseMappings(int[] bookVerionIds) throws Exception {
        List<CourseMapping> result = new LinkedList<CourseMapping>();
        for (int bookVerionId : bookVerionIds) {
            List<CourseMapping> courseMappings = getCourseMappingByBookVerionId(bookVerionId);
            if (CollectionUtils.isNotEmpty(courseMappings)) {
                result.addAll(courseMappings);
            }
        }
        return result;
    }

    @Override
    public List<ChapterSection> getChapterSections(int[] courseMappingIds) throws Exception {
        List<ChapterSection> result = new LinkedList<ChapterSection>();
        for (int courseMappingId : courseMappingIds) {
            List<ChapterSection> chapterSections = getChapterSectionByCourseMappingId(courseMappingId);
            if (CollectionUtils.isNotEmpty(chapterSections)) {
                result.addAll(chapterSections);
            }
        }
        return result;
    }

    @Override
    public UserBindResponse getUserSession(GetUserSessionRequest request) throws Exception {
        request.setSign(SignedUtils.GetRequestSign(CommonConfig.getAppToken(), request));
        ApiResultEntity returnEntity = OpenApiHttpUtils.sendPost(CommonConfig.getOpenAPI().concat("/UserBind/GetUserSession"), request);
        if (null != returnEntity && returnEntity.getApiResultType() == 1 && null != returnEntity) {
            return JSONObject.parseObject(returnEntity.getValue(), UserBindResponse.class);
        }
        return null;
    }

    @Override
    public SCPEvaluationResultView submitExamResult(String SCPStudentId, int gradeId, List<StudentQuestion> studentQuestions, int examSetId) throws Exception {
        SCPExamDataRequest request = new SCPExamDataRequest(CommonConfig.getOpenAPIId());
        request.setScpStudentId(SCPStudentId)
                .setExaminationId(examSetId)
                .setGradeId(gradeId)
                .setScpOrganizationCode("双师")
                .setStudentQuestions(studentQuestions);
        request.setSign(SignedUtils.GetRequestSign(CommonConfig.getAppToken(), request));
        ApiResultEntity returnEntity = OpenApiHttpUtils.sendPost(CommonConfig.getOpenAPI().concat("/Shanceping/SubmitExamResult"), request);
        if (null != returnEntity && returnEntity.getApiResultType() == 1 && null != returnEntity) {
            return JSONObject.parseObject(returnEntity.getValue(), SCPEvaluationResultView.class);
        }
        return null;
    }

    @Override
    public SCPQuestionDetailView getQuestionDetail(ExamSet exam) throws Exception {
        ExamSetSCPQuestionDetailCache cache = RedisUtil.getObj(new ExamSetSCPQuestionDetailCache(exam.getExamSetId()).getKey(), ExamSetSCPQuestionDetailCache.class);
        if (cache == null) {
            GetQuestionRequest request = new GetQuestionRequest(CommonConfig.getOpenAPIId());
            List<String> inputMenuIds = Arrays.asList(exam.getQuestionIds().split(","));
            List<Integer> questionIds = new LinkedList<Integer>();
            CollectionUtils.collect(inputMenuIds, new Transformer() {
                @Override
                public Object transform(Object o) {
                    return Integer.valueOf(o.toString());
                }
            }, questionIds);
            request.setQuestionIds(questionIds);
            request.setSign(SignedUtils.GetRequestSign(CommonConfig.getAppToken(), request));
            ApiResultEntity returnEntity = OpenApiHttpUtils.sendPost(CommonConfig.getOpenAPI().concat("/Shanceping/QuestionDetails"), request);
            SCPQuestionDetailView result = null;
            if (null != returnEntity && returnEntity.getApiResultType() == 1 && null != returnEntity && returnEntity.getValue() != null) {
                result = JSONObject.parseObject(returnEntity.getValue(), SCPQuestionDetailView.class);
                RedisUtil.setObj(new ExamSetSCPQuestionDetailCache(exam.getExamSetId(), result), CommonConst.MINUTE_120);
            }
            return result;
        }

        return cache.getScpQuestionDetailView();
    }

    @Override
    public TeacherExamDetailModel getTeacherExamDetail(String examId) throws Exception {
        GetTeacherExamDetailRequest request = new GetTeacherExamDetailRequest(CommonConfig.getOpenAPIId()).setTeacherExamId(examId);
        request.setSign(SignedUtils.GetRequestSign(CommonConfig.getAppToken(), request));
        ApiResultEntity returnEntity = OpenApiHttpUtils.sendPost(CommonConfig.getOpenAPI().concat("/Exam/GetTeacherExamDetail"), request);
        if (null != returnEntity && returnEntity.getApiResultType() == 1 && null != returnEntity) {
            return JSONObject.parseObject(returnEntity.getValue(), TeacherExamDetailModel.class);
        }
        return null;
    }

    @Override
    public ApiResultEntity checkQuestion(List<Integer> questionIds) throws Exception {
        CheckQuestionRequest request = new CheckQuestionRequest(CommonConfig.getOpenAPIId()).setQuestionIds(questionIds);
        request.setSign(SignedUtils.GetRequestSign(CommonConfig.getAppToken(), request));
        ApiResultEntity returnEntity = OpenApiHttpUtils.sendPost(CommonConfig.getOpenAPI().concat("/ShanCePing/CheckQuestion_V2"), request);
        return returnEntity;
    }


    private List<BookVersion> getBookVersionsByCourseId(Integer courseId) throws Exception {
        List<BookVersion> bookVersions = new LinkedList<BookVersion>();
        BookVersionsCache cache = RedisUtil.getObj(new BookVersionsCache(courseId).getKey(), BookVersionsCache.class);
        if (cache == null || CollectionUtils.isEmpty(cache.getBookVersions())) {
            GetBookVersionOpenApiRequest request = new GetBookVersionOpenApiRequest(CommonConfig.getOpenAPIId(), UUID.randomUUID().toString(), courseId);
            request.setSign(SignedUtils.GetRequestSign(CommonConfig.getAppToken(), request));
            ApiResultEntity returnEntity = OpenApiHttpUtils.sendPost(CommonConfig.getOpenAPI().concat("/Common/GetBookVersion"), request);
            if (null != returnEntity && returnEntity.getApiResultType() == 1 && null != returnEntity) {
                List<BookVersion> list = JSONObject.parseArray(returnEntity.getValue(), BookVersion.class);
                if (CollectionUtils.isNotEmpty(list)) {
                    list.forEach(m -> m.setCourseId(courseId));
                    bookVersions.addAll(list);
                    RedisUtil.setObj(new BookVersionsCache(courseId, list), CommonConst.MINUTE_120);
                    return list;
                }
            }
            return null;
        }
        return cache.getBookVersions();
    }

    private List<CourseMapping> getCourseMappingByBookVerionId(Integer bookVerionId) throws Exception {
        List<CourseMapping> courseMappings = new LinkedList<CourseMapping>();
        CourseMappingsCache cache = RedisUtil.getObj(new CourseMappingsCache(bookVerionId).getKey(), CourseMappingsCache.class);
        if (cache == null || CollectionUtils.isEmpty(cache.getCourseMappings())) {
            GetCourseMappingRequest request = new GetCourseMappingRequest(CommonConfig.getOpenAPIId(), UUID.randomUUID().toString(), bookVerionId);
            request.setSign(SignedUtils.GetRequestSign(CommonConfig.getAppToken(), request));
            ApiResultEntity returnEntity = OpenApiHttpUtils.sendPost(CommonConfig.getOpenAPI().concat("/Common/GetCourseMapping"), request);
            if (null != returnEntity && returnEntity.getApiResultType() == 1 && null != returnEntity) {
                List<CourseMapping> list = JSONObject.parseArray(returnEntity.getValue(), CourseMapping.class);
                if (CollectionUtils.isNotEmpty(list)) {
                    list.forEach(m -> m.setBookVersionId(bookVerionId));
                    courseMappings.addAll(list);
                    RedisUtil.setObj(new CourseMappingsCache(bookVerionId, list), CommonConst.MINUTE_120);
                    return list;
                }
            }
            return null;
        }
        return cache.getCourseMappings();
    }

    private List<ChapterSection> getChapterSectionByCourseMappingId(Integer courseMappingId) throws Exception {
        List<ChapterSection> chapterSections = new LinkedList<ChapterSection>();
        ChapterSectionsCache cache = RedisUtil.getObj(new ChapterSectionsCache(courseMappingId).getKey(), ChapterSectionsCache.class);
        if (cache == null || CollectionUtils.isEmpty(cache.getChapterSections())) {
            GetChapterSectionRequest request = new GetChapterSectionRequest(CommonConfig.getOpenAPIId(), UUID.randomUUID().toString(), courseMappingId);
            request.setSign(SignedUtils.GetRequestSign(CommonConfig.getAppToken(), request));
            ApiResultEntity returnEntity = OpenApiHttpUtils.sendPost(CommonConfig.getOpenAPI().concat("/Common/GetChapterSection"), request);
            if (null != returnEntity && returnEntity.getApiResultType() == 1 && null != returnEntity) {
                List<ChapterSection> list = JSONObject.parseArray(returnEntity.getValue(), ChapterSection.class);
                if (CollectionUtils.isNotEmpty(list)) {
                    list.forEach(m -> m.setCourseMappingId(courseMappingId));
                    chapterSections.addAll(list);
                    RedisUtil.setObj(new ChapterSectionsCache(courseMappingId, list), CommonConst.MINUTE_120);
                    return list;
                }
            }
            return null;
        }
        return cache.getChapterSections();
    }

}
