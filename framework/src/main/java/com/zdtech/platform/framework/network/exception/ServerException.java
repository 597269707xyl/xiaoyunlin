package com.zdtech.platform.framework.network.exception;

/**
 * ServerException
 *
 * @author panli
 * @date 2016/6/10
 */
public class ServerException extends RuntimeException{
    public ServerException(final Exception cause) {
        super(cause);
    }
}
