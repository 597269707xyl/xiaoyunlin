package com.zdtech.platform.web.interceptor;


import com.zdtech.platform.framework.rbac.ShiroUser;
import com.zdtech.platform.framework.service.UserService;
import com.zdtech.platform.framework.utils.SysUtils;
import com.zdtech.platform.log.LogContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class LogInterceptor extends HandlerInterceptorAdapter {

    private static Logger log = LoggerFactory.getLogger(LogInterceptor.class);
    private final UserService userService =  SysUtils.getBean("userService", UserService.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("Entering LogInterceptor preHandle......");
        String clientIp = request.getRemoteAddr();
        String url = request.getRequestURI();
        String param = request.getQueryString();
        if(StringUtils.isNotEmpty(param)){
            url = url + "?" +param;
        }
        String userName = "";
        ShiroUser user = userService.getCurrentUser();
        if (user!=null){
            userName = user.getUserName();
            LogContext.getInstance().pushData(user.getId().toString(),userName,clientIp,url);
        }else{
            LogContext.getInstance().pushData("-1","",clientIp,url);
        }
        log.debug("LogInterceptor preHandle got {} {} {}", new String[]{userName, clientIp, url});
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        LogContext.getInstance().clear();
        log.debug("LogInterceptor postHandle clear the LogContext.");
    }
}
