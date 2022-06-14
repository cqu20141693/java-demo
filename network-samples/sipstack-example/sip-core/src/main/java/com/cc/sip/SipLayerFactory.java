package com.cc.sip;

/**
 * sip layer 工厂
 * wcc 2022/5/14
 */
public interface SipLayerFactory {

    SipLayer createSipLayer(SipProperties properties);
}
