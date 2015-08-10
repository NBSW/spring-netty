package com.nishikinomaki.client;

import com.nishikinomaki.util.UUIDUtil;
import io.netty.channel.Channel;

import javax.annotation.PreDestroy;

/**
 * Created on 2015/7/21.
 *
 * @author Jax
 */
public class NettyClient {

    private String channelId = "channel-" + UUIDUtil.generateUUID() + System.nanoTime();

    private Channel channel;

    private long lastActiveTimestamp = 0;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @PreDestroy
    public void close(){
        if(channel != null){
            channel.disconnect().syncUninterruptibly();
        }
    }

    @Override
    public String toString() {
        String localAddress = channel != null && channel.localAddress() != null ? channel.localAddress().toString() : "";
        return "channelId:" + channelId + ", localAddress:" + localAddress;
    }

    public long getLastActiveTimestamp() {
        return lastActiveTimestamp;
    }

    public void setLastActiveTimestamp(long lastActiveTimestamp) {
        this.lastActiveTimestamp = lastActiveTimestamp;
    }
}
