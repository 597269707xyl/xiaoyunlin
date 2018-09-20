package com.zdtech.platform.framework.net.entity;

import java.io.Serializable;

/**
 * BaseMsg
 *
 * @author panli
 * @date 2016/5/23
 */
public class BaseMsg implements Serializable {
    private static final long serialVersionUID = 7167766849521118411L;

    public enum MsgType{
        SimInsStart,SimInsStop,SimResp
    }
    private MsgType msgType;
    private Long simInsId;

    public BaseMsg() {
    }

    public BaseMsg(MsgType msgType) {
        this.msgType = msgType;
    }

    public BaseMsg(MsgType msgType, Long simInsId) {
        this.msgType = msgType;
        this.simInsId = simInsId;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public Long getSimInsId() {
        return simInsId;
    }

    public void setSimInsId(Long simInsId) {
        this.simInsId = simInsId;
    }
}
