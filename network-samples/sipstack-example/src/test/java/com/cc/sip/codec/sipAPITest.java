package com.cc.sip.codec;

import io.pkts.buffer.Buffers;
import io.pkts.packet.sip.address.impl.AddressImpl;
import io.pkts.packet.sip.address.impl.SipURIImpl;
import io.pkts.packet.sip.header.FromHeader;
import io.pkts.packet.sip.header.impl.ContentTypeHeaderImpl;
import io.pkts.packet.sip.header.impl.FromHeaderImpl;
import org.junit.Test;

/**
 * @author gow
 * @date 2022/2/16
 */
public class sipAPITest {

    @Test
    public void testApi() {
        AddressImpl address = new AddressImpl(null,
                new SipURIImpl(true, Buffers.wrap("cc"), Buffers.wrap("127.0.0.1"), Buffers.wrap(5060), null, null));
        FromHeader header = new FromHeaderImpl(address, null);

        ContentTypeHeaderImpl contentTypeHeader =
                new ContentTypeHeaderImpl(Buffers.wrap("application"), Buffers.wrap("sdp"), null);
        System.out.println(contentTypeHeader.getValue().toString());
    }


}
