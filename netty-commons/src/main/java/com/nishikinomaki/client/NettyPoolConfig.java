package com.nishikinomaki.client;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * Created on 2015/7/20.
 *
 * @author Jax
 */
public class NettyPoolConfig extends GenericObjectPoolConfig {

    public NettyPoolConfig() {
        setTestOnBorrow(true);
        //踢出池对象的最小空闲时间 默认600000毫秒(10分钟)
        setMinEvictableIdleTimeMillis(600000);
        //对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)
        setSoftMinEvictableIdleTimeMillis(600000);
        //踢出线程的运行间隔10分钟
        setTimeBetweenEvictionRunsMillis(600000);
        setNumTestsPerEvictionRun(-1);
        //设置获取连接最大等待时间，单位milliseconds，此处如果池已没空闲链接,borrowObject()会阻塞，超过时间后会抛异常，值为-1时表示一直阻塞
        setMaxWaitMillis(-1);
    }

}
