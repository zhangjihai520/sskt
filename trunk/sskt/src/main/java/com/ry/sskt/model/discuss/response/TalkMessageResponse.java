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

    @JSONField(name = "RoomUserType")
    private int roomUserType;

    @JSONField(name = "RoomStatus")
    private int roomStatus;
    /// <summary>
    /// 随堂作业开放状态  0关闭  1开放
    /// </summary>
    @JSONField(name="ExamSetStatus")
    public int examSetStatus;

    @JSONField(name = "UserInfoList")
    private List<UserOnlineInfo> userInfoList;

    public TalkMessageResponse() {
    }

    public TalkMessageResponse(List<Message> talkMessageList, List<UserOnlineInfo> userInfoList) {
        this.talkMessageList = talkMessageList;
        this.userInfoList = userInfoList;
    }
}
