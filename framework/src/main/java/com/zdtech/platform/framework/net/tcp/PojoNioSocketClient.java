package com.zdtech.platform.framework.net.tcp;

import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.List;
import java.util.logging.SocketHandler;

/**
 * PojoNioSocketClient
 *
 * @author panli
 * @date 2016/5/24
 */
public class PojoNioSocketClient extends AbstractNioSocketClient {
    public PojoNioSocketClient(String host, Integer port, ChannelHandler handler) {
        super(host, port, handler);
    }

    @Override
    protected ChannelInitializer<?> channelInitializer() {
        ChannelInitializer<SocketChannel> ci = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                //设置发送消息编码器
                ch.pipeline().addLast(new ObjectEncoder());
                //添加POJO对象解码器 禁止缓存类加载器
                ch.pipeline().addLast(new ObjectDecoder(1024, ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));

                //设置自己实现handler
                if (handler != null){
                    ch.pipeline().addLast(handler);
                }
            }
        };
        return ci;
    }
}
