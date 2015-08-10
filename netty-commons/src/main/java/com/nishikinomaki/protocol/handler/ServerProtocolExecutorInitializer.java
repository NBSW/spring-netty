package com.nishikinomaki.protocol.handler;

import com.nishikinomaki.log.Log;
import com.nishikinomaki.protocol.codec.ProtocolByteBufDecoder;
import com.nishikinomaki.protocol.codec.ProtocolByteBufEncoder;
import com.nishikinomaki.protocol.codec.ProtocolLengthFieldDecoder;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import org.slf4j.Logger;

/**
 * Created on 2015/5/27.
 *
 * @author Jax
 */
@Sharable
public class ServerProtocolExecutorInitializer extends ChannelInitializer<SocketChannel> {

    Logger logger = Log.getLogger();

    private int DEFAULT_THREAD_COUNT = Runtime.getRuntime().availableProcessors() * 2;

    ProtocolByteBufEncoder protocolByteBufEncoder;

    ProtocolServerHandler protocolServerHandler;

    DefaultEventExecutorGroup defaultEventExecutorGroup;

    public ServerProtocolExecutorInitializer(ProtocolByteBufEncoder protocolByteBufEncoder, ProtocolServerHandler protocolServerHandler) {
        this.protocolByteBufEncoder = protocolByteBufEncoder;
        this.protocolServerHandler = protocolServerHandler;
        this.defaultEventExecutorGroup = new DefaultEventExecutorGroup(DEFAULT_THREAD_COUNT);
    }

    public ServerProtocolExecutorInitializer(ProtocolByteBufEncoder protocolByteBufEncoder,
                                             ProtocolServerHandler protocolServerHandler,
                                             int threadsCount) {
        this.protocolByteBufEncoder = protocolByteBufEncoder;
        this.protocolServerHandler = protocolServerHandler;
        this.defaultEventExecutorGroup = new DefaultEventExecutorGroup(threadsCount);
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        if(protocolServerHandler == null){
            throw new IllegalArgumentException("protocolServerHandler must not be null");
        }
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new ProtocolLengthFieldDecoder());
        pipeline.addLast(new ProtocolByteBufDecoder());
        pipeline.addLast(defaultEventExecutorGroup, "businessExecutorGroup", protocolServerHandler);
        pipeline.addLast(protocolByteBufEncoder);
    }

    public void setProtocolByteBufEncoder(ProtocolByteBufEncoder protocolByteBufEncoder) {
        this.protocolByteBufEncoder = protocolByteBufEncoder;
    }

    public void setProtocolServerHandler(ProtocolServerHandler protocolServerHandler) {
        this.protocolServerHandler = protocolServerHandler;
    }

    public DefaultEventExecutorGroup getDefaultEventExecutorGroup() {
        return defaultEventExecutorGroup;
    }
}
