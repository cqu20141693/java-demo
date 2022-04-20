package com.gow.codec.encode.topology;

import com.gow.codec.exception.EncodeException;
import com.gow.codec.model.topology.TopologyOperationType;
import lombok.Getter;

/**
 * @author gow
 * @date 2021/9/23
 */
@Getter
public class TopologyOpRecord {

    private final byte control;
    private final byte[] fatherGroupKey;
    private final byte[] childGroupKey;
    private final byte[] fatherSN;
    private final byte[] childSN;

    public TopologyOpRecord(Builder builder) {
        this.control = builder.control;
        this.fatherGroupKey = builder.fatherGroupKey;
        this.childGroupKey = builder.childGroupKey;
        this.fatherSN = builder.fatherSN;
        this.childSN = builder.childSN;
    }


    public static class Builder {
        private byte control;
        private byte[] fatherGroupKey;
        private byte[] childGroupKey;
        private final byte[] fatherSN;
        private final byte[] childSN;

        public Builder(byte[] fatherSN, byte[] childSN) {
            if (fatherSN == null || fatherSN.length == 0 || fatherSN.length > 127) {
                throw new EncodeException("topology diff fatherSN error");
            }
            if (childSN == null || childSN.length == 0 || childSN.length > 127) {
                throw new EncodeException("topology diff childSN error");
            }
            this.fatherSN = fatherSN;
            this.childSN = childSN;
            this.control |= 0xc0;
        }

        public Builder fatherGroupKey(byte[] fatherGroupKey) {
            if (fatherGroupKey == null || fatherGroupKey.length != 16) {
                throw new EncodeException("topology diff fatherGroupKey error");
            }
            this.fatherGroupKey = fatherGroupKey;
            this.control &= 0x7f;
            return this;
        }

        public Builder childGroupKey(byte[] childGroupKey) {
            if (childGroupKey == null || childGroupKey.length != 16) {
                throw new EncodeException("topology diff childGroupKey error");
            }
            this.childGroupKey = childGroupKey;
            this.control &= 0xbf;
            return this;
        }

        public Builder bind(TopologyOperationType type) {
            if (type == null) {
                throw new EncodeException("topology diff operation type error");
            }
            this.control |= type.getIndex();
            return this;
        }

        public TopologyOpRecord build() {
            if ((control & 0x03) == 0) {
                throw new EncodeException("topology diff operation type must be set");
            }
            return new TopologyOpRecord(this);
        }
    }
}
