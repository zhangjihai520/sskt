package com.ry.sskt.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.ry.sskt.model.openapi.request.OpenApiRequestBase;
import com.ry.sskt.model.openapi.response.ApiResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.*;

/**
 * Http工具类
 *
 * @author 幸仁强
 * @ClassName: HttpUtils
 * @date 2018年12月17日
 */
@Component
@Slf4j
public class DledcHttpUtils {
    private static RequestConfig requestConfig;

    static {
        // 设置请求和传输超时时间
        requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 设置连接主机服务超时时间
                .setConnectionRequestTimeout(35000)// 设置连接请求超时时间
                .setSocketTimeout(60000)// 设置读取数据连接超时时间
                .build();
    }

    public static String sendPost(String url, Object request, String authToken)
            throws Exception {
        long now = new Date().getTime();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String returnStr = StringUtils.EMPTY;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            StringEntity se = new StringEntity(JSON.toJSONString(request), ContentType.create("application/json", "UTF-8"));
            httpPost.setEntity(se);
            if (StringUtils.isNotBlank(authToken)) {
                httpPost.addHeader("Authorization", "Bearer " + authToken);
            }
            response = httpclient.execute(httpPost);

            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                returnStr = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (Exception e) {
            log.info(String.format("POST请求失败:%s,参数:%s,token:%s", url, JSON.toJSONString(request), authToken), e);
            throw e;
        } finally {
            if (response != null) {
                response.close();
            }
            if (httpclient != null) {
                httpclient.close();
            }
        }
        log.info(String.format("POST请求:%s,参数:%s,token:%s,耗时%s毫秒", url, JSON.toJSONString(request), authToken, new Date().getTime() - now));
        return returnStr;
    }

    /// <summary>
    /// 发送post请求
    /// </summary>
    /// <param name="url"></param>
    /// <param name="authToken"></param>
    /// <param name="parameters"></param>
    /// <param name="timeout"></param>
    /// <returns></returns>
    /**  public static String sendPost(String url, String authToken, Map<String, String> parameters) throws Exception {
     String data = StringUtils.EMPTY;
     if (parameters != null && parameters.size() > 0) {
     data = BuildQuery(parameters);
     }
     return sendPost(url,parameters);
     }**/
    public static String sendPost(String url, Map<String, String> paramMap) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        String result = "";
        // 创建httpClient实例
        httpClient = HttpClients.createDefault();
        // 创建httpPost远程连接实例
        HttpPost httpPost = new HttpPost(url);
        // 为httpPost实例设置配置
        httpPost.setConfig(requestConfig);
        // 设置请求头
        httpPost.addHeader("Content-Type", "application/json");
        // 封装post请求参数
        if (null != paramMap && paramMap.size() > 0) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            // 通过map集成entrySet方法获取entity
            Set<Map.Entry<String, String>> entrySet = paramMap.entrySet();
            // 循环遍历，获取迭代器
            Iterator<Map.Entry<String, String>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> mapEntry = iterator.next();
                nvps.add(new BasicNameValuePair(mapEntry.getKey(), mapEntry.getValue().toString()));
            }

            // 为httpPost设置封装好的请求参数
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        try {
            // httpClient对象执行post请求,并返回响应参数对象
            httpResponse = httpClient.execute(httpPost);
            // 从响应对象中获取响应内容
            HttpEntity entity = httpResponse.getEntity();
            result = EntityUtils.toString(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != httpResponse) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static String sendGet(String url, Map<String, String> param, String authToken)
            throws Exception {
        long now = new Date().getTime();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String returnStr = StringUtils.EMPTY;
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
            if (StringUtils.isNotBlank(authToken)) {
                httpGet.addHeader("Authorization", "Bearer " + authToken);
            }
            response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                returnStr = EntityUtils.toString(entity, "UTF-8");
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
        return returnStr;
    }


    /// <summary>
    /// 组装普通文本请求参数。
    /// </summary>
    /// <param name="parameters">Key-Value形式请求参数字典</param>
    /// <returns>URL编码后的请求数据</returns>
    public static String BuildQuery(Map<String, String> parameters) throws UnsupportedEncodingException {
        StringBuilder postData = new StringBuilder();
        boolean hasParam = false;
        for (Map.Entry<String, String> n : parameters.entrySet()) {
            if (StringUtils.isNotBlank(n.getKey()) && n.getValue() != null) {
                if (hasParam) {
                    postData.append("&");
                }
                postData.append(n.getKey());
                postData.append("=");
                postData.append(URLEncoder.encode(n.getValue(), "UTF-8"));
                hasParam = true;
            }
        }
        return postData.toString();
    }
}