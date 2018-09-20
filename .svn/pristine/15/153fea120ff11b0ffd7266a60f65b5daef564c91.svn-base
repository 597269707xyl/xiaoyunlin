package com.zdtech.platform.simserver.net.jms;

import com.zdtech.platform.framework.utils.SpringContextUtils;
import com.zdtech.platform.simserver.service.WLSimService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * TimeOutCheckThread
 *
 * @author panli
 * @date 2017/9/11
 */
public class TimeOutCheckThread extends Thread {
    private static Logger logger = LoggerFactory.getLogger(TimeOutCheckThread.class);

    private boolean isRunning;
    private WLSimService wlSimService;

    public TimeOutCheckThread() {
        this.isRunning = true;
        wlSimService = SpringContextUtils.getBean(WLSimService.class);
    }

    @Override
    public void run() {
        while (isRunning) {
            //寻找所有已发送-未应答的记录
            List<Long> idList = wlSimService.findTimeoutExecSendId();
            if (idList != null){
                for (Long id:idList){
                    wlSimService.updateTimeoutState(id);
                }
            }
            try {
                //logger.info("超时巡视......");
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void stopServer() {
        this.isRunning = false;
    }
}
