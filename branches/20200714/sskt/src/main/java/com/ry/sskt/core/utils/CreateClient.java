package com.ry.sskt.core.utils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

public class CreateClient {
    public static AmazonS3 getClient(String accessKey, String secretKey, String oosDomain) {
        // 创建一个AmazonS3 客户端对象
        AmazonS3 client = new AmazonS3Client(new AWSCredentials() {
            public String getAWSAccessKeyId() {
                return accessKey;//你的accesskey
            }

            public String getAWSSecretKey() {
                return secretKey;//你的secretKey
            }
        });
        // 设置API服务器
        client.setEndpoint(oosDomain);//设置你的资源池域名，我的是https://oos-hq-sh.ctyunapi.cn
        return client;
    }
}
