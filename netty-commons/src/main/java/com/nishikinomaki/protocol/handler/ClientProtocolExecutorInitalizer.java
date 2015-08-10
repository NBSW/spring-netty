package com.nishikinomaki.protocol.handler;

import com.nishikinomaki.protocol.codec.ProtocolByteBufDecoder;
import com.nishikinomaki.protocol.codec.ProtocolByteBufEncoder;
import com.nishikinomaki.protocol.codec.ProtocolLengthFieldDecoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

/**
 * Created on 2015/5/27.
 *
 * @author Jax
 */
@Sharable
public class ClientProtocolExecutorInitalizer extends ChannelInitializer<SocketChannel> {

    final static int DEFAULT_THREADS_COUNT = Runtime.getRuntime().availableProcessors() * 2;

    private boolean useExecutors = false;

    ProtocolByteBufEncoder protocolByteBufEncoder;

    ChannelHandler clientCoreHandler;

    ReadTimeoutHandler readTimeoutHandler;

    private int threadsCount;

    public ClientProtocolExecutorInitalizer(){}

    public ClientProtocolExecutorInitalizer(ProtocolByteBufEncoder protocolByteBufEncoder, ChannelHandler clientCoreHandler) {
        this.protocolByteBufEncoder = protocolByteBufEncoder;
        this.clientCoreHandler = clientCoreHandler;
    }

    public ClientProtocolExecutorInitalizer(ProtocolByteBufEncoder protocolByteBufEncoder, ChannelHandler clientCoreHandler, int threadsCount) {
        this.protocolByteBufEncoder = protocolByteBufEncoder;
        this.clientCoreHandler = clientCoreHandler;
        this.threadsCount = threadsCount;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        if(clientCoreHandler == null){
            throw new IllegalArgumentException("clientCoreHandler must not be null");
        }
        ChannelPipeline pipeline = ch.pipeline();
        if(readTimeoutHandler != null){
            pipeline.addLast(readTimeoutHandler);
        }
        pipeline.addLast(new ProtocolLengthFieldDecoder());
        pipeline.addLast(new ProtocolByteBufDecoder());
        if(useExecutors){
            pipeline.addLast(new DefaultEventExecutorGroup(threadsCount > 0 ? threadsCount : DEFAULT_THREADS_COUNT), "clientHandlerExecutor", clientCoreHandler);
        }else{
            pipeline.addLast("clientCoreHandler", clientCoreHandler);
        }
        pipeline.addLast(protocolByteBufEncoder);
    }

    public void setClientCoreHandler(ChannelHandler clientCoreHandler) {
        this.clientCoreHandler = clientCoreHandler;
    }

    public void setProtocolByteBufEncoder(ProtocolByteBufEncoder protocolByteBufEncoder) {
        this.protocolByteBufEncoder = protocolByteBufEncoder;
    }

    public void setThreadsCount(int threadsCount) {
        this.threadsCount = threadsCount;
    }

    public void setUseExecutors(boolean useExecutors) {
        this.useExecutors = useExecutors;
    }

    public void setReadTimeoutHandler(ReadTimeoutHandler readTimeoutHandler) {
        this.readTimeoutHandler = readTimeoutHandler;
    }
}
