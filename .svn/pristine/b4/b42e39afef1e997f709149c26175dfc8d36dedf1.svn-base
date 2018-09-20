package com.zdtech.platform.simserver.server;


import com.zdtech.platform.framework.network.entity.Request;
import com.zdtech.platform.framework.network.entity.Response;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * DispatcherHandler
 *
 * @author panli
 * @date 2016/7/26
 */
public class DispatcherHandler extends ChannelHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(DispatcherHandler.class);
    private ThreadPoolExecutor threadPool;

    public DispatcherHandler(ThreadPoolExecutor threadPool) {
        this.threadPool = threadPool;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Response respMsg = new Response(true,"");
        if (msg != null && msg instanceof Request){
            logger.info("收到消息:"+msg.toString());
            Request req = (Request)msg;
            MessageProcessor processor = new MessageProcessor(req);
            this.threadPool.execute(processor);
            respMsg.setId(req.getId());
            respMsg.setMsg("后台收到消息，正在处理");
        }else {
            logger.warn("收到无法识别的消息");
            respMsg.setSuccess(false);
            respMsg.setId(-1L);
            respMsg.setMsg("无法识别的消息");
        }
        ctx.writeAndFlush(respMsg);
    }
}
