package com.nishikinomaki.protocol.message;

/**
 * @author Jax.Gong
 * date:2015年5月25日 内部通信协议包
 * +-----------------------------------------------------------------------------+
 * |                        包头（二进制数据）                           |报文     |
 * |  命令类型   |   版本号   |  长度  | 校验CRC | 服务器ID |    预备     | 数据    |
 *  cmd_type(2)  version(2)   len(4)   crc(4)   serverid(4)  extend(16)   json(N)
 * +---------------------------------------------------------------------------+
 */
public final class ProtocolMessage {

    private ProtocolHeader protocolHeader = new ProtocolHeader();
    private String json;
    /**
     * 包体数据的索引位置
     */
    public final static int ENTITY_INDEX = 32;
    /**
     * 扩展字段长度
     */
    public final static int EXTEND_LENGTH = 16;
    public ProtocolMessage() {
    }

    public ProtocolHeader getProtocolHeader() {
        return protocolHeader;
    }

    public void setProtocolHeader(ProtocolHeader protocolHeader) {
        this.protocolHeader = protocolHeader;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @Override
    public String toString() {
        return "ProtocolMessage{" +
                "protocolHeader=" + protocolHeader +
                ", json='" + json + '\'' +
                '}';
    }
}
