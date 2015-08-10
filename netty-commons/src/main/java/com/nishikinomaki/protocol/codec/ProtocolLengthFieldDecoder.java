package com.nishikinomaki.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Created on 2015/6/10.
 *
 * @author Jax
 * 包长解码器,解决拆包粘包
 */
public class ProtocolLengthFieldDecoder extends LengthFieldBasedFrameDecoder {

    /**
     *  maxFrameLength 定义最大数据包长度 100MB
     */
    private static final int MAX_PACKET_LENGTH = 1024 * 1024 * 100;
    /**
     *  lengthFieldOffset len字段偏移量
     */
    private static final int LENGTH_FIELD_OFFSET = 4;
    /**
     *  lengthFieldLength len字段长度
     */
    private static final int LENGTH_FIELD_LENGTH = 4;
    /**
     *  lengthAdjustment 调整长度(len与包体之间的长度)
     */
    private static final int LENGTH_FIELD_ADJUSTMENT = 24;
    /**
     *  initialBytesToStrip 跳过字节数(0表示不跳过,保留包头数据)
     */
    private static final int INITIAL_BYTES_TO_STRIP = 0;


    public ProtocolLengthFieldDecoder() {
        super(MAX_PACKET_LENGTH, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH, LENGTH_FIELD_ADJUSTMENT, INITIAL_BYTES_TO_STRIP);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        return super.decode(ctx, in);
    }
}
