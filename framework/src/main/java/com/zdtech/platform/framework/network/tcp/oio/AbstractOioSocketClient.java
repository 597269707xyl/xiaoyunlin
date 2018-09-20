package com.zdtech.platform.framework.network.tcp.oio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.SocketFactory;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * @author lcheng
 * @date 2015/10/31
 */
public abstract class AbstractOioSocketClient{

    private static Logger log = LoggerFactory.getLogger(AbstractOioSocketClient.class);

    protected int lenFieldLength;
    protected LengthFieldByteStringEnDeCoder enDeCoder;
    protected String host;
    protected Integer port;

    protected String localHost;
    protected Integer localPort;

    public AbstractOioSocketClient(String host, Integer port,
                                   LengthFieldByteStringEnDeCoder enDeCoder ) {
        this.enDeCoder = enDeCoder;
        this.host = host;
        this.port = port;
    }
    public AbstractOioSocketClient(String host, Integer port,String localHost,Integer localPort,
                                   LengthFieldByteStringEnDeCoder enDeCoder ) {
        this.enDeCoder = enDeCoder;
        this.host = host;
        this.port = port;
        this.localHost = localHost;
        this.localPort = localPort;
    }

    public <T> void send(T msg) {
        try (Socket socket = SocketFactory.getDefault().createSocket(host,port)){
            doSend(socket,msg);
        }catch (IOException e) {
            log.error(" 客户端连接出错!", e);
        }
    }

    public <T> T receive() {
        return receive(0);
    }

    public <T> T receive(int timeout) {
        try (Socket socket = SocketFactory.getDefault().createSocket(host,port)){
            return (T)doReceive(socket,timeout);
        } catch (IOException e) {
            log.error("客户端连接出错!", e);
        }
        return null;
    }

    public <S,R> R sendAndReceive(S msg) {
        return sendAndReceive(msg,0);
    }

    public <S, R> R sendAndReceive(S msg, int timeout) {
        /*try (Socket socket = SocketFactory.getDefault().createSocket(host,port)){*/
        try (Socket socket = SocketFactory.getDefault().createSocket(host,port, InetAddress.getByName(localHost),localPort)){
            OutputStream os = new BufferedOutputStream(socket.getOutputStream());
            byte[] toSend = enDeCoder.encode(msg.toString());
            os.write(toSend);
            os.flush();

            Object oRecv = doReceive(socket,timeout);
            if (oRecv == null){
                return null;
            }
            String strRecv = (String)oRecv;
            log.info("收到消息:{}",strRecv);
            return (R)strRecv;
        }catch (IOException e) {
            log.error("客户端连接出错!", e);
            log.error("发送接收异常：{}",e.getMessage());
        }
        return null;
    }

    protected  void doSend(Socket socket,Object msg){
        byte[] toSend = enDeCoder.encode(msg.toString());
        try (OutputStream os = new BufferedOutputStream(socket.getOutputStream())) {
            os.write(toSend);
            os.flush();
        } catch (IOException e) {
            log.error(" 发送消息失败!", e);
        }
    }

    protected abstract Object doReceive(Socket socket,int timeout);

    protected byte[] readByte(int toReadLen,InputStream is){
        if (toReadLen<0 || is==null ){
            throw new IllegalArgumentException("客户端错误的参数设置!");
        }
        byte[] result = new byte[toReadLen];
        int hasRead=0;
        int temp =-10;
        try {
            while(hasRead< toReadLen && (temp=is.read(result,hasRead,toReadLen-hasRead))>0){
                hasRead += temp;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
