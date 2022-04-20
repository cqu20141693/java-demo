package com.gow.codec.encode.schema;

import com.gow.codec.exception.EncodeException;
import java.util.List;

/**
 * @author gow
 * @date 2021/9/22
 */
public class SchemaDiffContext {

    private final byte version;

    private final byte control;

    private final byte[] groupKey;

    private final byte[] SN;

    private final byte[] cloudVersion;

    private final SensorIndex sensorIndexUpdate;
    private final SensorIndex sensorIndexDelete;

    private final SensorIndex selfSensorIndexUpdate;
    private final SensorIndex selfSensorIndexDelete;

    private final OperationIndex operationIndexUpdate;
    private final OperationIndex operationIndexDelete;

    private final int total;

    private SchemaDiffContext(Builder builder) {
        this.version = builder.version;
        this.control = builder.control;
        this.groupKey = builder.groupKey;
        this.SN = builder.SN;
        this.cloudVersion = builder.cloudVersion;
        this.sensorIndexUpdate = builder.sensorIndexUpdate;
        this.sensorIndexDelete = builder.sensorIndexDelete;
        this.selfSensorIndexUpdate = builder.selfSensorIndexUpdate;
        this.selfSensorIndexDelete = builder.selfSensorIndexDelete;
        this.operationIndexUpdate = builder.operationIndexUpdate;
        this.operationIndexDelete = builder.operationIndexDelete;
        this.total = builder.total;
    }


    public byte[] encode() {
        byte[] bytes = new byte[total];
        Integer index = 0;
        if (total > index) {
            bytes[index++] = version;
            bytes[index++] = control;
            for (byte b : groupKey) {
                bytes[index++] = b;
            }
            byte snLength = (byte) SN.length;
            bytes[index++] = snLength;
            for (byte b : SN) {
                bytes[index++] = b;
            }

            for (byte b : cloudVersion) {
                bytes[index++] = b;
            }
            index = addSensor(bytes, index, sensorIndexUpdate);
            index = addSensor(bytes, index, sensorIndexDelete);
            index = addSensor(bytes, index, selfSensorIndexUpdate);
            index = addSensor(bytes, index, selfSensorIndexDelete);
            index = addOperation(bytes, index, operationIndexUpdate);
            index = addOperation(bytes, index, operationIndexDelete);

        } else {
            throw new EncodeException("index exceed total.");
        }
        if (total != index) {
            throw new EncodeException("index not equal total.");
        }
        return bytes;
    }

    private Integer addOperation(byte[] bytes, Integer index, OperationIndex operationIndex) {
        if (operationIndex != null) {
            for (byte b : operationIndex.getNumber()) {
                bytes[index++] = b;
            }
            index = addIndexLoop(bytes, index, operationIndex.getIndexLoops());
        }
        return index;
    }

    private Integer addSensor(byte[] bytes, Integer index, SensorIndex sensorIndex) {
        if (sensorIndex != null) {
            for (byte b : sensorIndex.getNumber()) {
                bytes[index++] = b;
            }
            index = addIndexLoop(bytes, index, sensorIndex.getIndexLoops());
        }
        return index;
    }

    private Integer addIndexLoop(byte[] bytes, Integer index, List<IndexLoop> indexLoops) {

        for (IndexLoop indexLoop : indexLoops) {
            byte nameLength = (byte) indexLoop.getName().length;
            bytes[index++] = nameLength;
            for (byte b : indexLoop.getName()) {
                bytes[index++] = b;
            }
        }
        return index;
    }

    public static class Builder {
        private final byte version;

        private byte control;

        private byte[] groupKey;

        private byte[] SN;

        private byte[] cloudVersion;

        private SensorIndex sensorIndexUpdate;
        private SensorIndex sensorIndexDelete;

        private SensorIndex selfSensorIndexUpdate;
        private SensorIndex selfSensorIndexDelete;

        private OperationIndex operationIndexUpdate;
        private OperationIndex operationIndexDelete;
        private int total;

        public Builder(byte version) {
            if (version <= 0) {
                throw new EncodeException("version not set.");
            }
            this.version = version;
            control = 0;
            total = 2;
        }

        public Builder commonChanged() {
            control |= 0x80;
            return this;
        }

        public Builder groupKey(byte[] groupKey) {
            if (groupKey == null || groupKey.length != 16) {
                throw new EncodeException("groupKey error.");
            }
            this.groupKey = groupKey;
            total += groupKey.length;
            return this;
        }

        public Builder SN(byte[] SN) {
            if (SN == null || SN.length == 0 || SN.length > 127) {
                throw new EncodeException("SN error.");
            }
            this.SN = SN;
            total++;
            total += SN.length;
            return this;
        }

        public Builder cloudVersion(byte[] cloudVersion) {

            if (cloudVersion == null || cloudVersion.length == 0 || cloudVersion.length > 4) {
                throw new EncodeException("cloudVersion error.");
            }
            this.cloudVersion = cloudVersion;
            total += cloudVersion.length;
            return this;
        }

        public Builder sensorUpdate(SensorIndex sensorIndexUpdate) {
            checkSensor(sensorIndexUpdate);
            this.sensorIndexUpdate = sensorIndexUpdate;
            total += sensorIndexUpdate.getNumber().length;
            sensorIndexUpdate.getIndexLoops().forEach(indexLoop -> {
                total++;
                total += indexLoop.getName().length;
            });
            control |= 0x40;
            return this;
        }

        public Builder sensorDelete(SensorIndex sensorIndexDelete) {
            checkSensor(sensorIndexDelete);
            this.sensorIndexDelete = sensorIndexDelete;
            total += sensorIndexDelete.getNumber().length;
            sensorIndexDelete.getIndexLoops().forEach(indexLoop -> {
                total++;
                total += indexLoop.getName().length;
            });
            control |= 0x20;
            return this;
        }

        public Builder selfSensorUpdate(SensorIndex selfSensorIndexUpdate) {
            checkSensor(selfSensorIndexUpdate);
            this.selfSensorIndexUpdate = selfSensorIndexUpdate;
            total += selfSensorIndexUpdate.getNumber().length;
            selfSensorIndexUpdate.getIndexLoops().forEach(indexLoop -> {
                total++;
                total += indexLoop.getName().length;
            });
            control |= 0x10;
            return this;
        }

        public Builder selfSensorDelete(SensorIndex selfSensorIndexDelete) {
            checkSensor(selfSensorIndexDelete);
            this.selfSensorIndexDelete = selfSensorIndexDelete;
            total += selfSensorIndexDelete.getNumber().length;
            selfSensorIndexDelete.getIndexLoops().forEach(indexLoop -> {
                total++;
                total += indexLoop.getName().length;
            });
            control |= 0x08;
            return this;
        }

        public Builder operationUpdate(OperationIndex operationIndexUpdate) {
            checkOperation(operationIndexUpdate);
            this.operationIndexUpdate = operationIndexUpdate;
            total += operationIndexUpdate.getNumber().length;
            operationIndexUpdate.getIndexLoops().forEach(indexLoop -> {
                total++;
                total += indexLoop.getName().length;
            });
            control |= 0x04;
            return this;
        }

        public Builder operationDelete(OperationIndex operationIndexDelete) {
            checkOperation(operationIndexDelete);
            this.operationIndexDelete = operationIndexDelete;
            total += operationIndexDelete.getNumber().length;
            operationIndexDelete.getIndexLoops().forEach(indexLoop -> {
                total++;
                total += indexLoop.getName().length;
            });
            control |= 0x02;
            return this;
        }

        private void checkOperation(OperationIndex operationIndex) {
            if (operationIndex != null) {
                byte[] number = operationIndex.getNumber();
                if (number == null || number.length == 0 || number.length > 2) {
                    throw new EncodeException("operation number error.");
                }
                checkIndex(operationIndex.getIndexLoops());
            }
        }

        private void checkSensor(SensorIndex sensors) {
            if (sensors != null) {
                byte[] number = sensors.getNumber();
                if (number == null || number.length == 0 || number.length > 2) {
                    throw new EncodeException("sensor number error.");
                }
                checkIndex(sensors.getIndexLoops());
            }
        }

        private void checkIndex(List<IndexLoop> indexLoops) {
            if (indexLoops == null || indexLoops.size() == 0) {
                throw new EncodeException("sensor or operation number error.");
            }

            indexLoops.forEach(indexLoop -> {
                byte[] name = indexLoop.getName();
                if (name == null || name.length == 0 || name.length > 127) {
                    throw new EncodeException("sensor or operation index error.");
                }
            });
        }

        public SchemaDiffContext build() {
            return new SchemaDiffContext(this);
        }
    }

}
