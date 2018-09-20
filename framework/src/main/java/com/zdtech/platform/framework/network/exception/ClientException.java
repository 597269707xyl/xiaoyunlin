package com.zdtech.platform.framework.network.exception;

/**
 * ClientException
 *
 * @author panli
 * @date 2016/6/10
 */
public class ClientException extends RuntimeException {
    public ClientException(final Exception cause) {
        super(cause);
    }
}
