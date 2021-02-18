package com.ry.sskt.mapper;

import com.ry.sskt.model.common.entity.User;
import com.ry.sskt.model.examset.entity.ExamSet;
import com.ry.sskt.model.examset.view.ExamSetListView;
import com.ry.sskt.model.examset.view.GetStatistListView;
import com.ry.sskt.model.subject.entity.Subject;
import com.ry.sskt.model.subject.entity.view.SubjectListByFilterView;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 作业 Mapper 接口
 * </p>
 *
 * @author xrq
 * @since 2020-04-20
 */
public interface ExamSetMapper {

    /**
     * 保存作业
     *
     * @return
     */
    @Select({
            "call VideoClassMain_SP_ExamSet_Save(",
            "#{model.examSetId,mode=IN,jdbcType=INTEGER},",
            "#{model.subjectId,mode=IN,jdbcType=INTEGER},",
            "#{model.examSetName,mode=IN,jdbcType=VARCHAR},",
            "#{model.courseId,mode=IN,jdbcType=INTEGER},",
            "#{model.gradeId,mode=IN,jdbcType=INTEGER},",
            "#{model.examSetTypeId,mode=IN,jdbcType=INTEGER},",
            "#{model.questionNumber,mode=IN,jdbcType=INTEGER},",
            "#{model.motkExamSetId,mode=IN,jdbcType=VARCHAR},",
            "#{model.questionIds,mode=IN,jdbcType=VARCHAR},",
            "#{model.statusFlag,mode=IN,jdbcType=INTEGER},",
            "#{model.score,mode=IN,jdbcType=DECIMAL},",
            "#{model.createDateTime,mode=IN,jdbcType=DATE},",
            "#{model.updateDateTime,mode=IN,jdbcType=DATE})"
    })
    @Options(statementType = StatementType.CALLABLE)
    int save(@Param("model") ExamSet model);

    /// <summary>
    /// 根据主键获取一条ExamSet表数据
    /// </summary>
    /// <param name="examSetId">作业id</param>
    /// <returns>查询到的表实体对象</returns>
    @Select("CALL VideoClassMain_SP_ExamSet_ReadByKey(#{examSetId})")
    @Options(statementType = StatementType.CALLABLE)
    ExamSet getByKey(@Param("examSetId") int examSetId);

    /// <summary>
    /// 获取主教老师的课堂作业列表
    /// </summary>
    /// <param name="subjectId"></param>
    /// <param name="courseId"></param>
    /// <param name="examSetName"></param>
    /// <param name="statusFlag"></param>
    /// <param name="pageSize"></param>
    /// <param name="pageIndex"></param>
    /// <returns></returns>
    @Select({
            "call VideoClassMain_SP_ExamSet_GetListBySubjectId(",
            "#{map.subjectId,mode=IN,jdbcType=INTEGER},",
            "#{map.courseId,mode=IN,jdbcType=INTEGER},",
            "#{map.statusFlag,mode=IN,jdbcType=INTEGER},",
            "#{map.examSetName,mode=IN,jdbcType=VARCHAR},",
            "#{map.pageSkip,mode=IN,jdbcType=INTEGER},",
            "#{map.pageSize,mode=IN,jdbcType=INTEGER},",
            "#{map.totalCount,mode=OUT,jdbcType=INTEGER});"
    })
    @Options(statementType = StatementType.CALLABLE)
    List<ExamSetListView> getExamSetListForTeacher(@Param("map") Map map);

    /// <summary>
    /// 获取作业统计列表
    /// </summary>
    /// <param name="examSetId"></param>
    /// <param name="subjectRoomId"></param>
    /// <param name="userTrueName"></param>
    /// <param name="pageSize"></param>
    /// <param name="pageIndex"></param>
    /// <returns></returns>
    @Select({
            "call VideoClassMain_SP_ExamSet_GetStatistList(",
            "#{map.examSetId,mode=IN,jdbcType=INTEGER},",
            "#{map.subjectRoomId,mode=IN,jdbcType=INTEGER},",
            "#{map.userTrueName,mode=IN,jdbcType=VARCHAR},",
            "#{map.pageSkip,mode=IN,jdbcType=INTEGER},",
            "#{map.pageSize,mode=IN,jdbcType=INTEGER},",
            "#{map.totalCount,mode=OUT,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    List<GetStatistListView> getStatistList(@Param("map") Map map);

    /// <summary>
    /// 获取课程下面的所有作业
    /// </summary>
    /// <param name="subjectId"></param>
    /// <returns></returns>
    @Select("CALL VideoClassMain_SP_ExamSet_GetAllBySubjectId(#{subjectId})")
    @Options(statementType = StatementType.CALLABLE)
    List<ExamSet> getExamSetList(@Param("subjectId") int subjectId);

    /// <summary>
    /// 获取课程下面的作业
    /// </summary>
    /// <param name="subjectIds"></param>
    /// <returns></returns>
    @Select("Call VideoClassMain_SP_ExamSet_GetAllBySubjectIdList(#{subjectIds})")
    @Options(statementType = StatementType.CALLABLE)
    List<ExamSet> getAllBySubjectIdList(@Param("subjectIds") String subjectIds);

    /// <summary>
    /// 删除作业
    /// </summary>
    /// <param name="examSetIds"></param>
    /// <returns></returns>
    @Select("Call VideoClassMain_SP_ExamSet_DeleteByIds(#{examSetIds})")
    @Options(statementType = StatementType.CALLABLE)
    int deleteExamSetList(@Param("examSetIds") String examSetIds);

    /// <summary>
    /// 根据MotkExamSetID获取一条ExamSet表数据
    /// </summary>
    /// <param name="examSetId">作业id</param>
    /// <returns>查询到的表实体对象</returns>
    @Select("Call VideoClassMain_SP_ExamSet_ReadByMotkExamSetID(#{motkExamSetID})")
    @Options(statementType = StatementType.CALLABLE)
    ExamSet getByMotkExamSetID(@Param("motkExamSetID") String motkExamSetID);

    /// <summary>
    /// 根据状态获取作业数量
    /// </summary>
    /// <param name="subjectId"></param>
    /// <param name="examSetTypeId"></param>
    /// <returns></returns>
    @Select("Call VideoClassMain_SP_ExamSet_GetCountForTypeId(#{examSetTypeId},#{examSetTypeId})")
    @Options(statementType = StatementType.CALLABLE)
    int getCountForTypeId(@Param("subjectId") int subjectId, @Param("examSetTypeId") int examSetTypeId);
}
