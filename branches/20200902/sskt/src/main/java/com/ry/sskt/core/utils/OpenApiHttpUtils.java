package com.ry.sskt.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.ry.sskt.model.openapi.response.ApiResultEntity;
import com.ry.sskt.model.openapi.request.OpenApiRequestBase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Date;
import java.util.Map;

/**
 * Http工具类
 *
 * @author 幸仁强
 * @ClassName: HttpUtils
 * @date 2018年12月17日
 */
@Component
@Slf4j
public class OpenApiHttpUtils {
    private static RequestConfig requestConfig;

    static {
        // 设置请求和传输超时时间
        requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
    }

    public static <T extends OpenApiRequestBase> ApiResultEntity sendPost(String url, T request)
            throws Exception {
        long now = new Date().getTime();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        ApiResultEntity returnEntity = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            StringEntity se = new StringEntity(JSON.toJSONString(request), ContentType.create("application/json", "UTF-8"));
            httpPost.setEntity(se);
            response = httpclient.execute(httpPost);

            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                returnEntity = JSONObject.parseObject(EntityUtils.toString(entity, "UTF-8"), ApiResultEntity.class);
            }
        } catch (Exception e) {
            log.info(String.format("POST请求失败:%s,参数:%s", url, JSON.toJSONString(request)), e);
            throw e;
        } finally {
            if (response != null) {
                response.close();
            }
            if (httpclient != null) {
                httpclient.close();
            }
        }
        log.info(String.format("POST请求:%s,参数:%s,耗时%s毫秒", url, JSON.toJSONString(request), new Date().getTime() - now));
        return returnEntity;
    }


    public static <T extends OpenApiRequestBase> ApiResultEntity sendGet(String url, Map<String, String> param, String authToken)
            throws Exception {
        long now = new Date().getTime();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        ApiResultEntity returnEntity = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();
            HttpGet httpGet = new HttpGet(uri);
            response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                returnEntity = JSONObject.parseObject(EntityUtils.toString(entity, "UTF-8"), ApiResultEntity.class);
            }
        } catch (Exception e) {
            log.info(String.format("POST请求失败:%s,参数:%s,token:%s", url, JSON.toJSONString(param), authToken), e);
            throw e;
        } finally {
            if (response != null) {
                response.close();
            }
            if (httpclient != null) {
                httpclient.close();
            }
        }
        log.info(String.format("GET请求:%s,参数:%s,token:%s,耗时%s毫秒", url, JSON.toJSONString(param), authToken, new Date().getTime() - now));
        return returnEntity;
    }
}