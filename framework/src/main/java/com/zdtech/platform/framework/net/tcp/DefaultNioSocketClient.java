package com.zdtech.platform.framework.net.tcp;

import com.zdtech.platform.framework.net.tcp.handler.StringLengthFieldDecoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * DefaultNioSocketClient
 *
 * @author panli
 * @date 2016/6/3
 */
public class DefaultNioSocketClient extends AbstractNioSocketClient{
    public DefaultNioSocketClient(String host, Integer port, ChannelHandler handler) {
        super(host, port, handler);
    }

    @Override
    protected ChannelInitializer<?> channelInitializer() {
        ChannelInitializer<SocketChannel> ci = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new StringLengthFieldDecoder(10*1024*1024,0,8,0,8));
                ch.pipeline().addLast(new StringDecoder());
                ch.pipeline().addLast(handler);
            }
        };
        return ci;
    }
}
