package com.ry.sskt.controller.action;

import com.ry.sskt.model.common.request.*;
import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.discuss.entity.UserOnlineInfo;
import com.ry.sskt.model.discuss.entity.cache.UserSubjectRoomCouchBaseObject;
import com.ry.sskt.model.discuss.request.MessageApiRequest;
import com.ry.sskt.model.discuss.request.SendFileApiRequest;
import com.ry.sskt.model.discuss.request.SendMessageApiRequest;
import com.ry.sskt.model.discuss.request.TalkMessageIdApiRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 消息 服务类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
public interface IDiscussActionService {
    ResponseBase SendMessage(SendMessageApiRequest request);

    ResponseBase ChangeRoomStatus(SendMessageApiRequest request);

    ResponseBase ChangeUserStatus(SendMessageApiRequest request);

    ResponseBase SendFile(SendFileApiRequest request) throws Exception;

    ResponseBase RemoveMessage(TalkMessageIdApiRequest request) throws Exception;

    ResponseBase Message(MessageApiRequest request);

    List<UserOnlineInfo> GenerateUserOnlineInfos(int subjectRoomId);

    ResponseBase ChangeExamSetStatus(SendMessageApiRequest request);
}
