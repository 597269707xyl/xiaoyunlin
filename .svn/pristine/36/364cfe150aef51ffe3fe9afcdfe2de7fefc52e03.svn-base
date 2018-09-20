package com.zdtech.platform.simserver.net.http;

import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.network.entity.Message;
import com.zdtech.platform.framework.network.entity.SimAutoTestMessage;
import com.zdtech.platform.framework.network.entity.SimSendMessage;
import com.zdtech.platform.framework.utils.SpringContextUtils;
import com.zdtech.platform.simserver.net.AbstractServer;
import com.zdtech.platform.simserver.net.config.ServerConfig;
import com.zdtech.platform.simserver.service.EpccService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.nio.bootstrap.HttpServer;
import org.apache.http.impl.nio.bootstrap.ServerBootstrap;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * EpccHttpServer
 *
 * @author panli
 * @date 2017/11/11
 */
public class EpccHttpServer extends AbstractServer {
    private Logger logger = LoggerFactory.getLogger(EpccHttpServer.class);

    private HttpServer httpServer;
    private ExecutorService executor;
    private EpccService wlService;
    private boolean isSend = false;  //判断是否发送下一笔报文，当上一笔报文组装完成发送前设置可以发送下一笔报文，不必等到这一笔交易收到应答报文再发下一笔

    public EpccHttpServer(ServerConfig serverConfig) {
        super(serverConfig);
    }

    @Override
    protected void init() {
        IOReactorConfig config = IOReactorConfig.custom()
                .setSoTimeout(15000)
                .setTcpNoDelay(true)
                .build();
        String localUrl = serverConfig.getLocalUrl();
        URL url = null;
        InetAddress localAddress = null;
        try {
            url = new URL(localUrl);
            localAddress = InetAddress.getByName(url.getHost());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        int port = url.getPort();
        String ctx = url.getPath();
        try {
            if (localUrl.startsWith("http:")){
                httpServer = ServerBootstrap.bootstrap()
                        .setIOReactorConfig(config)
                        .setLocalAddress(localAddress)
                        .setListenerPort(port)
                        .setConnectionReuseStrategy(new DefaultConnectionReuseStrategy())
                        .registerHandler(ctx,new EpccRequestHandler(serverConfig))
                        .create();
            }
            if (localUrl.startsWith("https:")){
                URL ksUrl = EpccService.class.getResource("/my.keystore");
                System.out.println(ksUrl.getPath());
                SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(ksUrl,"123456".toCharArray(),"123456".toCharArray()).build();
                httpServer = ServerBootstrap.bootstrap()
                        .setSslContext(sslContext)
                        .setIOReactorConfig(config)
                        .setLocalAddress(localAddress)
                        .setListenerPort(port)
                        .setConnectionReuseStrategy(new DefaultConnectionReuseStrategy())
                        .registerHandler(ctx,new EpccRequestHandler(serverConfig))
                        .create();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        executor = Executors.newFixedThreadPool(10);
        wlService = SpringContextUtils.getBean(EpccService.class);
    }

    @Override
    protected boolean doStart() {
        try {
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected boolean doStop() {
        httpServer.shutdown(5, TimeUnit.SECONDS);
        HttpSynClient.close();
        return true;
    }

    @Override
    protected boolean doRestart() {
        return false;
    }

    @Override
    protected boolean doSendMessage(Message msg) {
        if (msg instanceof SimSendMessage){
        }else if (msg instanceof SimAutoTestMessage){
            autoSendMessage((SimAutoTestMessage) msg);
        }
        return true;
    }

    private void autoSendMessage(SimAutoTestMessage message){
        List<Long> ucIds = message.getMessageIdList();
        Long batchId = message.getBatchId();
        if (ucIds == null || ucIds.size() < 1){
            logger.warn("批量执行用例id为空");
            return;
        }
        Long sendInternal = message.getSendInternal();
        for (Long ucId:ucIds){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    //1.获取用例要发送的报文数据（用例第一步报文）
                    NxyFuncUsecaseData ucData = wlService.getUseCaseData(ucId,1);
                    doAutoSend(ucData,null,batchId);
                }
            };
            while (isSend){
                logger.info("。。。。。。。。。。。。。。。。。。。");
            }
            //发送间隔
            if (sendInternal > 0){
                try {
                    Thread.sleep(sendInternal);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            executor.submit(runnable);
            isSend = true;
        }
    }

    private void doAutoSend(NxyFuncUsecaseData ucData,Long execId,Long batchId){
        if (ucData == null){
            return;
        }
        String standard = ucData.getSimMessage().getStandard();
        if("zfb".equals(standard) || "cft".equals(standard)){ //支付宝或者财付通
            String strMsg = wlService.buildZTAutoSendMsg(ucData,execId,batchId,serverConfig.getSimParamsMap());
            if(strMsg.indexOf("sendFail") != -1){
                isSend = false;
                String error = String.format("报文未执行。错误信息：%s", "不支持的报文案例或者未提供正确的案例信息!");
                wlService.saveZTAutoExecData(batchId,execId,ucData,strMsg,"noexec",error);
                logger.error("报文未执行，用例数据id[{}],错误[{}]",ucData.getId(),error);
                return;
            }
            logger.info("自动化发送报文：{}",strMsg);
            try {
                //发送报文
                String execId_sendId = wlService.saveZTSendData(batchId,execId,ucData,strMsg,"execsucc","报文发送成功。");
                isSend = false;
                String respMsg = HttpSynClient.post(serverConfig.getRemoteUrl(),strMsg, ucData.getSimMessage().getMsgType(), serverConfig.getSimParamsMap());
                String descript = "",result = "";
                if (respMsg.equalsIgnoreCase("resptimeout")){
                    result = "resptimeout";
                    descript = "应答报文超时。";
                    respMsg = "";
                }
                else if (respMsg.equalsIgnoreCase("execerror")){
                    result = "execerror";
                    descript = "报文发送失败，请检查http/https通信参数配置。";
                    respMsg = "";
                }else {
                    result = "execsucc";
                    descript = "报文发送成功。";
                }
                //保存执行数据
                String[] execId_sendIdArr = execId_sendId.split(":");
                execId = Long.parseLong(execId_sendIdArr[0]);
                Long sendId = Long.parseLong(execId_sendIdArr[1]);
                String next = wlService.saveZTAutoExecData(serverConfig.getSimInsId(),execId,ucData,strMsg,respMsg,result,descript, sendId);
                //有下一步报文
                if (StringUtils.isNotEmpty(next)){
                    String[] arr = next.split("_");
                    Long nextExecId = Long.parseLong(arr[0]);
                    Long nextDataId = Long.parseLong(arr[1]);
                    NxyFuncUsecaseData nextUcData = wlService.getUseCaseData(nextDataId);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    doAutoSend(nextUcData,nextExecId,null);
                }
            } catch (Exception e) {
                isSend = false;
                e.printStackTrace();
                String error = String.format("报文发送失败，请检查http/https通信参数配置。错误信息：%s",e.getMessage());
                wlService.saveZTAutoExecData(batchId,execId,ucData,strMsg,"execerror",error);
                logger.error("报文发送失败，用例数据id[{}],错误[{}]",ucData.getId(),e.getMessage());
            }
        } else { //第三方机构
            String strMsg = wlService.buildAutoSendMsg(ucData,execId,batchId,serverConfig.getSimParamsMap());
            if(strMsg.indexOf("sendFail") != -1){
                isSend = false;
                String error = String.format("报文未执行。错误信息：%s", "不支持的报文案例或者未提供正确的案例信息!");
                wlService.saveAutoExecData(batchId,execId,ucData,strMsg,"noexec",error);
                logger.error("报文未执行，用例数据id[{}],错误[{}]",ucData.getId(),error);
                return;
            }
            logger.info("自动化发送报文：{}",strMsg);
            try {
                //发送报文
                String dstInstgId = wlService.getDstInstgId(serverConfig.getSimInsId());
                String execId_sendId = wlService.saveSendData(batchId,execId,ucData,strMsg,"execsucc","报文发送成功。");
                isSend = false;
                String respMsg = HttpSynClient.post(serverConfig.getRemoteUrl(),strMsg, dstInstgId);
                String descript = "",result = "";
                if (respMsg.equalsIgnoreCase("resptimeout")){
                    result = "resptimeout";
                    descript = "应答报文超时。";
                    respMsg = "";
                }
                else if (respMsg.equalsIgnoreCase("execerror")){
                    result = "execerror";
                    descript = "报文发送失败，请检查http/https通信参数配置。";
                    respMsg = "";
                }else {
                    result = "execsucc";
                    descript = "报文发送成功。";
                }
                //保存执行数据
                String[] execId_sendIdArr = execId_sendId.split(":");
                execId = Long.parseLong(execId_sendIdArr[0]);
                Long sendId = Long.parseLong(execId_sendIdArr[1]);
                String next = wlService.saveAutoExecData(serverConfig.getSimInsId(),execId,ucData,strMsg,respMsg,result,descript, sendId);
                //有下一步报文
                if (StringUtils.isNotEmpty(next)){
                    String[] arr = next.split("_");
                    Long nextExecId = Long.parseLong(arr[0]);
                    Long nextDataId = Long.parseLong(arr[1]);
                    NxyFuncUsecaseData nextUcData = wlService.getUseCaseData(nextDataId);
                    doAutoSend(nextUcData,nextExecId,null);
                }
            } catch (Exception e) {
                isSend = false;
                e.printStackTrace();
                String error = String.format("报文发送失败，请检查http/https通信参数配置。错误信息：%s",e.getMessage());
                wlService.saveAutoExecData(batchId,execId,ucData,strMsg,"execerror",error);
                logger.error("报文发送失败，用例数据id[{}],错误[{}]",ucData.getId(),e.getMessage());
            }
        }
    }
}
