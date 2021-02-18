package com.ry.sskt.service;

import com.ry.sskt.model.common.constant.SubjectClassStateEnum;
import com.ry.sskt.model.common.constant.SubjectGenreEnum;
import com.ry.sskt.model.common.constant.SubjectListOrderFiledEnum;
import com.ry.sskt.model.common.constant.SubjectStatusFlagEnum;
import com.ry.sskt.model.student.entity.view.*;
import com.ry.sskt.model.subject.entity.Subject;
import com.ry.sskt.model.subject.entity.view.SubjectListByFilterView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 表Subject的接口
 */
public interface ISubjectService {

    /**
     * 获取所有公益课课程
     *
     * @param ifReadCache
     * @return
     */
    List<SubjectListByFilterView> getAllPublicBenefitSubject(boolean ifReadCache);

    /// <summary>
    /// 学生课堂报名
    /// </summary>
    /// <param name="subjectId"></param>
    /// <param name="userId"></param>
    /// <returns></returns>
    int studentRegistSubject(int subjectId, int userId, boolean isAbsent);

    /// <summary>
    /// 老师听课课堂报名
    /// </summary>
    /// <param name="subjectId"></param>
    /// <param name="userId"></param>
    /// <returns></returns>
    int teacherRegistSubject(int subjectId, int userId, boolean isAbsent);

    /// <summary>
    /// 新增或者更新一条Subject表数据
    /// </summary>
    /// <param name="dataModel">实体类</param>
    /// <returns>新增或更新后的主键Id</returns>
    int Save(Subject dataModel) throws Exception;

    /// <summary>
    /// 根据主键获取一条Subject表数据
    /// </summary>
    /// <param name="subjectId">课程id</param>
    /// <returns>查询到的表实体对象</returns>
    Subject getByKey(int subjectId);

    /// <summary>
    /// 根据老师ID获取对应课程
    /// </summary>
    /// <param name="teacherId"></param>
    /// <returns></returns>
    List<Subject> GetListByTeacherId(int teacherId, int isOnline);

    /// <summary>
    /// 根据筛选条件获取课程列表
    /// </summary>
    /// <param name="teacherId">教师Id</param>
    /// <param name="key">关键字</param>
    /// <param name="courseId">学科ID</param>
    /// <param name="gradeId">年级Id</param>
    /// <param name="beginTime">最小课程创建时间,无限制为DateTime.MinValue</param>
    /// <param name="endTime">最大课程创建时间,无限制为DateTime.MinValue</param>
    /// <param name="pageSkip"></param>
    /// <param name="pageSize"></param>
    /// <param name="status">课程状态</param>
    /// <param name="classState">上课状态</param>
    /// <param name="maxTimeField">最大时间字段</param>
    /// <param name="minTimeField">最小时间字段</param>
    /// <param name="totalCount"></param>
    /// <param name="isOnline"></param>
    /// <param name="subjectGenreEnum">课程类型 <see cref="SubjectGenreEnum"/> </param>
    /// <param name="beginTimeMax">最大课程上课时间,无限制为DateTime.MinValue</param>
    /// <param name="beginTimeMin">最小课程上课时间，无限制为DateTime.MinValue</param>
    /// <param name="orderFiledEnum">排序字段枚举</param>
    /// <returns></returns>
    ///int teacherId, String key, int courseId, int gradeId,
    //                                                  LocalDateTime beginTime, LocalDateTime endTime, int pageSkip, int pageSize, SubjectStatusFlagEnum status,
    //                                                  SubjectClassStateEnum classState, String maxTimeField, String minTimeField, ref int totalCount,
    //                                                  int isOnline, SubjectGenreEnum subjectGenreEnum, LocalDateTime beginTimeMax, LocalDateTime beginTimeMin,
    //                                                  SubjectListOrderFiledEnum orderFiledEnum
    List<SubjectListByFilterView> GetListByFilter(Map<String, Object> map);

    /// <summary>
    /// 修改指定课程的状态
    /// </summary>
    /// <param name="subjectId"></param>
    /// <param name="status"></param>
    /// <returns></returns>
    boolean UpdateStatusFlag(int subjectId, SubjectStatusFlagEnum status);

    /// <summary>
    /// H5获取时间段内的学生开课情况
    /// </summary>
    /// <param name="beginMonth"></param>
    /// <param name="endMonth"></param>
    /// <param name="beginDay"></param>
    /// <param name="endDay"></param>
    /// <param name="teacherId"></param>
    /// <param name="studentId"></param>
    /// <returns></returns>
    List<GetUserSubjectListH5View> GetUserSubjectListH5(LocalDate beginMonth, LocalDate endMonth,
                                                        LocalDate beginDay, LocalDate endDay, int teacherId, int studentId, int isOnline, int subjectGenreId);

    /// <summary>
    /// 获取学生课程H5信息，包含作业信息
    /// </summary>
    /// <param name="studentId">学生ID</param>
    /// <returns></returns>
    List<GetStudentSubjectListH5View> GetStudentSubjectListH5(int studentId, int isOnline);

    /// <summary>
    /// 获取课程的评价数
    /// </summary>
    /// <param name="subjectIds">课程ID列表</param>
    /// <returns></returns>
    List<GetPraiseView> GetPraise(List<Integer> subjectIds);

    /// <summary>
    /// 获取课程上课人数
    /// </summary>
    /// <param name="subjectIds">课程ID列表</param>
    /// <returns></returns>
    List<GetClassAttendanceView> GetClassAttendance(List<Integer> subjectIds);

    /// <summary>
    /// 搜索学生报名的课程，根据课程名称或主讲老师
    /// </summary>
    /// <param name="key">搜索Key</param>
    /// <param name="studentId">学生ID</param>
    /// <param name="minBeginTime">最小开始时间</param>
    /// <returns></returns>
    List<SearchStudentSubjectView> SearchStudentSubject(String key, int studentId, LocalDateTime minBeginTime, int isOnline);

    /// <summary>
    /// 根据老师ID和时间获取对应课程
    /// </summary>
    /// <param name="teacherId"></param>
    /// <param name="helperTeacherId"></param>
    /// <param name="beginTime"></param>
    /// <param name="endDateTime"></param>
    /// <returns></returns>
    List<Subject> GetTeacherSubjectByDate(int teacherId, int helperTeacherId, LocalDate beginTime, LocalDate endDateTime, int isOnline);

    /// <summary>
    /// 获取学校/年级人数占比
    /// </summary>
    /// <param name="teacherId"></param>
    /// <param name="dataType"></param>
    /// <returns></returns>
    List<SchoolStudentCountView> GetSchoolStudentCountView(int teacherId, int dataType, int isOnline);

    /// <summary>
    /// 根据课程ID集合获取课程列表
    /// </summary>
    /// <param name="subjectIds">课程ID列表</param>
    /// <returns></returns>
    List<ExportSubjectListView> GetSubjectListBySubjectIds(List<Integer> subjectIds);

    /// <summary>
    /// 获取课程热门数据
    /// </summary>
    /// <param name="teacherId"></param>
    /// <param name="pageSize"></param>
    /// <param name="pageSkip"></param>
    /// <param name="count">总数</param>
    /// <returns></returns>
    ///int teacherId, int pageSize, int pageSkip, ref int count, int isOnline
    List<HotDataView> GetHotSubjectList(Map<String, Object> map);

    /// <summary>
    /// 获取热门课程总数
    /// </summary>
    /// <param name="teacherId"></param>
    /// <returns></returns>
    HotDataTotalView GetHotSubjectTotal(int teacherId, int isOnline);

    /// <summary>
    /// 获取教师热门数据
    /// </summary>
    /// <param name="pageSize"></param>
    /// <param name="pageSkip"></param>
    /// <param name="count">总数</param>
    /// <returns></returns>
    ///int pageSize, int pageSkip, ref int count, int isOnline
    List<HotDataView> GetHotTeacherList(Map<String, Object> map);

    /// <summary>
    /// 获取热门教师总数
    /// </summary>
    /// <returns></returns>
    HotDataTotalView GetHotTeacherTotal(int isOnline);

    /// <summary>
    /// 获取学校热门数据
    /// </summary>
    /// <param name="pageSize"></param>
    /// <param name="pageSkip"></param>
    /// <param name="count">总数</param>
    /// <returns></returns>
    ///int pageSize, int pageSkip, ref int count, int isOnline
    List<HotDataView> GetHotSchoolList(Map<String, Object> map);

    /// <summary>
    /// 获取热门学校总数
    /// </summary>
    /// <returns></returns>
    HotDataTotalView GetHotSchoolTotal(int isOnline);

    /// <summary>
    /// 插入学生学习轨迹并更新课程状态
    /// </summary>
    /// <returns></returns>
    boolean UpdateSubjectStateTask();


    /// <summary>
    /// 获取学生课程列表
    /// </summary>
    /// <param name="studentId"></param>
    /// <param name="courseId"></param>
    /// <param name="gradeId"></param>
    /// <param name="typeEnum"></param>
    /// <param name="isMySubject"></param>
    /// <param name="pageSize"></param>
    /// <param name="pageSkip"></param>
    /// <param name="count"></param>
    /// <param name="isOnline"></param>
    /// <param name="keyWord"></param>
    /// <param name="subjectGenreEnum"></param>
    /// <returns></returns>
    ///int studentId, int courseId, int gradeId, GetStudentSubjectListEnum typeEnum, bool isMySubject, int pageSize, int pageSkip, ref int count, int isOnline, string keyWord, SubjectGenreEnum subjectGenreEnum
    List<GetStudentSubjectListView> GetStudentSubjectList(Map<String, Object> map);

    /// <summary>
    /// 根据老师ID获取课程总数及时间间隔内的课程数
    /// </summary>
    /// <param name="teacherId"></param>
    /// <param name="maxDateTime"></param>
    /// <param name="minDateTime">一般为本周第一天</param>
    /// <returns></returns>
    GetTeacherSubjectCountView GetTeacherSubjectCount(int teacherId, LocalDateTime maxDateTime, LocalDateTime minDateTime, int isOnline);

    /// <summary>
    /// 更新主线路视频地址
    /// </summary>
    /// <param name="dataModel"></param>
    /// <returns></returns>
    void UpdateFilePath(Subject dataModel);

    /// <summary>
    /// 更新PPT线路视频地址
    /// </summary>
    /// <param name="dataModel"></param>
    /// <returns></returns>
    void UpdatePPTFilePath(Subject dataModel);

    /// <summary>
    /// H5获取时间段内开课情况
    /// </summary>
    /// <param name="beginMonth"></param>
    /// <param name="endMonth"></param>
    /// <param name="beginDay"></param>
    /// <param name="endDay"></param>
    /// <param name="isOnline"></param>
    /// <param name="userId"></param>
    /// <returns></returns>
    List<GetUserSubjectListH5View> GetALLSubjectListH5(LocalDate beginMonth, LocalDate endMonth, LocalDate beginDay, LocalDate endDay, int isOnline, int userId, int subjectGenreId);

    /// <summary>
    /// 搜索上架课程
    /// </summary>
    /// <param name="beginTime"></param>
    /// <param name="key"></param>
    /// <param name="systemTypeId"></param>
    /// <param name="userId"></param>
    /// <returns></returns>
    List<SearchSubjectView> SearchSubject(LocalDateTime beginTime, String key, int systemTypeId, int userId);

    /// <summary>
    /// 课程手动下课
    /// 如果subjectIds为空或null，将所有符合条件的课程自动下课
    /// </summary>
    /// <param name="subjectIds"></param>
    /// <returns></returns>
    void CloseSubjects(List<Integer> subjectIds);

    /// <summary>
    /// 上课
    /// </summary>
    /// <param name="subjectId"></param>
    /// <returns></returns>
    void attendSubject(int subjectId, String pptFilePath);

    /// <summary>
    /// 下课
    /// </summary>
    /// <param name="subjectId"></param>
    /// <returns></returns>
    void closeSubject(int subjectId, String pptFilePath);

}
