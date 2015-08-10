package com.nishikinomaki.util;

import com.alibaba.fastjson.JSONObject;
import com.nishikinomaki.protocol.message.ProtocolMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2015/6/17.
 *
 * @author Jax
 */
public final class ProtocolMessageUtil {

    public static <T> ProtocolMessage successMessage(T t){
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("code", "0");
        jsonMap.put("data", t);
        String json = JSONObject.toJSONString(jsonMap);
        ProtocolMessage protocolMessage = new ProtocolMessage();
        protocolMessage.setJson(json);
        return protocolMessage;
    }

    public static ProtocolMessage errorMessage(int code, String msg){
        Map<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("code", code);
        jsonMap.put("msg", msg);
        String json = JSONObject.toJSONString(jsonMap);
        ProtocolMessage protocolMessage = new ProtocolMessage();
        protocolMessage.setJson(json);
        return protocolMessage;
    }

    public static ProtocolMessage phpServiceMessage(String serviceName, Map<String,Object> params){
        Map<String,Object> map = new HashMap<>();
        map.put("class", serviceName);
        map.put("params", params);
        String json = JSONObject.toJSONString(map);
        ProtocolMessage protocolMessage = new ProtocolMessage();
        protocolMessage.setJson(json);
        return protocolMessage;
    }
}
