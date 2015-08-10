package com.nishikinomaki.server.config;

import com.nishikinomaki.log.Log;
import com.nishikinomaki.protocol.codec.ProtocolByteBufEncoder;
import com.nishikinomaki.protocol.handler.ProtocolServerHandler;
import com.nishikinomaki.protocol.handler.ServerProtocolExecutorInitializer;
import com.nishikinomaki.protocol.proxy.SpringBeanMethodInvoke;
import com.nishikinomaki.server.ServerShutdownHook;
import com.nishikinomaki.server.TCPServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created on 2015/6/29.
 *
 * @author Jax
 */
@Configuration
public class NettyConfig{

    Logger logger = Log.getLogger();

    //配置boss线程数,boss线程主要处理socket请求,一个端口绑定需要对应一个boss线程
    @Value("${boss.thread.count}")
    private int bossCount;

    @Value("${worker.thread.count}")
    private int workerCount;

    @Value("${tcp.port}")
    private int tcpPort;

    @Value("${so.backlog}")
    private int backlog;

    @Value("${epoll}")
    private boolean epoll;

    @Autowired
    private AbstractApplicationContext context;

    @Autowired
    private ServerShutdownHook serverShutdownHook;

    @Bean(name = "bossGroup" , destroyMethod = "shutdownGracefully")
    public EventLoopGroup bossGroup() {
        if (epoll) {
            return new EpollEventLoopGroup(bossCount);
        } else {
            return new NioEventLoopGroup(bossCount);
        }
    }

    @Bean(name = "workerGroup" , destroyMethod = "shutdownGracefully")
    public EventLoopGroup workerGroup() {
        if (epoll) {
            return new EpollEventLoopGroup(workerCount);
        } else {
            return new NioEventLoopGroup(workerCount);
        }
    }

    @Bean(name = "protocolServerHandler")
    public ProtocolServerHandler protocolServerHandler(){
        ProtocolServerHandler protocolServerHandler = new ProtocolServerHandler();
        protocolServerHandler.setSpringBeanMethodInvoke(new SpringBeanMethodInvoke(context));
        return protocolServerHandler;
    }

    @Bean(name = "channelInitializer")
    public ServerProtocolExecutorInitializer channelInitializer(){
        ServerProtocolExecutorInitializer channelInitializer = new ServerProtocolExecutorInitializer(new ProtocolByteBufEncoder(),protocolServerHandler());
        channelInitializer.setProtocolByteBufEncoder(new ProtocolByteBufEncoder());
        channelInitializer.setProtocolServerHandler(protocolServerHandler());
        return channelInitializer;
    }

    @Bean(name = "tcpChannelOptions")
    public Map<ChannelOption<?>, Object> tcpChannelOptions() {
        Map<ChannelOption<?>, Object> options = new HashMap<>();
        options.put(ChannelOption.SO_BACKLOG, backlog);
        options.put(ChannelOption.SO_KEEPALIVE, true);
        options.put(ChannelOption.TCP_NODELAY, true);
        if (epoll) {
            //options.put(EpollChannelOption.TCP_CORK, );
        }
        return options;
    }

    @Bean(name = "serverBootstrap")
    public ServerBootstrap serverBootstrap() throws InterruptedException {
        SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();//将System.out桥接到slf4j上
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup(), workerGroup());
        logger.info("load epoll mode:" + epoll);
        if (epoll) {
            serverBootstrap.channel(EpollServerSocketChannel.class);
        } else {
            serverBootstrap.channel(NioServerSocketChannel.class);
        }
        serverBootstrap.handler(new LoggingHandler(LogLevel.DEBUG));
        serverBootstrap.childHandler(channelInitializer());
        Map<ChannelOption<?>, Object> tcpChannelOptions = tcpChannelOptions();
        Set<ChannelOption<?>> keySet = tcpChannelOptions.keySet();
        for (ChannelOption option : keySet) {
            serverBootstrap.option(option, tcpChannelOptions.get(option));
        }
        return serverBootstrap;
    }

    @Bean(name = "inetSocketAddress")
    public InetSocketAddress inetSocketAddress(){
        return new InetSocketAddress(tcpPort);
    }

    @Bean(name = "tcpServer")
    public TCPServer tcpServer() throws InterruptedException {
        return new TCPServer(serverBootstrap(), inetSocketAddress());
    }

    @Bean
    public ServerShutdownHook serverShutdownHook() throws InterruptedException {
        return new ServerShutdownHook(serverBootstrap(), channelInitializer().getDefaultEventExecutorGroup());
    }

}
