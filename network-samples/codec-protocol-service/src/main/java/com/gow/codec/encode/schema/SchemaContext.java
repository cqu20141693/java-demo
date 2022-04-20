package com.gow.codec.encode.schema;

import static com.gow.codec.util.CodecUtils.getVariableNumberBytes;
import com.gow.codec.exception.EncodeException;
import java.util.List;

/**
 * @author gow
 * @date 2021/9/23
 */
public class SchemaContext {
    private final byte version;
    private final byte control;
    private final byte[] groupKey;
    private final byte[] SN;
    private final byte[] schemaVersion;
    private final List<Sensor> sensors;
    private final List<Sensor> selfSensors;
    private final List<Operation> operations;
    private final Integer total;


    private SchemaContext(Builder builder) {
        this.version = builder.version;
        this.control = builder.control;
        this.groupKey = builder.groupKey;
        this.SN = builder.SN;
        this.schemaVersion = builder.schemaVersion;
        this.sensors = builder.sensors;
        this.selfSensors = builder.selfSensors;
        this.operations = builder.operations;
        this.total = builder.total;
    }

    public byte[] encode() {
        byte[] bytes = new byte[total];
        int index = 0;
        if (index < total) {
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
            for (byte b : schemaVersion) {
                bytes[index++] = b;
            }
            if (sensors != null) {
                index = composeSensor(bytes, index, sensors);
            }
            if (selfSensors != null) {
                index = composeSensor(bytes, index, selfSensors);
            }
            if (operations != null) {
                byte[] number = getVariableNumberBytes(operations.size());
                for (byte b : number) {
                    bytes[index++] = b;
                }
                for (Operation operation : operations) {
                    byte nameLength = (byte) operation.getName().length;
                    bytes[index++] = nameLength;
                    for (byte b : operation.getName()) {
                        bytes[index++] = b;
                    }
                    bytes[index++] = operation.getControlAndEncode();
                }
            }
        } else {
            throw new EncodeException("schema encode : total is invalid");
        }
        if (index != total) {
            throw new EncodeException("schema encode : index not equal total");
        }

        return bytes;
    }

    private int composeSensor(byte[] bytes, int index, List<Sensor> sensors) {
        byte[] number = getVariableNumberBytes(sensors.size());
        for (byte b : number) {
            bytes[index++] = b;
        }
        for (Sensor sensor : sensors) {
            byte nameLength = (byte) sensor.getName().length;
            bytes[index++] = nameLength;
            for (byte b : sensor.getName()) {
                bytes[index++] = b;
            }
            byte streamLength = (byte) sensor.getStream().length;
            bytes[index++] = streamLength;
            for (byte b : sensor.getStream()) {
                bytes[index++] = b;
            }
            bytes[index++] = sensor.getControlAndEncode();
            if (sensor.getProperties() != null) {
                int propsLength = sensor.getProperties().length;
                byte[] length = getVariableNumberBytes(propsLength);
                for (byte b : length) {
                    bytes[index++] = b;
                }
                for (byte property : sensor.getProperties()) {
                    bytes[index++] = property;
                }
            }
        }
        return index;
    }

    public static class Builder {
        private final byte version;
        private byte control;
        private byte[] groupKey;
        private byte[] SN;
        private byte[] schemaVersion;
        private List<Sensor> sensors;
        private List<Sensor> selfSensors;
        private List<Operation> operations;
        private Integer total;

        public Builder(byte version) {
            if (version <= 0) {
                throw new EncodeException("version not set.");
            }
            this.version = version;
            control = 0;
            total = 2;
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

        public Builder schemaVersion(byte[] schemaVersion) {

            if (schemaVersion == null || schemaVersion.length == 0 || schemaVersion.length > 4) {
                throw new EncodeException("schemaVersion error.");
            }
            this.schemaVersion = schemaVersion;
            total += schemaVersion.length;
            return this;
        }

        public Builder sensor(List<Sensor> sensors) {
            if (sensors != null && sensors.size() > 0) {

                addSensor(sensors);
                this.sensors = sensors;
                this.control |= 0x80;
            }
            return this;
        }

        private void addSensor(List<Sensor> sensors) {
            for (Sensor sensor : sensors) {
                if (sensor == null) {
                    throw new EncodeException("sensor null error.");
                }
            }

            int size = sensors.size();
            byte[] number = getVariableNumberBytes(size);
            if (number.length > 2) {
                throw new EncodeException("sensor number exceed the limit 2 bytes.");
            }
            total += number.length;
            for (Sensor sensor : sensors) {
                total++;
                total += sensor.getName().length;
                total++;
                total += sensor.getStream().length;
                total++;
                if (sensor.getProperties() != null) {
                    int length = sensor.getProperties().length;
                    total += getVariableNumberBytes(length).length;
                    total += length;
                }
            }
        }

        public Builder selfSensor(List<Sensor> sensors) {

            if (sensors != null && sensors.size() > 0) {
                addSensor(sensors);
                this.selfSensors = sensors;
                this.control |= 0x40;
            }
            return this;
        }

        public Builder operation(List<Operation> operations) {
            if (operations != null && operations.size() > 0) {

                for (Operation operation : operations) {
                    if (operation == null) {
                        throw new EncodeException("operation null error.");
                    }
                }
                int size = operations.size();
                byte[] number = getVariableNumberBytes(size);
                if (number.length > 2) {
                    throw new EncodeException("operation number exceed the limit 2 bytes.");
                }
                total += number.length;
                for (Operation operation : operations) {
                    total++;
                    total += operation.getName().length;
                    total++;
                }

                this.operations = operations;
                this.control |= 0x20;
            }
            return this;
        }

        public SchemaContext build() {
            return new SchemaContext(this);
        }
    }
}
