package com.zdtech.platform.framework.network.tcp;

import com.zdtech.platform.framework.network.Client;
import com.zdtech.platform.framework.network.entity.Request;
import com.zdtech.platform.framework.network.entity.Response;
import com.zdtech.platform.framework.network.exception.ClientException;
import com.zdtech.platform.framework.network.tcp.handler.ClientChannelInitializer;
import com.zdtech.platform.framework.network.tcp.handler.ClientDispatchHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * NettyClient
 *
 * @author panli
 * @date 2016/6/10
 */
public class NettyClient implements Client{
    private String host;
    private Integer port;
    private EventLoopGroup group;
    private ClientChannelInitializer initializer;
    protected Channel channel;

    public NettyClient(String host, Integer port) {
        this.host = host;
        this.port = port;
        initializer = new ClientChannelInitializer(new ClientDispatchHandler());
    }

    @Override
    public void connect() {
        Bootstrap bootstrap = new Bootstrap();
        group = new NioEventLoopGroup();
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,10000);
        bootstrap.handler(initializer);
        channel = bootstrap.connect(host, port).syncUninterruptibly().channel();
    }

    @Override
    public Response send(Request msg) {
        channel.writeAndFlush(msg);

        return initializer.getResponse(msg.getId());
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        SocketAddress remoteAddress = channel.remoteAddress();
        if (!(remoteAddress instanceof InetSocketAddress)) {
            throw new ClientException(new RuntimeException("Get remote address error, should be InetSocketAddress"));
        }
        return (InetSocketAddress) remoteAddress;
    }

    @Override
    public void close() {
        if (null == channel) {
            throw new ClientException(new RuntimeException("client close exception"));
        }
        group.shutdownGracefully();
        channel.closeFuture().syncUninterruptibly();
        group = null;
        channel = null;
    }
}
