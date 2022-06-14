package com.cc.sip;

import lombok.SneakyThrows;

import javax.sip.ClientTransaction;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.address.AddressFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;

/**
 * SIP 协议层操作接口，用于进行SIP底层操作。
 * <p>
 * wcc 2022/5/14
 */
public interface SipLayer {
    /**
     * 获取 SipProvider
     *
     * @param transport 协议，UDP 或者 TCP
     * @return SipProvider
     */
    default SipProvider getProvider(String transport) {
        return "UDP".equalsIgnoreCase(transport)
                ? getUdpProvider()
                : getTcpProvider();
    }

    /**
     * 根据通信协议和SIP请求创建一个客户端事务
     *
     * @param transport 协议，UDP 或者 TCP
     * @param request   请求
     * @return ClientTransaction
     */
    @SneakyThrows
    default ClientTransaction newTransaction(String transport, Request request) {
        return "TCP".equals(transport)
                ? getTcpProvider().getNewClientTransaction(request)
                : getUdpProvider().getNewClientTransaction(request);
    }

    /**
     * @return TCP SipProvider
     */
    SipProvider getTcpProvider();

    /**
     * @return UDP SipProvider
     */
    SipProvider getUdpProvider();

    /**
     * 获取消息工厂,用于创建请求,响应消息。
     *
     * @return MessageFactory
     */
    MessageFactory getMessageFactory();

    /**
     * 获取消息头工厂，用于创建SIP消息头。
     *
     * @return HeaderFactory
     */
    HeaderFactory getHeaderFactory();

    /**
     * @return 地址工厂
     */
    AddressFactory getAddressFactory();

    /**
     * @param listener 监听器
     */
    void addListener(SipListener listener);

    /**
     * 关闭sip
     */
    void dispose();

    boolean isDisposed();

    default RequestBuilder newRequestBuilder() {
        return new SimpleRequestBuilder(this);
    }

    default RequestBuilder newRequestBuilder(SipProperties properties) {
        return new SimpleRequestBuilder(this);
    }
}
