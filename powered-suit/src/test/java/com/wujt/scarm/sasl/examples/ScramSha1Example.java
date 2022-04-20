package com.wujt.scarm.sasl.examples;


import com.wujt.scram.sasl.client.ScramSaslClientProcessor;
import com.wujt.scram.sasl.client.ScramSha1SaslClientProcessor;
import com.wujt.scram.sasl.common.ScramException;
import com.wujt.scram.sasl.server.ScramSaslServerProcessor;
import com.wujt.scram.sasl.server.ScramSha1SaslServerProcessor;
import com.wujt.scram.sasl.server.UserData;

/**
 * Shows how to use both client and server with SCRAM SHA-1
 */
public class ScramSha1Example {
    public static void main(String[] args) {
        ScramSha1SaslServerProcessor.Listener serverListener = new ScramSha1SaslServerProcessor.Listener() {

            @Override
            public void onSuccess(long connectionId) {
                System.out.println("Server success");
            }


            @Override
            public void onFailure(long connectionId) {
                System.out.println("Server fail");
            }
        };


        ScramSaslClientProcessor.Listener clientListener = new ScramSaslClientProcessor.Listener() {
            @Override
            public void onSuccess() {
                System.out.println("Client success");
            }


            @Override
            public void onFailure() {
                System.out.println("Client fail");
            }
        };


        @SuppressWarnings("Convert2Lambda")
        ScramSaslServerProcessor.UserDataLoader loader = new ScramSaslServerProcessor.UserDataLoader() {
            @Override
            public void loadUserData(String username, long connectionId, ScramSaslServerProcessor interested) {
                //noinspection SpellCheckingInspection
                interested.onUserDataLoaded(
                        new UserData("TWLQ7cNG4uHZn38AlBSE7XacApO76SjN",
                                4096,
                                "bEBbN+QCeFi1rtCQPn/15+mvuNg=",
                                "pxF02K1QQ/t5PcweqxjzZwPOolU="
                        ));
            }
        };

        MyToServerSender toServerSender = new MyToServerSender();
        ScramSha1SaslClientProcessor client = new ScramSha1SaslClientProcessor(clientListener, toServerSender);

        MyToClientSender toClientSender = new MyToClientSender(client);
        ScramSha1SaslServerProcessor server = new ScramSha1SaslServerProcessor(1, serverListener, loader,
                toClientSender);

        toServerSender.setServer(server);

        try {
            client.start("ogre", "ogre1234");
        } catch (ScramException e) {
            e.printStackTrace();
        }

    }

}
