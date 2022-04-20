package com.gow.codec.decode.topology;

import com.gow.codec.ProtocolDecoder;
import com.gow.codec.exception.DecodeException;
import com.gow.codec.model.topology.DeviceTopologyDiffModel;
import com.gow.codec.model.topology.TopologyDiffSendModel;
import com.gow.codec.util.CodecUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gow
 * @date 2021/9/27
 */
@Slf4j
public class TopologyDiffDecoder implements ProtocolDecoder<TopologyDiffSendModel> {

    private static final int GROUP_KEY_LEN = 16;

    @Override
    public TopologyDiffSendModel decode(byte[] bytes) {

        TopologyDiffSendModel diffSendModel = new TopologyDiffSendModel();
        ParsePhase phase = ParsePhase.PARSE_VERSION;
        TopologyDiffParseContext context = new TopologyDiffParseContext(bytes.length, phase);
        while (context.getPhase() != ParsePhase.END) {
            switch (context.getPhase()) {
                case PARSE_VERSION:
                    parseVersion(bytes, context);
                    context.setPhase(ParsePhase.PARSE_CLOUD_VERSION);
                    break;
                case PARSE_CLOUD_VERSION:
                    Integer version = parseCloudVersion(bytes, context);
                    if (version < 0) {
                        throw new DecodeException(phase + " cloud version error");
                    }

                    diffSendModel.setCloudVersion(version);
                    context.setPhase(ParsePhase.PARSE_SEND_VERSION);
                    break;
                case PARSE_SEND_VERSION:
                    Integer sendVersion = parseSendVersion(bytes, context);
                    if (sendVersion < 0) {
                        throw new DecodeException(phase + " send version error");
                    }
                    if (sendVersion > 0) {
                        context.hasDiff();
                        List<DeviceTopologyDiffModel> models = new ArrayList<>();
                        diffSendModel.setModels(models);
                    }
                    diffSendModel.setSendVersion(sendVersion);
                    context.setPhase(ParsePhase.PARSE_GATEWAY_GROUP_KEY);
                    break;
                case PARSE_GATEWAY_GROUP_KEY:
                    diffSendModel.setGatewayGroupKey(parseGatewayGroupKey(bytes, context));
                    context.setPhase(ParsePhase.PARSE_RECORD_NUMBER);
                    break;
                case PARSE_RECORD_NUMBER:
                    context.setRecordNumber(parseNumber(bytes, context, 2));
                    context.setPhase(ParsePhase.PARSE_OP_RECORD);
                    break;
                case PARSE_OP_RECORD:
                    if (context.isHasDiff()) {
                        for (int i = 0; i < context.getRecordNumber(); i++) {
                            diffSendModel.getModels().add(parseOpRecord(bytes, context,
                                    diffSendModel.getGatewayGroupKey()));
                        }
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
        return diffSendModel;
    }

    private DeviceTopologyDiffModel parseOpRecord(byte[] bytes, TopologyDiffParseContext context,
                                                  String gatewayGroupKey) {
        DeviceTopologyDiffModel diffModel = new DeviceTopologyDiffModel();
        DeviceTopologyDiffModel.DeviceTag fatherTag = new DeviceTopologyDiffModel.DeviceTag();
        diffModel.setFatherDeviceTag(fatherTag);
        DeviceTopologyDiffModel.DeviceTag childTag = new DeviceTopologyDiffModel.DeviceTag();
        diffModel.setChildDeviceTag(childTag);

        context.endIndexIncrDelta(1);
        byte control = bytes[context.getStartIndex()];
        context.startIndexIncrDelta(1);
        if ((control & 0x80) != 0) {
            fatherTag.setGroupKey(gatewayGroupKey);
        } else {
            fatherTag.setGroupKey(parseString(bytes, context, GROUP_KEY_LEN));
        }
        if ((control & 0x40) != 0) {
            childTag.setGroupKey(gatewayGroupKey);
        } else {
            childTag.setGroupKey(parseString(bytes, context, GROUP_KEY_LEN));
        }
        diffModel.setOperationType((byte) (control & 0x03));
        context.endIndexIncrDelta(1);
        byte fatherSNLen = bytes[context.getStartIndex()];
        context.startIndexIncrDelta(1);
        fatherTag.setSn(parseString(bytes, context, fatherSNLen));

        context.endIndexIncrDelta(1);
        byte childSNLen = bytes[context.getStartIndex()];
        context.startIndexIncrDelta(1);
        childTag.setSn(parseString(bytes, context, childSNLen));

        return diffModel;
    }

    private String parseGatewayGroupKey(byte[] bytes, TopologyDiffParseContext context) {
        return parseString(bytes, context, GROUP_KEY_LEN);
    }

    private Integer parseCloudVersion(byte[] bytes, TopologyDiffParseContext context) {
        return parseNumber(bytes, context, 4);
    }

    private Integer parseSendVersion(byte[] bytes, TopologyDiffParseContext context) {
        return parseNumber(bytes, context, 4);
    }


    private void parseVersion(byte[] bytes, TopologyDiffParseContext context) {
        context.endIndexIncrDelta(1);
        byte version = bytes[context.getStartIndex()];
        context.startIndexIncrDelta(1);
        log.debug("schema version={}", version);

    }

    private String parseString(byte[] bytes, TopologyDiffParseContext context, int length) {
        context.endIndexIncrDelta(length);
        byte[] stringBytes = new byte[length];
        for (int i = 0; context.getStartIndex() < context.getEndIndex(); ) {
            stringBytes[i++] = bytes[context.getStartIndex()];
            context.startIndexIncrDelta(1);
        }
        return new String(stringBytes);
    }

    private int parseNumber(byte[] bytes, TopologyDiffParseContext context, int maxLength) {
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
        PARSE_CLOUD_VERSION,
        PARSE_SEND_VERSION,
        PARSE_GATEWAY_GROUP_KEY,
        PARSE_RECORD_NUMBER,
        PARSE_OP_RECORD,

        END;
    }

    @Getter
    private static class TopologyDiffParseContext {
        private final int total;

        private ParsePhase phase;
        private int startIndex = 0;
        private int endIndex = 0;

        private boolean hasDiff = false;

        private int recordNumber = 0;

        public TopologyDiffParseContext(int total, ParsePhase phase) {

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

        public void hasDiff() {
            this.hasDiff = true;
        }

        public void setRecordNumber(int recordNumber) {
            this.recordNumber = recordNumber;
        }
    }
}
