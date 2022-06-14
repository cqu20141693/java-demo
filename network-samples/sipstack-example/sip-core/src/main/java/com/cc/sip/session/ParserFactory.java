package com.cc.sip.session;

import com.cc.gb28181.ssrc.AudioDescParser;
import com.cc.gb28181.ssrc.SsrcSDPParser;
import gov.nist.core.InternalErrorHandler;
import gov.nist.javax.sdp.parser.*;
import lombok.Data;

import java.text.ParseException;
import java.util.Hashtable;
import java.util.function.Function;

/**
 * factory for creating parsers for the SDP stuff.
 * SDP解析工厂
 * wcc 2022/5/24
 */
@Data
public class ParserFactory {
    private static Hashtable<String, Function<String, SDPParser>> parserTable;

    static {
        parserTable = new Hashtable();
        parserTable.put("a", AttributeFieldParser::new);
        parserTable.put("b", BandwidthFieldParser::new);
        parserTable.put("c", ConnectionFieldParser::new);
        parserTable.put("e", EmailFieldParser::new);
        parserTable.put("i", InformationFieldParser::new);
        parserTable.put("k", KeyFieldParser::new);
        parserTable.put("m", MediaFieldParser::new);
        parserTable.put("o", OriginFieldParser::new);
        parserTable.put("p", PhoneFieldParser::new);
        parserTable.put("v", ProtoVersionFieldParser::new);
        parserTable.put("r", RepeatFieldParser::new);
        parserTable.put("s", SessionNameFieldParser::new);
        parserTable.put("t", TimeFieldParser::new);
        parserTable.put("u", URIFieldParser::new);
        parserTable.put("z", ZoneFieldParser::new);
        //ssrc
        parserTable.put("y", SsrcSDPParser::new);
        parserTable.put("f", AudioDescParser::new);
    }

    public static SDPParser createParser(String field) throws ParseException {
        String fieldName = Lexer.getFieldName(field);
        if (fieldName == null)
            return null;
        Function<String, SDPParser> parserClass = parserTable.get(fieldName.toLowerCase());

        if (parserClass != null) {
            try {
                return parserClass.apply(field);
            } catch (Exception ex) {
                InternalErrorHandler.handleException(ex);
                return null; // to placate the compiler.
            }
        } else
            throw new ParseException(
                    "Could not find parser for " + fieldName,
                    0);
    }
}
