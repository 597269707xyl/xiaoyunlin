package com.zdtech.platform.framework.net;

/**
 * Client
 *
 * @author panli
 * @date 2016/5/23
 */
public interface Client {
    void connect() throws Exception;
    <T> void send(T msg);
    void close();
}
