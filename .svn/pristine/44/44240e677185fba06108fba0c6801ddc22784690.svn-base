package com.zdtech.platform.framework.network.entity;

/**
 * Created by lyj on 2018/7/20.
 */
public class InternalTimeMessage extends Message {
    private int internalTime;
    public InternalTimeMessage(Long simInsId, Long adapterId, int internalTime) {
        super(MessageType.InternalTimeMsg, simInsId, adapterId);
        this.internalTime = internalTime;
    }

    public int getInternalTime() {
        return internalTime;
    }

    public void setInternalTime(int internalTime) {
        this.internalTime = internalTime;
    }
}
