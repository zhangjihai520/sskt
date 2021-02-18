package com.ry.sskt.mapper;

import com.ry.sskt.model.common.entity.TwoTuple;
import com.ry.sskt.model.student.entity.EvaluateRecord;
import com.ry.sskt.model.student.entity.StudentEvaluateSubject;
import com.ry.sskt.model.student.entity.TeacherEvaluateSubject;
import com.ry.sskt.model.student.entity.view.GetAllSubjectEvaluateView;
import com.ry.sskt.model.student.entity.view.GetSubjectEvaluateView;
import com.ry.sskt.model.student.entity.view.TeacherToStudentEvaluateView;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评价表 Mapper 接口
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
public interface EvaluateRecordMapper {
    /// <summary>
    /// 新增或者更新一条EvaluateRecord表数据
    /// </summary>
    /// <param name="dataModel">实体类</param>
    /// <returns>新增或更新后的主键Id</returns>
    @Select({
            "call VideoClassMain_SP_EvaluateRecord_Save(",
            "#{model.subjectId,mode=IN,jdbcType=INTEGER},",
            "#{model.sourseUserId,mode=IN,jdbcType=INTEGER},",
            "#{model.targetUserId,mode=IN,jdbcType=INTEGER},",
            "#{model.evaluateTypeId,mode=IN,jdbcType=INTEGER},",
            "#{model.evaluateLevel,mode=IN,jdbcType=INTEGER},",
            "#{model.evaluateComment,mode=IN,jdbcType=VARCHAR},",
            "#{model.statusFlag,mode=IN,jdbcType=INTEGER},",
            "#{model.createDateTime,mode=IN,jdbcType=DATE},",
            "#{model.updateDateTime,mode=IN,jdbcType=DATE})"
    })
    @Options(statementType = StatementType.CALLABLE)
    int Save(@Param("model") EvaluateRecord model);

    /// <summary>
    /// 根据主键获取一条EvaluateRecord表数据
    /// </summary>
    /// <param name="subjectId">课程id</param>
    /// <param name="sourseUserId">评价用户id</param>
    /// <param name="targetUserId">被评价用户id</param>
    /// <returns>查询到的表实体对象</returns>
    @Select({
            "call VideoClassMain_SP_EvaluateRecord_ReadByKey(",
            "#{subjectId,mode=IN,jdbcType=INTEGER},",
            "#{sourseUserId,mode=IN,jdbcType=INTEGER},",
            "#{targetUserId,mode=IN,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    EvaluateRecord GetByKey(@Param("subjectId") int subjectId, @Param("sourseUserId") int sourseUserId, @Param("targetUserId") int targetUserId);

    /// <summary>
    /// 获取助教对课程评价列表
    /// </summary>
    /// <param name="teacherId"></param>
    /// <param name="pageIndex"></param>
    /// <param name="pageSize"></param>
    /// <returns></returns>
    @Select({
            "call VideoClassMain_SP_EvaluateRecord_GetTeacherEvaluateSubjectList(",
            "#{map.teacherId,mode=IN,jdbcType=INTEGER},",
            "#{map.pageSkip,mode=IN,jdbcType=INTEGER},",
            "#{map.pageSize,mode=IN,jdbcType=INTEGER},",
            "#{map.isOnline,mode=IN,jdbcType=INTEGER},",
            "#{map.totalCount,mode=OUT,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    List<TeacherEvaluateSubject> GetTeacherEvaluateSubjectList(@Param("map") Map<String, Object> map);

    /// <summary>
    /// 获取学生对课程的评价列表
    /// </summary>
    /// <param name="studentId"></param>
    /// <param name="pageIndex"></param>
    /// <param name="pageSize"></param>
    /// <returns></returns>
    @Select({
            "call VideoClassMain_SP_EvaluateRecord_GetStudentEvaluateSubjectList(",
            "#{map.studentId,mode=IN,jdbcType=INTEGER},",
            "#{map.pageSkip,mode=IN,jdbcType=INTEGER},",
            "#{map.pageSize,mode=IN,jdbcType=INTEGER},",
            "#{map.isOnline,mode=IN,jdbcType=INTEGER},",
            "#{map.totalCount,mode=OUT,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    List<StudentEvaluateSubject> GetStudentEvaluateSubjectList(@Param("map") Map<String, Object> map);

    /// <summary>
    /// 获取老师对学生的评价
    /// </summary>
    /// <param name="studentId"></param>
    /// <param name="pageIndex"></param>
    /// <param name="pageSize"></param>
    /// <returns></returns>
    @Select({
            "call VideoClassMain_SP_EvaluateRecord_GetStuEvaFromTea(",
            "#{map.studentId,mode=IN,jdbcType=INTEGER},",
            "#{map.pageSkip,mode=IN,jdbcType=INTEGER},",
            "#{map.pageSize,mode=IN,jdbcType=INTEGER},",
            "#{map.isOnline,mode=IN,jdbcType=INTEGER},",
            "#{map.totalCount,mode=OUT,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    List<TeacherToStudentEvaluateView> GetTeacherToStudentEvaluateList(@Param("map") Map<String, Object> map);

    /// <summary>
    /// 获取主教老师所有课程的评价
    /// </summary>
    /// <param name="teacherId"></param>
    /// <param name="pageIndex"></param>
    /// <param name="pageSize"></param>
    /// <returns></returns>
    @Select({
            "call VideoClassMain_SP_Subject_AllSubjectEvaluate(",
            "#{map.teacherId,mode=IN,jdbcType=INTEGER},",
            "#{map.pageSkip,mode=IN,jdbcType=INTEGER},",
            "#{map.pageSize,mode=IN,jdbcType=INTEGER},",
            "#{map.isOnline,mode=IN,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    List<GetAllSubjectEvaluateView> GetAllSubjectEvaluate(@Param("map") Map<String, Object> map);

    /// <summary>
    /// 获取用户对主讲老师的评价列表
    /// </summary>
    /// <param name="teacherId"></param>
    /// <param name="subjectId"></param>
    /// <param name="evaluateTypeId"></param>
    /// <param name="pageIndex"></param>
    /// <param name="pageSize"></param>
    /// <returns></returns>
    @Select({
            "call VideoClassMain_SP_EvaluateRecord_GetListByMainTeacher(",
            "#{map.teacherId,mode=IN,jdbcType=INTEGER},",
            "#{map.subjectId,mode=IN,jdbcType=INTEGER},",
            "#{map.evaluateTypeId,mode=IN,jdbcType=INTEGER},",
            "#{map.pageSkip,mode=IN,jdbcType=INTEGER},",
            "#{map.pageSize,mode=IN,jdbcType=INTEGER},",
            "#{map.isOnline,mode=IN,jdbcType=INTEGER},",
            "#{map.totalCount,mode=OUT,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    List<GetSubjectEvaluateView> GetSubjectEvaluate(@Param("map") Map<String, Object> map);
}
