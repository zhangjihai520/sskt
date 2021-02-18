/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.ry.sskt.controller;


import com.ry.sskt.core.utils.JwtUtil;
import com.ry.sskt.model.common.entity.User;
import com.ry.sskt.service.IUserService;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller公共组件
 */
public abstract class AbstractController {

    @Autowired
    IUserService userService;

    protected int getUserId(HttpServletRequest request) {
        Cookie[] cookie = request.getCookies();
        String token = "";
        if (null != cookie) {
            Cookie cook = Arrays.asList(cookie).stream().filter(switchEnum -> switchEnum.getName().equals("Authorization")).findFirst().orElse(null);
            if (null != cook && StringUtils.isNotBlank(cook.getValue())) {
                token = cook.getValue();
            }
        }
        if (token == "") {
            token = request.getHeader("Authorization");
        }
        return JwtUtil.getUserId(token);
    }

    protected User getUserInfo(HttpServletRequest request) {
        Cookie[] cookie = request.getCookies();
        String token = "";
        if (null != cookie) {
            Cookie cook = Arrays.asList(cookie).stream().filter(switchEnum -> switchEnum.getName().equals("Authorization")).findFirst().orElse(null);
            if (null != cook && StringUtils.isNotBlank(cook.getValue())) {
                token = cook.getValue();
            }
        }
        if (token == "") {
            token = request.getHeader("Authorization");
        }
        var userId = JwtUtil.getUserId(token);
        if (userId > 0) {
            return userService.getByKey(userId, true);
        }
        return null;
    }

    /**
     *      * 获取客户端请求参数中所有的信息
     *      * @param request
     *      * @return
     *      
     */
    protected Map<String, String> getAllRequestParam(HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);//如果字段的值为空，判断若值为空，则删除这个字段>
            }
        }
        return res;
    }
}