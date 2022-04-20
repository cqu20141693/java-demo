package com.gow.pulsar.test.schema.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author gow
 * @date 2021/7/19
 */
@Data
public class RawData {
    private String topic;
    private Integer type;
    private byte[] data;
}
