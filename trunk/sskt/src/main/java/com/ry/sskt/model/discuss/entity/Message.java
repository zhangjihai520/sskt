package com.ry.sskt.model.discuss.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.core.utils.sensi.SensitiveFilter;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class Message {
    /// <summary>
    /// 消息Id
    /// </summary>
    @JSONField(name = "TalkMessageId")
    private int talkMessageId;

    /// <summary>
    /// 用户类型
    /// </summary>
    @JSONField(name = "UserType")
    private int userType;

    @JSONField(name = "UserId")
    private int userId;

    /// <summary>
    /// 用户名字
    /// </summary>
    @JSONField(name = "UserTrueName")
    private String userTrueName;

    /// <summary>
    /// 消息类型，1文字 2图片 3附件
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
    @JSONField(name = "CreateDateTime", format = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime createDateTime;

    /// <summary>
    /// 消息时间
    /// </summary>
    @JSONField(name = "ShowDateTime", format = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime showDateTime;

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

    @JSONField(name = "StartTimeZone")
    private Long startTimeZone;

    @JSONField(name = "ToUserId")
    private int toUserId;

    public Message() {
        //this.createDateTime = LocalDateTime.now();
    }

    public void setContent(String content) {
        if (StringUtils.isNotBlank(content)) {
            SensitiveFilter filter = SensitiveFilter.DEFAULT;
            this.content = filter.filter(content, '*');
        }
    }

    public Message(int talkMessageId, int userType, String userTrueName, int talkTypeId, String content, String attachmentUrl, String fileSize, String thumbImageUrl, String userFace) {
        SensitiveFilter filter = SensitiveFilter.DEFAULT;
        this.talkMessageId = talkMessageId;
        this.userType = userType;
        this.userTrueName = userTrueName;
        this.talkTypeId = talkTypeId;
        this.content = filter.filter(content, '*');
        this.attachmentUrl = attachmentUrl;
        //this.createDateTime = LocalDateTime.now();
        this.fileSize = fileSize;
        this.thumbImageUrl = thumbImageUrl;
        this.userFace = userFace;
    }
}
