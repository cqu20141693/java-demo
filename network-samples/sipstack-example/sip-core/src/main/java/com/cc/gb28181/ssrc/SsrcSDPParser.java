package com.cc.gb28181.ssrc;

import gov.nist.javax.sdp.parser.Lexer;
import gov.nist.javax.sdp.parser.SDPParser;

import java.text.ParseException;

/**
 * 视频SDP SSRC 解析器
 * wcc 2022/5/24
 */
public class SsrcSDPParser extends SDPParser {

    public SsrcSDPParser(String mediaField) {
        lexer = new Lexer("charLexer", mediaField);
    }

    @Override
    public Ssrc parse() throws ParseException {
        this.lexer.match('y');
        this.lexer.SPorHT();
        this.lexer.match('=');
        this.lexer.SPorHT();
        return new Ssrc(this.lexer.getRest());
    }
}
