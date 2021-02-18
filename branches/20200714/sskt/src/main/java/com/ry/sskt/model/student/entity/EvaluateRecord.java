package com.ry.sskt.model.student.entity;

import com.alibaba.fastjson.annotation.JSONField;
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
 * 评价表
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
@Data
@Accessors(chain = true)
public class EvaluateRecord {

    /**
     * 课程id
     */
    @JSONField(name="SubjectId")
    private Integer subjectId;

    /**
     * 评价用户id
     */
    @JSONField(name="SourseUserId")
    private Integer sourseUserId;

    /**
     * 被评价用户id
     */
    @JSONField(name="TargetUserId")
    private Integer targetUserId;

    /**
     * 评价类型，1学生对课程的评价，2助教对课程的评价，3家长对课程的评价，4老师对学生的评价
     */
    @JSONField(name="EvaluateTypeId")
    private Integer evaluateTypeId;

    /**
     * 评价星级
     */
    @JSONField(name="EvaluateLevel")
    private Integer evaluateLevel;

    /**
     * 评价内容
     */
    @JSONField(name="EvaluateComment")
    private String evaluateComment;

    /**
     * 状态，0删除，1未删除
     */
    @JSONField(name="StatusFlag")
    private Integer statusFlag;

    /**
     * 创建时间
     */
    @JSONField(name="CreateDateTime")
    private LocalDateTime createDateTime;

    /**
     * 更新时间
     */
    @JSONField(name="UpdateDateTime")
    private LocalDateTime updateDateTime;

}
