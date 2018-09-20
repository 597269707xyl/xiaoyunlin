package com.zdtech.platform.framework.network.tcp.handler;

import com.zdtech.platform.framework.network.entity.Response;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * ClientChannelInitializer
 *
 * @author panli
 * @date 2016/6/10
 */
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    private ClientDispatchHandler handler;

    public ClientChannelInitializer(ClientDispatchHandler handler) {
        this.handler = handler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
        //设置发送消息编码器
        ch.pipeline().addLast(new ObjectEncoder());
        //添加POJO对象解码器 禁止缓存类加载器
        ch.pipeline().addLast(new ObjectDecoder(1024, ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));

        //设置自己实现handler
        ch.pipeline().addLast(handler);
    }

    public Response getResponse(final long messageId) {
        return handler.getResponse(messageId);
    }
}
