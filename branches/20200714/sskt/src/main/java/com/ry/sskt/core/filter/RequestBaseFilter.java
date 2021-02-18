package com.ry.sskt.core.filter;

import com.ry.sskt.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * 拦截request对象，并填充公共属性拦截器 <描述类的作用>
 *
 * @author 幸仁强
 * @ClassName: RequestBaseFilter
 * @date 2020年4月18日
 */
@Component
@WebFilter(filterName = "requestBaseFilter", urlPatterns = "/*")
@Slf4j
public class RequestBaseFilter implements Filter {

    //TODO  限流1万
    //private static RateLimiter limiter = RateLimiter.create(10000);
    @Autowired
    IUserService userService;

    public void destroy() {

    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException,
            ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            String method = request.getMethod();
            String url = request.getRequestURI();
            String contentType = request.getContentType();
            log.info("请求路径:" + url);
            if (!getWhiteList().contains(url) && "POST".equalsIgnoreCase(method) && (contentType == null || (!contentType.startsWith("multipart/form-data") && !contentType.equals("application/x-www-form-urlencoded")))) {
                ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(request, userService); // 一次请求request只能读取一次，所以需要重写request
                requestWrapper.setCharacterEncoding("UTF-8");
                chain.doFilter(requestWrapper, response);
            } else {
                chain.doFilter(request, response);
            }
        } catch (Exception e) {
            log.error("RequestBaseFilter拦截器异常，URL为：" + request.getRequestURI(), e);
            chain.doFilter(request, response);
        }
    }


    public void init(FilterConfig fConfig)
            throws ServletException {
    }

    private List<String> getWhiteList() {
        List<String> list = new LinkedList<>();
        return list;
    }
}
