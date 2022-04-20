package com.cc.link.model;

import gov.nist.javax.sdp.fields.SDPField;

/**
 * @author gow
 * @date 2022/2/17
 */
public class YField extends SDPField {


    private String ssrc;

    public YField() {
        super("y=");
    }

    public void setSsrc(String ssrc) {
        this.ssrc = ssrc;
    }

    @Override
    public String encode() {
        return this.fieldName + this.ssrc + "\r\n";
    }
}
