package com.zdtech.platform.simserver.server;

import com.zdtech.platform.framework.network.entity.*;
import com.zdtech.platform.simserver.net.AbstractServer;
import com.zdtech.platform.simserver.server.simulator.SimulatorServerFactory;
import com.zdtech.platform.simserver.server.simulator.SimulatorServerMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentMap;

/**
 * MessageProcessor
 *
 * @author panli
 * @date 2016/7/28
 */
public class MessageProcessor implements Runnable{
    private static Logger logger = LoggerFactory.getLogger(MessageProcessor.class);
    private Request msg;

    public MessageProcessor(Request msg) {
        this.msg = msg;
    }

    @Override
    public void run() {
        process();
    }
    private void process(){
        Message message = msg.getMessage();
        Message.MessageType msgType = message.getMsgType();
        switch (msgType){
            case SimInsStart:  //启动适配器
                startSimulator(msg.getMessage().getSimInsId(), msg.getMessage().getAdapterId());
                return;
            case SimInsStop:  //停止适配器
                stopSimulator(msg.getMessage().getSimInsId(), msg.getMessage().getAdapterId());
                return;
            case SimAutoTestMsg:  //自动化执行
                sendMessage(message);
                return;
            case SimAutoBatchStop:  //批量执行停止
                stopBatchExec((SimAutoBatchStopMessage) message);
                return;
            case SimSendMsg:  //来账手动应答
                sendMessage(message);
                return;
            case InternalTimeMsg:  //关联交易时发送下一笔报文前缓冲时间
                internalTime((InternalTimeMessage)message);
                return;
            default:
                logger.info("未识别的消息类型");
                return;
        }

    }

    private void startSimulator(Long simInsId, Long adapterId){
        Server server = SimulatorServerFactory.getInstance().createServer(simInsId, adapterId);
        if (server == null){
            logger.warn("适配器启动失败，适配器id：{},实例id：{}",adapterId,simInsId);
            return;
        }
        server.start();
        logger.info("适配器启动成功，适配器id：{},实例id：{}",adapterId,simInsId);
    }

    private void stopSimulator(Long simInsId, Long adapterId){
        Server server = SimulatorServerMgr.getSimulator(simInsId,adapterId);
        if (server != null){
            server.stop();
            logger.info("适配器停止，适配器id：{},实例id：{}",adapterId,simInsId);
        }else {
            logger.info("适配器停止失败，未找到适配器,实例id：{}",adapterId,simInsId);
        }
    }

    private void stopBatchExec(SimAutoBatchStopMessage message){
        Long simInsId = message.getSimInsId();
        Long adapterId = message.getAdapterId();
        Long batchId = message.getBatchId();
        Server server = SimulatorServerMgr.getSimulator(simInsId,adapterId);
        if (server != null){
            AbstractServer s = ((AbstractServer) server);
            if(s.getBatchId() != null && s.getBatchId().equals(batchId)){
                s.setBatchExecFlag(false);
            }
            logger.info("自动化批量执行停止，适配器id：{},实例id：{}",adapterId,simInsId);
        }else {
            logger.info("自动化批量执行停止失败，未找到自动化批量执行服务，适配器id：{},实例id：{}",adapterId,simInsId);
        }
    }

    private void internalTime(InternalTimeMessage message){
        Long simInsId = message.getSimInsId();
        Long adapterId = message.getAdapterId();
        int internalTime = message.getInternalTime();
        Server server = SimulatorServerMgr.getSimulator(simInsId,adapterId);
        if (server != null){
            ((AbstractServer) server).setInternalTime(internalTime);
            logger.info("关联交易时发送下一笔报文前缓冲时间设置成功，时间：{}，单位：毫秒", internalTime);
        }else {
            logger.info("关联交易时发送下一笔报文前缓冲时间设置失败，时间：{}，单位：毫秒", internalTime);
        }
    }

    private void sendMessage(Message msg){
        if (msg instanceof SimAutoTestMessage || msg instanceof SimSendMessage){
            Server server = SimulatorServerMgr.getSimulator(msg.getSimInsId(),msg.getAdapterId());
            if (server == null){
                logger.error("报文发送失败，未找到适配器");
                return;
            }
            server.sendMessage(msg);
            logger.info("找到适配器，执行报文发送任务");
        }
    }

}
