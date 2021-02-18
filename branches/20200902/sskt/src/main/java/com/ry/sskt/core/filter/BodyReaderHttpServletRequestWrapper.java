package com.ry.sskt.core.filter;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ry.sskt.core.utils.JwtUtil;
import com.ry.sskt.model.common.entity.User;
import com.ry.sskt.service.IUserService;
import com.ry.sskt.service.impl.UserService;
import jodd.io.StreamUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private byte[] body; // 用于保存读取body中数据

    public String param = "";

    public BodyReaderHttpServletRequestWrapper(HttpServletRequest httpRequest, IUserService userService) {
        super(httpRequest);
        try {
            body = StreamUtil.readBytes(httpRequest.getReader(), "UTF-8");
            param = getBodyString(this.getReader());
            String systemTypeId = httpRequest.getHeader("CurUserSystem");
            String fromTypeId = httpRequest.getHeader("FromTypeId");
            Map<String, Object> map = JSONStrConvertToMap(param);
            Cookie[] cookie = httpRequest.getCookies();
            String token = "";
            if (null != cookie) {
                Cookie cook = Arrays.asList(cookie).stream().filter(switchEnum -> switchEnum.getName().equals("Authorization")).findFirst().orElse(null);
                if (null != cook && StringUtils.isNotBlank(cook.getValue())) {
                    token = cook.getValue();
                }
            }
            if (StringUtils.isBlank(token)) {
                token = httpRequest.getHeader("Authorization");
            }
            if (StringUtils.isNotBlank(token) && null != JwtUtil.parseJWT(token)) {
                int userId = JwtUtil.getUserId(token);
                if (CollectionUtils.isEmpty(map)) {
                    map = new HashMap<>();
                }
                map.put("UserId", userId);
                if (userId > 0) {
                    User user = userService.getByKey(userId, true);
                    map.put("CurrentUserTrueName", StringUtils.isNotBlank(user.getUserTrueName()) ? user.getUserTrueName() : user.getUserName());
                    map.put("UserFace", user.getUserFace());
                    map.put("UserTypeId", user.getUserTypeId());
                    map.put("SystemTypeId", StringUtils.isNotBlank(systemTypeId) ? Integer.valueOf(systemTypeId) : 1);
                    map.put("FromTypeId", StringUtils.isNotBlank(fromTypeId) ? Integer.valueOf(fromTypeId) : 1);
                }
            }
            String json = JSON.toJSONString(map, true);
            body = StreamUtil.readBytes(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            log.error(httpRequest.getRequestURI() + "发生全局未处理的异常：", e);
        }
    }

    /**
     * JSON字符串转换成Map
     *
     * @param string
     * @return
     */
    private Map<String, Object> JSONStrConvertToMap(String string) {
        try {
            return JSONObject.parseObject(string);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getBodyString(BufferedReader br) {
        String inputLine;
        String str = "";
        try {
            while ((inputLine = br.readLine()) != null) {
                str += inputLine;
            }
            br.close();
        } catch (IOException e) {
            log.error("IOException: " + e);
        }
        return str;
    }

    @Override
    public BufferedReader getReader()
            throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream()
            throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {

            @Override
            public int read()
                    throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener arg0) {
            }
        };
    }
}