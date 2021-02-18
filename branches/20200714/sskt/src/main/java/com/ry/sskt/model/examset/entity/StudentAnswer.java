package com.ry.sskt.model.examset.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Accessors(chain = true)
public class StudentAnswer {
    /// <summary>
    /// 作业id
    /// </summary>
    @JSONField(name="ExamSetId")
    private int examSetId;

    /// <summary>
    /// 学生id
    /// </summary>
    @JSONField(name="UserId")
    private int userId;

    /// <summary>
    /// 课程id
    /// </summary>
    @JSONField(name="SubjectId")
    private int subjectId;

    /// <summary>
    /// 教室id [暂时作废]
    /// </summary>
    @JSONField(name="SubjectRoomId")
    private int subjectRoomId;

    /// <summary>
    /// 成绩
    /// </summary>
    @JSONField(name="Score")
    private BigDecimal score;

    /// <summary>
    /// 学生作答结果，json格式存储
    /// </summary>
    @JSONField(name="QuestionResult")
    private String questionResult;

    /// <summary>
    /// 魔题库测评结果
    /// </summary>
    @JSONField(name="MotkResultInfo")
    private String motkResultInfo;

    /// <summary>
    /// 作答数
    /// </summary>
    @JSONField(name="AnswerNumber")
    private int answerNumber;

    /// <summary>
    /// 状态，0删除，1未删除
    /// </summary>
    @JSONField(name="StatusFlag")
    private int statusFlag;

    /// <summary>
    /// 创建时间
    /// </summary>
    @JSONField(name="CreateDateTime")
    private LocalDateTime createDateTime;

    /// <summary>
    /// 更新时间
    /// </summary>
    @JSONField(name="UpdateDateTime")
    private LocalDateTime updateDateTime;
}
