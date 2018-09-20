package com.zdtech.platform.framework.network;

import com.zdtech.platform.framework.network.entity.Request;
import com.zdtech.platform.framework.network.entity.Response;

import java.net.InetSocketAddress;

/**
 * Client
 *
 * @author panli
 * @date 2016/6/10
 */
public interface Client {
    void connect();
    Response send(Request msg);
    InetSocketAddress getRemoteAddress();
    void close();
}
