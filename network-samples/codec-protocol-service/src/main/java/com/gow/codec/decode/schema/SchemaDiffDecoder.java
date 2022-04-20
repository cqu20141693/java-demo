package com.gow.codec.decode.schema;

import static com.gow.codec.decode.schema.SchemaDiffDecoder.ParsePhase.END;
import static com.gow.codec.decode.schema.SchemaDiffDecoder.ParsePhase.PARSE_GROUP_KEY;
import static com.gow.codec.decode.schema.SchemaDiffDecoder.ParsePhase.PARSE_OPERATION_DELETE;
import static com.gow.codec.decode.schema.SchemaDiffDecoder.ParsePhase.PARSE_SCHEMA_VERSION;
import static com.gow.codec.decode.schema.SchemaDiffDecoder.ParsePhase.PARSE_SN;
import com.gow.codec.ProtocolDecoder;
import com.gow.codec.exception.DecodeException;
import com.gow.codec.model.schema.SchemaDiffModel;
import com.gow.codec.util.CodecUtils;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gow
 * @date 2021/9/23
 */
@Slf4j
public class SchemaDiffDecoder implements ProtocolDecoder<SchemaDiffModel> {

    private static final int GROUP_KEY_LEN = 16;

    @Override
    public SchemaDiffModel decode(byte[] bytes) {
        SchemaDiffModel diffModel = new SchemaDiffModel();

        ParsePhase phase = ParsePhase.PARSE_VERSION;
        SchemaDiffParseContext context = new SchemaDiffParseContext(bytes.length, phase);
        while (context.getPhase() != END) {
            switch (context.getPhase()) {
                case PARSE_VERSION:
                    parseVersion(bytes, context);
                    context.setPhase(ParsePhase.PARSE_CONTROL);
                    break;
                case PARSE_CONTROL:
                    parseControl(bytes, diffModel, context);
                    context.setPhase(PARSE_GROUP_KEY);
                    break;
                case PARSE_GROUP_KEY:
                    diffModel.setGroupKey(parseGroupKey(bytes, context));
                    context.setPhase(PARSE_SN);
                    break;
                case PARSE_SN:
                    diffModel.setSn(parseSN(bytes, context));
                    context.setPhase(PARSE_SCHEMA_VERSION);
                    break;
                case PARSE_SCHEMA_VERSION:
                    diffModel.setVersion(parseSchemaVersion(bytes, context));
                    context.setPhase(ParsePhase.PARSE_SENSOR_UPDATE);
                    break;
                case PARSE_SENSOR_UPDATE:
                    if (context.isHasSensorUpdate()) {
                        parseIndex(bytes, diffModel.getSensorMeasurementUpdateIndex(), context);
                    }
                    context.setPhase(ParsePhase.PARSE_SENSOR_DELETE);
                    break;
                case PARSE_SENSOR_DELETE:
                    if (context.isHasSensorDelete()) {
                        parseIndex(bytes, diffModel.getSensorMeasurementDeleteIndex(), context);
                    }
                    context.setPhase(ParsePhase.PARSE_SELF_SENSOR_UPDATE);
                    break;
                case PARSE_SELF_SENSOR_UPDATE:
                    if (context.isHasSelfSensorUpdate()) {
                        parseIndex(bytes, diffModel.getSelfMeasurementUpdateIndex(), context);
                    }
                    context.setPhase(ParsePhase.PARSE_SELF_SENSOR_DELETE);
                    break;
                case PARSE_SELF_SENSOR_DELETE:
                    if (context.isHasSelfSensorDelete()) {
                        parseIndex(bytes, diffModel.getSelfMeasurementDeleteIndex(), context);
                    }
                    context.setPhase(ParsePhase.PARSE_OPERATION_UPDATE);
                    break;
                case PARSE_OPERATION_UPDATE:
                    if (context.isHasOperationUpdate()) {
                        parseIndex(bytes, diffModel.getOperationUpdateIndex(), context);
                    }
                    context.setPhase(PARSE_OPERATION_DELETE);
                case PARSE_OPERATION_DELETE:
                    if (context.isHasOperationDelete()) {
                        parseIndex(bytes, diffModel.getOperationDeleteIndex(), context);
                    }
                    context.setPhase(END);
                    break;
            }
            if (context.getStartIndex() != context.getEndIndex()) {
                throw new DecodeException(context.getPhase() + " startIndex!=endIndex");
            }
        }
        if (context.getEndIndex() < bytes.length ) {
            log.warn("decode bytes not used");
        }
        return diffModel;
    }

    private void parseIndex(byte[] bytes, Set<String> modelIndexes, SchemaDiffParseContext context) {

        int number = parseNumber(bytes, context, 2);
        if (number <= 0) {
            throw new DecodeException(context.getPhase() + " indexes length must be more than 0");
        }
        for (int i = 0; i < number; i++) {
            context.endIndexIncrDelta(1);
            byte nameLen = bytes[context.getStartIndex()];
            if (nameLen <= 0) {
                throw new DecodeException(context.getPhase() + " Name length must be more than 0");
            }
            context.startIndexIncrDelta(1);
            modelIndexes.add(parseString(bytes, context, nameLen));
        }
    }

    private Integer parseSchemaVersion(byte[] bytes, SchemaDiffParseContext context) {
        return parseNumber(bytes, context, 4);
    }

    private int parseNumber(byte[] bytes, SchemaDiffParseContext context, int maxLength) {
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
            throw new DecodeException(context.getPhase() + " schema version length decode failed");
        }
        return CodecUtils.decode(numberLen);
    }

    private void parseControl(byte[] bytes, SchemaDiffModel schemaModel, SchemaDiffParseContext context) {
        context.endIndexIncrDelta(1);
        byte control = bytes[context.getStartIndex()];
        context.startIndexIncrDelta(1);
        if ((control & 0x80) != 0) {
            context.commonChanged();
            schemaModel.setCommonChanged(true);
        }
        if ((control & 0x40) != 0) {
            context.hasSensorUpdate();
            Set<String> sensors = new HashSet<>();
            schemaModel.setSensorMeasurementUpdateIndex(sensors);
        }

        if ((control & 0x20) != 0) {
            context.hasSensorDelete();
            Set<String> sensors = new HashSet<>();
            schemaModel.setSensorMeasurementDeleteIndex(sensors);
        }
        if ((control & 0x10) != 0) {
            context.hasSelfSensorUpdate();
            HashSet<String> selfSensor = new HashSet<>();
            schemaModel.setSelfMeasurementUpdateIndex(selfSensor);
        }
        if ((control & 0x08) != 0) {
            context.hasSelfSensorDelete();
            HashSet<String> selfSensor = new HashSet<>();
            schemaModel.setSelfMeasurementDeleteIndex(selfSensor);
        }
        if ((control & 0x04) != 0) {
            context.hasOperationUpdate();
            HashSet<String> operations = new HashSet<>();
            schemaModel.setOperationUpdateIndex(operations);
        }
        if ((control & 0x02) != 0) {
            context.hasOperationDelete();
            HashSet<String> operations = new HashSet<>();
            schemaModel.setOperationUpdateIndex(operations);
        }
    }

    private String parseGroupKey(byte[] bytes, SchemaDiffParseContext context) {
        return parseString(bytes, context, GROUP_KEY_LEN);
    }

    private String parseSN(byte[] bytes, SchemaDiffParseContext context) {
        context.endIndexIncrDelta(1);
        byte SNLen = bytes[context.startIndex];
        if (SNLen <= 0) {
            throw new DecodeException(context.getPhase() + " SN length must be more than 0");
        }
        context.startIndexIncrDelta(1);
        return parseString(bytes, context, SNLen);
    }

    private String parseString(byte[] bytes, SchemaDiffParseContext context, int length) {
        context.endIndexIncrDelta(length);
        byte[] stringBytes = new byte[length];
        for (int i = 0; context.getStartIndex() < context.getEndIndex(); ) {
            stringBytes[i++] = bytes[context.getStartIndex()];
            context.startIndexIncrDelta(1);
        }
        return new String(stringBytes);
    }


    private void parseVersion(byte[] bytes, SchemaDiffParseContext context) {
        context.endIndexIncrDelta(1);
        byte version = bytes[context.getStartIndex()];
        context.startIndexIncrDelta(1);
        log.debug("schema version={}", version);

    }

    enum ParsePhase {
        PARSE_VERSION,
        PARSE_CONTROL,
        PARSE_GROUP_KEY,
        PARSE_SN,
        PARSE_SCHEMA_VERSION,
        PARSE_SENSOR_UPDATE,
        PARSE_SENSOR_DELETE,
        PARSE_SELF_SENSOR_UPDATE,
        PARSE_SELF_SENSOR_DELETE,
        PARSE_OPERATION_UPDATE,
        PARSE_OPERATION_DELETE,

        END;
    }

    @Getter
    private static class SchemaDiffParseContext {

        private final int total;

        private ParsePhase phase;
        private int startIndex = 0;
        private int endIndex = 0;

        private boolean commonChanged = false;
        private boolean hasSensorUpdate = false;
        private boolean hasSensorDelete = false;
        private boolean hasSelfSensorUpdate = false;
        private boolean hasSelfSensorDelete = false;
        private boolean hasOperationUpdate = false;
        private boolean hasOperationDelete = false;

        public SchemaDiffParseContext(int total, ParsePhase phase) {
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

        public void commonChanged() {
            this.commonChanged = true;
        }

        public void hasSensorUpdate() {
            this.hasSensorUpdate = true;
        }

        public void hasSensorDelete() {
            this.hasSensorDelete = true;
        }

        public void hasSelfSensorUpdate() {
            this.hasSelfSensorUpdate = true;
        }

        public void hasSelfSensorDelete() {
            this.hasSelfSensorDelete = true;
        }

        public void hasOperationUpdate() {
            this.hasOperationUpdate = true;
        }

        public void hasOperationDelete() {
            this.hasOperationDelete = true;
        }

    }
}
