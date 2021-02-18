package com.ry.sskt.mapper;

import com.ry.sskt.model.common.entity.User;
import com.ry.sskt.model.student.entity.SubjectRoomStudent;
import com.ry.sskt.model.student.entity.view.GetStudentListView;
import com.ry.sskt.model.student.entity.view.StudentListByGroupView;
import com.ry.sskt.model.student.entity.view.SubjectStudentCountView;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 学生报名表 Mapper 接口
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
public interface SubjectRoomStudentMapper {
    /// <summary>
    /// 新增或者更新一条SubjectRoomStudent表数据
    /// </summary>
    /// <param name="dataModel">实体类</param>
    /// <returns>新增或更新后的主键Id</returns>
    @Select({
            "call VideoClassMain_SP_SubjectRoomStudent_Save(",
            "#{model.subjectRoomId,mode=IN,jdbcType=INTEGER},",
            "#{model.userId,mode=IN,jdbcType=INTEGER},",
            "#{model.subjectId,mode=IN,jdbcType=INTEGER},",
            "#{model.isAbsent,mode=IN,jdbcType=INTEGER},",
            "#{model.isEvaluate,mode=IN,jdbcType=INTEGER},",
            "#{model.statusFlag,mode=IN,jdbcType=INTEGER},",
            "#{model.createDateTime,mode=IN,jdbcType=DATE},",
            "#{model.updateDateTime,mode=IN,jdbcType=DATE})"
    })
    @Options(statementType = StatementType.CALLABLE)
    int Save(@Param("model") SubjectRoomStudent model);

    /// <summary>
    /// 根据主键获取一条SubjectRoomStudent表数据
    /// </summary>
    /// <param name="subjectRoomId">教室id</param>
    /// <param name="userId">学生id</param>
    /// <returns>查询到的表实体对象</returns>
    @Select("call VideoClassMain_SP_SubjectRoomStudent_ReadByKey(#{subjectRoomId},#{userId})")
    @Options(statementType = StatementType.CALLABLE)
    SubjectRoomStudent GetByKey(@Param("subjectRoomId") int subjectRoomId, @Param("userId") int userId);

    /// <summary>
    /// 根据课程Id和学生Id获取教室学生信息
    /// </summary>
    /// <param name="subjectId"></param>
    /// <param name="userId"></param>
    /// <returns></returns>
    @Select("call VideoClassMain_SP_SubjectRoomStudent_ReadByUserIdAndSubjectId(#{subjectId},#{userId})")
    @Options(statementType = StatementType.CALLABLE)
    SubjectRoomStudent GetByUserIdAndSubjectId(@Param("subjectId") int subjectId, @Param("userId") int userId);

    /// <summary>
    /// 获取学生课程数
    /// </summary>
    /// <param name="userId">用户id</param>
    /// <returns>课程数量</returns>
    @Select("call VideoClassMain_SP_SubjectRoomStudent_GetSubjectCountByUserId(#{userId})")
    @Options(statementType = StatementType.CALLABLE)
    int GetSubjectCountByUserId(@Param("userId") int userId);

    /// <summary>
    /// 获取学生作业数
    /// </summary>
    /// <param name="userId">用户id</param>
    /// <returns>作业数量</returns>
    @Select("call VideoClassMain_SP_SubjectRoomStudent_GetExamSetCountByUserId(#{userId})")
    @Options(statementType = StatementType.CALLABLE)
    int GetExamSetCountByUserId(@Param("userId") int userId);

    /// <summary>
    /// 获取目标天数内的课程学生上课数量
    /// </summary>
    /// <param name="teacherId"></param>
    /// <param name="days"></param>
    /// <returns></returns>
    ///int teacherId, DateTime beginDate, DateTime endDate, int isOnline
    @Select({
            "call VideoClassMain_SP_Subject_GetStudentCount(",
            "#{teacherId,mode=IN,jdbcType=INTEGER},",
            "#{beginDate,mode=IN,jdbcType=DATE},",
            "#{endDate,mode=IN,jdbcType=DATE},",
            "#{isOnline,mode=IN,jdbcType=INTEGER})"
    })
    List<SubjectStudentCountView> GetStudentCountByTeacherId(@Param("teacherId") int teacherId, @Param("beginDate") LocalDate beginDate, @Param("endDate") LocalDate endDate, @Param("isOnline") int isOnline);

    /// <summary>
    /// 老师获取指定课程或教室的学生列表
    /// </summary>
    /// <param name="teacherId"></param>
    /// <param name="subjectId"></param>
    /// <param name="subjectRoomId"></param>
    /// <param name="searchKey">学生名称</param>
    /// <param name="isGetAttendance">是否只获取上课学生</param>
    /// <param name="pageSkip"></param>
    /// <param name="pageSize"></param>
    /// <param name="total"></param>
    /// <returns></returns>
    ///int teacherId, int subjectId, int subjectRoomId, string searchKey, bool isGetAttendance, int pageSkip, int pageSize, ref int total
    @Select({
            "call VideoClassMain_SP_SubjectRoomStudent_GetStudentListByFilter(",
            "#{map.teacherId,mode=IN,jdbcType=INTEGER},",
            "#{map.subjectId,mode=IN,jdbcType=INTEGER},",
            "#{map.subjectRoomId,mode=IN,jdbcType=INTEGER},",
            "#{map.searchKey,mode=IN,jdbcType=VARCHAR},",
            "#{map.isGetAttendance,mode=IN,jdbcType=INTEGER},",
            "#{map.pageSkip,mode=IN,jdbcType=INTEGER},",
            "#{map.pageSize,mode=IN,jdbcType=INTEGER},",
            "#{map.totalCount,mode=OUT,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    List<GetStudentListView> GetStudentList(@Param("map") Map map);

    /// <summary>
    /// 更新学生上课状态
    /// </summary>
    /// <param name="subjectId"></param>
    /// <param name="userId"></param>
    /// <returns></returns>
    @Select("call VideoClassMain_SP_SubjectRoomStudent_UpdateStatus(#{subjectId},#{userId},#{statusFlag})")
    @Options(statementType = StatementType.CALLABLE)
    boolean UpdateSubjectStudentStatus(@Param("subjectId") int subjectId, @Param("userId") int userId, @Param("statusFlag") int statusFlag);

    /// <summary>
    /// 批量插入学生报名
    /// </summary>
    /// <param name="dataModels"></param>
    /// <returns></returns>


    @Insert({
            "INSERT INTO subjectroomstudent (SubjectRoomID,UserID,SubjectID,IsAbsent,IsEvaluate,StatusFlag,CreateDateTime,UpdateDateTime) VALUES",
            "<foreach collection='models' item='item' index='index' separator=','>",
            "(#{item.subjectRoomId}, #{item.userId}, #{item.subjectId}, #{item.isAbsent}, #{item.isEvaluate}, #{item.statusFlag}, #{item.createDateTime}, #{item.updateDateTime})",
            "</foreach>",
    })
    int BatchInsertSubjectRoomStudent(@Param("models") List<SubjectRoomStudent> models);

    /// <summary>
    /// 获取学生分组列表
    /// </summary>
    /// <param name="subjectId"></param>
    /// <param name="subjectRoomId"></param>
    /// <param name="pageSkip"></param>
    /// <param name="pageSize"></param>
    /// <param name="totalCount"></param>
    /// <returns></returns>
    ///int subjectId, int subjectRoomId, int pageSkip, int pageSize, out int totalCount
    @Select({
            "call VideoClassMain_SP_SubjectRoomStudent_GetListByGroup(",
            "#{map.subjectId,mode=IN,jdbcType=INTEGER},",
            "#{map.subjectRoomId,mode=IN,jdbcType=INTEGER},",
            "#{map.pageSkip,mode=IN,jdbcType=INTEGER},",
            "#{map.pageSize,mode=IN,jdbcType=INTEGER},",
            "#{map.totalCount,mode=OUT,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    List<StudentListByGroupView> GetByGroupViews(@Param("map") Map map);

    /// <summary>
    /// 获取课程下的报名学生列表
    /// </summary>
    /// <param name="subjectId"></param>
    /// <returns></returns>
    @Select("call VideoClassMain_SP_SubjectRoomStudent_GetAllBySubjectId(#{subjectId})")
    @Options(statementType = StatementType.CALLABLE)
    List<SubjectRoomStudent> GetAllBySubjectId(@Param("subjectId") int subjectId);

    /// <summary>
    /// 删除课程下面的所有学生
    /// </summary>
    /// <param name="subjectId"></param>
    /// <returns></returns>
    @Select("call VideoClassMain_SP_SubjectRoomStudent_DeleteAllBySubjectId(#{subjectId})")
    @Options(statementType = StatementType.CALLABLE)
    int DeleteAllBySubjectId(@Param("subjectId") int subjectId);

    /// <summary>
    /// 获取课堂下的报名学生列表及助教老师列表
    /// </summary>
    /// <param name="subjectRoomId"></param>
    /// <returns></returns>
    @Select("call VideoClassMain_SP_SubjectRoomStudent_GetAllBySubjectRoomId(#{subjectRoomId})")
    @Options(statementType = StatementType.CALLABLE)
    List<User> getAllBySubjectRoomId(@Param("subjectRoomId") int subjectRoomId);
}
