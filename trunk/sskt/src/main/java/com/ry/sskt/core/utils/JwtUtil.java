package com.ry.sskt.core.utils;


import com.alibaba.fastjson.JSON;
import com.ry.sskt.model.common.constant.CommonConst;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;


/**
 * JWT工具类 <描述类的作用>
 *
 * @author 幸仁强
 * @ClassName: JwtUtil
 * @date 2020年4月16日
 */
@Component
@Slf4j
public class JwtUtil {

    public static Claims parseJWT(String jsonWebToken) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(GetSecretKey()).parseClaimsJws(jsonWebToken);
            Claims body = claimsJws.getBody();
            return body;
        } catch (Exception ex) {
            return null;
        }
    }

    private static Key GetSecretKey() {
        Key KEY = new SecretKeySpec("VideoClass_JwtSecret".getBytes(),
                SignatureAlgorithm.HS256.getJcaName());
        return KEY;
    }

    public static String CreateToken(String userId, Integer userType) {
        if (StringUtils.isBlank(userId) || Integer.parseInt(userId) == 0) {
            log.error(String.format("userId不能为%s或者空", userId));
            return "";
        }
        Map<String, Object> stringObjectMap = new HashMap<>();
        stringObjectMap.put("typ", "JWT");
        Map<String, Object> payload = new HashMap<>();
        payload.put("sub", UrlUtil.encrypt(userId, CommonConst.UidKey));
        payload.put("utp", userType);
        payload.put("exp", System.currentTimeMillis() + 259200000);
        String payloadStr = JSON.toJSONString(payload);
        String compactJws = Jwts.builder().setHeader(stringObjectMap).setPayload(payloadStr).signWith(SignatureAlgorithm.HS256, GetSecretKey()).compact();
        return compactJws;
    }

    /**
     * 用于获取Token中的用户ID <描述方法的作用>s
     *
     * @param jsonWebToken
     * @return
     * @Title: getUserId
     * @author 幸仁强
     */
    public static int getUserId(String jsonWebToken) {
        Claims claims = parseJWT(jsonWebToken);
        String sub = claims.get("sub", String.class);
        return UrlUtil.decrypt(sub,CommonConst.UidKey, Integer.class);
    }

    /**
     * 用于获取Token中的用户ID <描述方法的作用>s
     *
     * @param jsonWebToken
     * @return
     * @Title: getUserId
     * @author 幸仁强
     */
    public static Integer getUserType(String jsonWebToken) {
        Claims claims = parseJWT(jsonWebToken);
        Integer userType = claims.get("utp", Integer.class);
        return userType;
    }

    public static void main(String[] args) {
        String token = CreateToken("3360342", 1);
        System.out.println("token:" + token);
        int userId = getUserId(token);
        System.out.println("userId:" + userId);
        // System.out.println("jwt body user-id:" + i);
        //System.out.println("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJUJTJmSGZEbk1xZGNVJTNkIiwiZXhwIjoxNjAyNDcxNjM1fQ.z-zrI45ORhkPW85-vhR1nJOpwpvgxydMkVKeqaPkmz0");
        // eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsNFo5NUF0MkJGdGMzRCIsImV4cCI6MTYwMDAwMH0.4wLRZH_UYuVuzn1BQ7OTZrp0w6ggGujLIgEPLzGL53Y
    }
}
