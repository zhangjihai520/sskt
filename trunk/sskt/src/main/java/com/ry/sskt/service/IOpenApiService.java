package com.ry.sskt.service;

import com.ry.sskt.model.common.entity.ChapterSection;
import com.ry.sskt.model.examset.entity.ExamSet;
import com.ry.sskt.model.examset.view.SCPEvaluationResultView;
import com.ry.sskt.model.examset.view.SCPQuestionDetailView;
import com.ry.sskt.model.openapi.entity.BookVersion;
import com.ry.sskt.model.openapi.entity.CourseMapping;
import com.ry.sskt.model.openapi.entity.StudentQuestion;
import com.ry.sskt.model.openapi.entity.TeacherExamDetailModel;
import com.ry.sskt.model.openapi.request.GetUserSessionRequest;
import com.ry.sskt.model.openapi.response.ApiResultEntity;
import com.ry.sskt.model.openapi.response.UserBindResponse;

import java.util.List;

public interface IOpenApiService {
    /**
     * 通过学科获取教材
     *
     * @param courseIds
     * @return
     */
    List<BookVersion> getBookVersions(int[] courseIds) throws Exception;

    /**
     * 通过教材版本获取课本
     *
     * @param bookVerionIds
     * @return
     */
    List<CourseMapping> getCourseMappings(int[] bookVerionIds) throws Exception;

    /**
     * 通过课本获取章节
     *
     * @param courseMappingIds
     * @return
     */
    List<ChapterSection> getChapterSections(int[] courseMappingIds) throws Exception;

    /// <summary>
    /// 用户注册
    /// </summary>
    /// <param name="request"></param>
    /// <returns></returns>
    UserBindResponse getUserSession(GetUserSessionRequest request) throws Exception;

    /// <summary>
    /// 提交学生做题结果，并返回学生测评结果
    /// </summary>
    /// <param name="SCPStudentId"></param>
    /// <param name="gradeId"></param>
    /// <param name="studentQuestions"></param>
    /// <param name="examSetId">暂时不用试卷的统计信息，所以该值先不传递</param>
    /// <returns></returns>
    SCPEvaluationResultView submitExamResult(String SCPStudentId, int gradeId, List<StudentQuestion> studentQuestions, int examSetId) throws Exception;

    /// <summary>
    /// 获取题集题目信息
    /// </summary>
    /// <param name="exam"></param>
    SCPQuestionDetailView getQuestionDetail(ExamSet exam) throws Exception;

    /// <summary>
    /// 获取题集详细信息
    /// </summary>
    /// <param name="examId"></param>
    /// <returns></returns>
    TeacherExamDetailModel getTeacherExamDetail(String examId) throws Exception;

    /// <summary>
    /// 校验题目
    /// </summary>
    /// <param name="questionIds"></param>
    /// <returns></returns>
    ApiResultEntity checkQuestion(List<Integer> questionIds) throws Exception;
}
