package com.zdtech.platform.framework.network.entity;

/**
 * SimStopMessage
 * 自动批量执行停止
 * @author panli
 * @date 2016/6/10
 */
public class SimAutoBatchStopMessage extends Message {
    private Long batchId;
    public SimAutoBatchStopMessage(Long simInsId, Long adapterId, Long batchId) {
        super(MessageType.SimAutoBatchStop, simInsId, adapterId);
        this.batchId = batchId;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }
}
