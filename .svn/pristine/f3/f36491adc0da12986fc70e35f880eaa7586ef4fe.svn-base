package com.zdtech.platform.web.filter;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author lcheng
 * @date 2015/12/17
 */
public class AjaxReqSessionCheckFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletReq, ServletResponse servletRep, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)servletReq;
        HttpServletResponse rep = (HttpServletResponse) servletRep;
        HttpSession session = req.getSession(false);
        String key = "org.apache.shiro.subject.support.DefaultSubjectContext_AUTHENTICATED_SESSION_KEY";
        if (session==null || session.getAttribute(key)==null ){
            String reqType = req.getHeader("X-Requested-With");
            if (!StringUtils.isEmpty(reqType) && reqType.equalsIgnoreCase("XMLHttpRequest")){ //代表此为Ajax请求
                rep.setHeader("sessionStatus","timeout");
                rep.setContentType("application/x-json;charset=utf-8");
                rep.getWriter().write("{}");rep.setStatus(404);
                return;
            }
        }
        filterChain.doFilter(servletReq,servletRep);
    }

    @Override
    public void destroy() {

    }
}
