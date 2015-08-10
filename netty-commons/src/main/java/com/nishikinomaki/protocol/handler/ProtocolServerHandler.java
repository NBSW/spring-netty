package com.nishikinomaki.protocol.handler;

import com.nishikinomaki.log.Log;
import com.nishikinomaki.protocol.message.ProtocolMessage;
import com.nishikinomaki.protocol.proxy.SpringBeanMethodInvoke;
import com.nishikinomaki.util.NettyChannelAliveUtil;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;

@Sharable
public class ProtocolServerHandler extends SimpleChannelInboundHandler<ProtocolMessage> {

    Logger logger = Log.getLogger();

    private SpringBeanMethodInvoke springBeanMethodInvoke;

    @Override
    public void channelRead0(ChannelHandlerContext ctx,ProtocolMessage msg) {
        logger.debug("接受客户端响应:" + msg);
        final ProtocolMessage readMessage = msg;
        final ChannelHandlerContext channelHandlerContext = ctx;
        ctx.executor().execute(new Runnable() {
            @Override
            public void run() {
                //invokeMethod方法调用的是spring管理的类,非spring管理的类暂时不支持
                ProtocolMessage resultMsg = null;
                try {
                    resultMsg = springBeanMethodInvoke.invokeMethod(readMessage);
                } catch (Exception e) {
                    logger.error("ProtocolServerHandler error", e);
                }finally {
                    logger.debug("调用结果:" + resultMsg);
                    if (resultMsg != null) {
                        channelHandlerContext.channel().writeAndFlush(resultMsg);
                    }
                }
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
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.warn("exceptionCaught", cause);
        NettyChannelAliveUtil.closeOnFlush(ctx.channel());
    }

    public void setSpringBeanMethodInvoke(SpringBeanMethodInvoke springBeanMethodInvoke) {
        this.springBeanMethodInvoke = springBeanMethodInvoke;
    }


}
