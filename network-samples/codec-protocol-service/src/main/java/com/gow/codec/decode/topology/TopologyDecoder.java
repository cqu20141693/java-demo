package com.gow.codec.decode.topology;

import com.gow.codec.ProtocolDecoder;
import com.gow.codec.exception.DecodeException;
import com.gow.codec.model.topology.DeviceTopologyModel;
import com.gow.codec.model.topology.TopologySendModel;
import com.gow.codec.util.CodecUtils;
import java.util.HashSet;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gow
 * @date 2021/9/27
 */
@Slf4j
public class TopologyDecoder implements ProtocolDecoder<TopologySendModel> {

    private static final int GROUP_KEY_LEN = 16;

    @Override
    public TopologySendModel decode(byte[] bytes) {
        TopologySendModel topologySendModel = new TopologySendModel();
        ParsePhase phase = ParsePhase.PARSE_VERSION;
        TopologyParseContext context = new TopologyParseContext(bytes.length, phase);
        while (context.getPhase() != ParsePhase.END) {
            switch (context.getPhase()) {
                case PARSE_VERSION:
                    parseVersion(bytes, context);
                    context.setPhase(ParsePhase.PARSE_TOPOLOGY_VERSION);
                    break;
                case PARSE_TOPOLOGY_VERSION:
                    Integer version = parseTopologyVersion(bytes, context);
                    if (version < 0) {
                        throw new DecodeException(phase + " topology version error");
                    }
                    if (version > 0) {
                        context.hasTopology();
                        topologySendModel.setTopology(new DeviceTopologyModel());
                    }
                    topologySendModel.setVersion(version);
                    context.setPhase(ParsePhase.PARSE_GATEWAY_GROUP_KEY);
                    break;
                case PARSE_GATEWAY_GROUP_KEY:
                    topologySendModel.setGatewayGroupKey(parseGatewayGroupKey(bytes, context));
                    context.setPhase(ParsePhase.PARSE_TOPOLOGY);
                    break;
                case PARSE_TOPOLOGY:
                    if (context.isHasTopology()) {
                        parseTopology(bytes, context, topologySendModel.getTopology(),
                                topologySendModel.getGatewayGroupKey());
                    }
                    context.setPhase(ParsePhase.END);
                    break;
            }
            if (context.getStartIndex() != context.getEndIndex()) {
                throw new DecodeException(context.getPhase() + " startIndex!=endIndex");
            }
        }
        if (context.getEndIndex() < bytes.length ) {
            log.warn("decode bytes not used");
        }
        return topologySendModel;
    }

    private void parseTopology(byte[] bytes, TopologyParseContext context, DeviceTopologyModel topology,
                               String gatewayGroupKey) {
        context.endIndexIncrDelta(1);
        byte SNLen = bytes[context.getStartIndex()];
        context.startIndexIncrDelta(1);
        if (SNLen < 1) {
            throw new DecodeException(context.getPhase() + " SN len error");
        }
        topology.setSn(parseString(bytes, context, SNLen));
        context.endIndexIncrDelta(2);
        byte[] control = new byte[2];
        control[0] = bytes[context.getStartIndex()];
        context.startIndexIncrDelta(1);
        control[1] = bytes[context.getStartIndex()];
        context.startIndexIncrDelta(1);
        if ((control[0] & 0x80) != 0) {
            topology.setGroupKey(gatewayGroupKey);
        } else {
            topology.setGroupKey(parseString(bytes, context, GROUP_KEY_LEN));
        }
        int nodeLen = (control[0] & 0x01) << 8;
        nodeLen += control[1] & 0xff;
        if (nodeLen > 0) {
            HashSet<DeviceTopologyModel> models = new HashSet<>();
            topology.setNodes(models);
            for (int i = 0; i < nodeLen; i++) {
                DeviceTopologyModel model = new DeviceTopologyModel();
                parseTopology(bytes, context, model, gatewayGroupKey);
                models.add(model);
            }
        }
    }

    private String parseGatewayGroupKey(byte[] bytes, TopologyParseContext context) {
        return parseString(bytes, context, GROUP_KEY_LEN);
    }

    private Integer parseTopologyVersion(byte[] bytes, TopologyParseContext context) {
        return parseNumber(bytes, context, 4);
    }


    private void parseVersion(byte[] bytes, TopologyParseContext context) {
        context.endIndexIncrDelta(1);
        byte version = bytes[context.getStartIndex()];
        context.startIndexIncrDelta(1);
        log.debug("schema version={}", version);

    }

    private String parseString(byte[] bytes, TopologyParseContext context, int length) {
        context.endIndexIncrDelta(length);
        byte[] stringBytes = new byte[length];
        for (int i = 0; context.getStartIndex() < context.getEndIndex(); ) {
            stringBytes[i++] = bytes[context.getStartIndex()];
            context.startIndexIncrDelta(1);
        }
        return new String(stringBytes);
    }

    private int parseNumber(byte[] bytes, TopologyParseContext context, int maxLength) {
        while (true) {
            if (bytes.length == context.getEndIndex()) {
                break;
            }
            if (bytes[context.getEndIndex()] < 0) {
                context.endIndexIncrDelta(1);
            } else {
                context.endIndexIncrDelta(1);
                break;
            }
        }
        byte[] numberLen = new byte[context.getEndIndex() - context.getStartIndex()];
        for (int i = 0; context.getStartIndex() < context.getEndIndex(); ) {
            numberLen[i++] = bytes[context.getStartIndex()];
            context.startIndexIncrDelta(1);
        }
        if (numberLen.length > maxLength || numberLen.length == 0) {
            throw new DecodeException(context.getPhase() + " topology version length decode failed");
        }
        return CodecUtils.decode(numberLen);
    }

    enum ParsePhase {
        PARSE_VERSION,
        PARSE_TOPOLOGY_VERSION,
        PARSE_GATEWAY_GROUP_KEY,
        PARSE_TOPOLOGY,

        END;
    }

    @Getter
    private static class TopologyParseContext {
        private final int total;

        private ParsePhase phase;
        private int startIndex = 0;
        private int endIndex = 0;

        private boolean hasTopology = false;

        public TopologyParseContext(int total, ParsePhase phase) {

            this.total = total;
            this.phase = phase;
        }

        public void setPhase(ParsePhase phase) {
            this.phase = phase;
        }

        public void endIndexIncrDelta(int delta) {
            if (delta <= 0) {
                throw new DecodeException(phase + " endIndex incr delta >=0");
            }
            this.endIndex += delta;
            if (endIndex > total) {
                throw new DecodeException(phase + " endIndex exceed");
            }
        }

        public void startIndexIncrDelta(int delta) {
            this.startIndex += delta;
        }

        public void hasTopology() {
            this.hasTopology = true;
        }
    }
}
