package com.ry.sskt.mapper;

import com.ry.sskt.model.common.constant.SubjectGenreEnum;
import com.ry.sskt.model.common.constant.SubjectStatusFlagEnum;
import com.ry.sskt.model.common.entity.Course;
import com.ry.sskt.model.common.entity.CourseType;
import com.ry.sskt.model.common.entity.Grade;
import com.ry.sskt.model.student.entity.view.*;
import com.ry.sskt.model.subject.entity.Subject;
import com.ry.sskt.model.subject.entity.view.SubjectListByFilterView;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.type.JdbcType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程表 Mapper 接口
 * </p>
 *
 * @author xrq
 * @since 2020-04-20
 */
public interface SubjectMapper {
    /// <summary>
    /// 新增或者更新一条Subject表数据
    /// </summary>
    /// <param name="dataModel">实体类</param>
    /// <returns>新增或更新后的主键Id</returns>
    @Select({
            "call VideoClassMain_SP_Subject_Save_V2(",
            "#{model.subjectId,mode=IN,jdbcType=INTEGER},",
            "#{model.subjectName,mode=IN,jdbcType=VARCHAR},",
            "#{model.registBeginTime,mode=IN,jdbcType=DATE},",
            "#{model.registEndTime,mode=IN,jdbcType=DATE},",
            "#{model.beginTime,mode=IN,jdbcType=DATE},",
            "#{model.endTime,mode=IN,jdbcType=DATE},",
            "#{model.gradeId,mode=IN,jdbcType=INTEGER},",
            "#{model.courseId,mode=IN,jdbcType=INTEGER},",
            "#{model.teacherId,mode=IN,jdbcType=INTEGER},",
            "#{model.imagePath,mode=IN,jdbcType=VARCHAR},",
            "#{model.comment,mode=IN,jdbcType=VARCHAR},",
            "#{model.statusFlag,mode=IN,jdbcType=INTEGER},",
            "#{model.subjectTypeId,mode=IN,jdbcType=INTEGER},",
            "#{model.videoRoom,mode=IN,jdbcType=VARCHAR},",
            "#{model.classStateId,mode=IN,jdbcType=INTEGER},",
            "#{model.filePath,mode=IN,jdbcType=VARCHAR},",
            "#{model.pptFilePath,mode=IN,jdbcType=VARCHAR},",
            "#{model.subjectGenreId,mode=IN,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    int save(@Param("model") Subject model);

    /// <summary>
    /// 根据主键获取一条Subject表数据
    /// </summary>
    /// <param name="subjectId">课程id</param>
    /// <returns>查询到的表实体对象</returns>
    @Select("Call VideoClassMain_SP_Subject_ReadByKey(#{subjectId})")
    @Options(statementType = StatementType.CALLABLE)
    Subject getByKey(@Param("subjectId") int subjectId);


    /// <summary>
    /// 根据老师ID获取对应课程
    /// </summary>
    /// <param name="teacherId"></param>
    /// <returns></returns>
    @Select("Call VideoClassMain_SP_Subject_GetListByTeacherId(#{teacherId},#{isOnline})")
    @Options(statementType = StatementType.CALLABLE)
    List<Subject> getListByTeacherId(@Param("teacherId") int teacherId, @Param("isOnline") int isOnline);

    /**
     * 根据筛选条件获取课程列表
     *
     * @return
     */
    @Select({
            "call VideoClassMain_SP_Subject_GetListByFilterV3(",
            "#{map.teacherId,mode=IN,jdbcType=INTEGER},",
            "#{map.key,mode=IN,jdbcType=VARCHAR},",
            "#{map.courseId,mode=IN,jdbcType=INTEGER},",
            "#{map.gradeId,mode=IN,jdbcType=INTEGER},",
            "#{map.maxTimeField,mode=IN,jdbcType=VARCHAR},",
            "#{map.minTimeField,mode=IN,jdbcType=VARCHAR},",
            "#{map.beginTime,mode=IN,jdbcType=VARCHAR},",
            "#{map.endTime,mode=IN,jdbcType=VARCHAR},",
            "#{map.beginTimeMax,mode=IN,jdbcType=VARCHAR},",
            "#{map.beginTimeMin,mode=IN,jdbcType=VARCHAR},",
            "#{map.status,mode=IN,jdbcType=INTEGER},",
            "#{map.classState,mode=IN,jdbcType=INTEGER},",
            "#{map.pageSkip,mode=IN,jdbcType=INTEGER},",
            "#{map.pageSize,mode=IN,jdbcType=INTEGER},",
            "#{map.isOnline,mode=IN,jdbcType=INTEGER},",
            "#{map.subjectGenreId,mode=IN,jdbcType=INTEGER},",
            "#{map.OrderByField,mode=IN,jdbcType=VARCHAR},",
            "#{map.totalCount,mode=OUT,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    List<SubjectListByFilterView> GetListByFilter(@Param("map") Map map);

    /// <summary>
    /// 修改指定课程的状态
    /// </summary>
    /// <param name="subjectId"></param>
    /// <param name="status"></param>
    /// <returns></returns>
    @Select("Call VideoClassMain_SP_Subject_UpdateStatusFlag(#{subjectId},#{status})")
    @Options(statementType = StatementType.CALLABLE)
    void updateStatusFlag(@Param("subjectId") int subjectId, @Param("status") int status);//int


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
    @Select({
            "call VideoClassMain_SP_Subject_GetListH5(",
            "#{beginMonth,mode=IN,jdbcType=DATE},",
            "#{endMonth,mode=IN,jdbcType=DATE},",
            "#{beginDay,mode=IN,jdbcType=DATE},",
            "#{endDay,mode=IN,jdbcType=DATE},",
            "#{teacherId,mode=IN,jdbcType=INTEGER},",
            "#{studentId,mode=IN,jdbcType=INTEGER},",
            "#{isOnline,mode=IN,jdbcType=INTEGER},",
            "#{subjectGenreEnumCode,mode=IN,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    List<GetUserSubjectListH5View> GetUserSubjectListH5(@Param("beginMonth") LocalDate beginMonth, @Param("endMonth") LocalDate endMonth,
                                                        @Param("beginDay") LocalDate beginDay, @Param("endDay") LocalDate endDay,
                                                        @Param("teacherId") int teacherId, @Param("studentId") int studentId,
                                                        @Param("isOnline") int isOnline, @Param("subjectGenreEnumCode") int subjectGenreEnumCode);

    /// <summary>
    /// 获取学生课程H5信息，包含作业信息
    /// </summary>
    /// <param name="studentId">学生ID</param>
    /// <returns></returns>
    @Select("Call VideoClassMain_SP_Subject_GetListByStudentH5(#{studentId},#{isOnline})")
    @Options(statementType = StatementType.CALLABLE)
    List<GetStudentSubjectListH5View> GetStudentSubjectListH5(@Param("studentId") int studentId, @Param("isOnline") int isOnline);

    /// <summary>
    /// 获取课程的评价数
    /// </summary>
    /// <param name="subjectIds">课程ID列表</param>
    /// <returns></returns>
    @Select("Call VideoClassMain_SP_Subject_GetPraise(#{subjectIds})")
    @Options(statementType = StatementType.CALLABLE)
    List<GetPraiseView> GetPraise(@Param("subjectIds") String subjectIds);

    /// <summary>
    /// 获取课程上课人数
    /// </summary>
    /// <param name="subjectIds">课程ID列表</param>
    /// <returns></returns>
    @Select("Call VideoClassMain_SP_Subject_GetClassAttendance(#{subjectIds})")
    @Options(statementType = StatementType.CALLABLE)
    List<GetClassAttendanceView> GetClassAttendance(@Param("subjectIds") String subjectIds);

    /// <summary>
    /// 搜索学生报名的课程，根据课程名称或主讲老师
    /// </summary>
    /// <param name="key">搜索Key</param>
    /// <param name="studentId">学生ID</param>
    /// <param name="minBeginTime">最小开始时间</param>
    /// <returns></returns>
    @Select({
            "call VideoClassMain_SP_Subject_SearchStudentSubject(",
            "#{key,mode=IN,jdbcType=VARCHAR},",
            "#{studentId,mode=IN,jdbcType=INTEGER},",
            "#{minBeginTime,mode=IN,jdbcType=DATE},",
            "#{isOnline,mode=IN,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    List<SearchStudentSubjectView> SearchStudentSubject(@Param("key") String key, @Param("studentId") int studentId, @Param("minBeginTime") LocalDateTime minBeginTime, @Param("isOnline") int isOnline);

    /// <summary>
    /// 根据老师ID和时间获取对应课程
    /// </summary>
    /// <param name="teacherId"></param>
    /// <param name="helperTeacherId"></param>
    /// <param name="beginTime"></param>
    /// <param name="endDateTime"></param>
    /// <returns></returns>
    @Select({
            "call VideoClassMain_SP_Subject_GetTeacherSubjectByDate(",
            "#{teacherId,mode=IN,jdbcType=INTEGER},",
            "#{helperTeacherId,mode=IN,jdbcType=INTEGER},",
            "#{endTime,mode=IN,jdbcType=DATE},",
            "#{beginTime,mode=IN,jdbcType=DATE},",
            "#{isOnline,mode=IN,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    List<Subject> GetTeacherSubjectByDate(@Param("teacherId") int teacherId, @Param("helperTeacherId") int helperTeacherId,
                                          @Param("beginTime") LocalDate beginTime, @Param("endTime") LocalDate endTime, @Param("isOnline") int isOnline);

    /// <summary>
    /// 获取学校/年级人数占比
    /// </summary>
    /// <param name="teacherId"></param>
    /// <param name="dataType"></param>
    /// <returns></returns>
    @Select({
            "call VideoClassMain_SP_Subject_GetSchoolStudentCount(",
            "#{teacherId,mode=IN,jdbcType=INTEGER},",
            "#{isOnline,mode=IN,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    List<SchoolStudentCountView> GetSchoolStudentCountView(@Param("teacherId") int teacherId, @Param("isOnline") int isOnline);

    /// <summary>
    /// 获取学校/年级人数占比
    /// </summary>
    /// <param name="teacherId"></param>
    /// <param name="dataType"></param>
    /// <returns></returns>
    @Select({
            "call VideoClassMain_SP_Subject_GetGradeStudentCount(",
            "#{teacherId,mode=IN,jdbcType=INTEGER},",
            "#{isOnline,mode=IN,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    List<SchoolStudentCountView> GetGradeStudentCountView(@Param("teacherId") int teacherId, @Param("isOnline") int isOnline);

    /// <summary>
    /// 根据课程ID集合获取课程列表
    /// </summary>
    /// <param name="subjectIds">课程ID列表</param>
    /// <returns></returns>
    @Select("Call VideoClassMain_SP_Subject_GetListBySubjectIds(#{subjectIds})")
    @Options(statementType = StatementType.CALLABLE)
    List<ExportSubjectListView> GetSubjectListBySubjectIds(@Param("subjectIds") String subjectIds);

    /// <summary>
    /// 获取课程热门数据
    /// </summary>
    /// <param name="teacherId"></param>
    /// <param name="pageSize"></param>
    /// <param name="pageSkip"></param>
    /// <param name="count">总数</param>
    /// <returns></returns>
    ///int teacherId, int pageSize, int pageSkip, ref int count, int isOnline
    @Select({
            "call VideoClassMain_SP_Subject_GetHotSubject(",
            "#{map.teacherId,mode=IN,jdbcType=INTEGER},",
            "#{map.pageSkip,mode=IN,jdbcType=INTEGER},",
            "#{map.pageSize,mode=IN,jdbcType=INTEGER},",
            "#{map.isOnline,mode=IN,jdbcType=INTEGER},",
            "#{map.totalCount,mode=OUT,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    List<HotDataView> GetHotSubjectList(@Param("map") Map map);

    /// <summary>
    /// 获取热门课程总数
    /// </summary>
    /// <param name="teacherId"></param>
    /// <returns></returns>
    @Select("Call VideoClassMain_SP_Subject_GetHotSubjectTotal(#{teacherId},#{isOnline})")
    @Options(statementType = StatementType.CALLABLE)
    HotDataTotalView GetHotSubjectTotal(@Param("teacherId") int teacherId, @Param("isOnline") int isOnline);

    /// <summary>
    /// 获取教师热门数据
    /// </summary>
    /// <param name="pageSize"></param>
    /// <param name="pageSkip"></param>
    /// <param name="count">总数</param>
    /// <returns></returns>
    ///int pageSize, int pageSkip, ref int count, int isOnline
    @Select({
            "call VideoClassMain_SP_Subject_GetHotTeacher(",
            "#{map.pageSkip,mode=IN,jdbcType=INTEGER},",
            "#{map.pageSize,mode=IN,jdbcType=INTEGER},",
            "#{map.isOnline,mode=IN,jdbcType=INTEGER},",
            "#{map.totalCount,mode=OUT,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    List<HotDataView> GetHotTeacherList(@Param("map") Map map);

    /// <summary>
    /// 获取热门教师总数
    /// </summary>
    /// <returns></returns>
    @Select("Call VideoClassMain_SP_Subject_GetHotTeacherTotal(#{isOnline})")
    @Options(statementType = StatementType.CALLABLE)
    HotDataTotalView GetHotTeacherTotal(@Param("isOnline") int isOnline);

    /// <summary>
    /// 获取学校热门数据
    /// </summary>
    /// <param name="pageSize"></param>
    /// <param name="pageSkip"></param>
    /// <param name="count">总数</param>
    /// <returns></returns>
    ///int pageSize, int pageSkip, ref int count, int isOnline
    @Select({
            "call VideoClassMain_SP_Subject_GetHotSchool(",
            "#{map.pageSkip,mode=IN,jdbcType=INTEGER},",
            "#{map.pageSize,mode=IN,jdbcType=INTEGER},",
            "#{map.isOnline,mode=IN,jdbcType=INTEGER},",
            "#{map.totalCount,mode=OUT,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    List<HotDataView> GetHotSchoolList(@Param("map") Map map);

    /// <summary>
    /// 获取热门学校总数
    /// </summary>
    /// <returns></returns>
    @Select("Call VideoClassMain_SP_Subject_GetHotSchoolTotal(#{isOnline})")
    @Options(statementType = StatementType.CALLABLE)
    HotDataTotalView GetHotSchoolTotal(@Param("isOnline") int isOnline);

    /// <summary>
    /// 插入学生学习轨迹并更新课程状态
    /// </summary>
    /// <returns></returns>
    @Select("Call VideoClassMain_SP_Subject_UpdateStateTask()")
    @Options(statementType = StatementType.CALLABLE)
    void UpdateSubjectStateTask();

    /// <summary>
    /// 课堂报名
    /// </summary>
    /// <param name="subjectId"></param>
    /// <param name="userId"></param>
    /// <param name="statusFlag"></param>
    /// <returns></returns>
    //TODO
    @Select("Call VideoClassMain_SP_Subject_RegistSubjectV2(#{subjectId},#{userId},#{maxRegisterNumber},#{statusFlag})")
    @Options(statementType = StatementType.CALLABLE)
    int studentRegistSubject(@Param("subjectId") int subjectId, @Param("userId") int userId, @Param("maxRegisterNumber") int maxRegisterNumber, @Param("statusFlag") int statusFlag);

    /// <summary>
    /// 课堂报名
    /// </summary>
    /// <param name="subjectId"></param>
    /// <param name="userId"></param>
    /// <param name="statusFlag"></param>
    /// <returns></returns>
    //TODO
    @Select("Call VideoClassMain_SP_Subject_TeacherRegistSubject(#{subjectId},#{userId},#{statusFlag})")
    @Options(statementType = StatementType.CALLABLE)
    int teacherRegistSubject(@Param("subjectId") int subjectId, @Param("userId") int userId, @Param("statusFlag") int statusFlag);

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
    @Select({
            "call VideoClassMain_SP_Subject_GetStudentListByFilter(",
            "#{map.studentId,mode=IN,jdbcType=INTEGER},",
            "#{map.courseId,mode=IN,jdbcType=INTEGER},",
            "#{map.gradeId,mode=IN,jdbcType=INTEGER},",
            "#{map.isEnd,mode=IN,jdbcType=INTEGER},",
            "#{map.isMine,mode=IN,jdbcType=INTEGER},",
            "#{map.pageSkip,mode=IN,jdbcType=INTEGER},",
            "#{map.pageSize,mode=IN,jdbcType=INTEGER},",
            "#{map.isOnline,mode=IN,jdbcType=INTEGER},",
            "#{map.subjectGenreId,mode=IN,jdbcType=INTEGER},",
            "#{map.keyWord,mode=IN,jdbcType=VARCHAR},",
            "#{map.totalCount,mode=OUT,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    List<GetStudentSubjectListView> GetStudentSubjectList(@Param("map") Map map);

    /// <summary>
    /// 根据老师ID获取课程总数及时间间隔内的课程数
    /// </summary>
    /// <param name="teacherId"></param>
    /// <param name="maxDateTime"></param>
    /// <param name="minDateTime">一般为本周第一天</param>
    /// <returns></returns>
    @Select({
            "call VideoClassMain_SP_Subject_GetTeacherSubjectCount(",
            "#{teacherId,mode=IN,jdbcType=INTEGER},",
            "#{maxDateTime,mode=IN,jdbcType=DATE},",
            "#{minDateTime,mode=IN,jdbcType=DATE},",
            "#{isOnline,mode=IN,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    GetTeacherSubjectCountView GetTeacherSubjectCount(@Param("teacherId") int teacherId, @Param("maxDateTime") LocalDateTime maxDateTime, @Param("minDateTime") LocalDateTime minDateTime, @Param("isOnline") int isOnline);

    /// <summary>
    /// 更新主线路视频地址
    /// </summary>
    /// <param name="dataModel"></param>
    /// <returns></returns>
    @Select({
            "call VideoClassMain_SP_Subject_UpdateFilePath(",
            "#{model.subjectId,mode=IN,jdbcType=INTEGER},",
            "#{model.filePath,mode=IN,jdbcType=DATE},",
            "#{model.classStateId,mode=IN,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    void UpdateFilePath(@Param("model") Subject model);

    /// <summary>
    /// 更新PPT线路视频地址
    /// </summary>
    /// <param name="dataModel"></param>
    /// <returns></returns>
    @Select({
            "call VideoClassMain_SP_Subject_UpdatePPTFilePath(",
            "#{model.subjectId,mode=IN,jdbcType=INTEGER},",
            "#{model.pptFilePath,mode=IN,jdbcType=DATE},",
            "#{model.classStateId,mode=IN,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    void UpdatePPTFilePath(@Param("model") Subject model);

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
    @Select({
            "call VideoClassMain_SP_Subject_GetAllListH5(",
            "#{beginMonth,mode=IN,jdbcType=DATE},",
            "#{endMonth,mode=IN,jdbcType=DATE},",
            "#{beginDay,mode=IN,jdbcType=DATE},",
            "#{endDay,mode=IN,jdbcType=DATE},",
            "#{isOnline,mode=IN,jdbcType=INTEGER},",
            "#{userId,mode=IN,jdbcType=INTEGER},",
            "#{subjectGenreId,mode=IN,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    List<GetUserSubjectListH5View> GetALLSubjectListH5(@Param("beginMonth") LocalDate beginMonth, @Param("endMonth") LocalDate endMonth,
                                                       @Param("beginDay") LocalDate beginDay, @Param("endDay") LocalDate endDay,
                                                       @Param("isOnline") int isOnline, @Param("userId") int userId, @Param("subjectGenreId") int subjectGenreId);

    /// <summary>
    /// 搜索上架课程
    /// </summary>
    /// <param name="beginTime"></param>
    /// <param name="key"></param>
    /// <param name="systemTypeId"></param>
    /// <param name="userId"></param>
    /// <returns></returns>
    @Select({
            "call VideoClassMain_SP_Subject_Search(",
            "#{key,mode=IN,jdbcType=DATE},",
            "#{beginTime,mode=IN,jdbcType=DATE},",
            "#{isOnline,mode=IN,jdbcType=INTEGER},",
            "#{userId,mode=IN,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    List<SearchSubjectView> SearchSubject(@Param("beginTime") LocalDateTime beginTime, @Param("key") String key, @Param("isOnline") int isOnline, @Param("userId") int userId);

    /// <summary>
    /// 课程手动下课
    /// </summary>
    /// <param name="subjectIds"></param>
    /// <returns></returns>
    @Select("Call VideoClassMain_SP_Subject_UpdateClassStatusBySubjectIds(#{subjectIds})")
    @Options(statementType = StatementType.CALLABLE)
    void CloseSubjects(@Param("subjectIds") String subjectIds);

    /// <summary>
    /// 课程自动下课
    /// </summary>
    /// <param name="maxDateTime">最晚上课时间，将在此时间之前的上课中课程改为已下课</param>
    /// <returns></returns>
    @Select("Call VideoClassMain_SP_Subject_UpdateClassStatusAutomatic(#{maxDateTime})")
    @Options(statementType = StatementType.CALLABLE)
    void CloseSubjectsForAutomatic(@Param("maxDateTime") LocalDateTime maxDateTime);


    /// <summary>
    /// 课程自动下课
    /// </summary>
    /// <param name="maxDateTime">最晚上课时间，将在此时间之前的上课中课程改为已下课</param>
    /// <returns></returns>
    @Update("update Subject set ClassStateID = #{classStateId} , PPTFilePath= #{pptFilePath} where subjectId = #{subjectId}")
    void UpdateClassStatus(@Param("subjectId") int subjectId, @Param("pptFilePath") String pptFilePath, @Param("classStateId") int classStateId);
}
