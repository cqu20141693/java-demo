package com.wujt.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wujt
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaLocalInfo {
    private int numPartitions;
    // 实例个数大于partition 部分无效
    private int numInstances;
    private int numRecords;
    private short replicationFactor;
}
