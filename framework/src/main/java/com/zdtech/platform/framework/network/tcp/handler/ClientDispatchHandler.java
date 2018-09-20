package com.zdtech.platform.framework.network.tcp.handler;

import com.zdtech.platform.framework.network.entity.Request;
import com.zdtech.platform.framework.network.entity.Response;
import com.zdtech.platform.framework.network.exception.ClientException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * ClientDispatchHandler
 *
 * @author panli
 * @date 2016/6/10
 */
public class ClientDispatchHandler extends SimpleChannelInboundHandler<Response> {
    private final ConcurrentHashMap<Long, BlockingQueue<Response>> responseMap = new ConcurrentHashMap<>();

    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        if (msg instanceof Request) {
            Request request = (Request) msg;
            responseMap.putIfAbsent(request.getId(), new LinkedBlockingQueue<Response>(1));
        }
        super.write(ctx, msg, promise);
    }

    @Override
    protected void messageReceived(final ChannelHandlerContext ctx, final Response response) throws Exception {
        BlockingQueue<Response> queue = responseMap.get(response.getId());
        queue.add(response);
    }

    public Response getResponse(final long messageId) {
        Response result;
        responseMap.putIfAbsent(messageId, new LinkedBlockingQueue<Response>(1));
        try {
            result = responseMap.get(messageId).take();
            if (null == result) {
                result = getSystemMessage();
            }
        } catch (final InterruptedException ex) {
            throw new ClientException(ex);
        } finally {
            responseMap.remove(messageId);
        }
        return result;
    }

    private Response getSystemMessage() {
        try {
            return responseMap.get(-1L).poll(5, SECONDS);
        } catch (final InterruptedException ex) {
            throw new ClientException(ex);
        }
    }
}
