package com.cc.sip;

import org.springframework.http.MediaType;

import javax.sip.header.AuthorizationHeader;
import javax.sip.message.Request;

/**
 * 请求构造器
 * wcc 2022/5/14
 */
public interface RequestBuilder {

    MediaType APPLICATION_SDP = new MediaType("APPLICATION", "SDP");
    MediaType APPLICATION_MANSCDP_XML = new MediaType("APPLICATION", "MANSCDP+xml");
    MediaType APPLICATION_MANSRTSP = new MediaType("APPLICATION", "MANSRTSP");

    RequestBuilder requestLine(String sipId, String host, int port);

    RequestBuilder user(String user);

    RequestBuilder via(String host, int port, String transport, String viaTag);

    RequestBuilder from(String sipId, String domain, String fromTag);

    RequestBuilder to(String user, String domain, String toTag);

    RequestBuilder contact(String user, int port);

    RequestBuilder subject(String subject);

    RequestBuilder cSeq(int cSeq);

    RequestBuilder method(String method);

    RequestBuilder content(byte[] content, MediaType contentType);

    RequestBuilder authorization(AuthorizationHeader header);

    Request build(Request request);

    Request build();

    default RequestBuilder invite() {
        return method(Request.INVITE);
    }

    default RequestBuilder message() {
        return method(Request.MESSAGE);
    }

    default RequestBuilder subscribe() {
        return method(Request.SUBSCRIBE);
    }

}
