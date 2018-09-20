package com.zdtech.platform.framework.network.tcp;

import com.zdtech.platform.framework.network.Server;
import com.zdtech.platform.framework.network.entity.Request;
import com.zdtech.platform.framework.network.exception.ServerException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * NettyServer
 *
 * @author panli
 * @date 2016/6/10
 */
public class NettyServer implements Server{
    private Integer port;
    private ChannelInitializer<SocketChannel> channelInitializer;
    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private Channel channel;

    public NettyServer(Integer port, ChannelInitializer<SocketChannel> channelInitializer) {
        this.port = port;
        this.channelInitializer = channelInitializer;
    }

    @Override
    public void run() {
        try {
            boss = new NioEventLoopGroup();
            worker = new NioEventLoopGroup();
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, worker).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(channelInitializer);

            ChannelFuture f = b.bind(port).sync();
            channel = f.channel();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new ServerException(e);
        }
    }

    @Override
    public void processReq(Request request) {

    }

    @Override
    public void stop() {
        if (null == channel) {
            throw new ServerException(new RuntimeException("server stop exception"));
        }
        boss.shutdownGracefully();
        worker.shutdownGracefully();
        channel.closeFuture().syncUninterruptibly();
        boss = null;
        worker = null;
        channel = null;
    }
}
