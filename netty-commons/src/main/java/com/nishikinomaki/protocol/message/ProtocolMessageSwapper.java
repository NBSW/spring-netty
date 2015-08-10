package com.nishikinomaki.protocol.message;

import com.nishikinomaki.io.BytesNumberSwapUtil;
import com.nishikinomaki.log.Log;
import org.slf4j.Logger;

import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;

/**
 * Created on 2015/8/10.
 *
 * @author Jax
 */
public class ProtocolMessageSwapper {

    private static Logger logger = Log.getLogger();
    private final static String JSON_CHARSET = "UTF-8";
    /**
     * 封包,将包转换成byte数组 采用网络字节大端编码
     *
     * @return byte[]
     * @throws java.io.UnsupportedEncodingException
     */
    @Deprecated
    public static byte[] packDataToByteArray(ProtocolMessage protocolMessage) throws UnsupportedEncodingException {
        ProtocolHeader protocolHeader = protocolMessage.getProtocolHeader();
        String json = protocolMessage.getJson();
        if (null == protocolHeader) {
            logger.error("包头为空");
            return null;
        }
        if (null == json) {
            logger.warn("包体为空");
            //json = "";
            return null;
        }
        //获取包体二进制数据
        byte[] entityData = json.getBytes("UTF-8");
        //设置报文长度,报文长度=包体长度
        protocolHeader.setLen(entityData.length);
        int pktLength = ProtocolHeader.PROTO_HEAD_LENGTH + entityData.length;
        //创建二进制数组装载数据包
        byte[] pkt = new byte[pktLength];
        //set cmd_type
        byte[] cmdPkt = BytesNumberSwapUtil.shortToBytes(protocolHeader.getCmdType(), ByteOrder.BIG_ENDIAN);
        pkt[0] = cmdPkt[0];
        pkt[1] = cmdPkt[1];
        //set version
        byte[] versionPkt = BytesNumberSwapUtil.shortToBytes(protocolHeader.getVersion(), ByteOrder.BIG_ENDIAN);
        pkt[2] = versionPkt[0];
        pkt[3] = versionPkt[1];
        //set len
        byte[] lenPkt = BytesNumberSwapUtil.intToBytes(protocolHeader.getLen(), ByteOrder.BIG_ENDIAN);
        pkt[4] = lenPkt[0];
        pkt[5] = lenPkt[1];
        pkt[6] = lenPkt[2];
        pkt[7] = lenPkt[3];
        //set crc
        byte[] crcPkt = BytesNumberSwapUtil.intToBytes(protocolHeader.getCrc(), ByteOrder.BIG_ENDIAN);
        pkt[8] = crcPkt[0];
        pkt[9] = crcPkt[1];
        pkt[10] = crcPkt[2];
        pkt[11] = crcPkt[3];
        //set svrid
        byte[] svridPkt = BytesNumberSwapUtil.intToBytes(protocolHeader.getServerid(), ByteOrder.BIG_ENDIAN);
        pkt[12] = svridPkt[0];
        pkt[13] = svridPkt[1];
        pkt[14] = svridPkt[2];
        pkt[15] = svridPkt[3];
        System.arraycopy(entityData, 0, pkt, ProtocolMessage.ENTITY_INDEX, entityData.length);
        return pkt;
    }

    /**
     * 拆包,解析返回包体数据
     * @param pkt
     * @throws java.io.UnsupportedEncodingException
     */
    @Deprecated
    public static ProtocolMessage analysisPktFromBytes(byte[] pkt)throws UnsupportedEncodingException{
        ProtocolMessage protocolMessage = new ProtocolMessage();
        if (pkt == null || pkt.length < ProtocolHeader.PROTO_HEAD_LENGTH) {
            logger.error("包长不正确");
            return protocolMessage;
        }
        ProtocolHeader protocolHeader = new ProtocolHeader();
        //set cmd_type
        byte[] cmdPkt = new byte[2];
        cmdPkt[0] = pkt[0];
        cmdPkt[1] = pkt[1];
        short cmdType = BytesNumberSwapUtil.bytesToShort(cmdPkt, ByteOrder.BIG_ENDIAN);
        protocolHeader.setCmdType(cmdType);
        //set version
        byte[] versionPkt = new byte[2];
        versionPkt[0] = pkt[2];
        versionPkt[1] = pkt[3];
        short version = BytesNumberSwapUtil.bytesToShort(versionPkt, ByteOrder.BIG_ENDIAN);
        protocolHeader.setVersion(version);
        //set len
        byte[] lenPkt = new byte[4];
        lenPkt[0] = pkt[4];
        lenPkt[1] = pkt[5];
        lenPkt[2] = pkt[6];
        lenPkt[3] = pkt[7];
        int len = BytesNumberSwapUtil.bytesToInt(lenPkt, ByteOrder.BIG_ENDIAN);
        protocolHeader.setLen(len);
        //set crc
        byte[] crcPkt = new byte[4];
        crcPkt[0] = pkt[8];
        crcPkt[1] = pkt[9];
        crcPkt[2] = pkt[10];
        crcPkt[3] = pkt[11];
        int crc = BytesNumberSwapUtil.bytesToInt(crcPkt, ByteOrder.BIG_ENDIAN);
        protocolHeader.setCrc(crc);
        //set svrid
        byte[] svridPkt = new byte[4];
        svridPkt[0] = pkt[12];
        svridPkt[1] = pkt[13];
        svridPkt[2] = pkt[14];
        svridPkt[3] = pkt[15];
        int svrid = BytesNumberSwapUtil.bytesToInt(svridPkt, ByteOrder.BIG_ENDIAN);
        protocolHeader.setServerid(svrid);
        //包体长度
        int entityDateLength = pkt.length - ProtocolHeader.PROTO_HEAD_LENGTH;
        byte[] entityData = new byte[entityDateLength];
        System.arraycopy(pkt, ProtocolMessage.ENTITY_INDEX, entityData, 0, entityDateLength);
        String json = new String(entityData, JSON_CHARSET);
        //构建新的对象传回
        protocolMessage.setJson(json);
        protocolMessage.setProtocolHeader(protocolHeader);
        return protocolMessage;
    }

}
