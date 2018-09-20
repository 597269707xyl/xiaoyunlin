package com.zdtech.platform.web.controller.push;

import com.zdtech.platform.framework.rbac.ShiroUser;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * WebSocketConfig
 *
 * @author panli
 * @date 2016/8/20
 */
@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(usecaseExecMonitorWebSocketHandler(),"execMonitorPush").addInterceptors(new HandshakeInterceptor() {
            @Override
            public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
                ServletServerHttpRequest sshr = (ServletServerHttpRequest)serverHttpRequest;
                HttpSession session = sshr.getServletRequest().getSession(false);
                Object o = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                SimplePrincipalCollection s = (SimplePrincipalCollection)o;
                ShiroUser u = (ShiroUser) s.getPrimaryPrincipal();
                map.put("shiroUser",u);
                String batchId = sshr.getServletRequest().getParameter("batchId");
                map.put("batchId",batchId);
                return true;
            }

            @Override
            public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

            }
        });
    }
    @Bean
    public UsecaseExecMonitorWebSocketHandler usecaseExecMonitorWebSocketHandler(){
        return new UsecaseExecMonitorWebSocketHandler();
    }
}
