package com.nishikinomaki.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

/**
 * Created on 2015/7/21.
 * 产生唯一串工具类
 * @author Jax
 */
public class UUIDUtil {

    final static int DEFAULT_RAMDOMLENGTH = 6;

    public static String generateUUID(){
        return generateUUID(DEFAULT_RAMDOMLENGTH);
    }

    public static String generateUUID(int ramdomLength){
        String random = RandomStringUtils.randomAlphanumeric(ramdomLength);
        return random + "-" + UUID.randomUUID().toString();
    }
}
