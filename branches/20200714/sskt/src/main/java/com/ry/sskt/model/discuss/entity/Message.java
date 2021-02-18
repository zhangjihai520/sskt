package com.ry.sskt.model.discuss.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Message {
    /// <summary>
    /// 消息Id
    /// </summary>
    @JSONField(name = "TalkMessageId")
    private String talkMessageId;

    /// <summary>
    /// 用户类型
    /// </summary>
    @JSONField(name = "UserType")
    private int userType;


    /// <summary>
    /// 用户名字
    /// </summary>
    @JSONField(name = "UserTrueName")
    private String userTrueName;

    /// <summary>
    /// 消息类型
    /// </summary>
    @JSONField(name = "TalkTypeId")
    private int talkTypeId;


    /// <summary>
    /// 消息内容
    /// </summary>
    @JSONField(name = "Content")
    private String content;

    /// <summary>
    /// 附件地址
    /// </summary>
    @JSONField(name = "AttachmentUrl")
    private String attachmentUrl;

    /// <summary>
    /// 创建时间
    /// </summary>
    @JSONField(name = "CreateDateTime")
    private String createDateTime;

    /// <summary>
    /// 附件大小
    /// </summary>
    @JSONField(name = "FileSize")
    private String fileSize;

    /// <summary>
    /// 缩略图地址
    /// </summary>
    @JSONField(name = "ThumbImageUrl")
    private String thumbImageUrl;

    /// <summary>
    /// 用户头像
    /// </summary>
    @JSONField(name = "UserFace")
    private String userFace;

    public Message() {
    }

    public Message(String talkMessageId, int userType, String userTrueName, int talkTypeId, String content, String attachmentUrl, String createDateTime, String fileSize, String thumbImageUrl, String userFace) {
        this.talkMessageId = talkMessageId;
        this.userType = userType;
        this.userTrueName = userTrueName;
        this.talkTypeId = talkTypeId;
        this.content = content;
        this.attachmentUrl = attachmentUrl;
        this.createDateTime = createDateTime;
        this.fileSize = fileSize;
        this.thumbImageUrl = thumbImageUrl;
        this.userFace = userFace;
    }
}
