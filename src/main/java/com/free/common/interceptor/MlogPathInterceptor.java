package com.free.common.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 海加尔金鹰 www.hjljy.cn
 **/
@Slf4j
public class MlogPathInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求的路径
        String servletPath = request.getServletPath();
        log.info("request url " + servletPath);

        return true;
    }
}
