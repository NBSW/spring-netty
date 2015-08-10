package com.nishikinomaki.protocol.message;

import com.nishikinomaki.log.Log;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;

import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;

/**
 * Created on 2015/8/10.
 *
 * @author Jax
 */
public class ProtocolMessageSwapperNetty {

    private static Logger logger = Log.getLogger();

    private final static String JSON_CHARSET = "UTF-8";
    /**
     * 封包,将包转换成byte数组 采用网络字节大端编码
     *
     * @return byte[]
     * @throws java.io.UnsupportedEncodingException
     */
    public static ByteBuf packDataToByteBuf(ProtocolMessage protocolMessage) throws UnsupportedEncodingException {
        ProtocolHeader protocolHeader = protocolMessage.getProtocolHeader();
        String json = protocolMessage.getJson();
        if (null == protocolHeader) {
            logger.error("包头为空");
            return null;
        }
        if (null == json) {
            logger.warn("包体为空");
            return null;
        }
        //获取包体二进制数据
        byte[] entityData = json.getBytes("UTF-8");
        //设置报文长度,报文长度=包体长度
        protocolHeader.setLen(entityData.length);
        int pktLength = ProtocolHeader.PROTO_HEAD_LENGTH + entityData.length;
        ByteBuf buf = Unpooled.buffer(pktLength);
        buf.order(ByteOrder.BIG_ENDIAN);
        buf.writeShort(protocolHeader.getCmdType());
        buf.writeShort(protocolHeader.getVersion());
        buf.writeInt(protocolHeader.getLen());
        buf.writeInt(protocolHeader.getCrc());
        buf.writeInt(protocolHeader.getServerid());
        //包头结束，从包体开始
        buf.writerIndex(ProtocolHeader.PROTO_HEAD_LENGTH);
        buf.writeBytes(entityData, 0, entityData.length);
        return buf;
    }

    public static ProtocolMessage analysisPktFromByteBuf(ByteBuf buf) throws UnsupportedEncodingException {
        ProtocolMessage protocolMessage = new ProtocolMessage();
        int readableBytes = buf.readableBytes();
        if(readableBytes < ProtocolHeader.PROTO_HEAD_LENGTH){
            logger.error("包长不正确");
            return protocolMessage;
        }
        buf.order(ByteOrder.BIG_ENDIAN);
        short cmdType = buf.readShort();
        short version = buf.readShort();
        int len = buf.readInt();
        int crc = buf.readInt();
        int svrid = buf.readInt();
        byte[] reserve = new byte[ProtocolMessage.EXTEND_LENGTH];
        buf.readBytes(reserve, 0, ProtocolMessage.EXTEND_LENGTH);
        //包体长度
        int entityDataLength = readableBytes - ProtocolHeader.PROTO_HEAD_LENGTH;
        byte[] entityData = new byte[entityDataLength];
        buf.readBytes(entityData, 0, entityDataLength);
        String json = new String(entityData, JSON_CHARSET);
        ProtocolHeader protocolHeader = new ProtocolHeader();
        protocolHeader.setCmdType(cmdType);
        protocolHeader.setVersion(version);
        protocolHeader.setLen(len);
        protocolHeader.setCrc(crc);
        protocolHeader.setServerid(svrid);
        //构建新的对象传回
        protocolMessage.setJson(json);
        protocolMessage.setProtocolHeader(protocolHeader);
        return protocolMessage;
    }
}
