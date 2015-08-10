package com.nishikinomaki.util;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

/**
 * Created on 2015/6/10.
 *
 * @author Jax
 */
public class NettyChannelAliveUtil {

    /**
     * Closes the specified channel after all queued write requests are flushed.
     * @param ch
     */
    public static void closeOnFlush(Channel ch) {
        if (ch.isActive()) {
            ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }
}
