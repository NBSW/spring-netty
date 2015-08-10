package com.nishikinomaki.client;

import org.springframework.beans.factory.InitializingBean;

/**
 * Created on 2015/7/21.
 *
 * @author Jax
 */
public class NettyPoolFactory implements InitializingBean {

    private boolean usePool = true;

    private NettyPoolConfig poolConfig = new NettyPoolConfig();

    private NettyClientPool pool;

    private NettyClientFactory nettyClientFactory;

    public NettyPoolFactory() {
    }

    public NettyPoolFactory(NettyPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }

    public NettyPoolFactory(NettyPoolConfig poolConfig, NettyClientFactory nettyClientFactory) {
        this.poolConfig = poolConfig;
        this.nettyClientFactory = nettyClientFactory;
    }

    protected NettyClientPool createNettyConnectionPool(){
        return new NettyClientPool(poolConfig, nettyClientFactory);
    }

    public NettyClient getClient(){
        try {
            if(pool != null && usePool){
                return pool.getResource();
            }else if(pool == null && usePool) {
                pool = createNettyConnectionPool();
            }
            return nettyClientFactory.create();
        } catch (Exception e) {
            throw new NettyPoolException("Cannot get client from pool", e);
        }
    }

    public void setUsePool(boolean usePool) {
        this.usePool = usePool;
    }

    public void setPoolConfig(NettyPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }

    public void setNettyClientFactory(NettyClientFactory nettyClientFactory) {
        this.nettyClientFactory = nettyClientFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(usePool){
            pool = createNettyConnectionPool();
        }
    }

    public NettyClientPool getPool() {
        return pool;
    }

}
