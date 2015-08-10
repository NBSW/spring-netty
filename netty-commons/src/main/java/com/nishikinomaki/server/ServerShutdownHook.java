package com.nishikinomaki.server;

import com.nishikinomaki.log.Log;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;

/**
 * Created on 2015/6/9.
 *
 * @author Jax
 */
public class ServerShutdownHook {

    private Logger logger = Log.getLogger();

    private ServerBootstrap bootstrap;

    private DefaultEventExecutorGroup eventExecutorGroup;

    public ServerShutdownHook(ServerBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    public ServerShutdownHook(ServerBootstrap bootstrap, DefaultEventExecutorGroup eventExecutorGroup) {
        this.bootstrap = bootstrap;
        this.eventExecutorGroup = eventExecutorGroup;
    }

    @PostConstruct
    public void registerHook(){
        //注册关闭钩子,确保所有线程执行完毕再关闭服务
        //强杀进程无法触发钩子
        logger.info("register shutdownHook");
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                try {
                    logger.info("shutdownHook is running, please wait....");
                    EventLoopGroup bossGroup = bootstrap.group();
                    EventLoopGroup workerGroup = bootstrap.childGroup();
                    if(eventExecutorGroup != null){
                        logger.info("begin to shutdown defaultEventExecutorGroup.....");
                        eventExecutorGroup.shutdownGracefully().sync();
                        logger.info("begin to shutdown success");
                    }
                    logger.info("begin to shutdown workerGroup.....");
                    workerGroup.shutdownGracefully().sync();
                    logger.info("workerGroup shutdown success");
                    logger.info("begin to shutdown bossGroup.....");
                    bossGroup.shutdownGracefully().sync();
                    logger.info("bossGroup shutdown success");
                } catch (InterruptedException e) {
                    logger.error("shutdownhook error", e);
                }
            }
        });
    }
}
