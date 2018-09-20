package com.zdtech.platform.simserver.net.http;

import com.zdtech.platform.framework.entity.SimSysInsMessage;
import com.zdtech.platform.framework.utils.SpringContextUtils;
import com.zdtech.platform.framework.utils.XmlDocHelper4wl;
import com.zdtech.platform.simserver.net.config.ServerConfig;
import com.zdtech.platform.simserver.service.EpccService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.nio.protocol.*;
import org.apache.http.protocol.HttpContext;
import org.dom4j.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * EpccRequestHandler
 *
 * @author panli
 * @date 2017/11/12
 */
public class EpccRequestHandler implements HttpAsyncRequestHandler<HttpRequest> {
    private static Logger logger = LoggerFactory.getLogger(EpccRequestHandler.class);
    private EpccService service;
    private ServerConfig config;

    public EpccRequestHandler(ServerConfig config) {
        service = SpringContextUtils.getBean(EpccService.class);
        this.config = config;
    }

    @Override
    public HttpAsyncRequestConsumer processRequest(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        return new BasicAsyncRequestConsumer();
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpAsyncExchange httpAsyncExchange, HttpContext httpContext) throws HttpException, IOException {
        final HttpResponse response = httpAsyncExchange.getResponse();
        handleInternal(httpRequest, response, httpContext);
        httpAsyncExchange.submitResponse(new BasicAsyncResponseProducer(response));
    }

    private void handleInternal(
            final HttpRequest request,
            final HttpResponse response,
            final HttpContext context) throws HttpException, IOException {

        HttpEntity httpEntity = ((HttpEntityEnclosingRequest)request).getEntity();
        InputStream inputStream = httpEntity.getContent();
        String msg = IOUtils.toString(inputStream,"utf-8");
        process(msg,response);
    }

    private void process(String recv,HttpResponse response){
        logger.info("网联收到请求报文：{}",recv);
        response.setStatusCode(HttpStatus.SC_OK);
        if("true".equals(config.getSimParamsMap().get("async_notice"))){ //异步通知报文
            logger.info("异步通知报文");
            service.saveAsyncNoticeMessage(recv);
            NStringEntity entity = new NStringEntity(
                    "<xml><return_code>SUCCESS</return_code><return_msg>OK</return_msg></xml>",
                    ContentType.create("application/xml", "utf-8"));
            response.setEntity(entity);
            return;
        }
        //删除签名
        recv = service.removeSignStr(recv);
        Document recvDoc = XmlDocHelper4wl.getXmlFromStr(recv);
        String code = XmlDocHelper4wl.getNodeValue(recvDoc,"root/MsgHeader/MsgTp");
        //接收报文模板
        SimSysInsMessage ssim = service.getSysInsMessageByMsgCode(code);
        if (ssim == null){
            logger.warn("请求报文模板未找到, 报文编号为：{}", code);
            NStringEntity entity = new NStringEntity(
                    "",
                    ContentType.create("application/xml", "utf-8"));
            response.setEntity(entity);
            return;
        }
        Long recvId = service.saveRecvMessage(ssim, recvDoc, null); //保存来账信息
        String fieldId = service.getFuncCaseMark(code);
        if(StringUtils.isEmpty(fieldId)){
            service.updateRecvMessage(recvId, null, 0);
            logger.error("获取来账案例报文标识域ID为空, 报文编号为：{}", code);
            NStringEntity entity = new NStringEntity(
                    "",
                    ContentType.create("application/xml", "utf-8"));
            response.setEntity(entity);
            return;
        }
        String no = XmlDocHelper4wl.getNodeValue(recvDoc, fieldId);
        if(StringUtils.isEmpty(no)){
            service.updateRecvMessage(recvId, null, 0);
            logger.error("获取来账案例报文标识域值为空, 标识域ID为：{}", fieldId);
            NStringEntity entity = new NStringEntity(
                    "",
                    ContentType.create("application/xml", "utf-8"));
            response.setEntity(entity);
            return;
        }

        Object[] arr = service.getReplyMessage(ssim.getId(), no);
        if (arr == null){
            service.updateRecvMessage(recvId, no, 0);
            logger.warn("应答报文模板未找到");
            NStringEntity entity = new NStringEntity(
                    "",
                    ContentType.create("application/xml", "utf-8"));
            response.setEntity(entity);
            return;
        }
        service.updateRecvMessage(recvId, no, 3);
        String replyStr = service.getReplyMessageStr(arr[0],arr[1],arr[2],recvDoc);
        Pattern p = Pattern.compile(">\\s*|\t|\r|\n");
        Matcher m = p.matcher(replyStr);
        replyStr = m.replaceAll(">");
        if(!replyStr.contains("{S:")){
            replyStr += "{S:aaabbbccadfjalsdkjflasdjfaskjdfhaksdhfalsdkjhfalskdfhalsdkfhalsdfhlaskdjhfc}";
        }
        logger.info("网联应答报文：{}",replyStr);
        boolean responseModel = service.getRespModel(config.getAdapterId());
        logger.info("自动手动应答标识：{}, 注释：自动为：true，手动为：false", responseModel);
        if (responseModel){
            NStringEntity entity = new NStringEntity(
                    replyStr,
                    ContentType.create("application/xml", "utf-8"));
            response.setEntity(entity);
            service.updateRecvMessage(recvId, no, 1);
            service.saveSendMessage(recvId, arr[1], replyStr, config);
        } else {
            replyStr = service.removeSignStr(replyStr);
            Long sendId = service.saveSendMessage(recvId, arr[1], replyStr, config);
            long startTime = System.currentTimeMillis();
            boolean flag = false;
            while (!service.getSendResponse(recvId)){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logger.info("自动应答等待中。。。。。");
                long endTime = System.currentTimeMillis();
                if(endTime - startTime > 600000){  //默认十分钟后自动应答
                    flag = true;
                    break;
                }
            }
            if(flag){
                service.updateSendMessage(sendId);
                logger.info("超时应答。。。。。。。");
                return;
            }
            replyStr = service.getSendMessage(sendId);
            NStringEntity entity = new NStringEntity(
                    replyStr,
                    ContentType.create("application/xml", "utf-8"));
            response.setEntity(entity);
        }
    }
}
