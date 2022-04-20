package com.cc.link.handler;

import com.cc.link.model.SipInfo;
import com.cc.link.model.YField;
import gov.nist.javax.sdp.fields.URIField;
import io.netty.channel.Channel;
import io.pkts.packet.sip.SipMessage;
import io.pkts.packet.sip.address.impl.SipURIImpl;
import io.sipstack.netty.codec.sip.UdpConnection;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2022/2/16
 */
@Component
public class SipConnections {

    private Channel channel;
    private Map<String, SipInfo> connections = new HashMap<>();

    public SipInfo getInfo(String user) {
        return connections.get(user);
    }

    public static String userFromHeader(SipURIImpl uri) {
        return uri.getUser().toString();
    }

    public Boolean addConnection(SipInfo sipInfo) {
        if (sipInfo != null) {
            String user = userFromHeader(sipInfo.getCUri());
            if (user != null) {
                connections.put(user, sipInfo);
                return true;
            }
        }
        return false;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public void sendMessage(SipMessage message, SipURIImpl cUri) {
        UdpConnection connection =
                new UdpConnection(channel, new InetSocketAddress(cUri.getHost().toString(), cUri.getPort()));
        connection.send(message);
    }


    public static String createInfo(String channelID, int mediaPort, String mediaIp, String ssrc) throws SdpException {
        ArrayList<String> sdpInfos = new ArrayList<>();

        Version version = SdpFactory.getInstance().createVersion(0);

        sdpInfos.add(version.toString());

        Origin origin = SdpFactory.getInstance().createOrigin("34020000002000000001", 0, 0, "IN", "IP4", mediaIp);
        sdpInfos.add(origin.toString());

        // Play 直播，Playback 回放
        SessionName play = SdpFactory.getInstance().createSessionName("Play");
        sdpInfos.add(play.toString());


        URIField uriField = new URIField();
        // channelID:0

        uriField.setURI(channelID + ":0");
        sdpInfos.add(uriField.toString());

        Connection connection = SdpFactory.getInstance().createConnection("IN", "IP4", mediaIp);
        sdpInfos.add(connection.toString());

        Time time = SdpFactory.getInstance().createTime();
        sdpInfos.add(time.toString());

        Vector<String> rtpAvpTypes = new Vector<>();
        rtpAvpTypes.add("96");
        rtpAvpTypes.add("97");
        rtpAvpTypes.add("98");

        Media video = SdpFactory.getInstance().createMedia("video", mediaPort, 1, "RTP/AVP", rtpAvpTypes);

        sdpInfos.add(video.toString());

        Attribute recvonly = SdpFactory.getInstance().createAttribute("recvonly", null);
        Attribute rtpmapps = SdpFactory.getInstance().createAttribute("rtpmap", "96 PS/90000");
        Attribute rtpmapmpeg4 = SdpFactory.getInstance().createAttribute("rtpmap", "97 MPEG4/90000");
        Attribute rtpmaph264 = SdpFactory.getInstance().createAttribute("rtpmap", "98 H264/90000");

        sdpInfos.add(recvonly.toString());
        sdpInfos.add(rtpmapps.toString());
        sdpInfos.add(rtpmapmpeg4.toString());
        sdpInfos.add(rtpmaph264.toString());

        YField yField = new YField();

        yField.setSsrc(ssrc);
        sdpInfos.add(yField.toString());

        String join = String.join("", sdpInfos.toArray(new String[0]));
        return join;
    }

    public Map<String, SipInfo> getSession() {
        return connections;
    }
}
