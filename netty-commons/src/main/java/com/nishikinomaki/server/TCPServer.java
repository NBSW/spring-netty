package com.nishikinomaki.server;

import com.nishikinomaki.log.Log;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;

public class TCPServer {

    private ServerBootstrap bootstrap;

    private InetSocketAddress tcpSocketAddress;

    private Logger logger = Log.getLogger();

    public TCPServer(ServerBootstrap bootstrap, InetSocketAddress tcpSocketAddress) {
        this.bootstrap = bootstrap;
        this.tcpSocketAddress = tcpSocketAddress;
    }

    @PostConstruct
    public void start() throws InterruptedException {
        logger.info("Starting server bind " + tcpSocketAddress);
        Channel serverChannel = null;
        try {
            ChannelFuture future =  bootstrap.bind(tcpSocketAddress).sync();
            serverChannel = future.channel();
            serverChannel.closeFuture().sync();
        } catch (Exception e) {
            logger.error("server error", e);
        } finally {
            if(!bootstrap.childGroup().isShutdown()){
                bootstrap.childGroup().shutdownGracefully().sync();
            }
            if(!bootstrap.group().isShutdown()){
                bootstrap.group().shutdownGracefully().sync();
            }
            serverChannel.close();
        }
    }

}
