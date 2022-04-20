package com.gow.codec.encode.topology;

import com.gow.codec.exception.EncodeException;
import java.util.List;
import lombok.Getter;

/**
 * @author gow
 * @date 2021/9/23
 */
@Getter
public class TopologyNode {
    private final byte[] SN;
    private final byte[] control;
    private final byte[] groupKey;
    private final List<TopologyNode> children;

    public TopologyNode(Builder builder) {
        this.SN = builder.SN;
        this.control = builder.control;
        this.groupKey = builder.groupKey;
        this.children = builder.children;
    }


    public static class Builder {
        private final byte[] SN;
        private final byte[] control = new byte[2];
        private byte[] groupKey;
        private List<TopologyNode> children;

        public Builder(byte[] SN) {
            if (SN == null || SN.length == 0 || SN.length > 127) {
                throw new EncodeException("topology sn error");
            }
            this.SN = SN;
            control[0] = (byte) 0x80;
        }

        public Builder groupKey(byte[] groupKey) {
            if (groupKey == null || groupKey.length != 16) {
                throw new EncodeException("topology groupKey error");
            }
            this.groupKey = groupKey;
            control[0] &= 0x7f;
            return this;
        }

        public Builder children(List<TopologyNode> children) {
            if (children != null && children.size() > 0) {
                for (TopologyNode child : children) {
                    if (child == null) {
                        throw new EncodeException("topology children null error");
                    }
                }
                int size = children.size();
                if (size > 1023) {
                    throw new EncodeException("topology children the quantity exceeds the limit");
                }
                control[0] |= (size >> 8);
                control[1] |= size;
                this.children = children;
            }
            return this;
        }

        public TopologyNode build() {
            return new TopologyNode(this);
        }

    }
}
