package com.zdtech.platform.framework.net.tcp.handler;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteOrder;

/**
 * DefaultNioSocketClient
 *
 * @author panli
 * @date 2016/6/3
 */
public class StringLengthFieldDecoder extends LengthFieldBasedFrameDecoder {

    public StringLengthFieldDecoder(int maxFrameLength, int lengthFieldOffset,
                                    int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }


    @Override
    protected long getUnadjustedFrameLength(ByteBuf buf, int offset, int length, ByteOrder order) {
        buf = buf.order(order);
        byte[] lenByte = new byte[length];
        buf.getBytes(offset, lenByte);
        String lenStr = new String(lenByte);
        return Long.valueOf(lenStr);
    }
}
