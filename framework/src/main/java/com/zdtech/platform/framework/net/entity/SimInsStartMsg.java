package com.zdtech.platform.framework.net.entity;


/**
 * SimInsStartMsg
 *
 * @author panli
 * @date 2016/5/23
 */
public class SimInsStartMsg extends BaseMsg{

    public SimInsStartMsg() {
        super(MsgType.SimInsStart);
    }

    public SimInsStartMsg(Long simInsId) {
        super(MsgType.SimInsStart,simInsId);
    }

}
