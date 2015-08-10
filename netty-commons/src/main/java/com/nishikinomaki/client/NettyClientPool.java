package com.nishikinomaki.client;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import javax.annotation.PreDestroy;
import java.io.Closeable;

/**
 * Created on 2015/7/20.
 *
 * @author Jax
 */
public class NettyClientPool implements Closeable{

    protected GenericObjectPool<NettyClient> pool;

    public NettyClientPool() {
    }

    @Override
    @PreDestroy
    public void close() {
        closeInternalPool();
    }

    public boolean isClosed() {
        return this.pool.isClosed();
    }

    public NettyClientPool(final GenericObjectPoolConfig poolConfig, PooledObjectFactory<NettyClient> factory) {
        initPool(poolConfig, factory);
    }

    public void initPool(final GenericObjectPoolConfig poolConfig, PooledObjectFactory<NettyClient> factory) {
        if (this.pool != null) {
            try {
                closeInternalPool();
            } catch (Exception e) {
            }
        }
        AbandonedConfig abandonedConfig = new AbandonedConfig();
        this.pool = new GenericObjectPool<NettyClient>(factory, poolConfig, abandonedConfig);
    }

    public NettyClient getResource() {
        try {
            return pool.borrowObject();
        } catch (Exception e) {
            throw new NettyConnectionException("Could not get a resource from the pool", e);
        }
    }

    public void returnResourceObject(final NettyClient resource) {
        if (resource == null) {
            return;
        }
        try {
            pool.returnObject(resource);
        } catch (Exception e) {
            throw new NettyConnectionException("Could not return the resource to the pool", e);
        }
    }

    public void returnBrokenResource(final NettyClient resource) {
        if (resource != null) {
            returnBrokenResourceObject(resource);
        }
    }

    public void returnResource(final NettyClient resource) {
        if (resource != null) {
            try{
                returnResourceObject(resource);
            }catch (NettyConnectionException e){
                e.printStackTrace();
                returnBrokenResource(resource);
            }
        }
    }

    public void destroy() {
        closeInternalPool();
    }

    protected void returnBrokenResourceObject(final NettyClient resource) {
        try {
            pool.invalidateObject(resource);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NettyConnectionException("Could not return the resource to the pool", e);
        }
    }

    protected void closeInternalPool() {
        try {
            pool.close();
        } catch (Exception e) {
            throw new NettyConnectionException("Could not destroy the pool", e);
        }
    }

    public int getNumActive() {
        if (this.pool == null || this.pool.isClosed()) {
            return -1;
        }
        return this.pool.getNumActive();
    }

    public int getNumIdle() {
        if (this.pool == null || this.pool.isClosed()) {
            return -1;
        }
        return this.pool.getNumIdle();
    }

    public int getNumWaiters() {
        if (this.pool == null || this.pool.isClosed()) {
            return -1;
        }
        return this.pool.getNumWaiters();
    }
}
