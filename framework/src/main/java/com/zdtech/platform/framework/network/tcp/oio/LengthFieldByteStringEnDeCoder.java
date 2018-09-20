package com.zdtech.platform.framework.network.tcp.oio;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * @author lcheng
 * @date 2015/10/31
 */
public class LengthFieldByteStringEnDeCoder {

    private int lenFieldLength;
    private String encodeCharset;
    private String decodeCharset;

    public LengthFieldByteStringEnDeCoder(int lenFieldLength, String encodeCharset, String decodeCharset) {
        this.lenFieldLength = lenFieldLength;
        this.encodeCharset = encodeCharset;
        this.decodeCharset = decodeCharset;
    }

    public String decode(byte[] target) {
        try {
            String result = new String(target,decodeCharset);
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] encode(String origin) {
        try {
            byte[] toSend = origin.getBytes(encodeCharset);
            int len = toSend.length;
            ByteBuffer buffer = ByteBuffer.allocate(lenFieldLength+len);
            if (lenFieldLength==4){
                buffer.putInt(len);
            }else if (lenFieldLength==8){
                String str = String.valueOf(len);
                String lenStr = StringUtils.leftPad(str,8,'0');
                buffer.put(lenStr.getBytes());
            }else {
                //TODO
            }
            buffer.put(toSend);
            buffer.flip();
            return buffer.array();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
