package com.cc.link.model;

import io.pkts.packet.sip.address.impl.SipURIImpl;
import lombok.Data;

/**
 * @author gow
 * @date 2022/2/16
 */
@Data
public class SipInfo {
    private SipURIImpl sUri;
    private SipURIImpl cUri;
}
