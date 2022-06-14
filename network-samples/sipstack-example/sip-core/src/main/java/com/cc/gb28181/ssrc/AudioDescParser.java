package com.cc.gb28181.ssrc;

import gov.nist.javax.sdp.parser.Lexer;
import gov.nist.javax.sdp.parser.SDPParser;

import java.text.ParseException;

/**
 * 音频DESC 解析器
 * wcc 2022/5/24
 */
public class AudioDescParser extends SDPParser {

    public AudioDescParser(String mediaField) {
        lexer = new Lexer("charLexer", mediaField);
    }

    @Override
    public AudioDesc parse() throws ParseException {
        this.lexer.match('f');
        this.lexer.SPorHT();
        this.lexer.match('=');
        this.lexer.SPorHT();
        return new AudioDesc(this.lexer.getRest());
    }
}
