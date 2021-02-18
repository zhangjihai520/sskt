package com.ry.sskt.core.config;

import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
//@PropertySource("classpath:config/common.properties")
@NacosConfigurationProperties(dataId = "${spring.application.name}.${spring.cloud.nacos.file-extension}", autoRefreshed = true)
@Order(-1)
@RefreshScope
public class CommonConfig {
    public static String base64Security;
    public static Integer maxRegisterNumber;
    public static String couchBaseUrl;
    public static String configKey;
    public static String videoHost;
    public static String openAPI;
    public static String openAPIId;
    public static String motkUrl;
    public static String appToken;
    public static String allowFileTypes;
    public static String dledcApiUrl;
    public static String offLineRedirectUri;
    public static String offLineClientId;
    public static String offLineClientSecret;
    public static String offLineScope;
    public static String offLinePostLogoutRedirectUri;
    public static String onlineRedirectUri;
    public static String onlineClientId;
    public static String onlineClientSecret;
    public static String onlineScope;
    public static String onlinePostLogoutRedirectUri;
    public static String dledcCorpId;
    public static String dledcToken;
    public static String dledcEncodingAESKey;
    public static String red5Host;
    public static String dledcPlatformUrl;
    public static String accessKeyId;
    public static String accessSecret;
    public static String regionId;
    public static String aliLiveHost;
    public static String appName;
    public static int aliLiveThreshold;
    public static String urlAuthSecret;
    public static int urlAuthKeyExp;
    public static String ctyunOosBucketEndPoint;
    public static String ctyunOssBucketName;
    public static String ctyunOssAccessKey;
    public static String ctyunOssSecretKey;
    public static String ctyunOssCdnHost;
    public static boolean useSynchronizationUse;
    public static String unSynchronizationUser;
    public static String getBase64Security() {
        return base64Security;
    }

    @Value("${jwt.base64Security}")
    public void setBase64Security(String base64Security) {
        CommonConfig.base64Security = base64Security;
    }

    public static Integer getMaxRegisterNumber() {
        return maxRegisterNumber;
    }

    @Value("${maxRegisterNumber}")
    public void setMaxRegisterNumber(Integer maxRegisterNumber) {
        CommonConfig.maxRegisterNumber = maxRegisterNumber;
    }

    public static String getCouchBaseUrl() {
        return couchBaseUrl;
    }

    @Value("${couchBaseUrl}")
    public void setCouchBaseUrl(String couchBaseUrl) {
        CommonConfig.couchBaseUrl = couchBaseUrl;
    }

    public static String getConfigKey() {
        return configKey;
    }

    @Value("${systemConfig.configKey}")
    public void setConfigKey(String configKey) {
        CommonConfig.configKey = configKey;
    }

    public static String getVideoHost() {
        return videoHost;
    }

    @Value("${systemConfig.videoHost}")
    public void setVideoHost(String videoHost) {
        CommonConfig.videoHost = videoHost;
    }

    public static String getOpenAPI() {
        return openAPI;
    }

    @Value("${systemConfig.openAPI}")
    public void setOpenAPI(String openAPI) {
        CommonConfig.openAPI = openAPI;
    }

    public static String getOpenAPIId() {
        return openAPIId;
    }

    @Value("${systemConfig.openAPIId}")
    public void setOpenAPIId(String openAPIId) {
        CommonConfig.openAPIId = openAPIId;
    }

    public static String getMotkUrl() {
        return motkUrl;
    }

    @Value("${systemConfig.motkUrl}")
    public void setMotkUrl(String motkUrl) {
        CommonConfig.motkUrl = motkUrl;
    }

    public static String getAppToken() {
        return appToken;
    }
    @Value("${systemConfig.appToken}")
    public void setAppToken(String appToken) {
        CommonConfig.appToken = appToken;
    }

    public static String getAllowFileTypes() {
        return allowFileTypes;
    }

    @Value("${systemConfig.allowFileTypes}")
    public void setAllowFileTypes(String allowFileTypes) {
        CommonConfig.allowFileTypes = allowFileTypes;
    }

    public static String getDledcApiUrl() {
        return dledcApiUrl;
    }

    @Value("${systemConfig.dledcApiUrl}")
    public void setDledcApiUrl(String dledcApiUrl) {
        CommonConfig.dledcApiUrl = dledcApiUrl;
    }

    public static String getOffLineRedirectUri() {
        return offLineRedirectUri;
    }

    @Value("${systemConfig.offLineRedirectUri}")
    public void setOffLineRedirectUri(String offLineRedirectUri) {
        CommonConfig.offLineRedirectUri = offLineRedirectUri;
    }

    public static String getOffLineClientId() {
        return offLineClientId;
    }

    @Value("${systemConfig.offLineClientId}")
    public void setOffLineClientId(String offLineClientId) {
        CommonConfig.offLineClientId = offLineClientId;
    }

    public static String getOffLineClientSecret() {
        return offLineClientSecret;
    }

    @Value("${systemConfig.offLineClientSecret}")
    public void setOffLineClientSecret(String offLineClientSecret) {
        CommonConfig.offLineClientSecret = offLineClientSecret;
    }

    public static String getOffLineScope() {
        return offLineScope;
    }

    @Value("${systemConfig.offLineScope}")
    public void setOffLineScope(String offLineScope) {
        CommonConfig.offLineScope = offLineScope;
    }

    public static String getOffLinePostLogoutRedirectUri() {
        return offLinePostLogoutRedirectUri;
    }

    @Value("${systemConfig.offLinePostLogoutRedirectUri}")
    public void setOffLinePostLogoutRedirectUri(String offLinePostLogoutRedirectUri) {
        CommonConfig.offLinePostLogoutRedirectUri = offLinePostLogoutRedirectUri;
    }

    public static String getOnlineRedirectUri() {
        return onlineRedirectUri;
    }

    @Value("${systemConfig.onlineRedirectUri}")
    public void setOnlineRedirectUri(String onlineRedirectUri) {
        CommonConfig.onlineRedirectUri = onlineRedirectUri;
    }

    public static String getOnlineClientId() {
        return onlineClientId;
    }

    @Value("${systemConfig.onlineClientId}")
    public void setOnlineClientId(String onlineClientId) {
        CommonConfig.onlineClientId = onlineClientId;
    }

    public static String getOnlineClientSecret() {
        return onlineClientSecret;
    }

    @Value("${systemConfig.onlineClientSecret}")
    public void setOnlineClientSecret(String onlineClientSecret) {
        CommonConfig.onlineClientSecret = onlineClientSecret;
    }

    public static String getOnlineScope() {
        return onlineScope;
    }

    @Value("${systemConfig.onlineScope}")
    public void setOnlineScope(String onlineScope) {
        CommonConfig.onlineScope = onlineScope;
    }

    public static String getOnlinePostLogoutRedirectUri() {
        return onlinePostLogoutRedirectUri;
    }

    @Value("${systemConfig.onlinePostLogoutRedirectUri}")
    public void setOnlinePostLogoutRedirectUri(String onlinePostLogoutRedirectUri) {
        CommonConfig.onlinePostLogoutRedirectUri = onlinePostLogoutRedirectUri;
    }

    public static String getDledcCorpId() {
        return dledcCorpId;
    }

    @Value("${systemConfig.dledcCorpId}")
    public void setDledcCorpId(String dledcCorpId) {
        CommonConfig.dledcCorpId = dledcCorpId;
    }

    public static String getDledcToken() {
        return dledcToken;
    }

    @Value("${systemConfig.dledcToken}")
    public void setDledcToken(String dledcToken) {
        CommonConfig.dledcToken = dledcToken;
    }

    public static String getDledcEncodingAESKey() {
        return dledcEncodingAESKey;
    }

    @Value("${systemConfig.dledcEncodingAESKey}")
    public void setDledcEncodingAESKey(String dledcEncodingAESKey) {
        CommonConfig.dledcEncodingAESKey = dledcEncodingAESKey;
    }

    public static String getRed5Host() {
        return red5Host;
    }

    @Value("${systemConfig.red5Host}")
    public void setRed5Host(String red5Host) {
        CommonConfig.red5Host = red5Host;
    }

    public static String getDledcPlatformUrl() {
        return dledcPlatformUrl;
    }

    @Value("${systemConfig.dledcPlatformUrl}")
    public void setDledcPlatformUrl(String dledcPlatformUrl) {
        CommonConfig.dledcPlatformUrl = dledcPlatformUrl;
    }

    public static String getAccessKeyId() {
        return accessKeyId;
    }

    @Value("${systemConfig.accessKeyId}")
    public void setAccessKeyId(String accessKeyId) {
        CommonConfig.accessKeyId = accessKeyId;
    }

    public static String getAccessSecret() {
        return accessSecret;
    }

    @Value("${systemConfig.accessSecret}")
    public void setAccessSecret(String accessSecret) {
        CommonConfig.accessSecret = accessSecret;
    }

    public static String getRegionId() {
        return regionId;
    }

    @Value("${systemConfig.regionId}")
    public void setRegionId(String regionId) {
        CommonConfig.regionId = regionId;
    }

    public static String getAliLiveHost() {
        return aliLiveHost;
    }

    @Value("${systemConfig.aliLiveHost}")
    public void setAliLiveHost(String aliLiveHost) {
        CommonConfig.aliLiveHost = aliLiveHost;
    }

    public static String getAppName() {
        return appName;
    }

    @Value("${systemConfig.appName}")
    public void setAppName(String appName) {
        CommonConfig.appName = appName;
    }

    public static int getAliLiveThreshold() {
        return aliLiveThreshold;
    }

    @Value("${systemConfig.aliLiveThreshold}")
    public void setAliLiveThreshold(int aliLiveThreshold) {
        CommonConfig.aliLiveThreshold = aliLiveThreshold;
    }

    public static String getUrlAuthSecret() {
        return urlAuthSecret;
    }

    @Value("${systemConfig.urlAuthSecret}")
    public void setUrlAuthSecret(String urlAuthSecret) {
        CommonConfig.urlAuthSecret = urlAuthSecret;
    }

    public static int getUrlAuthKeyExp() {
        return urlAuthKeyExp;
    }

    @Value("${systemConfig.urlAuthKeyExp}")
    public void setUrlAuthKeyExp(int urlAuthKeyExp) {
        CommonConfig.urlAuthKeyExp = urlAuthKeyExp;
    }

    public static String getCtyunOosBucketEndPoint() {
        return ctyunOosBucketEndPoint;
    }

    @Value("${systemConfig.ctyunOosBucketEndPoint}")
    public void setCtyunOosBucketEndPoint(String ctyunOosBucketEndPoint) {
        CommonConfig.ctyunOosBucketEndPoint = ctyunOosBucketEndPoint;
    }

    public static String getCtyunOssBucketName() {
        return ctyunOssBucketName;
    }

    @Value("${systemConfig.ctyunOssBucketName}")
    public void setCtyunOssBucketName(String ctyunOssBucketName) {
        CommonConfig.ctyunOssBucketName = ctyunOssBucketName;
    }

    public static String getCtyunOssAccessKey() {
        return ctyunOssAccessKey;
    }

    @Value("${systemConfig.ctyunOssAccessKey}")
    public void setCtyunOssAccessKey(String ctyunOssAccessKey) {
        CommonConfig.ctyunOssAccessKey = ctyunOssAccessKey;
    }

    public static String getCtyunOssSecretKey() {
        return ctyunOssSecretKey;
    }

    @Value("${systemConfig.ctyunOssSecretKey}")
    public void setCtyunOssSecretKey(String ctyunOssSecretKey) {
        CommonConfig.ctyunOssSecretKey = ctyunOssSecretKey;
    }

    public static String getCtyunOssCdnHost() {
        return ctyunOssCdnHost;
    }

    @Value("${systemConfig.ctyunOssCdnHost}")
    public void setCtyunOssCdnHost(String ctyunOssCdnHost) {
        CommonConfig.ctyunOssCdnHost = ctyunOssCdnHost;
    }

    public static boolean isUseSynchronizationUse() {
        return useSynchronizationUse;
    }
    @Value("${systemConfig.useSynchronizationUse}")
    public void setUseSynchronizationUse(boolean useSynchronizationUse) {
        CommonConfig.useSynchronizationUse = useSynchronizationUse;
    }

    public static String getUnSynchronizationUser() {
        return unSynchronizationUser;
    }
    @Value("${systemConfig.unSynchronizationUser}")
    public void setUnSynchronizationUser(String unSynchronizationUser) {
        CommonConfig.unSynchronizationUser = unSynchronizationUser;
    }
}
