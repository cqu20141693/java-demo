package com.gow.codec.schema;

import com.alibaba.fastjson.JSONObject;
import com.gow.codec.decode.schema.SchemaDecoder;
import com.gow.codec.decode.schema.SchemaDiffDecoder;
import com.gow.codec.encode.schema.SchemaDiffEncoder;
import com.gow.codec.encode.schema.SchemaEncoder;
import com.gow.codec.model.EncodeTypeEnum;
import com.gow.codec.model.schema.OperationModel;
import com.gow.codec.model.schema.PropertiesType;
import com.gow.codec.model.schema.SchemaDiffModel;
import com.gow.codec.model.schema.SchemaModel;
import com.gow.codec.model.schema.SensorModel;
import java.util.HashSet;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author gow
 * @date 2021/9/23
 */
@Slf4j
public class SchemaCodecTest {

    @Test
    @DisplayName("test schema diff encoder")
    public void testSchemaDiffEncoder() {
        SchemaDiffModel schemaDiffModel = new SchemaDiffModel();
        schemaDiffModel.setCommonChanged(true);
        schemaDiffModel.setVersion(128);
        schemaDiffModel.setGroupKey("RTzQdVDPQEmEr5g2");
        schemaDiffModel.setSn("cc-techDevice0001");
        HashSet<String> sensorUpdate = new HashSet<>();
        sensorUpdate.add("update");
        schemaDiffModel.setSensorMeasurementUpdateIndex(sensorUpdate);

        HashSet<String> sensorDelete = new HashSet<>();
        sensorDelete.add("delete");
        schemaDiffModel.setSensorMeasurementDeleteIndex(sensorDelete);

        HashSet<String> selfSensorUpdate = new HashSet<>();
        selfSensorUpdate.add("selfUpdate");
        schemaDiffModel.setSelfMeasurementUpdateIndex(selfSensorUpdate);

        HashSet<String> selfSensorDelete = new HashSet<>();
        selfSensorDelete.add("selfDelete");
        schemaDiffModel.setSelfMeasurementDeleteIndex(selfSensorDelete);

        HashSet<String> operationUpdate = new HashSet<>();
        operationUpdate.add("operationUpdate");
        schemaDiffModel.setOperationUpdateIndex(operationUpdate);

        HashSet<String> operationDelete = new HashSet<>();
        operationDelete.add("operationDelete");
        schemaDiffModel.setOperationDeleteIndex(operationDelete);

        byte[] encode = new SchemaDiffEncoder().encode(schemaDiffModel, (byte) 1);

        SchemaDiffDecoder schemaDiffDecoder = new SchemaDiffDecoder();
        SchemaDiffModel decodeModel = schemaDiffDecoder.decode(encode);
    }

    @Test
    @DisplayName("test schema encoder")
    public void testSchemaEncoder() {
        SchemaModel schemaModel = new SchemaModel();
        schemaModel.setVersion(128);
        schemaModel.setGroupKey("RTzQdVDPQEmEr5g2");
        schemaModel.setSn("cc-techDevice0001");

        HashSet<SensorModel> sensorModels = new HashSet<>();
        SensorModel sensorModel = new SensorModel();
        sensorModel.setName("sensor");
        sensorModel.setStreamName("int-channel");
        sensorModel.setEncodeType(EncodeTypeEnum.TYPE_INT.getTypeCode());
        sensorModels.add(sensorModel);
        schemaModel.setSensorMeasurements(sensorModels);

        HashSet<SensorModel> selfSensorModels = new HashSet<>();
        SensorModel selfSensorModel = new SensorModel();
        selfSensorModel.setName("selfSensor");
        selfSensorModel.setStreamName("string-channel");
        selfSensorModel.setProsType(PropertiesType.SELF_DEFINE.getCode());
        selfSensorModel.setProperties("selfProperties");
        selfSensorModel.setEncodeType(EncodeTypeEnum.TYPE_STRING.getTypeCode());
        selfSensorModels.add(selfSensorModel);
        schemaModel.setSelfMeasurements(selfSensorModels);

        HashSet<OperationModel> operationModels = new HashSet<>();
        OperationModel operationModel = new OperationModel();
        operationModel.setName("operation");
        operationModel.setEncodeType((byte) 8);
        operationModels.add(operationModel);
        OperationModel operationModel1 = new OperationModel();
        operationModel1.setName("operation1");
        operationModel1.setEncodeType((byte) 1);
        operationModels.add(operationModel1);
        schemaModel.setOperations(operationModels);

        SchemaEncoder schemaEncoder = new SchemaEncoder();
        byte[] encode = schemaEncoder.encode(schemaModel, (byte) 1);

        // test decode
        SchemaDecoder schemaDecoder = new SchemaDecoder();
        SchemaModel decodeModel = schemaDecoder.decode(encode);
        System.out.println(JSONObject.toJSONString(decodeModel));
    }


    @Test
    @DisplayName("hex test")
    public void hexSchemaDecoder() {
        byte[] bytes =
                {0x01, (byte) 0x80, 0x52, 0x54, 0x7a, 0x51, 0x64, 0x56, 0x44, 0x50, 0x51, 0x45, 0x6d, 0x45, 0x72, 0x35,
                        0x67,
                        0x32, 0x14, 0x63, 0x68, 0x6f, 0x6e, 0x67, 0x63, 0x74, 0x65, 0x63, 0x68, 0x44, 0x65, 0x76, 0x69,
                        0x63, 0x65, 0x30, 0x30, 0x30, 0x31, 0x02, 0x01, 0x0d, 0x73, 0x74, 0x72, 0x69, 0x6e, 0x67, 0x2d,
                        0x73, 0x65, 0x6e, 0x73, 0x6f, 0x72, 0x0e, 0x73, 0x74, 0x72, 0x69, 0x6e, 0x67, 0x2d, 0x63, 0x68,
                        0x61, 0x6e, 0x6e, 0x65, 0x6c, 0x28};
        SchemaDecoder schemaDecoder = new SchemaDecoder();
        SchemaModel decodeModel = schemaDecoder.decode(bytes);
    }

    @Test
    @DisplayName("hex diff test")
    public void hexSchemaDiffDecoder() {
        byte[] bytes =
                {0x01, (byte) 0xc0, 0x52, 0x54, 0x7a, 0x51, 0x64, 0x56, 0x44, 0x50, 0x51, 0x45, 0x6d, 0x45, 0x72, 0x35,
                        0x67,
                        0x32, 0x14, 0x63, 0x68, 0x6f, 0x6e, 0x67, 0x63, 0x74, 0x65, 0x63, 0x68, 0x44, 0x65, 0x76, 0x69,
                        0x63, 0x65, 0x30, 0x30, 0x30, 0x31, 0x0b, 0x02, 0x06, 0x73, 0x65, 0x6e, 0x73, 0x6f, 0x72, 0x0b,
                        0x73, 0x65, 0x6c, 0x66, 0x2d, 0x73, 0x65, 0x6e, 0x73, 0x6f, 0x72};
        SchemaDiffDecoder schemaDiffDecoder = new SchemaDiffDecoder();
        SchemaDiffModel diffModel = schemaDiffDecoder.decode(bytes);
    }
}
