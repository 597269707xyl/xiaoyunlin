package com.zdtech.platform.framework.network.entity;

import java.io.Serializable;

/**
 * Request
 *
 * @author panli
 * @date 2016/6/10
 */
public class Request implements Serializable {
    private static final long serialVersionUID = 13440303701165360L;

    private long id;
    private Message message;

    public Request(Message message) {
        id = System.nanoTime();
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
