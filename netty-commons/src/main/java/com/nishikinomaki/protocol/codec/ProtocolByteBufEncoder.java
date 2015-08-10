package com.nishikinomaki.protocol.codec;

import com.nishikinomaki.protocol.message.ProtocolMessage;
import com.nishikinomaki.protocol.message.ProtocolMessageSwapperNetty;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created on 2015/5/27.
 *
 * @author Jax
 */
@Sharable
public class ProtocolByteBufEncoder extends MessageToByteEncoder<ProtocolMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ProtocolMessage msg, ByteBuf out) throws Exception {
        ByteBuf buf = ProtocolMessageSwapperNetty.packDataToByteBuf(msg);
        if(buf.readableBytes() > 0){
            out.writeBytes(buf);
        }
    }
}
