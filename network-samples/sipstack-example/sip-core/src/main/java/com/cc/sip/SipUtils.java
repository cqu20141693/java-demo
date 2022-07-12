package com.cc.sip;

import com.cc.sip.session.SessionDescription;
import gov.nist.javax.sip.SipStackImpl;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.stack.SIPServerTransaction;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;

import javax.sdp.MediaDescription;
import javax.sdp.TimeDescription;
import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.header.SubscriptionStateHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;
import java.util.Optional;
import java.util.Vector;

/**
 * sip 工具类
 */
public class SipUtils {

    @SneakyThrows
    public static ServerTransaction getServerTransaction(SipLayer sipLayer, RequestEvent evt) {
        Request request = evt.getRequest();
        ServerTransaction serverTransaction = evt.getServerTransaction();
        // 判断TCP还是UDP
        ViaHeader reqViaHeader = (ViaHeader) request.getHeader(ViaHeader.NAME);
        String transport = reqViaHeader.getTransport();

        if (serverTransaction == null) {
            SipStackImpl stack = (SipStackImpl) sipLayer.getProvider(transport).getSipStack();
            serverTransaction = (SIPServerTransaction) stack.findTransaction((SIPRequest) request, true);
            if (serverTransaction == null) {
                //适配通知时,如果没有订阅状态的头信息则手动添加一个
                if (request.getMethod().equals(Request.NOTIFY)
                        && request.getHeader(SubscriptionStateHeader.NAME) == null) {
                    request.addHeader(sipLayer
                            .getHeaderFactory()
                            .createSubscriptionStateHeader(SubscriptionStateHeader.ACTIVE)
                    );
                }
                serverTransaction = sipLayer.getProvider(transport).getNewServerTransaction(request);
            }
        }
        return serverTransaction;
    }

    @SneakyThrows
    public static Optional<MediaDescription> getMediaDescription(SessionDescription sdp) {
        for (MediaDescription mediaDescription : sdp.getMediaDescriptions(true)) {
            if (mediaDescription.getMedia().getMediaPort() > 0) {
                return Optional.of(mediaDescription);
            }
        }
        return Optional.empty();
    }

    @SneakyThrows
    public static Optional<TimeDescription> getTimeDescription(SessionDescription sdp) {
        Vector<TimeDescription> timeDescriptions = sdp.getTimeDescriptions(false);
        if (CollectionUtils.isNotEmpty(timeDescriptions)) {
            if (timeDescriptions.size() == 1) {
                return Optional.of(timeDescriptions.get(0));
            }
            for (TimeDescription timeDescription : timeDescriptions) {
                if (timeDescription.getTime() != null) {
                    return Optional.of(timeDescription);
                }
            }
        }
        return Optional.empty();

    }


}
