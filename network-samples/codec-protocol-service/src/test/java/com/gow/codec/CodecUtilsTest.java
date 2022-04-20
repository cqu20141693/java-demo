package com.gow.codec;

import static com.gow.codec.util.CodecUtils.getVariableNumberBytes;
import com.gow.codec.util.CodecUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author gow
 * @date 2021/9/27
 */
public class CodecUtilsTest {

    @Test
    @DisplayName("variable number codec")
    public void variableNumberCodec() {

        byte[] lengthBytes = getVariableNumberBytes(1);
        int decode = CodecUtils.decode(lengthBytes);
        assert 1 == decode : "decode failed";
        byte[] lengthBytes1 = getVariableNumberBytes(127);
        int decode1 = CodecUtils.decode(lengthBytes1);
        assert 127 == decode1 : "decode failed";
        byte[] lengthBytes2 = getVariableNumberBytes(128);
        int decode2 = CodecUtils.decode(lengthBytes2);
        assert 128 == decode2 : "decode failed";
        byte[] lengthBytes3 = getVariableNumberBytes(1 << 24);
        int decode3 = CodecUtils.decode(lengthBytes3);
        assert (1 << 24) == decode3 : "decode failed";
    }
}
