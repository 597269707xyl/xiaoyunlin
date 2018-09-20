package com.zdtech.platform.framework.network.entity;

/**
 * SimStartMessage
 * 启动适配器
 * @author panli
 * @date 2016/6/10
 */
public class SimStartMessage extends Message {
    public SimStartMessage(Long simInsId, Long adapterId) {
        super(MessageType.SimInsStart, simInsId, adapterId);
    }
}
