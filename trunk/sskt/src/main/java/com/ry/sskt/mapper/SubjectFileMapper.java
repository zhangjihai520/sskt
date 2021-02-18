package com.ry.sskt.mapper;

import com.ry.sskt.model.subject.entity.SubjectFile;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

/**
 * <p>
 * 课程附件 Mapper 接口
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
public interface SubjectFileMapper {
    /// <summary>
    /// 新增或者更新一条SubjectFile表数据
    /// </summary>
    /// <param name="dataModel">实体类</param>
    /// <returns>新增或更新后的主键Id</returns>
    @Select({
            "call VideoClassMain_SP_SubjectFile_Save(",
            "#{model.subjectFileId,mode=IN,jdbcType=INTEGER},",
            "#{model.subjectId,mode=IN,jdbcType=INTEGER},",
            "#{model.subjectFileName,mode=IN,jdbcType=VARCHAR},",
            "#{model.fileType,mode=IN,jdbcType=VARCHAR},",
            "#{model.filePath,mode=IN,jdbcType=VARCHAR},",
            "#{model.statusFlag,mode=IN,jdbcType=INTEGER},",
            "#{model.createDateTime,mode=IN,jdbcType=DATE},",
            "#{model.updateDateTime,mode=IN,jdbcType=DATE})"
    })
    int Save(@Param("model") SubjectFile model);

    /// <summary>
    /// 根据主键获取一条SubjectFile表数据
    /// </summary>
    /// <param name="subjectFileId">课程附件id</param>
    /// <returns>查询到的表实体对象</returns>
    @Select("call VideoClassMain_SP_SubjectFile_ReadByKey(#{subjectFileId})")
    @Options(statementType = StatementType.CALLABLE)
    SubjectFile GetByKey(@Param("subjectFileId") int subjectFileId);

    /// <summary>
    /// 根据SubjectId数据
    /// </summary>
    /// <param name="subjectFileId">课程附件id</param>
    /// <returns>查询到的表实体对象</returns>
    @Select("call VideoClassMain_SP_SubjectFile_ReadBySubjectId(#{subjectId})")
    @Options(statementType = StatementType.CALLABLE)
    List<SubjectFile> GetBySubjectId(@Param("subjectId") int subjectId);

    /// <summary>
    /// 获取SubjectFile列表
    /// </summary>
    /// <param name="subjectIds"></param>
    /// <returns></returns>
    @Select("call VideoClassMain_SP_SubjectFile_ReadBySubjectIdList(#{subjectIds})")
    @Options(statementType = StatementType.CALLABLE)
    List<SubjectFile> GetBySubjectIds(@Param("subjectIds") String subjectIds);

    /// <summary>
    /// 批量删除资料
    /// </summary>
    /// <param name="subjectFileIds"></param>
    /// <returns></returns>
    @Select("call VideoClassMain_SP_SubjectFile_DeleteByIds(#{subjectFileIds})")
    @Options(statementType = StatementType.CALLABLE)
    int DeleteSubjectFile(@Param("subjectFileIds") String subjectFileIds);
}
