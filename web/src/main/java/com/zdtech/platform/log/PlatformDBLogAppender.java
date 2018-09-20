package com.zdtech.platform.log;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.zdtech.platform.framework.entity.SysLog;
import com.zdtech.platform.framework.repository.SysLogDao;
import com.zdtech.platform.framework.utils.SysUtils;
import org.slf4j.MDC;

/**
 * Created by lcheng on 2015/5/11.
 * 日志的数据库Appender
 */
public class PlatformDBLogAppender extends AppenderBase<LoggingEvent> {
    private static String[] LOG_TYPE = {"S", "A", "M","C","P","T"};

    @Override
    protected void append(LoggingEvent event) {
        String level = event.getLevel().levelStr;
        String userId = MDC.get("userId");
        String userName = MDC.get("userName");
        String clientIp = MDC.get("clientIp");
        String url = MDC.get("url");

        String logContent = event.getFormattedMessage();
        if (logContent.startsWith("@")) {
            String[] content = logContent.substring(1).split("\\|");
            boolean result = true;
            String conStr = null, type = null;
            if (content.length == 3) {
                type = content[0];
                result = Boolean.valueOf(content[1]);
                conStr = content[2];
            } else if (content.length == 2) {
                type = getLogType(url);

                result = Boolean.valueOf(content[0]);
                conStr = content[1];
            } else if (content.length == 1) {
                type = getLogType(url);
                conStr = content[0];
            }
            //log入库，当前直接入库，如有性能问题，考虑定时批量入库
            SysLog sysLog = new SysLog(Long.valueOf(userId), userName, clientIp, url, level,type, conStr, result);
            //当前直接入库，如有性能问题，考虑定时批量入库
            SysUtils.getBean("sysLogDao", SysLogDao.class).save(sysLog);
        }

    }

    private String getLogType(String url) {
        String type = LOG_TYPE[1];
        if (url.startsWith("/sys")) {
            type = LOG_TYPE[0];
        } else if (url.startsWith("/test")) {
            type = LOG_TYPE[1];
        } else if (url.startsWith("/know")) {
            type = LOG_TYPE[2];
        }
        return type;
    }
}
