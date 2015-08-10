package com.nishikinomaki.io;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author Jax.Gong
 * @date:2015年5月25日
 */
public class BytesNumberSwapUtil {

    public static byte[] shortToBytes(short value,ByteOrder bo) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(2);
        byteBuffer.order(bo);
        byteBuffer.putShort(value);
        return byteBuffer.array();
    }

    public static byte[] intToBytes(int value, ByteOrder bo) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.order(bo);
        byteBuffer.putInt(value);
        return byteBuffer.array();
    }

    public static byte[] longToBytes(long value, ByteOrder bo) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        byteBuffer.order(bo);
        byteBuffer.putLong(value);
        return byteBuffer.array();
    }

    public static short bytesToShort(byte[] bts, ByteOrder bo) {
        if (bts == null || bts.length != 2)
            return 0;
        return ByteBuffer.wrap(bts).order(bo).getShort();
    }

    public static int bytesToInt(byte[] bts, ByteOrder bo) {
        if (bts == null || bts.length != 4)
            return 0;
        return ByteBuffer.wrap(bts).order(bo).getInt();
    }

    public static long bytesToLong(byte[] bts, ByteOrder bo) {
        if (bts == null || bts.length != 8)
            return 0;
        return ByteBuffer.wrap(bts).order(bo).getLong();
    }

}
