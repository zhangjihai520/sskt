package com.ry.sskt.mapper;

import com.ry.sskt.model.discuss.entity.TalkMessage;
import com.ry.sskt.model.dledc.entity.SensiWord;
import com.ry.sskt.model.subject.entity.view.SubjectListByFilterView;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程表 Mapper 接口
 * </p>
 *
 * @author xrq
 * @since 2020-04-20
 */
public interface TalkMessageMapper {

    /**
     * 获取所有公益课课程
     *
     * @return
     */
    @Select({
            "call VideoClassMain_SP_Message_Insert(",
            "#{message.talkMessageId,mode=IN,jdbcType=INTEGER},",
            "#{message.subjectId,mode=IN,jdbcType=INTEGER},",
            "#{message.subjectRoomId,mode=IN,jdbcType=INTEGER},",
            "#{message.userId,mode=IN,jdbcType=INTEGER},",
            "#{message.userTrueName,mode=IN,jdbcType=VARCHAR},",
            "#{message.userType,mode=IN,jdbcType=INTEGER},",
            "#{message.content,mode=IN,jdbcType=VARCHAR},",
            "#{message.talkTypeId,mode=IN,jdbcType=INTEGER},",
            "#{message.fileName,mode=IN,jdbcType=VARCHAR},",
            "#{message.fileSize,mode=IN,jdbcType=VARCHAR},",
            "#{message.thumbImageUrl,mode=IN,jdbcType=VARCHAR},",
            "#{message.startTimeZone,mode=IN,jdbcType=INTEGER},",
            "#{message.toUserId,mode=IN,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    void insertTalkMessage(@Param("message") TalkMessage message);

    /**
     * 加载敏感资源库
     */
    @Select("SELECT `wordId`,`courseTypeId`,`content` FROM `sensiword` where `statusFlag` = 1")
    List<SensiWord> loadSensiWord();

    /**
     * 逻辑删除消息
     *
     * @param talkmessageId
     */
    @Update("UPDATE `talkmessage` SET `StatusFlag` = 0 WHERE `TalkMessageID` =  #{talkmessageId}")
    void removeTalkMessage(@Param("talkmessageId") int talkmessageId);
}
