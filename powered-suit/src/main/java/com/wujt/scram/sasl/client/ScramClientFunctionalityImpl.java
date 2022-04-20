/*
 * Copyright 2016 Ognyan Bankov
 * <p>
 * All rights reserved. Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wujt.scram.sasl.client;


import com.wujt.scram.sasl.common.Base64;
import com.wujt.scram.sasl.common.ScramException;
import com.wujt.scram.sasl.common.ScramUtils;
import com.wujt.scram.sasl.common.StringPrep;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Provides building blocks for creating SCRAM authentication client
 */
@SuppressWarnings("unused")
public class ScramClientFunctionalityImpl implements ScramClientFunctionality {
    private static final Pattern SERVER_FIRST_MESSAGE = Pattern.compile("r=([^,]*),s=([^,]*),i=(.*)$");
    private static final Pattern SERVER_FINAL_MESSAGE = Pattern.compile("v=([^,]*)$");

    private static final String GS2_HEADER = "n,,";
    private static final Charset ASCII = Charset.forName("ASCII");

    private final String mDigestName;
    private final String mHmacName;
    private final String mClientNonce;
    private String mClientFirstMessageBare;

    private boolean mIsSuccessful = false;
    private byte[] mSaltedPassword;
    private String mAuthMessage;

    private State mState = State.INITIAL;


    /**
     * Create new ScramClientFunctionalityImpl
     *
     * @param digestName Digest to be used
     * @param hmacName   HMAC to be used
     */
    public ScramClientFunctionalityImpl(String digestName, String hmacName) {
        this(digestName, hmacName, UUID.randomUUID().toString());
    }


    /**
     * Create new ScramClientFunctionalityImpl
     *
     * @param digestName  Digest to be used
     * @param hmacName    HMAC to be used
     * @param clientNonce Client nonce to be used
     */
    public ScramClientFunctionalityImpl(String digestName, String hmacName, String clientNonce) {
        if (ScramUtils.isNullOrEmpty(digestName)) {
            throw new NullPointerException("digestName cannot be null or empty");
        }
        if (ScramUtils.isNullOrEmpty(hmacName)) {
            throw new NullPointerException("hmacName cannot be null or empty");
        }
        if (ScramUtils.isNullOrEmpty(clientNonce)) {
            throw new NullPointerException("clientNonce cannot be null or empty");
        }

        mDigestName = digestName;
        mHmacName = hmacName;
        mClientNonce = clientNonce;
    }


    /**
     * Prepares first client message
     * <p>
     * You may want to use {@link StringPrep#isContainingProhibitedCharacters(String)} in order to check if the
     * username contains only valid characters
     *
     * @param username Username
     * @return prepared first message
     * @throws ScramException if <code>username</code> contains prohibited characters
     */
    @Override
    public String prepareFirstMessage(String username) throws ScramException {
        if (mState != State.INITIAL) {
            throw new IllegalStateException("You can call this method only once");
        }

        try {
            // 此处可以不需要就是明文
            String asQueryString = StringPrep.prepAsQueryString(username);
            mClientFirstMessageBare = "n=" + asQueryString + ",r=" + mClientNonce;
            System.out.println("userNameAndClientNonce=" + mClientFirstMessageBare);
            mState = State.FIRST_PREPARED;
            return GS2_HEADER + mClientFirstMessageBare;
        } catch (StringPrep.StringPrepError e) {
            mState = State.ENDED;
            throw new ScramException("Username contains prohibited character");
        }
    }


    @Override
    public String prepareFinalMessage(String password, String serverFirstMessage) throws ScramException {
        if (mState != State.FIRST_PREPARED) {
            throw new IllegalStateException("You can call this method once only after " +
                    "calling prepareFirstMessage()");
        }

        Matcher m = SERVER_FIRST_MESSAGE.matcher(serverFirstMessage);
        if (!m.matches()) {
            mState = State.ENDED;
            return null;
        }

        String nonce = m.group(1);
        // 验证服务端是否是处理的服务端
        System.out.println("serverNonce=" + nonce + "clientNonce=" + mClientNonce);
        if (!nonce.startsWith(mClientNonce)) {
            mState = State.ENDED;
            return null;
        }


        String salt = m.group(2);
        String iterationCountString = m.group(3);
        int iterations = Integer.parseInt(iterationCountString);
        if (iterations <= 0) {
            mState = State.ENDED;
            return null;
        }


        try {
            //
            mSaltedPassword = ScramUtils.generateSaltedPassword(password,
                    Base64.decode(salt),
                    iterations,
                    mHmacName);


            String clientFinalMessageWithoutProof = "c=" + Base64.encodeBytes(GS2_HEADER.getBytes(ASCII)
                    , Base64.DONT_BREAK_LINES)
                    + ",r=" + nonce;

            mAuthMessage = mClientFirstMessageBare + "," + serverFirstMessage + "," + clientFinalMessageWithoutProof;
            System.out.println("auth=" + mAuthMessage);
            //client 端HMAC 摘要

            byte[] clientKey = ScramUtils.computeHmac(mSaltedPassword, mHmacName, "Client Key");
            byte[] storedKey = MessageDigest.getInstance(mDigestName).digest(clientKey);
            // HMAC 签名 HMAC(H(key[c]), Auth)
            byte[] clientSignature = ScramUtils.computeHmac(storedKey, mHmacName, mAuthMessage);
            // key[c]
            byte[] clientProof = clientKey.clone();
            for (int i = 0; i < clientProof.length; i++) {
                // 异或计算
                clientProof[i] ^= clientSignature[i];
            }

            mState = State.FINAL_PREPARED;
            String finalMsg = clientFinalMessageWithoutProof + ",p=" + Base64.encodeBytes(clientProof, Base64.DONT_BREAK_LINES);
            System.out.println("client-final-message=" + finalMsg);
            return finalMsg;
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            mState = State.ENDED;
            throw new ScramException(e);
        }
    }


    @Override
    public boolean checkServerFinalMessage(String serverFinalMessage) throws ScramException {
        System.out.println("server-final-message=" + serverFinalMessage);
        if (mState != State.FINAL_PREPARED) {
            throw new IllegalStateException("You can call this method only once after " +
                    "calling prepareFinalMessage()");
        }

        Matcher m = SERVER_FINAL_MESSAGE.matcher(serverFinalMessage);
        if (!m.matches()) {
            mState = State.ENDED;
            return false;
        }

        byte[] serverSignature = Base64.decode(m.group(1));

        mState = State.ENDED;

        byte[] expectedServerSignature = getExpectedServerSignature();

        mIsSuccessful = Arrays.equals(serverSignature, expectedServerSignature);
        System.out.println(String.format("serverSignature=%s,expect=%s,result=%b", serverFinalMessage, new String(expectedServerSignature), mIsSuccessful));
        return mIsSuccessful;
    }


    @Override
    public boolean isSuccessful() {
        if (mState == State.ENDED) {
            return mIsSuccessful;
        } else {
            throw new IllegalStateException("You cannot call this method before authentication is ended. " +
                    "Use isEnded() to check that");
        }
    }


    @Override
    public boolean isEnded() {
        return mState == State.ENDED;
    }


    @Override
    public State getState() {
        return mState;
    }


    private byte[] getExpectedServerSignature() throws ScramException {
        try {
            byte[] serverKey = ScramUtils.computeHmac(mSaltedPassword, mHmacName, "Server Key");
            return ScramUtils.computeHmac(serverKey, mHmacName, mAuthMessage);
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            mState = State.ENDED;
            throw new ScramException(e);
        }
    }
}
