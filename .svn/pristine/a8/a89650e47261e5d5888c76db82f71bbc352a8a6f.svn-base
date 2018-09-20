package com.zdtech.platform.framework.network.tcp.oio;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.Socket;

/**
 * @author lcheng
 * @date 2015/10/31
 */
public class DefaultLenFieldOioSocketClient extends AbstractOioSocketClient {
    private static Logger logger = LoggerFactory.getLogger(DefaultLenFieldOioSocketClient.class);
    public DefaultLenFieldOioSocketClient(String host, Integer port, int lenFieldLength,
                                          LengthFieldByteStringEnDeCoder enDeCoder) {
        super(host, port, enDeCoder);
        this.lenFieldLength = lenFieldLength;
    }
    public DefaultLenFieldOioSocketClient(String host, Integer port, String localhost,Integer localPort,int lenFieldLength,
                                          LengthFieldByteStringEnDeCoder enDeCoder) {
        super(host, port,localhost,localPort, enDeCoder);
        this.lenFieldLength = lenFieldLength;
    }

    protected Object doReceive(Socket socket,int timeout){
        try {
            socket.setSoTimeout(timeout);
            InputStream is = socket.getInputStream();
            byte[] lenByte = readByte(lenFieldLength,is);
            String lenTxt = new String(lenByte);
            if (StringUtils.isEmpty(lenTxt.trim()))
                return null;
            int len = Integer.parseInt(lenTxt);
            byte[] xmlByte = readByte(len,is);
            return enDeCoder.decode(xmlByte);
        } catch (Exception e) {
            logger.error("同步接收报文错误：{}",e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
