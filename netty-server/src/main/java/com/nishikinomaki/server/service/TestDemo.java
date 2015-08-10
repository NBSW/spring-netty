package com.nishikinomaki.server.service;

import com.nishikinomaki.annotation.ProtocolMethod;
import com.nishikinomaki.protocol.message.ProtocolMessage;
import com.nishikinomaki.util.ProtocolMessageUtil;
import org.springframework.stereotype.Component;

/**
 * Created on 2015/7/13.
 *
 * @author Jax
 */
@Component("testDemo")
public class TestDemo {

    @ProtocolMethod(name = "test")
    public ProtocolMessage test(int data) throws InterruptedException {
        return ProtocolMessageUtil.successMessage(data);
    }

}
