package com.cc.link.api;

import static com.cc.link.handler.SipConnections.createInfo;
import com.cc.link.handler.SipConnections;
import com.cc.link.model.SipInfo;
import com.cc.link.model.SrsResponse;
import com.gow.common.Result;
import io.pkts.buffer.Buffer;
import io.pkts.buffer.Buffers;
import io.pkts.packet.sip.SipMessage;
import io.pkts.packet.sip.address.impl.AddressImpl;
import io.pkts.packet.sip.address.impl.SipURIImpl;
import io.pkts.packet.sip.header.ToHeader;
import io.pkts.packet.sip.header.impl.CSeqHeaderImpl;
import io.pkts.packet.sip.header.impl.CallIdHeaderImpl;
import io.pkts.packet.sip.header.impl.ContentTypeHeaderImpl;
import io.pkts.packet.sip.header.impl.FromHeaderImpl;
import io.pkts.packet.sip.header.impl.MaxForwardsHeaderImpl;
import io.pkts.packet.sip.header.impl.ToHeaderImpl;
import io.pkts.packet.sip.header.impl.ViaHeaderImpl;
import io.pkts.packet.sip.impl.SipRequestImpl;
import io.pkts.packet.sip.impl.SipRequestLine;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.sdp.SdpException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author gow
 * @date 2022/2/16
 */
@RestController
@RequestMapping("api/device/sip")
public class SipController {

    @Autowired
    private SipConnections sipConnections;

    private AtomicInteger cseq = new AtomicInteger(1);

    @Autowired
    private RestTemplate restTemplate;

    private final String RFC3261BranchMagicCookie = "z9hG4bK";

    @PostMapping("addSession")
    public Result<String> addSession(@RequestParam("id") String id, @RequestParam("ip") String ip,
                                     @RequestParam("port") Integer port) {
        SipInfo info = new SipInfo();
        info.setSUri(
                new SipURIImpl(false, Buffers.wrap("cc"), Buffers.wrap("192.168.1.55"), Buffers.wrap(5060), null,
                        null));
        info.setCUri(new SipURIImpl(false, Buffers.wrap(id), Buffers.wrap(ip), Buffers.wrap(port), null, null));
        sipConnections.addConnection(info);
        return Result.ok("success");
    }

    @GetMapping("getSession")
    public Result<Map<String, SipInfo>> getSession() {

        return Result.ok(sipConnections.getSession());
    }


    @PostMapping("invite")
    public Result<String> invite(@RequestParam("id") String id, @RequestParam("channel") String channel)
            throws SdpException {
        SipInfo info = sipConnections.getInfo(id);
        if (info == null) {
            return Result.ok("not online");
        }

        Integer ssrc = getSsrc(info.getCUri().getUser().toString());


        SipRequestLine sipRequestLine = new SipRequestLine(Buffers.wrap("INVITE"), info.getCUri());
        String sdpInfo = createInfo(id, 9000, "47.108.93.28", "0" + ssrc);
        System.out.println(sdpInfo);

        FromHeaderImpl fromHeader =
                new FromHeaderImpl(new AddressImpl(null, info.getSUri()), Buffers.wrap(";tag="+RandomStringUtils.randomAlphabetic(8)));
        ToHeader toHeader = new ToHeaderImpl(new AddressImpl(null, info.getCUri()), null);
        CSeqHeaderImpl seqHeader = new CSeqHeaderImpl(cseq.getAndIncrement(), Buffers.wrap("INVITE"), null);
        CallIdHeaderImpl callIdHeader = new CallIdHeaderImpl(Buffers.wrap(RandomStringUtils.randomAlphabetic(10)));
        MaxForwardsHeaderImpl maxForwardsHeader = new MaxForwardsHeaderImpl(70);
        ViaHeaderImpl viaHeader =
                new ViaHeaderImpl(Buffers.wrap("UDP"), info.getSUri().getHost(), info.getSUri().getPort(),
                        Buffers.wrap(RFC3261BranchMagicCookie + RandomStringUtils.randomAlphabetic(10)));
//        viaHeader.setRPort(0);


        ContentTypeHeaderImpl contentTypeHeader =
                new ContentTypeHeaderImpl(Buffers.wrap("application"), Buffers.wrap("sdp"), null);
//        message.setHeader(contentTypeHeader);

        ArrayList<String> sipHeaders = new ArrayList<>();
        sipHeaders.add(fromHeader.toString());
        sipHeaders.add(toHeader.toString());
        sipHeaders.add(seqHeader.toString());
        sipHeaders.add(callIdHeader.toString());
        sipHeaders.add(maxForwardsHeader.toString());
        sipHeaders.add(viaHeader.toString());
        sipHeaders.add(contentTypeHeader.toString());
          sipHeaders.add("Content-Length: " + sdpInfo.getBytes(StandardCharsets.UTF_8).length);
        String join = String.join(System.lineSeparator(), sipHeaders.toArray(new String[0])) + System.lineSeparator();

        SipMessage message2 = new SipRequestImpl(sipRequestLine, Buffers.wrap(join), null);
        //   sipConnections.sendMessage(message2, info.getCUri());
        Buffer body = Buffers.wrap(System.lineSeparator() + sdpInfo);
        SipMessage message3 = new SipRequestImpl(sipRequestLine, Buffers.wrap(join), body);
        sipConnections.sendMessage(message3, info.getCUri());
        return Result.ok("success");
    }

    private Integer getSsrc(String user) {

        String url =
                "http://172.30.203.21:1985/api/v1/gb28181?action=create_channel&stream=[stream]&port_mode=fixed&app"
                        + "=live&id="
                        + user;
        ResponseEntity<SrsResponse> entity = restTemplate.getForEntity(url, SrsResponse.class);
        Integer ssrc = entity.getBody().getSsrc();
        return ssrc;
    }
}
