package com.zdtech.platform.thread;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zdtech.platform.framework.entity.NxyFuncUsecaseExec;
import com.zdtech.platform.framework.entity.NxyFuncUsecaseExecSend;
import com.zdtech.platform.framework.entity.NxyUsecaseExecBatch;
import com.zdtech.platform.framework.repository.NxyFuncUsecaseDataDao;
import com.zdtech.platform.framework.repository.NxyFuncUsecaseExecDao;
import com.zdtech.platform.framework.repository.NxyFuncUsecaseExecSendDao;
import com.zdtech.platform.framework.repository.NxyUsecaseExecBatchDao;
import com.zdtech.platform.framework.service.SysCodeService;
import com.zdtech.platform.framework.utils.SpringContextUtils;
import com.zdtech.platform.service.funcexec.FuncUsecaseService;
import com.zdtech.platform.web.controller.funcexec.FuncUsecaseController;
import com.zdtech.platform.web.controller.push.UsecaseExecMonitorWebSocketHandler;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.socket.TextMessage;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by yjli on 2017/9/13.
 */
public class BatchExecResultThread extends Thread {
    private Long batchId;
    private int count;
    private FuncUsecaseService funcUsecaseService;
    protected boolean isStop = false;

    public BatchExecResultThread(Long batchId, int count, FuncUsecaseService funcUsecaseService){
        this.batchId = batchId;
        this.count = count;
        this.funcUsecaseService = funcUsecaseService;
    }

    @Override
    public void run(){
        FuncUsecaseController.batchMap.put(batchId, this);
        long startTime = System.currentTimeMillis();
        while (true){
            System.out.println("isStop:" + isStop);
            if(isStop)break;
            long endTime = System.currentTimeMillis();
            if(funcUsecaseService.batch(batchId, count)) break;
            try {
                if(endTime - startTime > 24*60*60*1000) break;
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean stop) {
        isStop = stop;
    }
}
