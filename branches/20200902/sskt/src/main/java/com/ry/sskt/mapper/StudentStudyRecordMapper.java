package com.ry.sskt.mapper;

import com.ry.sskt.model.student.entity.StudentStudyRecord;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

/**
 * <p>
 * 学生轨迹表 Mapper 接口
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
public interface StudentStudyRecordMapper {
    /// <summary>
    /// 新增或者更新一条StudentStudyRecord表数据
    /// </summary>
    /// <param name="dataModel">实体类</param>
    /// <returns>新增或更新后的主键Id</returns>
    @Select({
            "call VideoClassMain_SP_StudentStudyRecord_Save(",
            "#{model.studentStudyRecordId,mode=IN,jdbcType=INTEGER},",
            "#{model.userId,mode=IN,jdbcType=INTEGER},",
            "#{model.examSetId,mode=IN,jdbcType=INTEGER},",
            "#{model.subjectId,mode=IN,jdbcType=INTEGER},",
            "#{model.studyRecordTypeId,mode=IN,jdbcType=INTEGER},",
            "#{model.content,mode=IN,jdbcType=VARCHAR},",
            "#{model.createDateTime,mode=IN,jdbcType=DATE},",
            "#{model.updateDateTime,mode=IN,jdbcType=DATE})"
    })
    @Options(statementType = StatementType.CALLABLE)
    int Save(@Param("model") StudentStudyRecord model);

    /// <summary>
    /// 根据主键获取一条StudentStudyRecord表数据
    /// </summary>
    /// <param name="studentStudyRecordId">轨迹id</param>
    /// <returns>查询到的表实体对象</returns>
    @Select("call VideoClassMain_SP_StudentStudyRecord_ReadByKey(#{studentStudyRecordId})")
    @Options(statementType = StatementType.CALLABLE)
    StudentStudyRecord GetByKey(@Param("studentStudyRecordId") int studentStudyRecordId);

    /// <summary>
    /// 获取历史记录
    /// </summary>
    /// <param name="userId"></param>
    /// <returns></returns>
    @Select("call VideoClassMain_SP_StudentStudyRecord_ReadByUserId(#{userId},#{isOnline})")
    @Options(statementType = StatementType.CALLABLE)
    List<StudentStudyRecord> GetListByUserId(@Param("userId") int userId, @Param("isOnline") int isOnline);
}
