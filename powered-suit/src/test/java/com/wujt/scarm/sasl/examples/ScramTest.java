package com.wujt.scarm.sasl.examples;

import com.wujt.scram.sasl.client.ScramSaslClientProcessor;
import com.wujt.scram.sasl.client.ScramSha512SaslClientProcessor;
import com.wujt.scram.sasl.common.ScramException;
import com.wujt.scram.sasl.common.ScramUtils;
import com.wujt.scram.sasl.server.ScramSaslServerProcessor;
import com.wujt.scram.sasl.server.ScramSha1SaslServerProcessor;
import com.wujt.scram.sasl.server.ScramSha512SaslServerProcessor;
import com.wujt.scram.sasl.server.UserData;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * https://github.com/ogrebgr/scram-sasl
 * https://blog.csdn.net/q375923078/article/details/98937760
 *
 * @author wujt
 */
@Slf4j
public class ScramTest {
    private final String userName = "wujt";
    private final String password = "hy12345";
    private final long connectionId = 1;
    ScramUtils.NewPasswordStringData newPasswordStringData;

    @Before
    public void init() throws InvalidKeyException, NoSuchAlgorithmException {
        // ... we generate salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[24];
        random.nextBytes(salt);
        // ... then we generate value for the 'iterations' between 4096 and 6000
        int iterations = 4096 + random.nextInt(1092);
        // Compute user data using SHA-512
        ScramUtils.NewPasswordByteArrayData newPassword = ScramUtils.newPassword(password,
                salt,
                iterations,
                "SHA-512",
                "HmacSHA512"
        );

        // transform the data into DB friendly format i.e. String
        newPasswordStringData = ScramUtils.byteArrayToStringData(newPassword);

        log.info("userName={},password={},newPassword={}", userName, password, newPasswordStringData);
    }

    @Test
    public void sha512Test() {

        // 服务端监听器
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


        ScramSaslServerProcessor.UserDataLoader loader = new ScramSaslServerProcessor.UserDataLoader() {
            @Override
            public void loadUserData(String username, long connectionId, ScramSaslServerProcessor interested) {
                //noinspection SpellCheckingInspection
                // 根据用户名获取用户的username、H(key[c])、key[s]、salt和iteration-count
                System.out.println("start load user authentication info ,client send userName=" + username);
                interested.onUserDataLoaded(
                        new UserData(newPasswordStringData.salt,
                                newPasswordStringData.iterations,
                                newPasswordStringData.serverKey,
                                newPasswordStringData.storedKey
                        ));
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

        MyToServerSender toServerSender = new MyToServerSender();
        ScramSha512SaslClientProcessor clientProcessor = new ScramSha512SaslClientProcessor(clientListener, toServerSender);

        MyToClientSender toClientSender = new MyToClientSender(clientProcessor);
        ScramSha512SaslServerProcessor serverProcessor = new ScramSha512SaslServerProcessor(
                connectionId,
                serverListener,
                loader,
                toClientSender
        );
        toServerSender.setServer(serverProcessor);

        try {
            clientProcessor.start(userName, password);
        } catch (ScramException e) {
            e.printStackTrace();
        }

    }

}
