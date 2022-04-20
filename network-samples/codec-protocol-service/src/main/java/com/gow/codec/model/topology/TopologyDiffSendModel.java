package com.gow.codec.model.topology;

import java.util.List;
import lombok.Data;

/**
 * @author gow
 * @date 2021/9/23
 */
@Data
public class TopologyDiffSendModel {
    private Integer cloudVersion = 0;
    private Integer sendVersion = 0;
    private String gatewayGroupKey;
    private List<DeviceTopologyDiffModel> models;
}
