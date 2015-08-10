package com.nishikinomaki.client;

import com.nishikinomaki.log.Log;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2015/7/17.
 *
 * @author Jax
 */
public class NettyClientFactory extends BasePooledObjectFactory<NettyClient> {

    private Logger logger = Log.getLogger();

    private String ip;

    private int port;

    private boolean epoll = false;

    private Bootstrap bootstrap;

    private ChannelInitializer channelInitializer;

    private Map<ChannelOption<?>, Object> tcpChannelOptions;

    public NettyClientFactory(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void setEpoll(boolean epoll) {
        this.epoll = epoll;
    }

    public void setChannelInitializer(ChannelInitializer channelInitializer) {
        this.channelInitializer = channelInitializer;
    }

    public void setTcpChannelOptions(Map<ChannelOption<?>, Object> tcpChannelOptions) {
        this.tcpChannelOptions = tcpChannelOptions;
    }

    public Bootstrap createBootStrap(ChannelInitializer channelInitializer){
        Bootstrap bootstrap = new Bootstrap();
        if(epoll){
            bootstrap.group(new EpollEventLoopGroup());
            bootstrap.channel(EpollSocketChannel.class);
        }else{
            bootstrap.group(new NioEventLoopGroup());
            bootstrap.channel(NioSocketChannel.class);
        }
        bootstrap.handler(new LoggingHandler(LogLevel.INFO)).handler(channelInitializer);
        if(tcpChannelOptions == null || tcpChannelOptions.isEmpty()){
            tcpChannelOptions = tcpChannelOptions();
        }
        for (ChannelOption option : tcpChannelOptions.keySet()) {
            bootstrap.option(option, tcpChannelOptions.get(option));
        }
        return bootstrap;
    }

    public Map<ChannelOption<?>, Object> tcpChannelOptions() {
        Map<ChannelOption<?>, Object> options = new HashMap<>();
        options.put(ChannelOption.SO_KEEPALIVE, true);
        options.put(ChannelOption.TCP_NODELAY, true);
        return options;
    }

    @Override
    public NettyClient create() throws Exception {
        if(bootstrap == null){
            bootstrap = createBootStrap(channelInitializer);
        }
        Channel channel = bootstrap.connect(ip, port).sync().channel();
        NettyClient client = new NettyClient();
        client.setChannel(channel);
        return client;
    }

    @Override
    public PooledObject<NettyClient> wrap(NettyClient client) {
        return new DefaultPooledObject<>(client);
    }

    @Override
    public void activateObject(PooledObject<NettyClient> p) throws Exception {
        p.getObject().setLastActiveTimestamp(System.currentTimeMillis());
    }

    @Override
    public boolean validateObject(PooledObject<NettyClient> p) {
        return super.validateObject(p);
    }

    @PreDestroy
    public void destroy(){
        if(bootstrap != null){
            bootstrap.group().shutdownGracefully().syncUninterruptibly();
        }
    }

    @Override
    public void destroyObject(PooledObject<NettyClient> p) throws Exception {
        NettyClient client = p.getObject();
        client.close();
    }

}
