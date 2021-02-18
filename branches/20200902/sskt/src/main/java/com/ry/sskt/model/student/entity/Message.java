package com.ry.sskt.model.student.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 消息
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
@Data
@Accessors(chain = true)
public class Message {

    /// <summary>
    /// 消息id
    /// </summary>
    @JSONField(name = "MessageId")
    private Integer messageId;

    /// <summary>
    /// 发起用户id
    /// </summary>
    @JSONField(name = "SourseUserId")
    private Integer sourseUserId;

    /**
     * 接收用户id
     */
    @JSONField(name = "TargetUserId")
    private Integer targetUserId;

    /**
     * 消息内容
     */
    @JSONField(name = "Content")
    private String content;

    /**
     * 回复消息ID
     */
    @JSONField(name = "ParentId")
    private Integer parentId;

    /**
     * 回复状态  1:已回复，0:未回复
     */
    @JSONField(name = "IsReply")
    private Integer isReply;

    /**
     * 消息状态，0未读、1已读
     */
    @JSONField(name = "StatusFlag")
    private Integer statusFlag;

    /**
     * 创建时间
     */
    @JSONField(name = "CreateDateTime")
    private LocalDateTime createDateTime;

    /**
     * 更新时间
     */
    @JSONField(name = "UpdateDateTime")
    private LocalDateTime updateDateTime;


}
