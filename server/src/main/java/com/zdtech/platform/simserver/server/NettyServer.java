package com.zdtech.platform.simserver.server;

import com.zdtech.platform.framework.network.entity.Request;
import com.zdtech.platform.simserver.service.CommonService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * NettyServer
 *
 * @author panli
 * @date 2016/7/25
 */
@Service
public class NettyServer implements Server {
    private String host;
    private Integer port;
    private EventLoopGroup boss;
    private EventLoopGroup worker;

    @Autowired
    private CommonService commonService;
    @Autowired
    private NettyServerChannelInitializer channelInitializer;

    @PostConstruct
    public void init(){
        host = commonService.getServerHost();
        port = commonService.getServerPort();
    }
    @Override
    public void start() {
        try {
            boss = new NioEventLoopGroup();
            worker = new NioEventLoopGroup();
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, worker).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(channelInitializer);

            //ChannelFuture f = b.bind(port).sync();
            ChannelFuture f = b.bind(host,port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        boss.shutdownGracefully();
        worker.shutdownGracefully();
        boss = null;
        worker = null;
    }

    @Override
    public void reStart() {

    }

    @Override
    public void sendMessage(Object msg) {

    }
}
