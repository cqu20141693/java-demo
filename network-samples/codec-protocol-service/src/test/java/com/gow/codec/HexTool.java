package com.gow.codec;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author gow
 * @date 2021/9/26
 */
public class HexTool {
    @Test
    @DisplayName("convert string to hex")
    public void convertStringToHex() {

        //schema commonByte
        String hexData="018052547a516456445051456d45723567321463686f6e6763746563684465766963653030303102010d737472696e672d73656e736f720e737472696e672d6368616e6e656c28";
        System.out.println(convert(hexData));
        // schema diff bytes
        String diffData="01c052547a516456445051456d45723567321463686f6e676374656368446576696365303030310b020673656e736f720b73656c662d73656e736f72";
        System.out.println(convert(diffData));

        // topology bytes
        String topologyData="010b52547a516456445051456d45723567321463686f6e676374656368446576696365303030318002066368696c64328000066368696c64318000";
        System.out.println(convert(topologyData));

        //topology diff bytes
        String topologyDiffData="010b0b52547a516456445051456d457235673202c11463686f6e67637465636844657669636530303031066368696c6432c11463686f6e67637465636844657669636530303031066368696c6431";
        System.out.println(convert(topologyDiffData));
    }

    public static String convert(String data) {
        char[] chars = data.toCharArray();
        if (chars.length % 2 != 0) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (int i = 0; i < chars.length; i++) {
            if((i+1)%2==0){
                builder.append("0x").append(chars[i-1]).append(chars[i]);
                if(i!=chars.length-1){
                    builder.append(",");
                }
            }
        }
        builder.append("}");
        return builder.toString();
    }
}
