package com.wujt.scarm.sasl.examples;

import com.wujt.scram.sasl.client.ScramSaslClientProcessor;
import com.wujt.scram.sasl.common.ScramException;
import com.wujt.scram.sasl.server.ScramSaslServerProcessor;

/**
 * @author wujt
 */
public class MyToServerSender implements ScramSaslClientProcessor.Sender {
    private ScramSaslServerProcessor mServer;


    @Override
    public void sendMessage(String msg) {
        try {
            mServer.onMessage(msg);
        } catch (ScramException e) {
            e.printStackTrace();
        }
    }


    public void setServer(ScramSaslServerProcessor server) {
        mServer = server;
    }
}
