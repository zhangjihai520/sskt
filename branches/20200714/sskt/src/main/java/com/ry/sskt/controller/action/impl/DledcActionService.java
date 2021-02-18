package com.ry.sskt.controller.action.impl;

import com.ry.sskt.controller.action.IDledcActionService;
import com.ry.sskt.core.config.CommonConfig;
import com.ry.sskt.core.dledc.DledcDefaultClient;
import com.ry.sskt.core.dledc.response.ConnectTokenResponse;
import com.ry.sskt.core.dledc.response.ConnectUserInfoResponse;
import com.ry.sskt.core.dledc.utils.DledcConvertHelper;
import com.ry.sskt.core.utils.JwtUtil;
import com.ry.sskt.core.utils.RedisUtil;
import com.ry.sskt.model.common.constant.*;
import com.ry.sskt.model.common.entity.TwoTuple;
import com.ry.sskt.model.common.entity.User;
import com.ry.sskt.model.common.entity.UserLoginInfo;
import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.dledc.request.DledcConfigApiRequest;
import com.ry.sskt.model.dledc.request.DledcLoginApiRequest;
import com.ry.sskt.model.dledc.request.DledcMobileLoginApiRequest;
import com.ry.sskt.model.dledc.response.DledcConfigResponse;
import com.ry.sskt.model.dledc.response.DledcLoginResponse;
import com.ry.sskt.model.dledc.response.DledcMobileLoginResponse;
import com.ry.sskt.model.user.cache.TokenCache;
import com.ry.sskt.service.IDledcService;
import com.ry.sskt.service.IUserLoginInfoService;
import com.ry.sskt.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

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
public class DledcActionService implements IDledcActionService {
    @Autowired
    IUserService userService;
    @Autowired
    IDledcService dledcService;
    @Autowired
    IUserLoginInfoService userLoginInfoService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase DledcLogin(DledcLoginApiRequest request) throws Exception {
        if (request.getIsTest() == 1) {
            return ResponseBase.GetSuccessResponse(new DledcLoginResponse(1, JwtUtil.CreateToken(request.getCode(), DledcConvertHelper.ConvertUserType(request.getT().toLowerCase()))));
        }
        String name = URLDecoder.decode(request.getName(), "UTF-8");
        String uid = request.getUid();
        String t = request.getT();
        int userid = 0;
        if (StringUtils.isBlank(uid) || CommonConfig.isUseSynchronizationUse()) {
            //同步处理
            TwoTuple<Integer, ResponseBase> useridAndResponse = SuccessResponse(request);
            if (useridAndResponse.first == 0) {
                return useridAndResponse.second;
            }
            userid = useridAndResponse.first;
        } else {
            User user = User(request, uid, name, userService);
            userid = user.getUserId();
        }

        userLoginInfoService.save(new UserLoginInfo().setUserId(userid).setLoginDate(LocalDate.now()).setLoginTime(LocalDateTime.now()).setLoginType(LoginTypeEnum.XinHua.getCode()));
        String token = JwtUtil.CreateToken(userid + "", DledcConvertHelper.ConvertUserType(request.getT().toLowerCase()));
        TokenCache tokenCache = new TokenCache().setToken(token).setFromTypeId(request.getFromTypeId()).setUserId(userid);
        if (request.getFromTypeId() == FromTypeEnum.PC.getIndex()) {
            RedisUtil.setObj(tokenCache, CommonConst.MINUTE_480);
        }
        if (request.getFromTypeId() == FromTypeEnum.H5.getIndex()) {
            RedisUtil.setObj(tokenCache, CommonConst.HALF_YEAR);
        }
        DledcLoginResponse response = new DledcLoginResponse(1, token);
        return ResponseBase.GetSuccessResponse(response);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase DledcMobileLogin(DledcMobileLoginApiRequest request) throws Exception {
        if (request.getIsTest() == 1) {
            return ResponseBase.GetSuccessResponse(new DledcLoginResponse(1, JwtUtil.CreateToken(request.getUid(), UserTypeEnum.All.getCode())));
        }
        TwoTuple<Integer, Integer> twoTuple = dledcService.sychroUser(request.getUid());

        userLoginInfoService.save(new UserLoginInfo().setUserId(twoTuple.first).setLoginDate(LocalDate.now()).setLoginTime(LocalDateTime.now()).setLoginType(LoginTypeEnum.XinHuaMobile.getCode()));
        String token = JwtUtil.CreateToken(twoTuple.first.toString(), twoTuple.second);
        DledcMobileLoginResponse response = new DledcMobileLoginResponse(1, token);
        return ResponseBase.GetSuccessResponse(response);
    }

    @Override
    public ResponseBase DledcConfig(DledcConfigApiRequest request) {
        DledcConfigResponse response = new DledcConfigResponse();
        if (request.getAccessTypeId() == 1) {
            response.setRedirectUri(CommonConfig.getOnlineRedirectUri())
                    .setClientId(CommonConfig.getOnlineClientId())
                    .setClientSecret(CommonConfig.getOnlineClientSecret())
                    .setPostLogoutRedirectUri(CommonConfig.getOnlinePostLogoutRedirectUri());
        } else {
            response.setRedirectUri(CommonConfig.getOffLineRedirectUri())
                    .setClientId(CommonConfig.getOffLineClientId())
                    .setClientSecret(CommonConfig.getOffLineClientSecret())
                    .setPostLogoutRedirectUri(CommonConfig.getOffLinePostLogoutRedirectUri());
        }
        response.setDledcApiUrl(CommonConfig.getDledcApiUrl());
        response.setDledcPlatformUrl(CommonConfig.getDledcPlatformUrl());
        return ResponseBase.GetSuccessResponse(response);
    }

    public static User User(DledcLoginApiRequest apiRequest, String uid, String name, IUserService userService) throws Exception {
        log.info(String.format("开始新华云快捷登录%s", uid));
        User user = userService.readBySource(SourceTypeEnum.JiaoFuPingTai.getCode(), uid);
        log.info(String.format("查询用户是否存在%s", uid));
        if (user.getUserId() == 0) {
            user.setUserTrueName(name);
            user.setCreateDateTime(LocalDateTime.now());
            user.setSourceTypeId(SourceTypeEnum.JiaoFuPingTai.getCode());
            user.setSourceId(uid);
            user.setStatusFlag(1);
            user.setUserTypeId(DledcConvertHelper.ConvertUserType(apiRequest.getT().toLowerCase()));
            user.setUserName(UUID.randomUUID().toString());
            user.setPassword("");
            user.setUserSex(1);
            user.setUserFace("");
            user.setEmailAddress("");
            user.setSchoolName("");
            user.setComment("");
            user.setPhone("");
            user.setStudentCode("等待同步");
            user.setUserRoles(DledcConvertHelper.ConvertUserType(apiRequest.getT().toLowerCase()) + "");
            user.setIdCard("");
            user.setUpdateDateTime(LocalDateTime.now());
            user.setUserId(userService.save(user));
            log.info(String.format("保存首次登录用户%s", uid));
        }
        return user;
    }

    private TwoTuple<Integer, ResponseBase> SuccessResponse(DledcLoginApiRequest apiRequest) throws Exception {
        log.info("开始普通南昌云平台登录接口");
        DledcDefaultClient client = dledcService.getDledcConfig(apiRequest.getAccessTypeId());
        log.info("获取配置成功");
        log.info("code:" + apiRequest.getCode());
        ConnectTokenResponse connectTokenResponse = client.ConnectTokenByCode(apiRequest.getCode());
        log.info("获取令牌成功:", connectTokenResponse.getAccess_token());
        if (connectTokenResponse == null) {
            return new TwoTuple<Integer, ResponseBase>(0, ResponseBase.GetErrorResponse("从云平台获取token失败"));
        }
        ConnectUserInfoResponse connectUserInfoResponse = client.ConnectUserInfo(connectTokenResponse.getAccess_token());
        log.info("获取用户信息成功");
        if (connectUserInfoResponse == null) {
            return new TwoTuple<Integer, ResponseBase>(0, ResponseBase.GetErrorResponse("从云平台获取uid失败"));
        }
        String uid = connectUserInfoResponse.getSub();
        TwoTuple<Integer, Integer> twoTuple = dledcService.sychroUser(uid);
        log.info("用户同步完成");
        return new TwoTuple<Integer, ResponseBase>(twoTuple.first, null);
    }
}
