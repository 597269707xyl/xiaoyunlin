package com.zdtech.platform.framework.net.tcp;

import com.zdtech.platform.framework.net.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * NioSocketServer
 *
 * @author panli
 * @date 2016/5/24
 */
public class NioSocketServer implements Server {

    private Integer port;
    private ChannelInitializer<SocketChannel> channelInitializer;
    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private boolean isSync;


    public NioSocketServer(Integer port, ChannelInitializer<SocketChannel> channelInitializer,boolean isSync) {
        this.port = port;
        this.channelInitializer = channelInitializer;
        this.isSync = isSync;
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
            if (isSync){
                f.channel().closeFuture().sync();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }/*finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }*/
    }

    @Override
    public <T> void processReq(T req) {

    }

    @Override
    public void stop() {
        if (boss != null) {
            boss.shutdownGracefully();
        }
        if (worker != null) {
            worker.shutdownGracefully();
        }
    }

}
