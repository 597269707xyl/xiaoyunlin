package com.zdtech.platform.simserver.net.config;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.List;
import java.util.Map;

/**
 * ServerConfig
 *
 * @author panli
 * @date 2016/7/28
 */
public class ServerConfig {
    public enum Protocol{
        TCP,JMS_MQ,JMS_TONGLINKQ,WEBSERVICE,TCPSHORTCONN
    }
    public enum TcpMode{
        FullDuplex,HalfDuplex
    }
    public enum ConnectMode{
        Initiative,Passive
    }
    public enum HeartBeatMode{
        Initiative,Passive
    }
    public enum MessageType{
        Self,Xml
    }
    private Protocol protocol;
    private TcpMode tcpMode;
    private ConnectMode connectMode;
    private String serverHost;
    private Integer serverPort;
    private String clientHost;
    private Integer clientPort;
    private String sendQueue;
    private String recvQueue;
    private String immeSendQueue;
    private String immeRecvQueue;
    private String queueManager;
    private String mqHostName;
    private Integer mqPort;
    private String mqChannel;
    private Integer mqCcsid;
    private String userId;
    private String passWord;

    private String encodeCharset;
    private String decodeCharset;

    private String heartBeatRecv;
    private String heartBeatSend;
    private HeartBeatMode heartBeatMode;
    private Integer heartBeatInterval;

    private MessageType messageType;

    private String wsUrl;

    private Long simInsId;
    private Long adapterId;

    private Map<String,String> simParamsMap;
    private List<Map<String,String>> mqRecvConfList;
    private List<Map<String,String>> mqSendConfList;

    private String localUrl;
    private String remoteUrl;
    private Integer internalTime;

    public List<Map<String, String>> getMqRecvConfList() {
        return mqRecvConfList;
    }

    public void setMqRecvConfList(List<Map<String, String>> mqRecvConfList) {
        this.mqRecvConfList = mqRecvConfList;
    }

    public List<Map<String, String>> getMqSendConfList() {
        return mqSendConfList;
    }

    public void setMqSendConfList(List<Map<String, String>> mqSendConfList) {
        this.mqSendConfList = mqSendConfList;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public TcpMode getTcpMode() {
        return tcpMode;
    }

    public void setTcpMode(TcpMode tcpMode) {
        this.tcpMode = tcpMode;
    }

    public ConnectMode getConnectMode() {
        return connectMode;
    }

    public void setConnectMode(ConnectMode connectMode) {
        this.connectMode = connectMode;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public String getClientHost() {
        return clientHost;
    }

    public void setClientHost(String clientHost) {
        this.clientHost = clientHost;
    }

    public Integer getClientPort() {
        return clientPort;
    }

    public void setClientPort(Integer clientPort) {
        this.clientPort = clientPort;
    }

    public String getSendQueue() {
        return sendQueue;
    }

    public void setSendQueue(String sendQueue) {
        this.sendQueue = sendQueue;
    }

    public String getRecvQueue() {
        return recvQueue;
    }

    public void setRecvQueue(String recvQueue) {
        this.recvQueue = recvQueue;
    }

    public Long getSimInsId() {
        return simInsId;
    }

    public void setSimInsId(Long simInsId) {
        this.simInsId = simInsId;
    }

    public Long getAdapterId() {
        return adapterId;
    }

    public void setAdapterId(Long adapterId) {
        this.adapterId = adapterId;
    }

    public String getEncodeCharset() {
        return encodeCharset;
    }

    public void setEncodeCharset(String encodeCharset) {
        this.encodeCharset = encodeCharset;
    }

    public String getDecodeCharset() {
        return decodeCharset;
    }

    public void setDecodeCharset(String decodeCharset) {
        this.decodeCharset = decodeCharset;
    }

    public String getHeartBeatRecv() {
        return heartBeatRecv;
    }

    public void setHeartBeatRecv(String heartBeatRecv) {
        this.heartBeatRecv = heartBeatRecv;
    }

    public String getHeartBeatSend() {
        return heartBeatSend;
    }

    public void setHeartBeatSend(String heartBeatSend) {
        this.heartBeatSend = heartBeatSend;
    }

    public HeartBeatMode getHeartBeatMode() {
        return heartBeatMode;
    }

    public void setHeartBeatMode(HeartBeatMode heartBeatMode) {
        this.heartBeatMode = heartBeatMode;
    }

    public Integer getHeartBeatInterval() {
        if (this.heartBeatInterval ==null){
            String value = getSimConf("FE_ECHO_INTERVAL");
            try {
                int i = Integer.parseInt(value);
                return i;
            }catch (Exception e){
                return 2;
            }
        }
        return heartBeatInterval;
    }

    public void setHeartBeatInterval(Integer heartBeatInterval) {
        this.heartBeatInterval = heartBeatInterval;
    }

    public String getWsUrl() {
        return wsUrl;
    }

    public void setWsUrl(String wsUrl) {
        this.wsUrl = wsUrl;
    }

    public Map<String, String> getSimParamsMap() {
        return simParamsMap;
    }

    public void setSimParamsMap(Map<String, String> simParamsMap) {
        this.simParamsMap = simParamsMap;
    }
    public String getSimConf(String name){
        if (this.simParamsMap.containsKey(name)){
            String s = this.simParamsMap.get(name);
            if (StringUtils.isNotEmpty(s)){
                return s;
            }
        }
        return "";
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public String getImmeSendQueue() {
        return immeSendQueue;
    }

    public void setImmeSendQueue(String immeSendQueue) {
        this.immeSendQueue = immeSendQueue;
    }

    public String getImmeRecvQueue() {
        return immeRecvQueue;
    }

    public void setImmeRecvQueue(String immeRecvQueue) {
        this.immeRecvQueue = immeRecvQueue;
    }

    public String getQueueManager() {
        return queueManager;
    }

    public void setQueueManager(String queueManager) {
        this.queueManager = queueManager;
    }

    public String getMqHostName() {
        return mqHostName;
    }

    public void setMqHostName(String mqHostName) {
        this.mqHostName = mqHostName;
    }

    public Integer getMqPort() {
        return mqPort;
    }

    public void setMqPort(Integer mqPort) {
        this.mqPort = mqPort;
    }

    public String getMqChannel() {
        return mqChannel;
    }

    public void setMqChannel(String mqChannel) {
        this.mqChannel = mqChannel;
    }

    public Integer getMqCcsid() {
        return mqCcsid;
    }

    public void setMqCcsid(Integer mqCcsid) {
        this.mqCcsid = mqCcsid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public int getUsablePort(int start){
        int ret = -1;
        //通过操作系统自动找可用端口
        try(ServerSocket ss = new ServerSocket(0);) {
            ret = ss.getLocalPort();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
        /*if (start >= 65535){
            return -1;
        }
        for (int i = 65535; i > start; i++){
            try (DatagramSocket ds = new DatagramSocket(i)){
                return i;
            }catch (SocketException e){

            }
        }
        return -1;*/
    }

    public String getLocalUrl() {
        return localUrl;
    }

    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public Integer getInternalTime() {
        return internalTime;
    }

    public void setInternalTime(Integer internalTime) {
        this.internalTime = internalTime;
    }
}
