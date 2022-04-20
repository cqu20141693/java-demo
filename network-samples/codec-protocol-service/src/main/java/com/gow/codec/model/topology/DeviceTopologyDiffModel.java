package com.gow.codec.model.topology;

import lombok.Data;

/**
 * @author gow
 * @date 2021/9/23
 */
@Data
public class DeviceTopologyDiffModel {

    private DeviceTag fatherDeviceTag;
    private DeviceTag childDeviceTag;
    /**
     * {@link TopologyOperationType}
     */
    private Byte operationType;

    @Data
    public static class DeviceTag {
        private String groupKey;
        private String sn;
    }
}
