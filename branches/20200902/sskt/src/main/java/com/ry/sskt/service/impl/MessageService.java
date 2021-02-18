package com.ry.sskt.service.impl;

import com.ry.sskt.mapper.MessageMapper;
import com.ry.sskt.model.common.entity.TwoTuple;
import com.ry.sskt.model.student.entity.Message;
import com.ry.sskt.model.student.entity.UserMessageInfo;
import com.ry.sskt.model.student.entity.view.TeacherToStudentEvaluateView;
import com.ry.sskt.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 消息 服务实现类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
@Service
public class MessageService implements IMessageService {

    @Autowired
    MessageMapper messageMapper;

    @Override
    public int Save(Message dataModel) throws Exception {
        if (dataModel == null) {
            throw new Exception("保存用户消息入参为空");
        }
        return messageMapper.Save(dataModel);
    }

    @Override
    public Message GetByKey(int messageId) {
        return messageMapper.GetByKey(messageId);
    }

    @Override
    public TwoTuple<Integer, List<UserMessageInfo>> GetMessageList(int userId, int messageTypeId, int pageIndex, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("messageTypeId", messageTypeId);
        map.put("pageSkip", (pageIndex - 1) * pageSize);
        map.put("pageSize", pageSize);
        map.put("totalCount", 0);
        List<UserMessageInfo> lists = messageMapper.GetMessageList(map);
        int total = Integer.parseInt(map.get("totalCount").toString());
        return new TwoTuple<Integer, List<UserMessageInfo>>(total, lists);
    }

    @Override
    public int GetUnReadMessageNumber(int userId) {
        return messageMapper.GetUnReadMessageNumber(userId);
    }
}
