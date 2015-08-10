package com.nishikinomaki.client;

/**
 * Created on 2015/7/24.
 *
 * @author Jax
 */
public class NettyPoolException extends RuntimeException {
    public NettyPoolException() {
        super();
    }

    public NettyPoolException(String message) {
        super(message);
    }

    public NettyPoolException(String message, Throwable cause) {
        super(message, cause);
    }

    public NettyPoolException(Throwable cause) {
        super(cause);
    }

    protected NettyPoolException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
