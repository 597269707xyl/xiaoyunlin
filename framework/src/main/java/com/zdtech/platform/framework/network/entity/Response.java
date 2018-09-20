package com.zdtech.platform.framework.network.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Response
 *
 * @author panli
 * @date 2016/6/10
 */
public class Response implements Serializable{
    private static final long serialVersionUID = 4927060975246332976L;

    private long id;
    //private Message message;
    private boolean success;
    private String msg;
    private Map<String,Object> data = new HashMap<>();


    public Response(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public Response(long id, boolean success, String msg) {
        this.id = id;
        this.success = success;
        this.msg = msg;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
