package com.zdtech.platform.simserver.server;

import com.zdtech.platform.simserver.service.CommonService;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * NettyServerChannelInitializer
 *
 * @author panli
 * @date 2016/7/25
 */
@Service
public class NettyServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    private ThreadPoolExecutor threadPool;

    @Autowired
    private CommonService commonService;

    @PostConstruct
    public void init(){
        initThreadPool();
    }
    private void initThreadPool(){
        int corePoolSize = commonService.getCorePoolSize();
        int maxPoolSize = commonService.getMaxPoolSize();
        int keepAliveSeconds = commonService.getKeepAlive();
        int queueCapacity = commonService.getWorkQueue();
        BlockingQueue<Runnable> queue = queueCapacity > 0?new LinkedBlockingQueue(queueCapacity):new SynchronousQueue();
        this.threadPool = new ThreadPoolExecutor(
                corePoolSize, maxPoolSize, keepAliveSeconds, TimeUnit.SECONDS,
                queue);
    }
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //添加对象编码器 在服务器对外发送消息的时候自动将实现序列化的POJO对象编码
        ch.pipeline().addLast(new ObjectEncoder());
        //添加对象解码器 负责对序列化POJO对象进行解码 设置对象序列化最大长度为1M 防止内存溢出
        //设置线程安全的WeakReferenceMap对类加载器进行缓存 支持多线程并发访问  防止内存溢出
        ch.pipeline().addLast(new ObjectDecoder(1024*1024, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
        DispatcherHandler dispatcherHandler = new DispatcherHandler(threadPool);
        ch.pipeline().addLast(dispatcherHandler);

    }
}
