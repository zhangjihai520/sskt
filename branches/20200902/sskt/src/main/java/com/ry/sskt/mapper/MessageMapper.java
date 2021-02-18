package com.ry.sskt.mapper;

import com.ry.sskt.model.student.entity.Message;
import com.ry.sskt.model.student.entity.UserMessageInfo;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 消息 Mapper 接口
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
public interface MessageMapper {

    /// <summary>
    /// 新增或者更新一条Message表数据
    /// </summary>
    /// <param name="dataModel">实体类</param>
    /// <returns>新增或更新后的主键Id</returns>
    @Select({
            "call VideoClassMain_SP_Message_Save(",
            "#{model.messageId,mode=IN,jdbcType=INTEGER},",
            "#{model.sourseUserId,mode=IN,jdbcType=INTEGER},",
            "#{model.targetUserId,mode=IN,jdbcType=INTEGER},",
            "#{model.content,mode=IN,jdbcType=VARCHAR},",
            "#{model.parentId,mode=IN,jdbcType=INTEGER},",
            "#{model.isReply,mode=IN,jdbcType=INTEGER},",
            "#{model.statusFlag,mode=IN,jdbcType=INTEGER},",
            "#{model.createDateTime,mode=IN,jdbcType=DATE},",
            "#{model.updateDateTime,mode=IN,jdbcType=DATE})"
    })
    @Options(statementType = StatementType.CALLABLE)
    int Save(@Param("model") Message model);

    /// <summary>
    /// 根据主键获取一条Message表数据
    /// </summary>
    /// <param name="messageId">课程id</param>
    /// <returns>查询到的表实体对象</returns>
    @Select("call VideoClassMain_SP_Message_ReadByKey(#{messageId})")
    @Options(statementType = StatementType.CALLABLE)
    Message GetByKey(@Param("messageId") int messageId);

    /// <summary>
    /// 获取用户消息列表
    /// </summary>
    /// <param name="userId"></param>
    /// <param name="messageTypeId"></param>
    /// <param name="pageIndex"></param>
    /// <param name="pageSize"></param>
    /// <returns></returns>
    @Select({
            "call VideoClassMain_SP_Message_GetMessageList(",
            "#{map.userId,mode=IN,jdbcType=INTEGER},",
            "#{map.messageTypeId,mode=IN,jdbcType=INTEGER},",
            "#{map.pageSkip,mode=IN,jdbcType=INTEGER},",
            "#{map.pageSize,mode=IN,jdbcType=INTEGER},",
            "#{map.totalCount,mode=OUT,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    List<UserMessageInfo> GetMessageList(@Param("map") Map map);

    /// <summary>
    /// 获取用户未读消息数量
    /// </summary>
    /// <param name="userId"></param>
    /// <returns></returns>
    @Select("call VideoClassMain_SP_Message_GetUnReadMessageNumber(#{userId})")
    @Options(statementType = StatementType.CALLABLE)
    int GetUnReadMessageNumber(@Param("userId") int userId);
}
