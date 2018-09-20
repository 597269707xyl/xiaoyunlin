package com.zdtech.platform.simserver.server.simulator;

import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.utils.SpringContextUtils;
import com.zdtech.platform.simserver.net.config.ServerConfig;
import com.zdtech.platform.simserver.net.http.EpccHttpServer;
import com.zdtech.platform.simserver.net.jms.WLNewJmsServer;
import com.zdtech.platform.simserver.server.Server;
import com.zdtech.platform.simserver.service.SimInsService;
import com.zdtech.platform.simserver.service.WLSimService;
import com.zdtech.platform.simserver.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SimulatorServerFactory
 *
 * @author panli
 * @date 2016/7/28
 */
public class SimulatorServerFactory {
    private Logger logger = LoggerFactory.getLogger(SimulatorServerFactory.class);
    private static SimulatorServerFactory instance = new SimulatorServerFactory();

    private SimulatorServerFactory(){

    }
    public static SimulatorServerFactory getInstance(){
        return instance;
    }
    public Server createServer(Long simInsId,Long adapterId){
        Server server = null;
        SimInsService service = SpringContextUtils.getBean(SimInsService.class);
        SimSystemInstance ssi = service.get(simInsId);
        SimSystem ss = ssi.getSimSystem();
        String strProtocol = ss.getProtocol();
        ServerConfig serverConfig = new ServerConfig();
        Map<String,String> c = service.getSimInsConfig(adapterId);
        serverConfig.setSimParamsMap(c);
        if (strProtocol.equalsIgnoreCase(Constants.SIMULATOR_PROTOCOL_HTTPS)){
            serverConfig.setSimInsId(simInsId);
            serverConfig.setAdapterId(adapterId);
            SysAdapterHttp http = service.getHttp(adapterId);
            serverConfig.setInternalTime(http.getInternalTime());
            serverConfig.setRemoteUrl(http.getRemoteUrl());
            serverConfig.setLocalUrl(http.getLocalUrl());
            server = new EpccHttpServer(serverConfig);
            logger.info("启动EpccHttpServer");
        }else if(strProtocol.equalsIgnoreCase(Constants.SIMULATOR_PROTOCOL_MQ)) {
            List<SysAdapterQueue> queueList = service.getQueue(adapterId);
            serverConfig.setSimInsId(simInsId);
            serverConfig.setAdapterId(adapterId);
            SysAdapterMq mq = service.getMq(adapterId);
            serverConfig.setInternalTime(mq.getInternalTime());
            List<Map<String,String>> sendConfs = new ArrayList<>();
            List<Map<String,String>> recvConfs = new ArrayList<>();
            for(SysAdapterQueue queue : queueList){
                Map<String,String> map = new HashMap<>();
                map.put("mqIp",queue.getIp());
                map.put("mqPort",queue.getPort().toString());
                map.put("mqQueueMgr",queue.getQueueMgr());
                map.put("mqChannel",queue.getChannel());
                map.put("mqCcsid",queue.getCcsid().toString());
                map.put("mqUserId",queue.getUser());
                map.put("mqPassword",queue.getPassword());
                map.put("mqQueue",queue.getQueue());
                if("send".equals(queue.getType())){
                    sendConfs.add(map);
                }
                if("recv".equals(queue.getType())){
                    recvConfs.add(map);
                }
            }
            serverConfig.setMqSendConfList(sendConfs);
            serverConfig.setMqRecvConfList(recvConfs);
            server = new WLNewJmsServer(serverConfig);
            logger.info("启动WLNewJmsServer");
        } else {
            logger.info("启动TCP,暂无该服务!");
        }
        return server;
    }
}
