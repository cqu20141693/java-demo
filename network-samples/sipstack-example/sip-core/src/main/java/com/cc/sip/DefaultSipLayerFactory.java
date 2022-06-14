package com.cc.sip;

import gov.nist.javax.sip.SipStackImpl;
import lombok.Data;
import lombok.SneakyThrows;

import javax.sip.ListeningPoint;
import javax.sip.SipFactory;
import javax.sip.SipProvider;

/**
 * 默认sip layer 工厂
 * wcc 2022/5/14
 */
@Data
public class DefaultSipLayerFactory implements SipLayerFactory {
    @SneakyThrows
    @Override
    public SipLayer createSipLayer(SipProperties properties) {
        SipFactory sipFactory = SipFactory.getInstance();
        SipStackImpl sipStack = new SipStackImpl(properties.toSiProperties());
        sipStack.setDeliverUnsolicitedNotify(true);
        try {
            ListeningPoint tcpListeningPoint = sipStack.createListeningPoint(properties.getLocalAddress(),
                    properties.getPort(), "TCP");
            SipProvider tcpProvider = sipStack.createSipProvider(tcpListeningPoint);

            ListeningPoint udpListeningPoint = sipStack.createListeningPoint(properties.getLocalAddress(),
                    properties.getPort(), "UDP");
            SipProvider udpProvider = sipStack.createSipProvider(udpListeningPoint);

            return new DefaultSipLayer(sipStack,
                    tcpProvider,
                    udpProvider,
                    sipFactory.createMessageFactory(),
                    sipFactory.createHeaderFactory(),
                    sipFactory.createAddressFactory())
                    .init();
        } catch (Throwable e) {
            try {
                sipStack.stop();
            } catch (Throwable ignore) {
            }
            throw e;
        }
    }
}
