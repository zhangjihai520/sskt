package com.ry.sskt.model.discuss.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.constant.TalkTypeEnum;
import io.protostuff.Tag;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 聊天消息
 */
@Data
@Accessors(chain = true)
public class TalkMessage {
    /// <summary>
    /// 讨论Id
    /// </summary>

    @JSONField(name = "TalkMessageId")
    private int talkMessageId;

    /// <summary>
    /// 课程Id
    /// </summary>

    @JSONField(name = "SubjectId")
    private int subjectId;

    /// <summary>
    /// 教室Id
    /// </summary>

    @JSONField(name = "SubjectRoomId")
    private int subjectRoomId;

    /// <summary>
    /// 发言人Id
    /// </summary>

    @JSONField(name = "UserId")
    private int userId;

    /// <summary>
    /// 发言人名字
    /// </summary>

    @JSONField(name = "UserTrueName")
    private String userTrueName;


    /// <summary>
    /// 发言人用户类型
    /// </summary>

    @JSONField(name = "UserType")
    private int userType;

    /// <summary>
    /// 发言内容
    /// </summary>

    @JSONField(name = "Content")
    private String content;

    /// <summary>
    /// 消息类型，1文字 2图片 3附件
    /// </summary>

    @JSONField(name = "TalkTypeId")
    private int talkTypeId;

    /// <summary>
    /// 附件名称
    /// </summary>

    @JSONField(name = "FileName")
    private String fileName;

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
    /// 消息时间
    /// </summary>

    @JSONField(name = "CreateDateTime")
    private Date createDateTime;

    /// <summary>
    /// 是否有效
    /// </summary>

    @JSONField(name = "StatusFlag")
    private int statusFlag;

    /// <summary>
    ///用户头像名字
    /// </summary>

    @JSONField(name = "UserFace")
    private String userFace;

    /// <summary>
    /// 更新时间
    /// </summary>

    @JSONField(name = "UpdateDateTime")
    private Date updateDateTime;

    public TalkMessage() {
    }

    /// <summary>
    /// 获取纯文字信息
    /// </summary>
    /// <param name="subjectId"></param>
    /// <param name="subjectRoomId"></param>
    /// <param name="content"></param>
    /// <param name="userId"></param>
    /// <param name="userTrueName"></param>
    /// <param name="userTypeId"></param>
    /// <returns></returns>
    public TalkMessage(Integer subjectId, int subjectRoomId, int userId, String userTrueName, int userTypeId, String content, String userFace) {

        this.subjectId = subjectId;
        this.subjectRoomId = subjectRoomId;
        this.content = content;
        this.createDateTime = new Date();
        this.talkTypeId = TalkTypeEnum.Text.getCode();
        this.userId = userId;
        this.userTrueName = userTrueName;
        this.userType = userTypeId;
        this.userFace = userFace;
    }

    /// <summary>
    /// 获取图片消息实体
    /// </summary>
    /// <param name="subjectId"></param>
    /// <param name="subjectRoomId"></param>
    /// <param name="userId"></param>
    /// <param name="userTrueName"></param>
    /// <param name="userTypeId"></param>
    /// <param name="url"></param>
    /// <param name="thumbUrl"></param>
    /// <returns></returns>
    public TalkMessage(Integer subjectId, int subjectRoomId, int userId, String userTrueName,
                       int userTypeId, String url, String thumbUrl, String userFace) {
        this.subjectId = subjectId;
        this.subjectRoomId = subjectRoomId;
        this.userId = userId;
        this.userTrueName = userTrueName;
        this.userType = userTypeId;
        this.talkTypeId = TalkTypeEnum.Image.getCode();
        this.content = url;
        this.thumbImageUrl = thumbUrl;
        this.createDateTime = new Date();
        this.userFace = userFace;
    }

    /// <summary>
    /// 获取文件消息实体
    /// </summary>
    /// <param name="subjectId"></param>
    /// <param name="subjectRoomId"></param>
    /// <param name="userId"></param>
    /// <param name="userTrueName"></param>
    /// <param name="userTypeId"></param>
    /// <param name="url"></param>
    /// <param name="fileName"></param>
    /// <param name="fileSize"></param>
    /// <returns></returns>
    public TalkMessage(Integer subjectId, int subjectRoomId, int userId, String userTrueName,
                       int userTypeId, String url, String fileName, String fileSize, String userFace) {
        this.subjectId = subjectId;
        this.subjectRoomId = subjectRoomId;
        this.userId = userId;
        this.userTrueName = userTrueName;
        this.userType = userTypeId;
        this.talkTypeId = TalkTypeEnum.Attachment.getCode();
        this.content = url;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.createDateTime = new Date();
        this.userFace = userFace;
    }
}
