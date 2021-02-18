package com.ry.sskt.model.discuss.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.discuss.entity.Message;
import com.ry.sskt.model.discuss.entity.UserOnlineInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 获取教材版本
 */
@Data
@Accessors(chain = true)
public class SocketTalkMessage {
    /**
     * 0正常聊天信息,1刷新用户列表
     */
    @JSONField(name = "MessageType")
    private int messageType;
    @JSONField(name = "TalkMessage")
    private Message talkMessage;
    @JSONField(name = "UserInfoList")
    private List<UserOnlineInfo> userInfoList;
}
