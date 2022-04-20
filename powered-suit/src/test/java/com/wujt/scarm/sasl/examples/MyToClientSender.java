package com.wujt.scarm.sasl.examples;

import com.wujt.scram.sasl.client.ScramSaslClientProcessor;
import com.wujt.scram.sasl.common.ScramException;
import com.wujt.scram.sasl.server.ScramSaslServerProcessor;

/**
 * @author wujt
 */
public class MyToClientSender implements ScramSaslServerProcessor.Sender {
    private final ScramSaslClientProcessor client;


    public MyToClientSender(ScramSaslClientProcessor client) {
        this.client = client;
    }


    @Override
    public void sendMessage(long connectionId, String msg) {
        try {
            client.onMessage(msg);
        } catch (ScramException e) {
            e.printStackTrace();
        }
    }
}

