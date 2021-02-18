package com.ry.sskt.model.discuss.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.entity.BookVersionInfo;
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
public class TalkMessageResponse {
    @JSONField(name = "TalkMessageList")
    private List<Message> talkMessageList;
    @JSONField(name = "UserInfoList")
    private List<UserOnlineInfo> userInfoList;

    public TalkMessageResponse() {
    }

    public TalkMessageResponse(List<Message> talkMessageList, List<UserOnlineInfo> userInfoList) {
        this.talkMessageList = talkMessageList;
        this.userInfoList = userInfoList;
    }
}
