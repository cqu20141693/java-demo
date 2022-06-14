package com.cc.sip;

import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;

import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.header.*;
import javax.sip.message.Request;
import java.util.ArrayList;
import java.util.List;

/**
 * 简单sip 请求构造器
 * wcc 2022/5/14
 */
@Data
public class SimpleRequestBuilder implements RequestBuilder {
    private final SipLayer sipLayer;

    private String host;
    private int port;
    private String user;
    private String method;
    private boolean transportTcp;
    private long cSeq = 1L;
    private final List<ViaHeader> viaHeaders = new ArrayList<>();

    private ContactHeader contactHeader;
    private SubjectHeader subjectHeader;
    private FromHeader fromHeader;
    private ToHeader toHeader;
    private AuthorizationHeader authorizationHeader;
    private ContentTypeHeader contentTypeHeader;
    private byte[] content;
    private SipURI requestLine;


    SimpleRequestBuilder(SipLayer sipLayer) {
        this.sipLayer = sipLayer;
    }

    @Override
    @SneakyThrows
    public RequestBuilder requestLine(String sipId, String host, int port) {
        requestLine = sipLayer.getAddressFactory().createSipURI(sipId, host + ":" + port);
        return this;
    }

    @Override
    public RequestBuilder user(String user) {
        this.user = user;
        return this;
    }

    @Override
    public RequestBuilder method(String method) {
        this.method = method;
        return this;
    }

    @Override
    @SneakyThrows
    public RequestBuilder via(String host,
                              int port,
                              String transport,
                              String viaTag) {
        this.port = port;
        this.transportTcp = "TCP".equals(transport);
        this.host = host;
        ViaHeader viaHeader = sipLayer
                .getHeaderFactory()
                .createViaHeader(host, port, transport, viaTag);
        viaHeader.setRPort();
        viaHeaders.add(viaHeader);
        return this;
    }

    @Override
    @SneakyThrows
    public RequestBuilder from(String sipId,
                               String domain,
                               String fromTag) {
        SipURI from = sipLayer.getAddressFactory().createSipURI(sipId, domain);
        Address fromAddress = sipLayer.getAddressFactory().createAddress(from);
        fromHeader = sipLayer
                .getHeaderFactory()
                .createFromHeader(fromAddress, fromTag);
        return this;
    }

    @Override
    @SneakyThrows
    public RequestBuilder to(String sipId,
                             String domain,
                             String toTag) {
        SipURI from = sipLayer.getAddressFactory().createSipURI(sipId, domain);
        Address fromAddress = sipLayer.getAddressFactory().createAddress(from);
        toHeader = sipLayer
                .getHeaderFactory()
                .createToHeader(fromAddress, toTag);

        return this;
    }

    @Override
    @SneakyThrows
    public RequestBuilder contact(String user, int port) {
        Address concatAddress = sipLayer
                .getAddressFactory()
                .createAddress(sipLayer
                        .getAddressFactory()
                        .createSipURI(user, user + ":" + port));
        contactHeader = sipLayer.getHeaderFactory().createContactHeader(concatAddress);
        return this;
    }

    @Override
    public RequestBuilder cSeq(int cSeq) {
        this.cSeq = cSeq;
        return this;
    }

    @Override
    @SneakyThrows
    public RequestBuilder subject(String subject) {
        subjectHeader = sipLayer
                .getHeaderFactory()
                .createSubjectHeader(subject);

        return this;
    }

    @Override
    @SneakyThrows
    public RequestBuilder content(byte[] content, MediaType contentType) {
        contentTypeHeader = sipLayer
                .getHeaderFactory()
                .createContentTypeHeader(contentType.getType(), contentType.getSubtype());
        this.content = content;
        return this;
    }

    @Override
    public RequestBuilder authorization(AuthorizationHeader header) {
        this.authorizationHeader = header;
        return this;
    }

    @Override
    @SneakyThrows
    public Request build(Request request) {

        //Authorization
        if (this.authorizationHeader != null) {
            request.addHeader(this.authorizationHeader);
        }
        //Contact
        if (contactHeader != null) {
            request.addHeader(contactHeader);
        }
        // Subject
        if (subjectHeader != null) {
            request.addHeader(subjectHeader);
        }
        //Content
        if (content != null) {
            request.setContent(content, contentTypeHeader);
        }

        return request;
    }

    @Override
    @SneakyThrows
    public Request build() {
        //请求行
        SipURI requestLine = this.requestLine == null ? sipLayer
                .getAddressFactory()
                .createSipURI(user, host + ":" + port)
                : this.requestLine;

        //callid
        CallIdHeader callIdHeader = transportTcp
                ? sipLayer.getTcpProvider().getNewCallId()
                : sipLayer.getUdpProvider().getNewCallId();

        //Forwards
        MaxForwardsHeader maxForwards = sipLayer.getHeaderFactory().createMaxForwardsHeader(70);

        //ceq
        CSeqHeader cSeqHeader = sipLayer.getHeaderFactory().createCSeqHeader(cSeq, method);
        Request request = sipLayer
                .getMessageFactory()
                .createRequest(requestLine, method, callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);
        return build(request);

    }
}
