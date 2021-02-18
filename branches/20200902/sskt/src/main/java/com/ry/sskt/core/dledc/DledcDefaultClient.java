package com.ry.sskt.core.dledc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ry.sskt.core.dledc.request.CheckUserAccountRequest;
import com.ry.sskt.core.dledc.response.CheckUserAccountResponse;
import com.ry.sskt.core.dledc.response.ConnectTokenResponse;
import com.ry.sskt.core.dledc.response.ConnectUserInfoResponse;
import com.ry.sskt.core.utils.DateUtil;
import com.ry.sskt.core.utils.DledcHttpUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Dledc客户端。
 */
@Component
@Data
@Slf4j
public class DledcDefaultClient {
    private String _serverUrl;
    public String _redirectUri;
    public String _clientId;
    public String _clientSecret;
    public String _postLogoutRedirectUri;
    private static Lock lock = new ReentrantLock();
    /// <summary>
    /// 基础token，用于访问api接口（该token不可用于获取用户id）
    /// </summary>
    private String _baseToken = StringUtils.EMPTY;

    /// <summary>
    /// 下次获取基础token的最晚时间
    /// </summary>
    private LocalDateTime _expireTime = DateUtil.LocalDateTimeMIN();

    /// <summary>
    /// 获取基础token
    /// </summary>
    /// <returns></returns>
    public String GetBaseToken() {
        if (_expireTime.isAfter(LocalDateTime.now())) {
            lock.lock();
            try {
                if (_expireTime.isAfter(LocalDateTime.now())) {
                    ConnectTokenResponse connectTokenResponse = this.ConnectBaseToken();
                    _baseToken = connectTokenResponse.getAccess_token();
                    _expireTime.plusMinutes(connectTokenResponse.getExpires_in() - 10);

                }
            } catch (Exception e) {
                log.error("获取云平台基础Token异常", e);
            } finally {
                lock.unlock();
            }
        }
        return _baseToken;
    }

    /// <summary>
    /// 构造函数
    /// </summary>
    /// <param name="serverUrl"></param>
    /// <param name="redirectUri"></param>
    /// <param name="clientId"></param>
    /// <param name="clientSecret"></param>
    /// <param name="postLogoutRedirectUri"></param>
    public DledcDefaultClient(String serverUrl, String redirectUri, String clientId, String clientSecret, String postLogoutRedirectUri) {
        this._serverUrl = serverUrl;
        this._redirectUri = redirectUri;
        this._clientId = clientId;
        this._clientSecret = clientSecret;
        this._postLogoutRedirectUri = postLogoutRedirectUri;
    }

    /// <summary>
    /// 执行Dledc基础API请求。
    /// </summary>
    /// <typeparam name="T">Response对象类型</typeparam>
    /// <param name="request">请求参数</param>
    /// <returns></returns>
    public <R extends DledcResponse, T extends IDledcRequest> R Execute(Class<R> responsClazz, T request) throws Exception {

        String token = this.GetBaseToken();
        String url = String.format("%s/%s", _serverUrl, request.GetApiPath());
        String responseStirng = "";
        if (request instanceof IDledcGetRequest) {
            Map parameters = ((IDledcGetRequest) request).GetParameters();
            responseStirng = DledcHttpUtils.sendGet(url, parameters, token);
        } else {
            responseStirng = DledcHttpUtils.sendPost(url, request, token);
        }
        return JSON.parseObject(responseStirng, responsClazz);
    }

    /// <summary>
    /// 获取基础接口访问令牌
    /// </summary>
    private ConnectTokenResponse ConnectBaseToken() throws Exception {
        String url = String.format("%s/core/connect/token", _serverUrl);
        String responseStirng = "";
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("grant_type", "client_credentials");
        parameters.put("client_id", _clientId);
        parameters.put("client_secret", _clientSecret);
        parameters.put("scope", "UserAPI BaseInfo");
        responseStirng = DledcHttpUtils.sendPost(url, parameters);

        return JSON.parseObject(responseStirng, new TypeReference<ConnectTokenResponse>() {
        });
    }

    /// <summary>
    /// 换取访问令牌
    /// </summary>
    /// <param name="request"></param>
    /// <param name="clientId"></param>
    /// <param name="clientSecret"></param>
    /// <param name="redirectUri"></param>
    /// <returns></returns>
    public ConnectTokenResponse ConnectTokenByCode(String code) throws Exception {
        String url = String.format("%s/core/connect/token", _serverUrl);
        String responseStirng = "";
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("code", code);
        parameters.put("grant_type", "authorization_code");
        parameters.put("client_id", _clientId);
        parameters.put("client_secret", _clientSecret);
        parameters.put("redirect_uri", _redirectUri);
        responseStirng = DledcHttpUtils.sendPost(url, parameters);
        return JSON.parseObject(responseStirng, new TypeReference<ConnectTokenResponse>() {
        });
    }

    /// <summary>
    /// 通过access_token拉取用户信息
    /// </summary>
    /// <param name="request"></param>
    /// <param name="accessToken"></param>
    /// <returns></returns>
    public ConnectUserInfoResponse ConnectUserInfo(String accessToken) throws Exception {
        String url = String.format("%s/core/connect/userinfo", _serverUrl);
        String responseStirng = DledcHttpUtils.sendGet(url, null, accessToken);
        return JSON.parseObject(responseStirng, new TypeReference<ConnectUserInfoResponse>() {
        });
    }

    /// <summary>
    /// 通过code获取uid
    /// </summary>
    /// <param name="code">学生：学籍号，老师、机构人员：身份证号</param>
    /// <returns></returns>
    public String GetUIdByCode(String code) throws Exception {
        String url = String.format("%s/api/users/getuidbycode?code=%s", _serverUrl, code);
        return DledcHttpUtils.sendGet(url, null, this.GetBaseToken());
    }
}
