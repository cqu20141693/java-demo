package com.gow.codec.model.topology;

import java.util.Set;
import lombok.Data;

/**
 * @author gow
 * @date 2021/9/23
 */
@Data
public class DeviceTopologyModel {

    private String groupKey;
    private String sn;
    private Set<DeviceTopologyModel> nodes;
}
