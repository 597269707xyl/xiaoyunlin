package com.zdtech.platform.framework.net.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * SimRespMsg
 *
 * @author panli
 * @date 2016/5/25
 */
public class SimRespMsg extends BaseMsg{
    private boolean success;
    private String msg;
    private Map<String,Object> data = new HashMap<>();

    public SimRespMsg() {
        super(MsgType.SimResp);
    }

    public SimRespMsg(Long simInsId) {
        super(MsgType.SimResp, simInsId);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
