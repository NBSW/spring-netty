package com.nishikinomaki.client;

import com.nishikinomaki.log.Log;
import com.nishikinomaki.protocol.message.ProtocolMessage;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;

@Sharable
public class ClientHandler extends SimpleChannelInboundHandler<ProtocolMessage> {

    Logger logger = Log.getLogger();

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ProtocolMessage msg)
            throws Exception {
        logger.info("接收服务端响应:" + msg);
        ctx.executor().execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("Channel is active");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("Channel is disconnected");
        super.channelInactive(ctx);
    }
}
