package com.gow.codec.encode.topology;

import static com.gow.codec.model.EncodeTypeEnum.TYPE_STRING;
import static com.gow.codec.util.CodecUtils.getVariableNumberBytes;
import com.gow.codec.ProtocolEncoder;
import com.gow.codec.model.topology.DeviceTopologyModel;
import com.gow.codec.model.topology.TopologySendModel;
import java.util.ArrayList;
import java.util.Set;

/**
 * @author gow
 * @date 2021/9/23
 */
public class TopologyEncoder implements ProtocolEncoder<TopologySendModel> {
    @Override
    public byte[] encode(TopologySendModel topologySendModel, Byte version) {
        String gatewayGroupKey = topologySendModel.getGatewayGroupKey();
        TopologyContext.Builder builder = new TopologyContext.Builder(version)
                .gatewayGroupKey(TYPE_STRING.getTypeConvert().convertToBytes(gatewayGroupKey))
                .topologyVersion(getVariableNumberBytes(topologySendModel.getVersion()));

        if (topologySendModel.getVersion() > 0) {
            DeviceTopologyModel topology = topologySendModel.getTopology();
            builder.topologyNode(buildTopologyTree(gatewayGroupKey, topology));
        }
        return builder.build().encode();
    }

    /**
     * 先遍历左子树，遍历完成后再遍历兄弟节点
     *
     * @param gatewayGroupKey 网关key
     * @param topology        tree
     * @return TopologyNode
     */
    private TopologyNode buildTopologyTree(String gatewayGroupKey, DeviceTopologyModel topology) {
        TopologyNode.Builder root =
                new TopologyNode.Builder(TYPE_STRING.getTypeConvert().convertToBytes(topology.getSn()));
        if (!topology.getGroupKey().equals(gatewayGroupKey)) {
            root.groupKey(TYPE_STRING.getTypeConvert().convertToBytes(topology.getGroupKey()));
        }
        Set<DeviceTopologyModel> nodes = topology.getNodes();
        if (nodes != null && nodes.size() > 0) {
            ArrayList<TopologyNode> topologyNodes = new ArrayList<>();
            for (DeviceTopologyModel node : nodes) {
                topologyNodes.add(buildTopologyTree(gatewayGroupKey, node));
            }
            root.children(topologyNodes);
        }
        return root.build();
    }
}
