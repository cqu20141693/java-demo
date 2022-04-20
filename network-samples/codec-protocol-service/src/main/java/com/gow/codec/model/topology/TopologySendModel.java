package com.gow.codec.model.topology;

import lombok.Data;

/**
 * @author gow
 * @date 2021/9/23
 */
@Data
public class TopologySendModel {

    private Integer version;
    private String gatewayGroupKey;
    private DeviceTopologyModel topology;
}
