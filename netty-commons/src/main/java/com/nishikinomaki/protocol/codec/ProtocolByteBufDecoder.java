package com.nishikinomaki.protocol.codec;

import com.nishikinomaki.protocol.message.ProtocolMessage;
import com.nishikinomaki.protocol.message.ProtocolMessageSwapperNetty;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created on 2015/6/10.
 *
 * @author Jax
 * 此解码器用于解决拆包粘包问题
 */
public class ProtocolByteBufDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(in.readableBytes() > 0){
            ProtocolMessage protocolMessage = ProtocolMessageSwapperNetty.analysisPktFromByteBuf(in);
            out.add(protocolMessage);
        }
    }
}
