package com.zdtech.platform.framework.net;

/**
 * server
 *
 * @author panli
 * @date 2016/5/23
 */
public interface Server {
    void run();

    <T> void processReq(T req);

    void stop();
}
