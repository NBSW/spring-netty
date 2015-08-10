package com.nishikinomaki.protocol.proxy;

import com.alibaba.fastjson.JSONObject;
import com.nishikinomaki.annotation.ProtocolMethod;
import com.nishikinomaki.log.Log;
import com.nishikinomaki.protocol.message.ProtocolMessage;
import com.nishikinomaki.util.ProtocolMessageUtil;
import com.nishikinomaki.util.TypeConvertUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created on 2015/6/26.
 * spring注解方法代理类
 * @author Jax
 */
public class SpringBeanMethodInvoke {

    public final static String KEY_CLASS = "class";
    public final static String KEY_PARAMS = "params";
    public final static String KEY_PARAMS_METHOD_NAME = "method";
    public final static String KEY_PARAMS_METHOD_PARAMS = "methodParams";

    Logger logger = Log.getLogger();

    private LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private ApplicationContext context;

    public SpringBeanMethodInvoke(ApplicationContext context) {
        this.context = context;
    }

    public ProtocolMessage invokeMethod(ProtocolMessage msg) {
        try{
            logger.debug("invokeMethod begin :" + msg);
            @SuppressWarnings("unchecked")
            Map<String,Object> jsonMap = (Map<String,Object>) JSONObject.parse(msg.getJson());
            String className = jsonMap.get(KEY_CLASS) + "";
            if(StringUtils.isBlank(className)){
                return ProtocolMessageUtil.errorMessage(1, "class is empty");
            }
            @SuppressWarnings("unchecked")
            Map<String,Object> params = (Map<String,Object>)jsonMap.get(KEY_PARAMS);
            if(params == null){
                return ProtocolMessageUtil.errorMessage(2, "params error");
            }
            String methodName = (String)params.get(KEY_PARAMS_METHOD_NAME);
            if(StringUtils.isBlank(methodName)){
                return ProtocolMessageUtil.errorMessage(3, "method is empty");
            }
            @SuppressWarnings("unchecked")
            Map<String,String> methodParams = (Map<String,String>)params.get(KEY_PARAMS_METHOD_PARAMS);
            if(methodParams == null){
                return ProtocolMessageUtil.errorMessage(4, "method params is empty");
            }
            Object service = context.getBean(className);
            logger.debug("get service:" + service);
            if(service == null){
                return ProtocolMessageUtil.errorMessage(5, "service not exists");
            }
            Method[] methods = service.getClass().getDeclaredMethods();
            Method callMethod = null;
            for(Method method : methods){
                ProtocolMethod protocolMethod = method.getAnnotation(ProtocolMethod.class);
                if(protocolMethod != null){
                    if(StringUtils.equals(protocolMethod.name(), methodName)){
                        logger.debug("invokeMethod find methodName:" + methodName);
                        callMethod = method;
                        break;
                    }
                    
                }
            }
            if(callMethod == null){
                return ProtocolMessageUtil.errorMessage(6, "method not exists");
            }
            if(callMethod.getReturnType() != ProtocolMessage.class){
                return ProtocolMessageUtil.errorMessage(7, "invaliad method return type");
            }
            String[] paramsNames = parameterNameDiscoverer.getParameterNames(callMethod);
            Class[] parameterTypes = callMethod.getParameterTypes();
            Object[] args = new Object[parameterTypes.length];
            for (int i = 0; i < paramsNames.length; i++) {
                String paramName = paramsNames[i];
                Class parameterType = parameterTypes[i];
                if(methodParams.containsKey(paramName)){
                    args[i] = TypeConvertUtil.convert(methodParams.get(paramName), parameterType);
                }else{
                    //参数不存在
                    return ProtocolMessageUtil.errorMessage(8, "invaliad method params return type");
                }
            }
            return (ProtocolMessage) callMethod.invoke(service, args);
        }catch (Exception e){
            logger.warn("SpringBeanMethodInvoke error", e.getMessage());
            return ProtocolMessageUtil.errorMessage(-1, e.getMessage());
        }

    }
}
