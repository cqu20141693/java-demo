package com.gow.codec.encode.topology;

import static com.gow.codec.util.CodecUtils.getVariableNumberBytes;
import com.gow.codec.exception.EncodeException;
import java.util.List;
import lombok.Getter;

/**
 * @author gow
 * @date 2021/9/23
 */
@Getter
public class TopologyDiffContext {

    private final byte version;
    // 0表示不存在拓扑信息
    private final byte[] cloudVersion;
    private final byte[] sendVersion;
    private final byte[] gatewayGroupKey;
    private final List<TopologyOpRecord> topologyOpRecords;
    private final Integer total;

    public TopologyDiffContext(Builder builder) {
        this.version = builder.version;
        this.cloudVersion = builder.cloudVersion;
        this.sendVersion = builder.sendVersion;
        this.gatewayGroupKey = builder.gatewayGroupKey;
        this.topologyOpRecords = builder.topologyOpRecords;
        this.total = builder.total;
    }

    public byte[] encode() {
        byte[] bytes = new byte[total];
        int index = 0;
        if (total > index) {
            bytes[index++] = version;
            for (byte b : cloudVersion) {
                bytes[index++] = b;
            }
            for (byte b : sendVersion) {
                bytes[index++] = b;
            }

            for (byte b : gatewayGroupKey) {
                bytes[index++] = b;
            }
            if (topologyOpRecords != null) {
                for (byte b : getVariableNumberBytes(topologyOpRecords.size())) {
                    bytes[index++] = b;
                }
                for (TopologyOpRecord record : topologyOpRecords) {
                    bytes[index++] = record.getControl();
                    if (record.getFatherGroupKey() != null) {
                        for (byte b : record.getFatherGroupKey()) {
                            bytes[index++] = b;
                        }
                    }
                    if (record.getChildGroupKey() != null) {
                        for (byte b : record.getChildGroupKey()) {
                            bytes[index++] = b;
                        }
                    }
                    byte fatherSnLength = (byte) record.getFatherSN().length;
                    bytes[index++] = fatherSnLength;
                    for (byte b : record.getFatherSN()) {
                        bytes[index++] = b;
                    }
                    byte childSnLength = (byte) record.getChildSN().length;
                    bytes[index++] = childSnLength;
                    for (byte b : record.getChildSN()) {
                        bytes[index++] = b;
                    }
                }
            }

        } else {
            throw new EncodeException("index exceed total.");
        }
        if (total != index) {
            throw new EncodeException("index not equal total.");
        }
        return bytes;
    }

    public static class Builder {
        private final byte version;
        private byte[] cloudVersion;
        private byte[] sendVersion;
        private byte[] gatewayGroupKey;
        private List<TopologyOpRecord> topologyOpRecords;
        private Integer total;

        public Builder(byte version) {
            if (version < 0) {
                throw new EncodeException("version error");
            }
            this.version = version;
            total = 1;
        }

        public Builder cloudAndSendVersion(byte[] cloudVersion, byte[] sendVersion) {
            if (cloudVersion == null || cloudVersion.length == 0 || cloudVersion.length > 4) {
                throw new EncodeException("topology diff cloud version error");
            }
            if (sendVersion == null || sendVersion.length == 0 || sendVersion.length > 4) {
                throw new EncodeException("topology diff send version error");
            }
            this.cloudVersion = cloudVersion;
            total += cloudVersion.length;
            this.sendVersion = sendVersion;
            total += cloudVersion.length;
            return this;
        }

        public Builder gatewayGroupKey(byte[] gatewayGroupKey) {
            if (gatewayGroupKey == null || gatewayGroupKey.length != 16) {
                throw new EncodeException("topology diff gateway groupKey error");
            }
            this.gatewayGroupKey = gatewayGroupKey;
            total += gatewayGroupKey.length;
            return this;
        }

        public Builder topologyOpRecords(List<TopologyOpRecord> topologyOpRecords) {
            if (topologyOpRecords != null && topologyOpRecords.size() > 0) {
                int size = topologyOpRecords.size();
                total += getVariableNumberBytes(size).length;
                for (TopologyOpRecord record : topologyOpRecords) {
                    if (record == null) {
                        throw new EncodeException("topology diff record null error");
                    }
                    total++;
                    total++;
                    total += record.getChildSN().length;
                    total++;
                    total += record.getFatherSN().length;
                    if (record.getChildGroupKey() != null) {
                        total += record.getChildGroupKey().length;
                    }
                    if (record.getFatherGroupKey() != null) {
                        total += record.getFatherGroupKey().length;
                    }
                }
                this.topologyOpRecords = topologyOpRecords;

            }
            return this;
        }

        public TopologyDiffContext build() {
            if (cloudVersion == null || sendVersion == null || gatewayGroupKey == null) {
                throw new EncodeException("topology diff cloudVersion，sendVersion，gatewayGroupKey must be set");
            }
            return new TopologyDiffContext(this);
        }
    }
}
