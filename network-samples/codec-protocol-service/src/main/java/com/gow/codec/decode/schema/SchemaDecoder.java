package com.gow.codec.decode.schema;

import static com.gow.codec.decode.schema.SchemaDecoder.ParsePhase.END;
import static com.gow.codec.decode.schema.SchemaDecoder.ParsePhase.PARSE_CONTROL;
import static com.gow.codec.decode.schema.SchemaDecoder.ParsePhase.PARSE_GROUP_KEY;
import static com.gow.codec.decode.schema.SchemaDecoder.ParsePhase.PARSE_OPERATION;
import static com.gow.codec.decode.schema.SchemaDecoder.ParsePhase.PARSE_SCHEMA_VERSION;
import static com.gow.codec.decode.schema.SchemaDecoder.ParsePhase.PARSE_SELF_SENSOR;
import static com.gow.codec.decode.schema.SchemaDecoder.ParsePhase.PARSE_SENSOR;
import static com.gow.codec.decode.schema.SchemaDecoder.ParsePhase.PARSE_SN;
import static com.gow.codec.model.EncodeTypeEnum.TYPE_JSON;
import com.gow.codec.ProtocolDecoder;
import com.gow.codec.exception.DecodeException;
import com.gow.codec.model.schema.OperationModel;
import com.gow.codec.model.schema.SchemaModel;
import com.gow.codec.model.schema.SensorModel;
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
public class SchemaDecoder implements ProtocolDecoder<SchemaModel> {

    private static final int GROUP_KEY_LEN = 16;

    @Override
    public SchemaModel decode(byte[] bytes) {
        SchemaModel schemaModel = new SchemaModel();

        ParsePhase phase = ParsePhase.PARSE_VERSION;
        SchemaParseContext context = new SchemaParseContext(bytes.length, phase);
        while (context.getPhase() != ParsePhase.END) {
            switch (context.getPhase()) {
                case PARSE_VERSION:
                    parseVersion(bytes, context);
                    context.setPhase(PARSE_CONTROL);
                    break;
                case PARSE_CONTROL:
                    parseControl(bytes, schemaModel, context);
                    context.setPhase(PARSE_GROUP_KEY);
                    break;
                case PARSE_GROUP_KEY:
                    schemaModel.setGroupKey(parseGroupKey(bytes, context));
                    context.setPhase(PARSE_SN);
                    break;
                case PARSE_SN:
                    schemaModel.setSn(parseSN(bytes, context));
                    context.setPhase(PARSE_SCHEMA_VERSION);
                    break;
                case PARSE_SCHEMA_VERSION:
                    schemaModel.setVersion(parseSchemaVersion(bytes, context));
                    context.setPhase(PARSE_SENSOR);
                    break;
                case PARSE_SENSOR:
                    if (context.isHasSensor()) {
                        parseSensor(bytes, schemaModel.getSensorMeasurements(), context);
                    }
                    context.setPhase(PARSE_SELF_SENSOR);
                    break;
                case PARSE_SELF_SENSOR:
                    if (context.isHasSelfSensor()) {
                        parseSensor(bytes, schemaModel.getSelfMeasurements(), context);
                    }
                    context.setPhase(PARSE_OPERATION);
                    break;
                case PARSE_OPERATION:
                    if (context.hasOperation) {
                        parseOperation(bytes, schemaModel.getOperations(), context);
                    }
                    context.setPhase(END);
                    break;
            }
            if (context.getStartIndex() != context.getEndIndex()) {
                throw new DecodeException(context.getPhase() + " startIndex!=endIndex");
            }
        }
        if (context.getEndIndex() < bytes.length) {
            log.warn("decode bytes not used");
        }
        return schemaModel;
    }

    private void parseOperation(byte[] bytes, Set<OperationModel> operationModels, SchemaParseContext context) {
        int operationNumbers = parseNumber(bytes, context, 2);
        for (int i = 0; i < operationNumbers; i++) {
            OperationModel operationModel = new OperationModel();

            context.endIndexIncrDelta(1);
            byte operationNameLen = bytes[context.getStartIndex()];
            if (operationNameLen <= 0) {
                throw new DecodeException(context.getPhase() + " operationName length must be more than 0");
            }
            context.startIndexIncrDelta(1);
            operationModel.setName(parseString(bytes, context, operationNameLen));

            context.endIndexIncrDelta(1);
            byte controlAndEncodeType = bytes[context.getStartIndex()];
            context.startIndexIncrDelta(1);
            operationModel.setEncodeType((byte) (controlAndEncodeType & 0x0f));
            operationModels.add(operationModel);
        }
    }

    private void parseSensor(byte[] bytes, Set<SensorModel> sensorModels, SchemaParseContext context) {

        int sensorNumbers = parseNumber(bytes, context, 2);
        if (sensorNumbers <= 0) {
            throw new DecodeException(context.getPhase() + " sensor length must be more than 0");
        }
        for (int i = 0; i < sensorNumbers; i++) {
            SensorModel sensorModel = new SensorModel();

            context.endIndexIncrDelta(1);
            byte sensorNameLen = bytes[context.getStartIndex()];
            if (sensorNameLen <= 0) {
                throw new DecodeException(context.getPhase() + " sensorName length must be more than 0");
            }
            context.startIndexIncrDelta(1);
            sensorModel.setName(parseString(bytes, context, sensorNameLen));

            context.endIndexIncrDelta(1);
            byte streamLen = bytes[context.getStartIndex()];
            if (streamLen <= 0) {
                throw new DecodeException(context.getPhase() + " stream length must be more than 0");
            }
            context.startIndexIncrDelta(1);
            sensorModel.setStreamName(parseString(bytes, context, streamLen));

            context.endIndexIncrDelta(1);
            byte encodeAndProsType = bytes[context.getStartIndex()];
            context.startIndexIncrDelta(1);

            if ((encodeAndProsType & 0x80) != 0) {
                byte prosType = (byte) (encodeAndProsType & 0x03);
                sensorModel.setProsType(prosType);
                int propsNumbers = parseNumber(bytes, context, 2);
                if (propsNumbers <= 0) {
                    throw new DecodeException(context.getPhase() + " properties length must be more than 0");
                }
                context.endIndexIncrDelta(propsNumbers);
                byte[] propsBytes = new byte[propsNumbers];
                for (int j = 0; context.getStartIndex() < context.getEndIndex(); ) {
                    propsBytes[j++] = bytes[context.getStartIndex()];
                    context.startIndexIncrDelta(1);
                }
                if (prosType == 1) {
                    sensorModel.setProperties(TYPE_JSON.getTypeConvert().rawDataConvert(propsBytes).getConvertResult());
                } else {
                    sensorModel.setProperties(new String(propsBytes));
                }
            }

            sensorModel.setEncodeType((byte) ((encodeAndProsType & 0x78) >> 3));
            sensorModels.add(sensorModel);
        }
    }

    private Integer parseSchemaVersion(byte[] bytes, SchemaParseContext context) {
        return parseNumber(bytes, context, 4);
    }

    private int parseNumber(byte[] bytes, SchemaParseContext context, int maxLength) {
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

    private void parseControl(byte[] bytes, SchemaModel schemaModel, SchemaParseContext context) {
        context.endIndexIncrDelta(1);
        byte control = bytes[context.getStartIndex()];
        context.startIndexIncrDelta(1);
        if ((control & 0x80) != 0) {
            context.hasSensor();
            Set<SensorModel> sensors = new HashSet<>();
            schemaModel.setSensorMeasurements(sensors);
        }

        if ((control & 0x40) != 0) {
            context.hasSelfSensor();
            Set<SensorModel> sensors = new HashSet<>();
            schemaModel.setSelfMeasurements(sensors);
        }
        if ((control & 0x20) != 0) {
            context.hasOperation();
            HashSet<OperationModel> operationModels = new HashSet<>();
            schemaModel.setOperations(operationModels);
        }
    }

    private String parseGroupKey(byte[] bytes, SchemaParseContext context) {
        return parseString(bytes, context, GROUP_KEY_LEN);
    }

    private String parseSN(byte[] bytes, SchemaParseContext context) {
        context.endIndexIncrDelta(1);
        byte SNLen = bytes[context.startIndex];
        if (SNLen <= 0) {
            throw new DecodeException(context.getPhase() + " SN length must be more than 0");
        }
        context.startIndexIncrDelta(1);
        return parseString(bytes, context, SNLen);
    }

    private String parseString(byte[] bytes, SchemaParseContext context, int length) {
        context.endIndexIncrDelta(length);
        byte[] stringBytes = new byte[length];
        for (int i = 0; context.getStartIndex() < context.getEndIndex(); ) {
            stringBytes[i++] = bytes[context.getStartIndex()];
            context.startIndexIncrDelta(1);
        }
        return new String(stringBytes);
    }


    private void parseVersion(byte[] bytes, SchemaParseContext context) {
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
        PARSE_SENSOR,
        PARSE_SELF_SENSOR,
        PARSE_OPERATION,

        END;
    }

    @Getter
    private static class SchemaParseContext {

        private final int total;

        private ParsePhase phase;
        private int startIndex = 0;
        private int endIndex = 0;

        private boolean hasSensor = false;
        private boolean hasSelfSensor = false;
        private boolean hasOperation = false;

        public SchemaParseContext(int total, ParsePhase phase) {
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

        public void hasSensor() {
            this.hasSensor = true;
        }

        public void hasSelfSensor() {
            this.hasSelfSensor = true;
        }

        public void hasOperation() {
            this.hasOperation = true;
        }

    }
}
