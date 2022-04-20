package com.gow.codec.model.schema;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;

/**
 * @author gow
 * @date 2021/9/22
 */
@Data
public class SchemaModel {
    private String groupKey;
    private String sn;
    private Integer version;
    private Set<SensorModel> sensorMeasurements = new HashSet<>();
    private Set<SensorModel> selfMeasurements = new HashSet<>();
    private Set<OperationModel> operations = new HashSet<>();
}
