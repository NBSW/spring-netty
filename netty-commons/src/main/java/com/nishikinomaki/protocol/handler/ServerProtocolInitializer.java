package com.nishikinomaki.protocol.handler;

import com.nishikinomaki.protocol.codec.ProtocolByteBufDecoder;
import com.nishikinomaki.protocol.codec.ProtocolByteBufEncoder;
import com.nishikinomaki.protocol.codec.ProtocolLengthFieldDecoder;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * Created on 2015/5/27.
 *
 * @author Jax
 */
@Sharable
public class ServerProtocolInitializer extends ChannelInitializer<SocketChannel> {

    ProtocolByteBufEncoder protocolByteBufEncoder;

    ProtocolServerHandler protocolServerHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new ProtocolLengthFieldDecoder());
        pipeline.addLast(new ProtocolByteBufDecoder());
        pipeline.addLast(protocolServerHandler);
        pipeline.addLast(protocolByteBufEncoder);
    }

    public void setProtocolByteBufEncoder(ProtocolByteBufEncoder protocolByteBufEncoder) {
        this.protocolByteBufEncoder = protocolByteBufEncoder;
    }

    public void setProtocolServerHandler(ProtocolServerHandler protocolServerHandler) {
        this.protocolServerHandler = protocolServerHandler;
    }
}
