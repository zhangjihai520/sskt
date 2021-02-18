package com.ry.sskt.mapper;

import com.ry.sskt.model.common.entity.TwoTuple;
import com.ry.sskt.model.common.entity.User;
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
 * 用户表Mapper 接口
 * </p>
 *
 * @author xrq
 * @since 2020-04-20
 */
public interface UserMapper {

    /**
     * 保存用户
     *
     * @return
     */
    @Select({
            "call VideoClassMain_SP_User_Save(",
            "#{model.userId,mode=IN,jdbcType=INTEGER},",
            "#{model.userName,mode=IN,jdbcType=VARCHAR},",
            "#{model.password,mode=IN,jdbcType=VARCHAR},",
            "#{model.userTypeId,mode=IN,jdbcType=INTEGER},",
            "#{model.userTrueName,mode=IN,jdbcType=VARCHAR},",
            "#{model.userSex,mode=IN,jdbcType=INTEGER},",
            "#{model.userFace,mode=IN,jdbcType=VARCHAR},",
            "#{model.emailAddress,mode=IN,jdbcType=VARCHAR},",
            "#{model.schoolName,mode=IN,jdbcType=VARCHAR},",
            "#{model.comment,mode=IN,jdbcType=VARCHAR},",
            "#{model.phone,mode=IN,jdbcType=VARCHAR},",
            "#{model.gradeId,mode=IN,jdbcType=INTEGER},",
            "#{model.sourceTypeId,mode=IN,jdbcType=INTEGER},",
            "#{model.sourceId,mode=IN,jdbcType=VARCHAR},",
            "#{model.statusFlag,mode=IN,jdbcType=INTEGER},",
            "#{model.createDateTime,mode=IN,jdbcType=DATE},",
            "#{model.updateDateTime,mode=IN,jdbcType=DATE},",
            "#{model.userRoles,mode=IN,jdbcType=VARCHAR},",
            "#{model.studentCode,mode=IN,jdbcType=VARCHAR},",
            "#{model.idCard,mode=IN,jdbcType=VARCHAR})"
    })
    @Options(statementType = StatementType.CALLABLE)
    int save(@Param("model") User model);


    /**
     * 根据用户ID获取用户信息
     *
     * @param userId
     * @return
     */
    @Select("Call VideoClassMain_SP_User_ReadByKey(#{userId})")
    @Options(statementType = StatementType.CALLABLE)
    User getUserFromDB(@Param("userId") int userId);


    @Select({
            "call VideoClassMain_SP_User_GetListBySearch(",
            "#{map.statusFlag,mode=IN,jdbcType=INTEGER},",
            "#{map.keyword,mode=IN,jdbcType=VARCHAR},",
            "#{map.pageSkip,mode=IN,jdbcType=INTEGER},",
            "#{map.pageSize,mode=IN,jdbcType=INTEGER},",
            "#{map.totalCount,mode=OUT,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    List<User> getListBySearch(@Param("map") Map map);


    /// <summary>
    /// 根据来源类型和来源id获取用户
    /// </summary>
    /// <param name="sourceTypeId"></param>
    /// <param name="sourceId"></param>
    /// <returns></returns>
    @Select("Call VideoClassMain_SP_User_ReadBySource(#{sourceTypeId},#{sourceId})")
    @Options(statementType = StatementType.CALLABLE)
    User readBySource(@Param("sourceTypeId") int sourceTypeId, @Param("sourceId") String sourceId);

    /// <summary>
    /// 根据学籍号获取用户
    /// </summary>
    /// <param name="sourceTypeId"></param>
    /// <param name="sourceId"></param>
    /// <returns></returns>
    @Select("Call VideoClassMain_SP_User_ReadByStudentCode(#{studentCode})")
    @Options(statementType = StatementType.CALLABLE)
    User readByStudentCode(@Param("studentCode") String studentCode);

    /// <summary>
    /// 清空用户表
    /// </summary>
    /// <returns></returns>
    @Select("Call VideoClassMain_SP_User_Clear()")
    @Options(statementType = StatementType.CALLABLE)
    void clear();


    /// <summary>
    /// 根据学籍号获取学生列表
    /// </summary>
    /// <param name="studentCodes"></param>
    /// <returns></returns>
    @Select("Call VideoClassMain_SP_User_GetListByStudentCodes(#{studentCodes})")
    @Options(statementType = StatementType.CALLABLE)
    List<User> getListByStudentCodes(@Param("studentCodes") String studentCodes);

    /// <summary>
    /// 根据来源类型和来源id批量删除用户
    /// </summary>
    /// <param name="sourceTypeId"></param>
    /// <param name="sourceIds"></param>
    /// <returns></returns>
    @Select("Call VideoClassMain_SP_User_DeleteBySourceIds(#{sourceTypeId},#{studentCodes})")
    @Options(statementType = StatementType.CALLABLE)
    int deleteBySourceIds(@Param("sourceTypeId") int sourceTypeId, @Param("sourceIds") String studentCodes);
}
