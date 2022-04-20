package com.gow.codec.encode.schema;

import static com.gow.codec.model.EncodeTypeEnum.TYPE_JSON;
import static com.gow.codec.model.EncodeTypeEnum.TYPE_STRING;
import static com.gow.codec.util.CodecUtils.getVariableNumberBytes;
import com.gow.codec.ProtocolEncoder;
import com.gow.codec.exception.EncodeException;
import com.gow.codec.model.schema.OperationModel;
import com.gow.codec.model.schema.SchemaModel;
import com.gow.codec.model.schema.SensorModel;
import java.util.ArrayList;
import java.util.Set;

/**
 * @author gow
 * @date 2021/9/23
 */
public class SchemaEncoder implements ProtocolEncoder<SchemaModel> {


    @Override
    public byte[] encode(SchemaModel schemaModel, Byte version) {

        SchemaContext.Builder builder = new SchemaContext.Builder((byte) 1)
                .groupKey(TYPE_STRING.getTypeConvert().convertToBytes(schemaModel.getGroupKey()))
                .SN(TYPE_STRING.getTypeConvert().convertToBytes(schemaModel.getSn()))
                .schemaVersion(getVariableNumberBytes(schemaModel.getVersion()));
        Set<SensorModel> sensorMeasurements = schemaModel.getSensorMeasurements();
        if (sensorMeasurements != null && sensorMeasurements.size() > 0) {
            ArrayList<Sensor> sensors = getSensors(sensorMeasurements);
            builder.sensor(sensors);
        }

        Set<SensorModel> selfMeasurements = schemaModel.getSelfMeasurements();
        if (selfMeasurements != null && selfMeasurements.size() > 0) {
            ArrayList<Sensor> sensors = getSensors(selfMeasurements);
            builder.selfSensor(sensors);
        }

        Set<OperationModel> operationModels = schemaModel.getOperations();
        if (operationModels != null && operationModels.size() > 0) {
            ArrayList<Operation> operations = new ArrayList<>();
            for (OperationModel operationModel : operationModels) {
                byte[] name = TYPE_STRING.getTypeConvert().convertToBytes(operationModel.getName());
                operations.add(new Operation.Builder(name, operationModel.getEncodeType()).build());
            }
            builder.operation(operations);
        }

        return builder.build().encode();
    }

    private ArrayList<Sensor> getSensors(Set<SensorModel> sensorMeasurements) {
        ArrayList<Sensor> sensors = new ArrayList<>();
        for (SensorModel sensorMeasurement : sensorMeasurements) {
            byte[] name = TYPE_STRING.getTypeConvert().convertToBytes(sensorMeasurement.getName());
            byte[] stream = TYPE_STRING.getTypeConvert().convertToBytes(sensorMeasurement.getStreamName());
            Sensor.Builder builder1 = new Sensor.Builder(name, stream, sensorMeasurement.getEncodeType());
            if (sensorMeasurement.getProperties() != null) {
                byte[] properties;
                if (sensorMeasurement.getProsType() == 0) {
                    properties = TYPE_STRING.getTypeConvert().convertToBytes(sensorMeasurement.getProperties());
                } else if (sensorMeasurement.getProsType() == 1) {
                    properties = TYPE_JSON.getTypeConvert().convertToBytes(sensorMeasurement.getProperties());
                } else {
                    throw new EncodeException("properties type not support");
                }
                builder1.properties(sensorMeasurement.getProsType(), properties);
            }
            sensors.add(builder1.build());
        }
        return sensors;
    }
}
