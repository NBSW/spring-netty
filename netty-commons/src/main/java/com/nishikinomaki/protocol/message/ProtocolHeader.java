package com.nishikinomaki.protocol.message;

/**
 * Created on 2015/8/10.
 *内部通信协议包头
 * @author Jax
 */
public final class ProtocolHeader {
    /**
     * 包头长度(32byte)
     */
    public final static int PROTO_HEAD_LENGTH = 32;

    /**
     * 命令类型(2byte)，供内部使用， 必填
     */
    private short cmdType = 1;

    /**
     * 版本信息，供内部使用(2byte)
     */
    private short version = 1;

    /**
     * 报文长度(4byte) = 包体字符串长度(不定长)
     */
    private int len;

    /**
     * CRC校验(4byte)，供内部使用
     */
    private int crc = 0;

    /**
     * 服务器id
     */
    private int serverid;

    /**
     * 预留16个byte的数据，预备扩展使用
     */
    private byte[] extend;

    public short getCmdType() {
        return cmdType;
    }

    public void setCmdType(short cmdType) {
        this.cmdType = cmdType;
    }

    public short getVersion() {
        return version;
    }

    public void setVersion(short version) {
        this.version = version;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public int getCrc() {
        return crc;
    }

    public void setCrc(int crc) {
        this.crc = crc;
    }

    public int getServerid() {
        return serverid;
    }

    public void setServerid(int serverid) {
        this.serverid = serverid;
    }

    public byte[] getExtend() {
        return extend;
    }

    public void setExtend(byte[] extend) {
        this.extend = extend;
    }

    @Override
    public String toString() {
        return "ProtocolHeader{" +
                "cmdType=" + cmdType +
                ", version=" + version +
                ", len=" + len +
                ", crc=" + crc +
                ", serverid=" + serverid +
                '}';
    }
}
