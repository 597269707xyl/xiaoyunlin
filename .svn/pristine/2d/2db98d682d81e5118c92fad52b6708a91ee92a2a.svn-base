package com.zdtech.platform.framework.network.entity;

import java.io.Serializable;

/**
 * Message
 *
 * @author panli
 * @date 2016/6/10
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1974578410566535251L;

    public enum MessageType{
        SimInsStart,SimInsStop,SimAutoTestMsg,SimAutoBatchStop,SimSendMsg, InternalTimeMsg
    }
    private MessageType msgType;
    private Long simInsId;
    private Long adapterId;

    public Message(MessageType msgType,Long simInsId, Long adapterId) {
        this.msgType = msgType;
        this.simInsId = simInsId;
        this.adapterId = adapterId;
    }

    public MessageType getMsgType() {
        return msgType;
    }

    public void setMsgType(MessageType msgType) {
        this.msgType = msgType;
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
}
