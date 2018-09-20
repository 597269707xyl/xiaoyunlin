package com.zdtech.platform.simserver.net;

import com.zdtech.platform.framework.entity.SimSystemInstance;
import com.zdtech.platform.framework.network.entity.Message;
import com.zdtech.platform.framework.network.entity.Request;
import com.zdtech.platform.framework.utils.SpringContextUtils;
import com.zdtech.platform.simserver.net.config.ServerConfig;
import com.zdtech.platform.simserver.server.Server;
import com.zdtech.platform.simserver.server.simulator.SimulatorServerMgr;
import com.zdtech.platform.simserver.service.SimInsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * AbstractServer
 *
 * @author panli
 * @date 2016/7/28
 */
public abstract class AbstractServer implements Server {
    private static Logger logger = LoggerFactory.getLogger(AbstractServer.class);
    protected volatile boolean closed = false;
    protected ServerConfig serverConfig;
    protected boolean batchExecFlag = true;  //自动批量执行是否停止
    protected int internalTime = 0; //毫秒
    protected Long batchId = null;

    private String getLocalIp(){
        String ip = "";
        try {
            InetAddress ia = null;
            ia = InetAddress.getLocalHost();
            ip = ia.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ip;
    }

    public AbstractServer(ServerConfig serverConfig){
        this.serverConfig = serverConfig;
    }
    @Override
    public void start() {
        if (this.safeCheck()){
            closed = false;
            SimInsService service = SpringContextUtils.getBean(SimInsService.class);
//            service.updateState(serverConfig.getSimInsId(),SimSystemInstance.SysInsState.Starting);
            init();
            if (doStart()){
//                service.updateSimState(serverConfig.getSimInsId(),
//                        SimSystemInstance.SysInsState.Started.ordinal(),SimSystemInstance.SysInsConnectStatus.Connected.ordinal(),getLocalIp());
                service.updateSimState(serverConfig.getAdapterId(), true);
                SimulatorServerMgr.addSimulator(serverConfig.getSimInsId(),serverConfig.getAdapterId(),this);
                logger.info("适配器启动成功");
            }else {
                stop();
                logger.info("适配器启动失败");
            }
        }else {
            logger.info("适配器启动失败，状态不正确");
        }
    }

    @Override
    public void stop() {
        closed = true;
        SimInsService service = SpringContextUtils.getBean(SimInsService.class);
//        service.updateState(serverConfig.getSimInsId(), SimSystemInstance.SysInsState.Finishing);
        boolean b = doStop();
        if (b){

//            SimulatorServerMgr.deleteSimulator(serverConfig.getProjectId(),serverConfig.getSimInsId(), serverConfig.getAdapterId());
            service.updateSimState(serverConfig.getAdapterId(), false);
            logger.info("适配器停止成功");
        }else {
            logger.warn("适配器停止失败");
        }
//        service.updateSimState(serverConfig.getSimInsId(),
//                SimSystemInstance.SysInsState.Finished.ordinal(),SimSystemInstance.SysInsConnectStatus.DisConnect.ordinal(),"");
        service.updateSimState(serverConfig.getAdapterId(), false);
    }

    @Override
    public void reStart() {
        if (closed){
            return;
        }
        doRestart();
    }

    @Override
    public void sendMessage(Object msg){
        if (msg == null || !(msg instanceof Message)){
            return;
        }
        doSendMessage((Message)msg);
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    private boolean safeCheck(){
        boolean ret = true;
        SimInsService service = (SimInsService)SpringContextUtils.getBean("simInsService");
        SimSystemInstance ssi = service.get(serverConfig.getSimInsId());
        if (ssi.getAdapter().getAdapterStatus()){
            ret = false;
        }
        return ret;
    }

    protected abstract void init();
    protected abstract boolean doStart();
    protected abstract boolean doStop();
    protected abstract boolean doRestart();
    protected abstract boolean doSendMessage(Message msg);

    public int getInternalTime() {
        return internalTime;
    }

    public void setInternalTime(int internalTime) {
        this.internalTime = internalTime;
    }

    public boolean isBatchExecFlag() {
        return batchExecFlag;
    }

    public void setBatchExecFlag(boolean batchExecFlag) {
        this.batchExecFlag = batchExecFlag;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }
}
