package com.zdtech.platform.framework.network.entity;

import java.util.List;

/**
 * SimAutoTestMessage
 * 自动化执行
 * @author panli
 * @date 2016/6/12
 */
public class SimAutoTestMessage extends Message {
    private List<Long> messageIdList;
    private Long batchId;
    //用例发送间隔，单位毫秒
    private Long sendInternal = 0L;

    public SimAutoTestMessage(Long simInsId, Long adapterId, List<Long> messageIdList) {
        super(MessageType.SimAutoTestMsg, simInsId, adapterId);
        this.messageIdList = messageIdList;
    }

    public List<Long> getMessageIdList() {
        return messageIdList;
    }

    public void setMessageIdList(List<Long> messageIdList) {
        this.messageIdList = messageIdList;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public Long getSendInternal() {
        return sendInternal;
    }

    public void setSendInternal(Long sendInternal) {
        this.sendInternal = sendInternal;
    }
}
