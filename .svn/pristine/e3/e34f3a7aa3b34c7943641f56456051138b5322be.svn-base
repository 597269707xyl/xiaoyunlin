package com.zdtech.platform.framework.net.tcp;

import com.zdtech.platform.framework.net.Client;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.List;

/**
 * AbstractNioSocketClient
 *
 * @author panli
 * @date 2016/5/24
 */
public abstract class AbstractNioSocketClient implements Client{
    private String host;
    private Integer port;
    private Bootstrap bootStrap = null;
    private EventLoopGroup group = null;
    protected ChannelHandler handler = null;
    protected SocketChannel channel = null;

    public AbstractNioSocketClient(String host, Integer port,ChannelHandler handler) {
        this.host = host;
        this.port = port;
        this.handler = handler;
    }
    @Override
    public void connect() throws Exception{
        try {
            bootStrap = new Bootstrap();
            group = new NioEventLoopGroup();
            bootStrap.group(group);
            bootStrap.channel(NioSocketChannel.class);
            bootStrap.option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,10000);
            bootStrap.handler(channelInitializer());
            ChannelFuture f = bootStrap.connect(this.host,this.port).sync();
            f.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    channel = (SocketChannel) channelFuture.channel();
                }
            });
            //f.channel().closeFuture().sync();
        }catch (Exception e){

        }
        /*finally {
            group.shutdownGracefully();
        }*/
    }

    @Override
    public <T> void send(T msg) {
        channel.writeAndFlush(msg);
    }

    @Override
    public void close(){
        if (group != null){
            group.shutdownGracefully();
        }
    }
    protected abstract ChannelInitializer<?> channelInitializer();


}
