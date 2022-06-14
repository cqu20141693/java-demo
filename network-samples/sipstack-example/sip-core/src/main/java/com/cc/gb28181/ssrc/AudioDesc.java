package com.cc.gb28181.ssrc;

import gov.nist.core.Separators;
import gov.nist.javax.sdp.fields.SDPField;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AudioDesc extends SDPField {
    private final String value;

    @Override
    public String encode() {
        return "f=" + value + Separators.NEWLINE;
    }
}
