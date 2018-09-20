package com.zdtech.platform.framework.network.entity;

/**
 * SimStartMessage
 * 来账手动应答
 * @author panli
 * @date 2016/6/10
 */
public class SimSendMessage extends Message {
    private Long caseSendId;
    public SimSendMessage(Long simInsId, Long adapterId, Long caseSendId) {
        super(MessageType.SimSendMsg, simInsId, adapterId);
        this.caseSendId=caseSendId;
    }

    public Long getCaseSendId() {
        return caseSendId;
    }

    public void setCaseSendId(Long caseSendId) {
        this.caseSendId = caseSendId;
    }
}
