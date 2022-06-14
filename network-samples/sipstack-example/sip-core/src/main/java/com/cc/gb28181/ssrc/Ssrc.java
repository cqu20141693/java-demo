package com.cc.gb28181.ssrc;

import gov.nist.core.Separators;
import gov.nist.javax.sdp.fields.SDPField;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Ssrc extends SDPField {
    private final String value;

    @Override
    public String encode() {
        return "y=" + value + Separators.NEWLINE;
    }
}
