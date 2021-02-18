package com.ry.sskt.core.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ry.sskt.core.annotation.AllowUserType;
import com.ry.sskt.core.annotation.AlwaysAccessible;
import com.ry.sskt.core.utils.JwtUtil;
import com.ry.sskt.model.common.constant.UserTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 定义Token过滤器
 *
 * @author 幸仁强
 * @ClassName: AllowUserTypeInterceptor
 * @date 2018年5月26日
 */

public class AllowUserTypeInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        // 将handler强转为HandlerMethod, 前面已经证实这个handler就是HandlerMethod
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 如果请求不来源于我们的controller，则直接返回true
        if (handlerMethod.getBeanType().getName().indexOf("com.ry.sskt") < 0 || handlerMethod.getBeanType().getName().indexOf(".controller.") < 0) {
            return true;
        }
        // 从方法处理器中获取出要调用的方法
        Method method = handlerMethod.getMethod();
        AlwaysAccessible alwaysAccessible = handlerMethod.getBeanType().getAnnotation(AlwaysAccessible.class);
        if (null == alwaysAccessible) {
            alwaysAccessible = method.getAnnotation(AlwaysAccessible.class);
        }
        if (null != alwaysAccessible && alwaysAccessible.value()) {
            return true;
        }
        AllowUserType allowUserType = handlerMethod.getBeanType().getAnnotation(AllowUserType.class);
        // 如果类头上没有注解,则判断方法头上是否有注解
        if (null == allowUserType) {
            allowUserType = method.getAnnotation(AllowUserType.class);
        }
        //取到了注解
        if (null != allowUserType) {
            if (allowUserType.userType().equals(UserTypeEnum.All)) {
                return true;
            }
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            Cookie[] cookie = httpRequest.getCookies();
            String token = "";
            if (null != cookie) {
                Cookie cook = Arrays.asList(cookie).stream().filter(switchEnum -> switchEnum.getName().equals("Authorization")).findFirst().orElse(null);
                if (null != cook && StringUtils.isNotBlank(cook.getValue())) {
                    token = cook.getValue();
                }
            }
            if (token == "") {
                token = httpRequest.getHeader("Authorization");
            }
            if (StringUtils.isNotBlank(token) && null != JwtUtil.parseJWT(token)) {
                int userType = JwtUtil.getUserType(token);
                if (userType == allowUserType.userType().getCode() || userType == UserTypeEnum.All.getCode()) {
                    return true;
                }
            }
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.setContentType("application/json; charset=utf-8");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ObjectMapper mapper = new ObjectMapper();
            httpResponse.getWriter().write(mapper.writeValueAsString("用户无此角色权限"));
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView
            modelAndView)
            throws Exception {
        // System.out.println("postHandler");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception
            ex)
            throws Exception {
        // System.out.println("afterCompletion");
    }
}
