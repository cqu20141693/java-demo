package com.gow.codec.encode.schema;

import com.gow.codec.exception.EncodeException;
import lombok.Data;

/**
 * @author gow
 * @date 2021/9/23
 */
@Data
public class Operation {
    private byte[] name;
    private byte controlAndEncode;

    private Operation(Builder builder) {
        this.name = builder.name;
        this.controlAndEncode = builder.controlAndEncode;
    }

    public static class Builder {

        private final byte[] name;
        private byte controlAndEncode;

        public Builder(byte[] name, byte encodeType) {
            if (name == null || name.length == 0 || name.length > 127) {
                throw new EncodeException("single sensor name error");
            }
            this.name = name;
            // encodeType check
            if (encodeType > 15 || encodeType < 1) {
                throw new EncodeException("single sensor encodeType error");
            }

            controlAndEncode |= encodeType;
        }

        public Operation build() {
            return new Operation(this);
        }
    }
}
