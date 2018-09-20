package com.zdtech.platform.log;

import org.slf4j.MDC;

import java.util.Map;

/**
 * Created by lcheng on 2015/5/11.
 */
public class LogContext {

    private static LogContext context = new LogContext();

    private LogContext() {
    }

    public static LogContext getInstance() {
        return context;
    }

    public void pushData(String userId,String userName, String clientIp, String url) {
        MDC.put("userId",userId);
        MDC.put("userName", userName);
        MDC.put("clientIp", clientIp);
        MDC.put("url", url);
    }

    public void clear() {
        MDC.clear();
    }
}
