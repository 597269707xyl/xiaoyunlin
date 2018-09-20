package com.zdtech.platform.simserver;


import com.zdtech.platform.simserver.server.NettyServer;
import com.zdtech.platform.simserver.server.Server;
import com.zdtech.platform.simserver.server.simulator.SimulatorServerMgr;
import com.zdtech.platform.simserver.service.SimInsService;
import com.zdtech.platform.simserver.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * SimServer
 *
 * @author panli
 * @date 2016/7/25
 */
@Component
public class SimServer {
    private static Logger logger = LoggerFactory.getLogger(SimServer.class);

    @Autowired
    private NettyServer server;

    public String getLocalIp(){
        String ip = "";
        try {
            InetAddress ia  = InetAddress.getLocalHost();
            ip = ia.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ip;
    }

    public static void main(String [] args){
        ApplicationContext context = new ClassPathXmlApplicationContext(Constants.SPRING_CONTEXT_FILE);
        SimServer simServer = context.getBean(SimServer.class);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                simServer.server.stop();
                logger.info("服务停止");
                Map<String,Server> map = SimulatorServerMgr.getSimulators();
                for (String s:map.keySet()){
                    map.get(s).stop();
                }
                logger.info("服务停止，模拟器状态置位");
            }
        }));
        String ip = simServer.getLocalIp();
        SimInsService service = context.getBean(SimInsService.class);
        service.resetSimInsStatus(ip);
        simServer.server.start();
    }

}

