package com.ry.sskt.controller.action.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.ry.sskt.MessageProducer;
import com.ry.sskt.controller.action.IDiscussActionService;
import com.ry.sskt.core.config.CommonConfig;
import com.ry.sskt.core.utils.*;
import com.ry.sskt.model.common.constant.*;
import com.ry.sskt.model.common.entity.TwoTuple;
import com.ry.sskt.model.common.entity.User;
import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.discuss.entity.Message;
import com.ry.sskt.model.discuss.entity.TalkMessage;
import com.ry.sskt.model.discuss.entity.UserOnlineInfo;
import com.ry.sskt.model.discuss.entity.cache.UserSubjectRoomCouchBaseObject;
import com.ry.sskt.model.discuss.request.MessageApiRequest;
import com.ry.sskt.model.discuss.request.SendFileApiRequest;
import com.ry.sskt.model.discuss.request.SendMessageApiRequest;
import com.ry.sskt.model.discuss.request.TalkMessageIdApiRequest;
import com.ry.sskt.model.discuss.response.SocketTalkMessage;
import com.ry.sskt.model.discuss.response.TalkMessageResponse;
import com.ry.sskt.service.*;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 消息 服务实现类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
@Slf4j
@Service
public class DiscussActionService implements IDiscussActionService {
    private final long ImageMaxLength = 3 * 1024 * 1024;

    private List<String> _allowImageExtension = new LinkedList<String>() {{
        add(".jpg");
        add(".png");
        add(".jpeg");
    }};

    private List<String> _allowAttachmentExtension = new LinkedList<String>() {{
        add(".ppt");
        add(".pptx");
        add(".pdf");
        add(".doc");
        add(".docx");
        add(".xlsx");
        add(".xls");
    }};

    private final long AttachmentMaxLength = 20 * 1024 * 1024;

    @Autowired
    ITalkMessageService talkMessageService;
    @Autowired
    ISubjectService subjectService;
    @Autowired
    ISubjectRoomService subjectRoomService;
    @Autowired
    ISubjectRoomStudentService subjectRoomStudentService;
    @Autowired
    IUserService userService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase SendMessage(SendMessageApiRequest request) {
        int userTypeId = request.getUserTypeId();
        String userFace = request.getUserFace();
        int subjectId = UrlUtil.decrypt(request.getSubjectId(), Integer.class);
        UserSubjectRoomCouchBaseObject userSubjectRoom = talkMessageService.getUserSubjectRoomInfo(request.getUserId(), userTypeId, subjectId);
        if (userSubjectRoom.getSubjectRoomId() <= 0) {
            return ResponseBase.GetValidateErrorResponse(Result.CANNOT_SPEECH);
        }
        StringBuilder userTrueName = new StringBuilder(request.getCurrentUserTrueName());
        if (userSubjectRoom.getRoomUserType() == 1) {
            userTrueName.append("（助教）");
        } else if (userSubjectRoom.getRoomUserType() == 2) {
            userTrueName.append("（教师）");
        } else if (userSubjectRoom.getRoomUserType() == 3) {
            userTrueName.append("（主讲）");
        }
        var subject = subjectService.getByKey(subjectId, true);
        TalkMessage message = new TalkMessage(subjectId, userSubjectRoom.getSubjectRoomId(),
                request.getUserId(), userTrueName.toString(), userTypeId, request.getMessageContent(), userFace, request.getToUserId());
        var nowTimeZone = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        if (subject.getRealBeginTimeZone() == null) {
            subject.setRealBeginTimeZone(nowTimeZone);
        }
        message.setStartTimeZone(nowTimeZone - subject.getRealBeginTimeZone());
        message.setTalkMessageId(RedisUtil.incr(CommonConst.IDINCR_TALKMESSAGE).intValue());
        var onLineUsers = GenerateUserOnlineInfos(userSubjectRoom.getSubjectRoomId());
        var socketTalkMessage = new SocketTalkMessage().setUserInfoList(onLineUsers).setTalkMessage(buildMessage(message)).setMessageType(0);
        RedisUtil.publish(userSubjectRoom.getSubjectRoomId() + "", JSONObject.toJSONString(socketTalkMessage));
        new MessageProducer(userSubjectRoom.getSubjectRoomId()).putMessage(JSON.toJSONString(message));
        //talkMessageService.sendMessage(message);
        return ResponseBase.GetSuccessResponse();
    }

    @Override
    public ResponseBase RemoveMessage(TalkMessageIdApiRequest request) throws Exception {
        TalkMessage message = new TalkMessage().setStatusFlag(0).setSubjectRoomId(request.getSubjectRoomId()).setTalkMessageId(request.getTalkMessageId()).setToUserId(request.getToUserId());
        var onLineUsers = GenerateUserOnlineInfos(request.getSubjectRoomId());
        var socketTalkMessage = new SocketTalkMessage().setUserInfoList(onLineUsers).setTalkMessage(buildMessage(message)).setMessageType(2);
        RedisUtil.publish(request.getSubjectRoomId() + "", JSONObject.toJSONString(socketTalkMessage));
        new MessageProducer(request.getSubjectRoomId()).putMessage(JSON.toJSONString(message));
        return ResponseBase.GetSuccessResponse();
    }

    @Override
    public ResponseBase ChangeRoomStatus(SendMessageApiRequest request) {
        int userTypeId = request.getUserTypeId();
        int subjectId = UrlUtil.decrypt(request.getSubjectId(), Integer.class);
        UserSubjectRoomCouchBaseObject userSubjectRoom = talkMessageService.getUserSubjectRoomInfo(request.getUserId(), userTypeId, subjectId);
        if (userSubjectRoom.getSubjectRoomId() <= 0) {
            return ResponseBase.GetValidateErrorResponse(Result.CANNOT_SPEECH);
        }
        talkMessageService.setRoomStatus(userSubjectRoom.getSubjectRoomId(), request.getUserId(), request.getStatus());
        var onLineUsers = GenerateUserOnlineInfos(userSubjectRoom.getSubjectRoomId());
        var socketTalkMessage = new SocketTalkMessage().setUserInfoList(onLineUsers).setMessageType(1);
        RedisUtil.publish(userSubjectRoom.getSubjectRoomId() + "", JSONObject.toJSONString(socketTalkMessage));
        return ResponseBase.GetSuccessResponse();
    }

    @Override
    public ResponseBase ChangeExamSetStatus(SendMessageApiRequest request) {
        int userTypeId = request.getUserTypeId();
        int subjectId = UrlUtil.decrypt(request.getSubjectId(), Integer.class);
        UserSubjectRoomCouchBaseObject userSubjectRoom = talkMessageService.getUserSubjectRoomInfo(request.getUserId(), userTypeId, subjectId);
        if (userSubjectRoom.getSubjectRoomId() <= 0) {
            return ResponseBase.GetValidateErrorResponse(Result.CANNOT_SPEECH);
        }
        talkMessageService.setExamSetStatus(userSubjectRoom.getSubjectRoomId(), request.getStatus());
        RedisUtil.publish(userSubjectRoom.getSubjectRoomId() + "", "OpenExam");
        return ResponseBase.GetSuccessResponse();
    }

    @Override
    public ResponseBase ChangeUserStatus(SendMessageApiRequest request) {
        int userTypeId = request.getUserTypeId();
        int subjectId = UrlUtil.decrypt(request.getSubjectId(), Integer.class);
        UserSubjectRoomCouchBaseObject userSubjectRoom = talkMessageService.getUserSubjectRoomInfo(request.getUserId(), userTypeId, subjectId);
        if (userSubjectRoom.getSubjectRoomId() <= 0) {
            return ResponseBase.GetValidateErrorResponse(Result.CANNOT_SPEECH);
        }
        talkMessageService.setUserStatus(request.getToUserId(), userSubjectRoom.getSubjectRoomId(), request.getStatus());
        var onLineUsers = GenerateUserOnlineInfos(userSubjectRoom.getSubjectRoomId());
        var socketTalkMessage = new SocketTalkMessage().setUserInfoList(onLineUsers).setMessageType(1);
        RedisUtil.publish(userSubjectRoom.getSubjectRoomId() + "", JSONObject.toJSONString(socketTalkMessage));
        return ResponseBase.GetSuccessResponse();
    }

    @Override
    public ResponseBase SendFile(SendFileApiRequest request) throws Exception {
        int subId = UrlUtil.decrypt(request.getSubjectId(), Integer.class);
        int userType = request.getUserTypeId();
        UserSubjectRoomCouchBaseObject UserSubjectRoom = talkMessageService.getUserSubjectRoomInfo(request.getUserId(), userType, subId);
        if (UserSubjectRoom.getSubjectRoomId() <= 0) {
            return ResponseBase.GetValidateErrorResponse(Result.CANNOT_SPEECH);
        }
        String errorMessag = FileValidate(request);
        if (StringUtils.isNotBlank(errorMessag)) {
            return ResponseBase.GetValidateErrorResponse(errorMessag);
        }
        errorMessag = SendMessageForCtyun(request, subId, UserSubjectRoom);
        if (StringUtils.isNotBlank(errorMessag)) {
            return ResponseBase.GetValidateErrorResponse(errorMessag);
        }
        return ResponseBase.GetSuccessResponse();
    }

    @Override
    public ResponseBase Message(MessageApiRequest request) {
        int subjectId = UrlUtil.decrypt(request.getSubjectId(), Integer.class);
        int userType = request.getUserTypeId();
        UserSubjectRoomCouchBaseObject UserSubjectRoom = talkMessageService.getUserSubjectRoomInfo(request.getUserId(), userType, subjectId);
        if (UserSubjectRoom == null || UserSubjectRoom.getSubjectRoomId() <= 0) {
            var subjectRoomId = 0;
            if (userType == UserTypeEnum.Teacher.getCode()) {
                subjectRoomId = subjectService.teacherRegistSubject(subjectId, request.getUserId(), false);
                if (subjectRoomId <= 0) {
                    return ResponseBase.GetValidateErrorResponse("");
                }
                subjectRoomService.clearSubjectRoomUserListCache(subjectRoomId);
            } else if (userType == UserTypeEnum.Student.getCode()) {
                if (subjectService.getByKey(subjectId, true).getSubjectGenreId() == SubjectGenreEnum.PublicBenefit.getIndex()) {
                    subjectRoomId = subjectService.studentRegistSubject(subjectId, request.getUserId(), false);
                    if (subjectRoomId <= 0) {
                        return ResponseBase.GetValidateErrorResponse(ErrorMessageStudent);
                    }
                    subjectRoomService.clearSubjectRoomUserListCache(subjectRoomId);
                } else {
                    return ResponseBase.GetValidateErrorResponse(ErrorMessageUnregistered);
                }
            } else {
                return ResponseBase.GetValidateErrorResponse(ErrorMessageUnregistered);
            }
            UserSubjectRoom.setSubjectRoomId(subjectRoomId);
        }
        var messageCache = talkMessageService.getMessageCache(UserSubjectRoom.getSubjectRoomId());
        List<TalkMessage> allMessage = messageCache.getMessageList();
        if (CollectionUtils.isNotEmpty(allMessage)) {
            if (request.getToUserId() > 0) {
                allMessage = allMessage.stream().filter(x -> (x.getToUserId() == request.getToUserId() && x.getUserId() == request.getUserId()) || (x.getToUserId() == request.getUserId() && x.getUserId() == request.getToUserId())).collect(Collectors.toList());
            } else if (request.getToUserId() == 0) {
                allMessage = allMessage.stream().filter(x -> x.getToUserId() == 0).collect(Collectors.toList());
            } else {
                allMessage = allMessage.stream().filter(x -> x.getToUserId() == 0 || x.getToUserId() == request.getUserId() || x.getUserId() == request.getUserId()).collect(Collectors.toList());
            }
            allMessage.sort(Comparator.comparing(TalkMessage::getTalkMessageId));
        }
        List<TalkMessage> messageList = null;
        if (request.getTypeId() == GetMessageTypeEnum.CurrentMessage.getCode()) {
            messageList = allMessage.stream().skip(Math.max(0, allMessage.size() - request.getPageSize())).limit(request.getPageSize()).collect(Collectors.toList());
        }
        if (request.getTypeId() == GetMessageTypeEnum.NewMessage.getCode()) {
            messageList = allMessage.stream().filter(m -> m.getTalkMessageId() > request.getBoundaryId()).skip(0).limit(request.getPageSize()).collect(Collectors.toList());
        }
        if (request.getTypeId() == GetMessageTypeEnum.OldMessage.getCode()) {
            int oldMessageCount = (int) allMessage.stream().filter(n -> n.getTalkMessageId() < request.getBoundaryId()).count();
            messageList = allMessage.stream().skip(Math.max(0, oldMessageCount - request.getPageSize())).limit(Math.min(request.getPageSize(), oldMessageCount)).collect(Collectors.toList());
        }
        TalkMessageResponse messageResponse = new TalkMessageResponse();
        messageResponse.setRoomStatus(messageCache.getRoomStatus());
        messageResponse.setExamSetStatus(messageCache.getExamSetStatus());
        List<Message> responseMessageList = ConvertToResponseMessage(messageList);
        messageResponse.setRoomUserType(UserSubjectRoom.getRoomUserType());
        messageResponse.setTalkMessageList(responseMessageList);
        messageResponse.setUserInfoList(GenerateUserOnlineInfos(request, UserSubjectRoom.getSubjectRoomId()));
        return ResponseBase.GetSuccessResponse(messageResponse);
    }

    /// <summary>
    /// 获取及更新在线信息
    /// </summary>
    /// <param name="request"></param>
    /// <param name="subjectRoomId"></param>
    /// <returns></returns>
    @Override
    public List<UserOnlineInfo> GenerateUserOnlineInfos(int subjectRoomId) {
        HashMap<Integer, Boolean> allOnlineUser = talkMessageService.getUserOnlineStatus(subjectRoomId);
        HashMap<Integer, Integer> allUserStatus = talkMessageService.getUserStatus(subjectRoomId);
        List<User> allUser = subjectRoomStudentService.GetAllBySubjectRoomId(subjectRoomId);
        List<UserOnlineInfo> userInfoList = new LinkedList<>();
        allUser.forEach(m -> {
            UserOnlineInfo userOnlineInfo = new UserOnlineInfo()
                    .setUserId(m.getUserId())
                    .setUserFace(m.getUserFace())
                    .setUserTypeId(m.getUserTypeId())
                    .setUserName(m.getUserTrueName())
                    .setRoomUserType(m.getRoomUserType())
                    .setOrderIndex(m.getOrderIndex());
            for (Map.Entry<Integer, Boolean> n : allOnlineUser.entrySet()) {
                if (m.getUserId() == n.getKey()) {
                    userOnlineInfo.setOnline(n.getValue());
                }
            }
            for (Map.Entry<Integer, Integer> n : allUserStatus.entrySet()) {
                if (m.getUserId() == n.getKey().intValue()) {
                    userOnlineInfo.setStatus(n.getValue());
                }
            }
            userInfoList.add(userOnlineInfo);
        });
        userInfoList.sort(Comparator.comparing(UserOnlineInfo::isOnline).reversed().thenComparing(UserOnlineInfo::getOrderIndex));
        return userInfoList;
    }

    /// <summary>
    /// 验证文件信息
    /// </summary>
    /// <param name="request"></param>
    /// <returns></returns>
    private String FileValidate(SendFileApiRequest request) throws IOException {
        if (request.getFile().getSize() == 0) {
            return "没有读取到文件";
        }
        MultipartFile file = request.getFile();
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")).toLowerCase();
        if (request.getFileType() == TalkTypeEnum.Image.getCode()) {
            if (!_allowImageExtension.contains(suffix)) {
                return "文件格式不支持";
            }
            if (file.getSize() > ImageMaxLength) {
                return "文件超出大小";
            }

            try {
                Image image = ImageIO.read(file.getInputStream());
            } catch (Exception e) {
                log.error(String.format("读取图片失败，参数：%s", JSON.toJSONString(request)), e);
                throw e;
            }
        }
        if (request.getFileType() == TalkTypeEnum.Attachment.getCode()) {
            if (!_allowAttachmentExtension.contains(suffix)) {
                return "文件格式不支持";
            }
            if (file.getSize() > AttachmentMaxLength) {
                return "文件超出大小";
            }
        }
        if (request.getFileType() != TalkTypeEnum.Image.getCode() && request.getFileType() != TalkTypeEnum.Attachment.getCode())
            return "传入参数错误";
        return StringUtils.EMPTY;
    }


    /// <summary>
    /// 发送消息
    /// </summary>
    /// <param name="request"></param>
    /// <param name="subjectId"></param>
    /// <param name="subjectRoomId"></param>
    private String SendMessageForCtyun(SendFileApiRequest request, int subjectId, UserSubjectRoomCouchBaseObject userSubjectRoom) throws IOException {
        MultipartFile file = request.getFile();
        String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")).toLowerCase();
        MimeMappings mimeMappings = new MimeMappings();
        String mimeMapping = mimeMappings.get(fileExtension.substring(1));
        String ossPath = MessageFormat.format("{0}/{1}/{2}/{3}{4}", CommonConst.OOS_File_Root_Dir_TalkUpdateFile, DateUtil.dateToString(new Date(), "yyyyMM/dd"), userSubjectRoom.getSubjectRoomId() + "", UUID.randomUUID(), fileExtension);
        TwoTuple<Boolean, String> resultTuple = UpdateFileToCtyun(file, ossPath, mimeMapping);
        if (!resultTuple.first) {
            return resultTuple.second;
        }
        String fullFileName = resultTuple.second;
        TalkMessage message = new TalkMessage();
        if (request.getFileType() == TalkTypeEnum.Image.getCode()) {
            message = GetImageTalkMessageByCtyun(request, fullFileName, subjectId, userSubjectRoom);
        }
        if (request.getFileType() == TalkTypeEnum.Attachment.getCode()) {
            message = GetAttachmentTalkMessageByCtyun(request, fullFileName, subjectId, userSubjectRoom);
        }
        StringBuilder userTrueName = new StringBuilder(request.getCurrentUserTrueName());
        if (userSubjectRoom.getRoomUserType() == 1) {
            userTrueName.append("（助教）");
        } else if (userSubjectRoom.getRoomUserType() == 2) {
            userTrueName.append("（教师）");
        } else if (userSubjectRoom.getRoomUserType() == 3) {
            userTrueName.append("（主讲）");
        }
        message.setUserTrueName(userTrueName.toString()).setUserId(request.getUserId());
        message.setToUserId(request.getToUserId());
        message.setTalkMessageId(RedisUtil.incr(CommonConst.IDINCR_TALKMESSAGE).intValue());
        var onLineUsers = GenerateUserOnlineInfos(userSubjectRoom.getSubjectRoomId());
        var socketTalkMessage = new SocketTalkMessage().setUserInfoList(onLineUsers).setTalkMessage(buildMessage(message));
        RedisUtil.publish(userSubjectRoom.getSubjectRoomId() + "", JSONObject.toJSONString(socketTalkMessage));
        //talkMessageService.sendMessage(message);
        new MessageProducer(userSubjectRoom.getSubjectRoomId()).putMessage(JSON.toJSONString(message));
        return StringUtils.EMPTY;
    }

    private Message buildMessage(TalkMessage message) {

        Message resultMessage = new Message();
        resultMessage.setTalkMessageId(message.getTalkMessageId())
                .setTalkTypeId(message.getTalkTypeId())
                .setUserId(message.getUserId())
                .setUserType(message.getUserType())
                .setUserFace(message.getUserFace())
                .setStartTimeZone(message.getStartTimeZone())
                .setCreateDateTime(message.getCreateDateTime())
                .setShowDateTime(message.getShowDateTime())
                .setToUserId(message.getToUserId())
                .setContent(message.getContent());
        resultMessage.setUserTrueName(message.getUserTrueName());
        if (resultMessage.getTalkTypeId() == TalkTypeEnum.Image.getCode()) {
            resultMessage.setThumbImageUrl(message.getThumbImageUrl()).setAttachmentUrl(message.getContent()).setContent(StringUtils.EMPTY);
        } else if (message.getTalkTypeId() == TalkTypeEnum.Attachment.getCode()) {
            resultMessage.setAttachmentUrl(message.getContent()).setFileSize(message.getFileSize()).setContent(message.getFileName());
        }
        return resultMessage;
    }

    /// <summary>
    /// 获取图片消息实体
    /// </summary>
    /// <param name="file"></param>
    /// <param name="request"></param>
    /// <param name="fullFileName"></param>
    /// <param name="subjectId"></param>
    /// <param name="subjectRoomId"></param>
    /// <param name="userName"></param>
    /// <param name="userTypeId"></param>
    /// <returns></returns>
    private TalkMessage GetImageTalkMessageByCtyun(SendFileApiRequest request, String fullFileName, int subjectId, UserSubjectRoomCouchBaseObject userSubjectRoom) {
        StringBuilder userTrueName = new StringBuilder(request.getCurrentUserTrueName());
        if (userSubjectRoom.getRoomUserType() == 1) {
            userTrueName.append("（助教）");
        } else if (userSubjectRoom.getRoomUserType() == 2) {
            userTrueName.append("（教师）");
        } else if (userSubjectRoom.getRoomUserType() == 3) {
            userTrueName.append("（主讲）");
        }
        String thumbRelateFilePath = String.format("%s@oosImage|90w_120h", fullFileName);
        TalkMessage message = new TalkMessage(subjectId, userSubjectRoom.getSubjectRoomId(),
                request.getUserId(), userTrueName.toString(), request.getUserTypeId(), fullFileName, thumbRelateFilePath, request.getUserFace(), request.getToUserId());
        return message;
    }


    /// <summary>
    /// 获取附件消息实体
    /// </summary>
    /// <param name="file"></param>
    /// <param name="request"></param>
    /// <param name="fullFileName"></param>
    /// <param name="subjectId"></param>
    /// <param name="subjectRoomId"></param>
    /// <param name="userName"></param>
    /// <param name="userTypeId"></param>
    /// <returns></returns>
    private TalkMessage GetAttachmentTalkMessageByCtyun(SendFileApiRequest request, String fullFileName, int subjectId, UserSubjectRoomCouchBaseObject userSubjectRoom) {
        StringBuilder userTrueName = new StringBuilder(request.getCurrentUserTrueName());
        if (userSubjectRoom.getRoomUserType() == 1) {
            userTrueName.append("（助教）");
        } else if (userSubjectRoom.getRoomUserType() == 2) {
            userTrueName.append("（教师）");
        } else if (userSubjectRoom.getRoomUserType() == 3) {
            userTrueName.append("（主讲）");
        }
        String fileSize = CommonUtil.getFileSize(request.getFile().getSize());
        TalkMessage message = new TalkMessage(subjectId, userSubjectRoom.getSubjectRoomId(), request.getUserId(), userTrueName.toString(), request.getUserTypeId(), fullFileName, request.getFile().getOriginalFilename(), fileSize, request.getUserFace(), request.getToUserId());
        return message;
    }

    /// <summary>
    /// 上传文件至天翼云，成功返回文件全地址，失败返回错误信息
    /// </summary>
    /// <param name="file"></param>
    /// <param name="ossFilePath"></param>
    /// <param name="mimeMapping"></param>
    /// <returns></returns>
    public TwoTuple<Boolean, String> UpdateFileToCtyun(MultipartFile file, String oosFilePath, String mimeMapping) {
        try {
            InputStream is = new ByteArrayInputStream(file.getBytes());
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentType(mimeMapping);
            meta.setContentLength(file.getSize());
            AmazonS3 client = CreateClient.getClient(CryptogramHelper.Decrypt3DES(CommonConfig.getCtyunOssAccessKey(), CommonConfig.getConfigKey()), CryptogramHelper.Decrypt3DES(CommonConfig.getCtyunOssSecretKey(), CommonConfig.getConfigKey()), CommonConfig.getCtyunOosBucketEndPoint());
            PutObjectResult result = client.putObject(CommonConfig.getCtyunOssBucketName(), oosFilePath, is, meta);
            String msg = String.format("%s/%s/%s", CommonConfig.getCtyunOssCdnHost(), CommonConfig.getCtyunOssBucketName(), oosFilePath);
            return new TwoTuple<Boolean, String>(StringUtils.isNotBlank(result.getETag()), msg);
        } catch (Exception e) {
            return new TwoTuple<Boolean, String>(false, e.getMessage());
        }
    }


    /// <summary>
    /// 自动注册课程
    /// 未注册或注册失败直接返回对应Response
    /// 成功则返回Null
    /// </summary>
    /// <param name="request"></param>
    /// <param name="subjectId"></param>
    /// <param name="userType"></param>
    /// <param name="subjectRoomId"></param>
    /// <returns></returns>
    private ResponseBase AutoRegistSubject(MessageApiRequest request, int subjectId, int userType, int subjectRoomId) {
        if (userType == UserTypeEnum.Teacher.getCode()) {
            subjectRoomId = subjectService.teacherRegistSubject(subjectId, request.getUserId(), false);
            if (subjectRoomId <= 0) {
                return ResponseBase.GetValidateErrorResponse("");
            }
            subjectRoomService.clearSubjectRoomUserListCache(subjectRoomId);
        } else if (userType == UserTypeEnum.Student.getCode()) {
            if (subjectService.getByKey(subjectId, true).getSubjectGenreId() == SubjectGenreEnum.PublicBenefit.getIndex()) {
                subjectRoomId = subjectService.studentRegistSubject(subjectId, request.getUserId(), false);
                if (subjectRoomId <= 0) {
                    return ResponseBase.GetValidateErrorResponse(ErrorMessageStudent);
                }
                subjectRoomService.clearSubjectRoomUserListCache(subjectRoomId);
            } else {
                return ResponseBase.GetValidateErrorResponse(ErrorMessageUnregistered);
            }
        } else {
            return ResponseBase.GetValidateErrorResponse(ErrorMessageUnregistered);
        }
        return null;
    }

    /// <summary>
    /// 获取及更新在线信息
    /// </summary>
    /// <param name="request"></param>
    /// <param name="subjectRoomId"></param>
    /// <returns></returns>
    private List<UserOnlineInfo> GenerateUserOnlineInfos(MessageApiRequest request, int subjectRoomId) {
        HashMap<Integer, Boolean> allOnlineUser = talkMessageService.getUserOnlineStatus(subjectRoomId);
        HashMap<Integer, Integer> allUserStatus = talkMessageService.getUserStatus(subjectRoomId);
        List<User> allUser = subjectRoomStudentService.GetAllBySubjectRoomId(subjectRoomId);
        talkMessageService.setUserOnline(request.getUserId(), subjectRoomId, true);
        allOnlineUser.put(request.getUserId(), true);
        List<UserOnlineInfo> userInfoList = new LinkedList<>();
        allUser.forEach(m -> {
            UserOnlineInfo userOnlineInfo = new UserOnlineInfo()
                    .setUserId(m.getUserId())
                    .setUserFace(m.getUserFace())
                    .setUserTypeId(m.getUserTypeId())
                    .setUserName(m.getUserTrueName())
                    .setRoomUserType(m.getRoomUserType())
                    .setOrderIndex(m.getOrderIndex());
            for (Map.Entry<Integer, Boolean> n : allOnlineUser.entrySet()) {
                if (m.getUserId() == n.getKey()) {
                    userOnlineInfo.setOnline(n.getValue());
                }
            }
            for (Map.Entry<Integer, Integer> n : allUserStatus.entrySet()) {
                if (m.getUserId() == n.getKey().intValue()) {
                    userOnlineInfo.setStatus(n.getValue());
                }
            }
            userInfoList.add(userOnlineInfo);
        });
        userInfoList.sort(Comparator.comparing(UserOnlineInfo::isOnline).reversed().thenComparing(UserOnlineInfo::getOrderIndex));
        return userInfoList;
    }

    /// <summary>
/// 转换成ResponseMessage
/// </summary>
/// <param name="messageList"></param>
/// <returns></returns>
    private List<Message> ConvertToResponseMessage(List<TalkMessage> messageList) {
        if (CollectionUtils.isEmpty(messageList)) {
            return new LinkedList<Message>();
        }
        List<Message> resultList = new LinkedList<>();
        for (TalkMessage m : messageList) {
            resultList.add(buildMessage(m));
        }
        return resultList;
    }

    /// <summary>
/// 老师听课自动报名失败
/// </summary>
    public static final String ErrorMessageTeacher = "老师自动报名失败，您不能在该讨论区发言!";

    /// <summary>
/// 学生自动报名失败
/// </summary>
    public static final String ErrorMessageStudent = "自动报名失败，您不能在该讨论区发言!";

    /// <summary>
/// 未报名
/// </summary>
    public static final String ErrorMessageUnregistered = "您不能在该讨论区发言";

}
