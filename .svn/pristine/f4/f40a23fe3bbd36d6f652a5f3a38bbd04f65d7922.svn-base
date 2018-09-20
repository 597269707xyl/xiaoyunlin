package com.zdtech.platform.simserver.net.jms;

import com.zdtech.platform.framework.entity.NxyFuncUsecaseData;
import com.zdtech.platform.framework.entity.SimSysInsMessage;
import com.zdtech.platform.framework.entity.SimSysInsMessageField;
import com.zdtech.platform.framework.network.entity.Message;
import com.zdtech.platform.framework.network.entity.SimAutoTestMessage;
import com.zdtech.platform.framework.network.entity.SimSendMessage;
import com.zdtech.platform.framework.utils.SpringContextUtils;
import com.zdtech.platform.framework.utils.XMLConverter;
import com.zdtech.platform.framework.utils.XmlDocHelper;
import com.zdtech.platform.simserver.net.AbstractServer;
import com.zdtech.platform.simserver.net.config.ServerConfig;
import com.zdtech.platform.simserver.service.WLSimService;
import com.zdtech.platform.simserver.utils.Constants;
import com.zdtech.platform.simserver.jar.JmsTemplateUtil;
import com.zdtech.platform.simserver.utils.reflect.ReflectWraper;
import javafx.util.Pair;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * WLNewJmsServer
 *
 * @author panli
 * @date 2017/9/6
 */
public class WLNewJmsServer extends AbstractServer {
    private static Logger logger = LoggerFactory.getLogger(WLNewJmsServer.class);
    private List<Pair<JmsTemplate,String>> sendTemplateList = new ArrayList<>();
    private List<Pair<JmsTemplate,String>> recvTemplateList = new ArrayList<>();
    private ExecutorService executor;
    private JmsMessageConverter jmsMessageConverter;
    private String msgDecodeCharset;
    //收到应答处理线程数
    private Integer threadCount = 20;
    //自动化发送线程数
    private Integer autoSendThreadCount = 20;
    private WLSimService wlSimService;
    private TimeOutCheckThread thread;
    private ExecutorService autoSendExecutor;

    public WLNewJmsServer(ServerConfig serverConfig) {
        super(serverConfig);
        wlSimService = SpringContextUtils.getBean(WLSimService.class);
    }

    @Override
    protected void init() {
        if (StringUtils.isEmpty(serverConfig.getUserId())||StringUtils.isEmpty(serverConfig.getPassWord())){
            List<Map<String,String>> sendMqConfs = serverConfig.getMqSendConfList();
            List<Map<String,String>> recvMqConfs = serverConfig.getMqRecvConfList();
            for (Map<String,String> map:sendMqConfs){
                JmsTemplate template = JmsTemplateUtil.jmsTemplate(map.get("mqIp"),
                        Integer.parseInt(map.get("mqPort")),map.get("mqQueueMgr"),
                        map.get("mqChannel"),Integer.parseInt(map.get("mqCcsid")));
                sendTemplateList.add(new Pair<>(template,map.get("mqQueue")));
            }
            for (Map<String,String> map:recvMqConfs){
                JmsTemplate template = JmsTemplateUtil.jmsTemplate(map.get("mqIp"),
                        Integer.parseInt(map.get("mqPort")),map.get("mqQueueMgr"),
                        map.get("mqChannel"),Integer.parseInt(map.get("mqCcsid")));
                recvTemplateList.add(new Pair<>(template,map.get("mqQueue")));
            }
        }else {
            List<Map<String,String>> sendMqConfs = serverConfig.getMqSendConfList();
            List<Map<String,String>> recvMqConfs = serverConfig.getMqRecvConfList();
            for (Map<String,String> map:sendMqConfs){
                JmsTemplate template = JmsTemplateUtil.jmsTemplate(map.get("mqIp"),
                        Integer.parseInt(map.get("mqPort")),map.get("mqQueueMgr"),
                        map.get("mqChannel"),Integer.parseInt(map.get("mqCcsid")),
                        map.get("mqUserId"),map.get("mqPassword"));
                sendTemplateList.add(new Pair<>(template,map.get("mqQueue")));
            }
            for (Map<String,String> map:recvMqConfs){
                JmsTemplate template = JmsTemplateUtil.jmsTemplate(map.get("mqIp"),
                        Integer.parseInt(map.get("mqPort")),map.get("mqQueueMgr"),
                        map.get("mqChannel"),Integer.parseInt(map.get("mqCcsid")),
                        map.get("mqUserId"),map.get("mqPassword"));
                recvTemplateList.add(new Pair<>(template,map.get("mqQueue")));
            }
        }
        this.internalTime = serverConfig.getInternalTime();
        executor = Executors.newFixedThreadPool(threadCount);
        jmsMessageConverter = new JmsMessageConverter();
        msgDecodeCharset = "UTF8";
        thread = new TimeOutCheckThread();
        autoSendExecutor = Executors.newFixedThreadPool(autoSendThreadCount);
    }

    @Override
    protected boolean doStart() {
        Thread loopThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!closed) {
                    for (Pair<JmsTemplate,String> pair:recvTemplateList){
                        JmsTemplate template = pair.getKey();
                        String queuestr = pair.getValue();
                        String[] queues = queuestr.split(",");
                        for (String queue:queues){
                            try {
                                javax.jms.Message msg = template.receive(queue);
                                if (msg != null){
                                    process(msg,System.currentTimeMillis());
                                    logger.info("从{}队列收到消息：{}",serverConfig.getRecvQueue(),msg.toString());
                                }

                            }catch (Exception e){
                                logger.error("从队列收消息异常：{}",e.getMessage());
                                logger.info("尝试重连");
                                ((CachingConnectionFactory)template.getConnectionFactory()).resetConnection();
                            }
                        }
                    }
                }
            }
        });
        loopThread.start();
        thread.start();
        return true;
    }

    @Override
    protected boolean doStop() {
        if (executor != null){
            executor.shutdown();
        }
        if (autoSendExecutor != null){
            autoSendExecutor.shutdown();
        }
        if (thread != null){
            thread.stopServer();
        }
        return true;
    }

    @Override
    protected boolean doRestart() {
        return true;
    }

    @Override
    protected boolean doSendMessage(Message msg) {
        if (msg instanceof SimAutoTestMessage){
            SimAutoTestMessage message = (SimAutoTestMessage)msg;
            this.batchId = message.getBatchId();
            processAutoSend(message);
        }
        if (msg instanceof SimSendMessage){
            SimSendMessage message = (SimSendMessage)msg;
            processSend(message);
        }
        return true;
    }
    //来账手动应答
    private void processSend(SimSendMessage message){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Long caseSendId = message.getCaseSendId();
                if (caseSendId == null){
                    logger.warn("手动应答id为空");
                    return;
                }
                String arr[] = wlSimService.getCaseSendMsg(caseSendId, message.getSimInsId());
                if(arr != null){
                    String msg = arr[1];
                    Long msgId = Long.parseLong(arr[0]);
                    msg = wlSimService.signMsg(msg,serverConfig, msgId);
                    logger.info("手动应答报文报文：{}",msg);
                    Random random = new Random();
                    int index = random.nextInt(sendTemplateList.size());
                    String sendQueue = sendTemplateList.get(index).getValue();
                    JmsTemplate jmsTemplate = sendTemplateList.get(index).getKey();
                    jmsTemplate.convertAndSend(sendQueue,msg);
                    wlSimService.saveSendMsg(msgId, msg, caseSendId, 2);
                }
            }
        };
        autoSendExecutor.submit(runnable);
    }



    protected void process(javax.jms.Message msg,Long timeStamp) {
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                String strMsg = null;
                try {
                    Object oMsg = jmsMessageConverter.fromMessage(msg);
                    if (oMsg instanceof String) {
                        logger.info("收到jms消息类型：String");
                        strMsg = (String) oMsg;
                    } else if (oMsg instanceof byte[]) {
                        logger.info("收到jms消息类型：byte[]");
                        strMsg = new String((byte[]) oMsg, msgDecodeCharset);
                    }
                    if(StringUtils.isNotEmpty(strMsg)) {
                        logger.info(String.format("收到jms消息：%s", strMsg));
                        //获取报文模板id
                        Long msgId = wlSimService.messageId(serverConfig.getSimInsId(),strMsg, serverConfig.getAdapterId());
                        if (msgId == null){
                            logger.error("收到报文，找不到相应的报文模板");
                            return;
                        }
                        //判断是请求还是应答
                        boolean isReq = wlSimService.isRequest(msgId);
                        logger.info("判断是请求还是应答：{}", isReq);
                        if (isReq){
                            //保存请求报文，返回主键
                            strMsg = XMLConverter.xmlStrByAssemble(strMsg);
                            String[] arr = wlSimService.saveReqMsgData(msgId,strMsg);
                            if (arr == null){
                                //如果是300报文没有获取到应答报文，则自动冲正
                                Document doc = XmlDocHelper.getXmlFromStr(strMsg);
                                String msgCode = XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/msgCd");
                                if("NPS.300.001.01".equals(msgCode)){
                                    arr = wlSimService.get301Xml(doc, msgId, serverConfig.getAdapterId());
                                    if(arr != null){
                                        logger.info("如果是300报文没有获取到应答报文，则自动冲正.");
                                        String respMsg = arr[1];
                                        Long repId = Long.parseLong(arr[0]);
                                        respMsg = wlSimService.signMsg(respMsg,serverConfig, repId);
                                        logger.info("自动化应答报文：{}",respMsg);
                                        Random random = new Random();
                                        int index = random.nextInt(sendTemplateList.size());
                                        String sendQueue = sendTemplateList.get(index).getValue();
                                        JmsTemplate jmsTemplate = sendTemplateList.get(index).getKey();
                                        jmsTemplate.convertAndSend(sendQueue, respMsg);
                                    }
                                }
                                logger.error("应答错误，获取应答报文为空");
                                return;
                            }
                            Long recvId = Long.parseLong(arr[1]);
                            String[] arr1 = wlSimService.getRespMsg(msgId, arr[0], strMsg);
                            Long caseSendId = wlSimService.saveCaseSendMsg(recvId, serverConfig);
                            if (arr1 == null){
                                //如果是300报文没有获取到应答报文，则自动冲正
                                Document doc = XmlDocHelper.getXmlFromStr(strMsg);
                                String msgCode = XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/msgCd");
                                if("NPS.300.001.01".equals(msgCode)){
                                    arr = wlSimService.get301Xml(doc, msgId, serverConfig.getAdapterId());
                                    if(arr != null){
                                        logger.info("如果是300报文没有获取到应答报文，则自动冲正.");
                                        String respMsg = arr[1];
                                        Long repId = Long.parseLong(arr[0]);
                                        respMsg = wlSimService.signMsg(respMsg,serverConfig, repId);
                                        logger.info("自动化应答报文：{}",respMsg);
                                        Random random = new Random();
                                        int index = random.nextInt(sendTemplateList.size());
                                        String sendQueue = sendTemplateList.get(index).getValue();
                                        JmsTemplate jmsTemplate = sendTemplateList.get(index).getKey();
                                        jmsTemplate.convertAndSend(sendQueue, respMsg);
                                    }
                                }
                                logger.error("应答错误，应答报文为空，reqMsgId：{}",msgId);
                                return;
                            }
                            String respMsg = arr1[0];
                            //组装报文
                            Long repId = Long.parseLong(arr1[1]);
                            respMsg = buildAutoSendMsg(repId, respMsg, serverConfig.getSimInsId(), serverConfig.getAdapterId());
                            //更改报文业务相关数据
                            Long ucDataId = Long.parseLong(arr1[2]);
                            respMsg = wlSimService.buildAutoSendBussMsg(ucDataId,respMsg, strMsg, serverConfig.getSimInsId(), serverConfig.getAdapterId());
                            boolean responseModel = wlSimService.getRespModel(serverConfig.getAdapterId());
                            logger.info("自动应答：true,手动应答：false，responseModel：{}", responseModel);
                            if(responseModel) {
                                //2.加签、加密等
                                respMsg = wlSimService.signMsg(respMsg, serverConfig, repId);
                                logger.info("自动化应答报文：{}", respMsg);
                                Random random = new Random();
                                int index = random.nextInt(sendTemplateList.size());
                                String sendQueue = sendTemplateList.get(index).getValue();
                                JmsTemplate jmsTemplate = sendTemplateList.get(index).getKey();
                                jmsTemplate.convertAndSend(sendQueue, respMsg);
                                wlSimService.saveSendMsg(repId, respMsg, caseSendId, 1);
                            } else {
                                wlSimService.saveSendMsg(repId, respMsg, caseSendId, 3);
                            }
                        }else {
                            //保存数据、用例执行结果
                            String result = wlSimService.saveRecvMsgData(msgId,strMsg,timeStamp);
                            //有下一步
                            if (result != null){
                                String[] arr = result.split("_");
                                Long execId = Long.parseLong(arr[0]);
                                Long dataId = Long.parseLong(arr[1]);
                                NxyFuncUsecaseData ucData = wlSimService.getUseCaseData(dataId);
                                logger.info("关联交易下一笔交易缓冲时间：{}（毫秒）", internalTime);
                                if (internalTime > 0){
                                    try {
                                        Thread.sleep(internalTime); //休息1秒再发送下一笔关联交易
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                doAutoSend(ucData,execId,null);
                            }
                            Document doc = XmlDocHelper.getXmlFromStr(strMsg);
                            String msgCode = XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/msgCd");
                            if("NPS.900.001.01".equals(msgCode) || "NPS.930.001.01".equals(msgCode)){
                                String[] arr = wlSimService.get990Xml(doc, msgId);
                                if(arr != null && arr.length == 2){
                                    String msg990 = arr[1];
                                    Long simMsgId = Long.parseLong(arr[0]);
                                    msg990 = wlSimService.signMsg(msg990,serverConfig, simMsgId);
                                    logger.info("自动化发送990报文：{}",msg990);
                                    Random random = new Random();
                                    int index = random.nextInt(sendTemplateList.size());
                                    String sendQueue = sendTemplateList.get(index).getValue();
                                    JmsTemplate sendTemplate = sendTemplateList.get(index).getKey();
                                    sendTemplate.convertAndSend(sendQueue,msg990);
                                }
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    logger.error("收到报文：{}，处理出错：{}",strMsg,e.getMessage());
                }
            }
        };
        executor.submit(runnable);
    }

    private void processAutoSend(SimAutoTestMessage message){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                List<Long> ucIds = message.getMessageIdList();
                Long batchId = message.getBatchId();
                if (ucIds == null || ucIds.size() < 1){
                    logger.warn("批量执行用例id为空");
                    return;
                }
                Long sendInternal = message.getSendInternal();
                for (Long ucId:ucIds){
                    if(!batchExecFlag){
                        logger.warn("停止自动批量执行。。。");
                        batchExecFlag = true;
                        break;   //停止自动批量执行
                    }
                    //1.获取用例要发送的报文数据（用例第一步报文）
                    NxyFuncUsecaseData ucData = wlSimService.getUseCaseData(ucId,1);
                    doAutoSend(ucData,null,batchId);
                    //发送间隔
                    if (sendInternal > 0){
                        try {
                            Thread.sleep(sendInternal);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        autoSendExecutor.submit(runnable);
    }
    private void doAutoSend(NxyFuncUsecaseData ucData,Long execId,Long batchId){
        if (ucData == null){
            return;
        }
        String strMsg = buildAutoSendMsg(ucData.getNxyFuncUsecase().getNxyFuncItem().getId(), ucData.getMessageCode(),ucData.getMessageMessage(), ucData.getSimMessage());
        if(strMsg.indexOf("sendFail") != -1){
            String error = String.format("报文未执行。错误信息：%s", "不支持的报文案例或者未提供正确的案例信息!");
            wlSimService.saveAutoExecData(batchId,execId,ucData,strMsg,"noexec",error);
            logger.error("报文未执行，用例数据id[{}],错误[{}]",ucData.getId(),error);
            return;
        }
        //更改报文业务相关数据
        strMsg = wlSimService.buildAutoSendBussMsg(ucData,execId,strMsg);
        //2.加签、加密等
        strMsg = wlSimService.signMsg(strMsg,serverConfig, ucData.getSimMessage().getId());
        logger.info("自动化发送报文：{}",strMsg);
        try {
            //3.发送报文
            Random random = new Random();
            int index = random.nextInt(sendTemplateList.size());
            String sendQueue = sendTemplateList.get(index).getValue();
            JmsTemplate sendTemplate = sendTemplateList.get(index).getKey();
            sendTemplate.convertAndSend(sendQueue,strMsg);

            //4.保存执行数据
            wlSimService.saveAutoExecData(batchId,execId,ucData,strMsg,"execsucc","报文发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            String error = String.format("报文发送失败，请检查仿真实例MQ通信参数配置。错误信息：%s",e.getMessage());
            wlSimService.saveAutoExecData(batchId,execId,ucData,strMsg,"execerror",error);
            logger.error("报文发送失败，用例数据id[{}],错误[{}]",ucData.getId(),e.getMessage());
        }
    }

    public String buildAutoSendMsg(Long itemId, String msgCode, String msg, SimSysInsMessage simSysInsMessage) {
       try{
           Document doc = XmlDocHelper.getXmlFromStr(msg);
           //按照设置好的自动化生成规则设置报文域值，如果配置文件中配置了该域，则不修改
           List<SimSysInsMessageField> fields = wlSimService.getMessageFields(simSysInsMessage.getId());
           Long adapterId = simSysInsMessage.getSystemInstance().getAdapter().getId();
           //按照报文域设置的方法取值
           for (SimSysInsMessageField field:fields){
               String name = field.getMsgField().getFieldId();
               String valueType = field.getRespValueType();
               String valueValue = field.getRespValue();
               if (Constants.MESSAGE_RESPONSE_FIELD_TYPE_INVOKE.equals(valueType)){
                   if(XmlDocHelper.getNodeValue(doc, name).startsWith("$")){ //如果该报文域设置了配置文件，就不按照方法获取值，用配置文件中值进行替换
                       continue;
                   }
                   String value = ReflectWraper.invoke(valueValue, "adapterId:" + adapterId);
                   if (StringUtils.isNotEmpty(value)){
                       if (msgCode.equalsIgnoreCase("NPS.300.001.01") && "/transaction/header/msg/seqNb".equals(name)){
                           if(StringUtils.isNotEmpty( XmlDocHelper.getNodeValue(doc,"/transaction/body/MsgId"))){
                               XmlDocHelper.setNodeValue(doc,"/transaction/header/msg/seqNb","N"+value);
                           } else {
                               XmlDocHelper.setNodeValue(doc,name, value);
                           }
                       } else if (msgCode.equalsIgnoreCase("NPS.372.001.01") && "/transaction/body/PymtAgrmt".equals(name) && !"MT01".equals(XmlDocHelper.getNodeValue(doc, "/transaction/body/MageTyp"))){
                       } else if (msgCode.equalsIgnoreCase("NPS.321.001.01") && "/transaction/body/EndToEndId".equals(name)){
                       } else {
                           XmlDocHelper.setNodeValue(doc,name,value);
                       }
                   }
               }
           }
           for (SimSysInsMessageField field:fields){
               String name = field.getMsgField().getFieldId();
               String valueType = field.getRespValueType();
               String valueValue = field.getRespValue();
               if(XmlDocHelper.getNodeValue(doc, name).startsWith("$")){ //如果该报文域设置了配置文件，就不按照方法获取值，用配置文件中值进行替换
                   continue;
               }
               if(Constants.MESSAGE_RESPONSE_FIELD_TYPE_EQLVALUE.equals(valueType)){
                   XmlDocHelper.setNodeValue(doc,name, XmlDocHelper.getNodeValue(doc, valueValue));
               }
               if(Constants.MESSAGE_RESPONSE_FIELD_TYPE_DEFAULT.equals(valueType)){
                   XmlDocHelper.setNodeValue(doc,name, valueValue);
               }
           }
           //替换配置文件中配置的域
           return wlSimService.buildAutoSendMsgConfig(itemId, doc.asXML()); //替换配置文件中配置的值
       } catch (Exception e){
           e.printStackTrace();
       }
        return msg;
    }

    public String buildAutoSendMsg(Long msgId, String msg, Long instanceId, Long adapterId) {
       try{
           Document doc = XmlDocHelper.getXmlFromStr(msg);
           //按照设置好的自动化生成规则设置报文域值，如果配置文件中配置了该域，则不修改
           List<SimSysInsMessageField> fields = wlSimService.getMessageFields(msgId);
           if(fields != null && fields.size() > 0){ //按照报文域设置的方法取值
               for (SimSysInsMessageField field:fields){
                   String name = field.getMsgField().getFieldId();
                   String valueType = field.getRespValueType();
                   String valueValue = field.getRespValue();
                   if (Constants.MESSAGE_RESPONSE_FIELD_TYPE_INVOKE.equals(valueType)){
                       if(XmlDocHelper.getNodeValue(doc, name).startsWith("$")){ //如果该报文域设置了配置文件，就不按照方法获取值，用配置文件中值进行替换
                           continue;
                       }
                       String value = ReflectWraper.invoke(valueValue, "adapterId:" + adapterId);
                       if (StringUtils.isNotEmpty(value)){
                           XmlDocHelper.setNodeValue(doc,name,value);
                       }
                   }
               }
               for (SimSysInsMessageField field:fields){
                   String name = field.getMsgField().getFieldId();
                   String valueType = field.getRespValueType();
                   String valueValue = field.getRespValue();
                   if(XmlDocHelper.getNodeValue(doc, name).startsWith("$")){ //如果该报文域设置了配置文件，就不按照方法获取值，用配置文件中值进行替换
                       continue;
                   }
                   if(Constants.MESSAGE_RESPONSE_FIELD_TYPE_EQLVALUE.equals(valueType)){
                       XmlDocHelper.setNodeValue(doc,name, XmlDocHelper.getNodeValue(doc, valueValue));
                   }
                   if(Constants.MESSAGE_RESPONSE_FIELD_TYPE_DEFAULT.equals(valueType)){
                       XmlDocHelper.setNodeValue(doc,name, valueValue);
                   }
               }
           }
           return doc.asXML();
       } catch (Exception e){
           e.printStackTrace();
       }
        return msg;
    }
}
