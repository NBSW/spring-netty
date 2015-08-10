package com.nishikinomaki.client;

/**
 * Created on 2015/7/20.
 *
 * @author Jax
 */
public class NettyConnectionException extends RuntimeException{

    public NettyConnectionException() {
        super();
    }

    public NettyConnectionException(String message) {
        super(message);
    }

    public NettyConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public NettyConnectionException(Throwable cause) {
        super(cause);
    }
}
