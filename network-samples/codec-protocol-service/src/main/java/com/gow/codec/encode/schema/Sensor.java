package com.gow.codec.encode.schema;

import com.gow.codec.exception.EncodeException;
import lombok.Getter;

/**
 * @author gow
 * @date 2021/9/23
 */
@Getter
public class Sensor {
    private final byte[] name;
    private final byte[] stream;
    private final byte controlAndEncode;
    private final byte[] properties;

    private Sensor(Builder builder) {
        this.name = builder.name;
        this.stream = builder.stream;
        this.controlAndEncode = builder.controlAndEncode;
        this.properties = builder.properties;
    }

    public static class Builder {

        private final byte[] name;
        private final byte[] stream;
        private byte controlAndEncode;
        private byte[] properties;

        public Builder(byte[] name, byte[] stream, byte encodeType) {
            if (name == null || name.length == 0 || name.length > 127) {
                throw new EncodeException("single sensor name error");
            }
            this.name = name;
            if (stream == null || stream.length == 0 || stream.length > 127) {
                throw new EncodeException("single sensor stream error");
            }
            this.stream = stream;
            // encodeType check
            if (encodeType > 15 || encodeType < 1) {
                throw new EncodeException("single sensor encodeType error");
            }

            encodeType <<= 3;
            controlAndEncode = encodeType;
        }

        public Builder properties(byte propsType, byte[] properties) {
            if (propsType > 3 || propsType < 0) {
                throw new EncodeException("single sensor properties type error");
            }
            controlAndEncode |= 0x80;
            controlAndEncode |= propsType;
            this.properties = properties;
            return this;
        }

        public Sensor build() {
            return new Sensor(this);
        }
    }

}
