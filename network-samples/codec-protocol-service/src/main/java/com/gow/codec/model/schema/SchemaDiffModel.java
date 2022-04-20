package com.gow.codec.model.schema;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;

/**
 * @author gow
 * @date 2021/9/22
 */
@Data
public class SchemaDiffModel {

    private boolean commonChanged = false;

    private String groupKey;

    private Integer version;
    private String sn;
    private Set<String> sensorMeasurementUpdateIndex = new HashSet<>();
    private Set<String> sensorMeasurementDeleteIndex = new HashSet<>();
    private Set<String> selfMeasurementUpdateIndex = new HashSet<>();
    private Set<String> selfMeasurementDeleteIndex = new HashSet<>();
    private Set<String> operationUpdateIndex = new HashSet<>();
    private Set<String> operationDeleteIndex = new HashSet<>();
}
