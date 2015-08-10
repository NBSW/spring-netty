package com.nishikinomaki.client;

import com.alibaba.fastjson.JSONObject;
import com.nishikinomaki.protocol.codec.ProtocolByteBufEncoder;
import com.nishikinomaki.protocol.handler.ClientProtocolExecutorInitalizer;
import com.nishikinomaki.protocol.message.ProtocolMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2015/7/21.
 *
 * @author Jax
 */
public class TestClientPool {

    public static void main(String[] args) throws Exception {
        singleClientTest();
    }

    //池调用demo
    public static void poolTest(){
        String ip = "127.0.0.1";
        int port = 12222;
        NettyClientFactory nettyClientFactory = new NettyClientFactory(ip, port);
        ClientProtocolExecutorInitalizer channelInitializer = new ClientProtocolExecutorInitalizer(new ProtocolByteBufEncoder(), new ClientHandler());
        nettyClientFactory.setChannelInitializer(channelInitializer);
        NettyPoolConfig poolConfig = new NettyPoolConfig();
        poolConfig.setMaxTotal(10);//设置连接池最大连接数
        poolConfig.setMaxIdle(5);//设置最大空闲连接数
        poolConfig.setMinIdle(0);//设置最低空闲连接数
        NettyPoolFactory poolFactory = new NettyPoolFactory(poolConfig, nettyClientFactory);
        NettyClient client = poolFactory.getClient();
        client.getChannel().writeAndFlush(buildMessage(""+1)).syncUninterruptibly();
        //释放对象
        poolFactory.getPool().returnResource(client);
    }

    public static void singleClientTest() throws Exception {
        String ip = "127.0.0.1";
        int port = 12222;
        NettyClientFactory nettyClientFactory = new NettyClientFactory(ip, port);
        ClientProtocolExecutorInitalizer channelInitializer = new ClientProtocolExecutorInitalizer(new ProtocolByteBufEncoder(), new ClientHandler());
        nettyClientFactory.setChannelInitializer(channelInitializer);
        NettyClient client = nettyClientFactory.create();
        client.getChannel().writeAndFlush(buildMessage(""+1)).syncUninterruptibly();
        //client.close();
    }

    public static ProtocolMessage buildMessage(String data){
        ProtocolMessage protoMessage = new ProtocolMessage();
        Map<String, Object> dataMap = new HashMap<>();
        Map<String, String> methodParams = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        params.put("methodParams", methodParams);
        methodParams.put("data", data);
        params.put("method", "test");
        dataMap.put("class", "testDemo");
        dataMap.put("params", params);
        String json = JSONObject.toJSONString(dataMap);
        protoMessage.setJson(json);
        return protoMessage;
    }
}
