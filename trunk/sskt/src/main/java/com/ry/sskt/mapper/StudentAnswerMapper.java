package com.ry.sskt.mapper;

import com.ry.sskt.model.examset.entity.ExamSet;
import com.ry.sskt.model.examset.entity.StudentAnswer;
import com.ry.sskt.model.examset.view.ExamSetListView;
import com.ry.sskt.model.examset.view.GetStatistListView;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 作答 Mapper 接口
 * </p>
 *
 * @author xrq
 * @since 2020-04-20
 */
public interface StudentAnswerMapper {

    /**
     * 保存作业
     *
     * @return
     */
    @Select({
            "call VideoClassMain_SP_StudentAnswer_Save(",
            "#{model.examSetId,mode=IN,jdbcType=INTEGER},",
            "#{model.userId,mode=IN,jdbcType=INTEGER},",
            "#{model.subjectId,mode=IN,jdbcType=INTEGER},",
            "#{model.subjectRoomId,mode=IN,jdbcType=INTEGER},",
            "#{model.score,mode=IN,jdbcType=DECIMAL},",
            "#{model.questionResult,mode=IN,jdbcType=VARCHAR},",
            "#{model.motkResultInfo,mode=IN,jdbcType=VARCHAR},",
            "#{model.answerNumber,mode=IN,jdbcType=INTEGER},",
            "#{model.statusFlag,mode=IN,jdbcType=INTEGER},",
            "#{model.createDateTime,mode=IN,jdbcType=DATE},",
            "#{model.updateDateTime,mode=IN,jdbcType=DATE})"
    })
    @Options(statementType = StatementType.CALLABLE)
    int save(@Param("model") StudentAnswer model);

    /// <summary>
    /// 根据主键获取一条ExamSet表数据
    /// </summary>
    /// <param name="examSetId">作业id</param>
    /// <returns>查询到的表实体对象</returns>
    @Select("CALL VideoClassMain_SP_StudentAnswer_ReadByKey(#{examSetId},#{userId})")
    @Options(statementType = StatementType.CALLABLE)
    StudentAnswer getByKey(@Param("examSetId") int examSetId, @Param("userId") int userId);


    /// <summary>
    /// 获取学生该课程下的所有作答
    /// </summary>
    /// <param name="userid"></param>
    /// <param name="subjectIds"></param>
    /// <returns></returns>
    @Select("Call VideoClassMain_SP_StudentAnswer_GetListBySubjectIds(#{userid},#{subjectIds})")
    @Options(statementType = StatementType.CALLABLE)
    List<StudentAnswer> getStudentAnswerBySubjectIds(@Param("userid") int userid, @Param("subjectIds") String subjectIds);


    /// <summary>
    /// 根据课程ID获取该课程下的所有作答
    /// </summary>
    /// <param name="userid"></param>
    /// <param name="subjectIds"></param>
    /// <returns></returns>
    @Select("Call VideoClassMain_SP_StudentAnswer_GetListBySubjectId(#{examSetId},#{subjectId})")
    @Options(statementType = StatementType.CALLABLE)
    List<StudentAnswer> getStudentAnswerBySubjectId(@Param("examSetId") int examSetId, @Param("subjectId") int subjectId);


    /// <summary>
    /// 根据房间id获取该课程下的所有作答
    /// </summary>
    /// <param name="userid"></param>
    /// <param name="subjectIds"></param>
    /// <returns></returns>
    @Select("Call VideoClassMain_SP_StudentAnswer_GetListBySubjectRoomId(#{examSetId},#{subjectRoomId})")
    @Options(statementType = StatementType.CALLABLE)
    List<StudentAnswer> getStudentAnswerBySubjectRoomId(@Param("examSetId") int examSetId, @Param("subjectRoomId") int subjectRoomId);
}
