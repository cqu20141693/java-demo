package com.gow.codec.encode.schema;

import static com.gow.codec.model.EncodeTypeEnum.TYPE_STRING;
import static com.gow.codec.util.CodecUtils.getVariableNumberBytes;
import com.gow.codec.ProtocolEncoder;
import com.gow.codec.model.schema.SchemaDiffModel;
import com.gow.codec.model.schema.SchemaModel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Set;

/**
 * @author gow
 * @date 2021/9/22
 */
public class SchemaDiffEncoder implements ProtocolEncoder<SchemaDiffModel> {


    public byte[] encode(SchemaDiffModel diffModel, Byte version) {
        SchemaDiffContext.Builder builder = new SchemaDiffContext.Builder(version)
                .groupKey(TYPE_STRING.getTypeConvert().convertToBytes(diffModel.getGroupKey()))
                .SN(TYPE_STRING.getTypeConvert().convertToBytes(diffModel.getSn()))
                .cloudVersion(getVariableNumberBytes(diffModel.getVersion()));
        if (diffModel.isCommonChanged()) {
            builder.commonChanged();
        }

        Set<String> updateSensorIndex = diffModel.getSensorMeasurementUpdateIndex();
        if (updateSensorIndex != null && updateSensorIndex.size() > 0) {
            builder.sensorUpdate(getSensor(updateSensorIndex));
        }
        Set<String> deleteSensorIndex = diffModel.getSensorMeasurementDeleteIndex();
        if (deleteSensorIndex != null && deleteSensorIndex.size() > 0) {
            builder.sensorDelete(getSensor(deleteSensorIndex));
        }
        Set<String> selfMeasurementUpdateIndex = diffModel.getSelfMeasurementUpdateIndex();
        if (selfMeasurementUpdateIndex != null && selfMeasurementUpdateIndex.size() > 0) {
            builder.selfSensorUpdate(getSensor(selfMeasurementUpdateIndex));
        }
        Set<String> selfMeasurementDeleteIndex = diffModel.getSelfMeasurementDeleteIndex();
        if (selfMeasurementDeleteIndex != null && selfMeasurementDeleteIndex.size() > 0) {
            builder.selfSensorDelete(getSensor(selfMeasurementDeleteIndex));
        }

        Set<String> operationUpdateIndex = diffModel.getOperationUpdateIndex();
        if (operationUpdateIndex != null && operationUpdateIndex.size() > 0) {
            builder.operationUpdate(getOperation(operationUpdateIndex));
        }
        Set<String> operationDeleteIndex = diffModel.getOperationDeleteIndex();
        if (operationDeleteIndex != null && operationDeleteIndex.size() > 0) {
            builder.operationDelete(getOperation(operationDeleteIndex));
        }
        return builder.build().encode();
    }

    private OperationIndex getOperation(Set<String> operations) {
        OperationIndex operationIndex = new OperationIndex();
        operationIndex.setNumber(getVariableNumberBytes(operations.size()));
        ArrayList<IndexLoop> indexLoops = new ArrayList<>();
        operationIndex.setIndexLoops(indexLoops);
        for (String sensorIndex : operations) {
            IndexLoop indexLoop = new IndexLoop();
            indexLoop.setName(sensorIndex.getBytes(StandardCharsets.UTF_8));
            indexLoops.add(indexLoop);
        }
        return operationIndex;
    }

    private SensorIndex getSensor(Set<String> sensors) {
        SensorIndex sensor = new SensorIndex();
        ArrayList<IndexLoop> indexLoops = new ArrayList<>();
        sensor.setIndexLoops(indexLoops);
        sensor.setNumber(getVariableNumberBytes(sensors.size()));
        for (String sensorIndex : sensors) {
            IndexLoop indexLoop = new IndexLoop();
            indexLoop.setName(sensorIndex.getBytes(StandardCharsets.UTF_8));
            indexLoops.add(indexLoop);
        }

        return sensor;
    }
}
