package com.ry.sskt.service;

import com.ry.sskt.model.common.entity.TwoTuple;
import com.ry.sskt.model.student.entity.Message;
import com.ry.sskt.model.student.entity.UserMessageInfo;

import java.util.List;

/**
 * <p>
 * 消息 服务类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
public interface IMessageService{
    /// <summary>
    /// 新增或者更新一条Message表数据
    /// </summary>
    /// <param name="dataModel">实体类</param>
    /// <returns>新增或更新后的主键Id</returns>
    int Save(Message dataModel) throws Exception;

    /// <summary>
    /// 根据主键获取一条Message表数据
    /// </summary>
    /// <param name="messageId">课程id</param>
    /// <returns>查询到的表实体对象</returns>
    Message GetByKey(int messageId);

    /// <summary>
    /// 获取用户消息列表
    /// </summary>
    /// <param name="userId"></param>
    /// <param name="messageTypeId"></param>
    /// <param name="pageIndex"></param>
    /// <param name="pageSize"></param>
    /// <returns></returns>
    TwoTuple<Integer, List<UserMessageInfo>> GetMessageList(int userId, int messageTypeId, int pageIndex, int pageSize);

    /// <summary>
    /// 获取用户未读消息数量
    /// </summary>
    /// <param name="userId"></param>
    /// <returns></returns>
    int GetUnReadMessageNumber(int userId);
}
