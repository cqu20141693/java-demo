package com.gow.codec.encode.topology;

import com.gow.codec.exception.EncodeException;
import lombok.Getter;

/**
 * @author gow
 * @date 2021/9/23
 */
@Getter
public class TopologyContext {

    private final byte version;
    // 0表示不存在拓扑信息
    private final byte[] topologyVersion;
    private final byte[] gatewayGroupKey;
    private final TopologyNode topologyNode;
    private final Integer total;

    public TopologyContext(Builder builder) {
        this.version = builder.version;
        this.topologyVersion = builder.topologyVersion;
        this.gatewayGroupKey = builder.gatewayGroupKey;
        this.topologyNode = builder.topologyNode;
        this.total = builder.total;
    }

    public byte[] encode() {
        byte[] bytes = new byte[total];
        int index = 0;
        if (total > index) {
            bytes[index++] = version;
            for (byte b : topologyVersion) {
                bytes[index++] = b;
            }
            for (byte b : gatewayGroupKey) {
                bytes[index++] = b;
            }

            index = composeTopologyNode(bytes, index, this.topologyNode);


        } else {
            throw new EncodeException("index exceed total.");
        }
        if (total != index) {
            throw new EncodeException("index not equal total.");
        }
        return bytes;
    }

    private int composeTopologyNode(byte[] bytes, int index, TopologyNode topologyNode) {
        if (this.topologyNode != null) {
            byte SNLength = (byte) topologyNode.getSN().length;
            bytes[index++] = SNLength;
            for (byte b : topologyNode.getSN()) {
                bytes[index++] = b;
            }
            for (byte b : topologyNode.getControl()) {
                bytes[index++] = b;
            }
            if (topologyNode.getGroupKey() != null) {
                for (byte b : topologyNode.getGroupKey()) {
                    bytes[index++] = b;
                }
            }
            if (topologyNode.getChildren() != null) {
                for (TopologyNode child : topologyNode.getChildren()) {
                    index = composeTopologyNode(bytes, index, child);
                }
            }
        }
        return index;
    }

    public static class Builder {
        private final byte version;
        // 0表示不存在拓扑信息
        private byte[] topologyVersion;
        private byte[] gatewayGroupKey;
        private TopologyNode topologyNode;
        private Integer total;

        public Builder(byte version) {
            if (version < 0) {
                throw new EncodeException("version error");
            }
            this.version = version;
            total = 1;
        }

        public Builder topologyVersion(byte[] topologyVersion) {
            if (topologyVersion == null || topologyVersion.length == 0 || topologyVersion.length > 4) {
                throw new EncodeException("topology version error");
            }
            this.topologyVersion = topologyVersion;
            total += topologyVersion.length;
            return this;
        }

        public Builder gatewayGroupKey(byte[] gatewayGroupKey) {
            if (gatewayGroupKey == null || gatewayGroupKey.length != 16) {
                throw new EncodeException("topology gateway groupKey error");
            }
            this.gatewayGroupKey = gatewayGroupKey;
            total += gatewayGroupKey.length;
            return this;
        }

        public Builder topologyNode(TopologyNode topologyNode) {
            if (topologyNode != null) {
                // SN
                total++;
                total += topologyNode.getSN().length;
                // control
                total += 2;
                // groupKey
                if (topologyNode.getGroupKey() != null) {
                    total += topologyNode.getGroupKey().length;
                }
                // children
                if (topologyNode.getChildren() != null) {
                    for (TopologyNode child : topologyNode.getChildren()) {
                        this.topologyNode(child);
                    }
                }

                this.topologyNode = topologyNode;
            }
            return this;
        }

        public TopologyContext build() {
            return new TopologyContext(this);
        }
    }
}
