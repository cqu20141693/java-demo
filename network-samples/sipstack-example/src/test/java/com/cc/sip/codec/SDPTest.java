package com.cc.sip.codec;

import static com.cc.link.handler.SipConnections.createInfo;
import com.cc.link.model.YField;
import gov.nist.javax.sdp.fields.URIField;
import java.util.ArrayList;
import java.util.Vector;
import javax.sdp.Attribute;
import javax.sdp.Connection;
import javax.sdp.Media;
import javax.sdp.Origin;
import javax.sdp.SdpException;
import javax.sdp.SdpFactory;
import javax.sdp.SessionName;
import javax.sdp.Time;
import javax.sdp.Version;
import lombok.SneakyThrows;
import org.junit.Test;

/**
 * @author gow
 * @date 2022/2/17
 */
public class SDPTest {

    @SneakyThrows
    @Test
    public void testSdp() throws SdpException {

        String channelID = "34020000001320000001";
        int mediaPort = 9000;
        String mediaIp = "127.0.0.1";
        String ssrc = "ssrc";

        String join = createInfo(channelID, mediaPort, mediaIp, ssrc);

        System.out.println(join);

    }

}
