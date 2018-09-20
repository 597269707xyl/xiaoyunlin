package com.zdtech.platform.framework.network.entity;

/**
 * SimStopMessage
 * 停止适配器
 * @author panli
 * @date 2016/6/10
 */
public class SimStopMessage extends Message {
    public SimStopMessage(Long simInsId, Long adapterId) {
        super(MessageType.SimInsStop, simInsId, adapterId);
    }
}
