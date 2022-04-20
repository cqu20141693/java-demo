package com.gow.codec.topology;

import static com.gow.codec.model.topology.TopologyOperationType.BIND;
import static com.gow.codec.model.topology.TopologyOperationType.UNBIND;
import com.alibaba.fastjson.JSONObject;
import com.gow.codec.decode.topology.TopologyDecoder;
import com.gow.codec.decode.topology.TopologyDiffDecoder;
import com.gow.codec.encode.topology.TopologyDiffEncoder;
import com.gow.codec.encode.topology.TopologyEncoder;
import com.gow.codec.model.topology.DeviceTopologyDiffModel;
import com.gow.codec.model.topology.DeviceTopologyModel;
import com.gow.codec.model.topology.TopologyDiffSendModel;
import com.gow.codec.model.topology.TopologySendModel;
import java.util.ArrayList;
import java.util.HashSet;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author gow
 * @date 2021/9/24
 */
@Slf4j
public class TopologyTest {

    @Test
    @DisplayName("test topology encoder")
    public void testTopologyEncoder() {

        TopologyEncoder topologyEncoder = new TopologyEncoder();
        TopologySendModel topologySendModel = new TopologySendModel();
        topologySendModel.setVersion(1);
        String gatewayGroupKey = "RTzQdVDPQEmEr5g2";
        topologySendModel.setGatewayGroupKey(gatewayGroupKey);
        DeviceTopologyModel deviceTopologyModel = new DeviceTopologyModel();
        topologySendModel.setTopology(deviceTopologyModel);
        deviceTopologyModel.setGroupKey(gatewayGroupKey);
        deviceTopologyModel.setSn("cc-techDevice0001");
        HashSet<DeviceTopologyModel> deviceTopologyModels = new HashSet<>();
        deviceTopologyModel.setNodes(deviceTopologyModels);

        DeviceTopologyModel child1 = new DeviceTopologyModel();
        child1.setGroupKey(gatewayGroupKey);
        child1.setSn("child1");
        deviceTopologyModels.add(child1);

        DeviceTopologyModel child2 = new DeviceTopologyModel();
        child2.setGroupKey(gatewayGroupKey);
        child2.setSn("child2");
        deviceTopologyModels.add(child2);

        DeviceTopologyModel child3 = new DeviceTopologyModel();
        deviceTopologyModels.add(child3);
        child3.setGroupKey("child3GroupKey56");
        child3.setSn("child3");
        HashSet<DeviceTopologyModel> nodes = new HashSet<>();
        child3.setNodes(nodes);

        DeviceTopologyModel child4 = new DeviceTopologyModel();
        nodes.add(child4);
        child4.setGroupKey(gatewayGroupKey);
        child4.setSn("child4");

        byte[] encode = topologyEncoder.encode(topologySendModel, (byte) 1);
        assert encode != null : "false";
        TopologyDecoder topologyDecoder = new TopologyDecoder();
        TopologySendModel decodeModel = topologyDecoder.decode(encode);
    }

    @Test
    @DisplayName("test topology diff encoder")
    public void testTopologyDiffEncoder() {
        TopologyDiffEncoder topologyDiffEncoder = new TopologyDiffEncoder();
        String gatewayGroupKey = "RTzQdVDPQEmEr5g2";
        TopologyDiffSendModel diffSendModel = new TopologyDiffSendModel();
        diffSendModel.setCloudVersion(1);
        diffSendModel.setSendVersion(1);
        diffSendModel.setGatewayGroupKey(gatewayGroupKey);
        ArrayList<DeviceTopologyDiffModel> diffModels = new ArrayList<>();
        diffSendModel.setModels(diffModels);

        DeviceTopologyDiffModel.DeviceTag fatherTag = new DeviceTopologyDiffModel.DeviceTag();
        fatherTag.setGroupKey(gatewayGroupKey);
        fatherTag.setSn("cc-techDevice0001");
        DeviceTopologyDiffModel.DeviceTag child1Tag = new DeviceTopologyDiffModel.DeviceTag();
        child1Tag.setGroupKey(gatewayGroupKey);
        child1Tag.setSn("child1");
        DeviceTopologyDiffModel.DeviceTag child2Tag = new DeviceTopologyDiffModel.DeviceTag();
        child2Tag.setGroupKey(gatewayGroupKey);
        child2Tag.setSn("child2");
        DeviceTopologyDiffModel.DeviceTag child3Tag = new DeviceTopologyDiffModel.DeviceTag();
        child3Tag.setGroupKey("child3GroupKey56");
        child3Tag.setSn("child3");
        DeviceTopologyDiffModel.DeviceTag child4Tag = new DeviceTopologyDiffModel.DeviceTag();
        child4Tag.setGroupKey("child4GroupKey56");
        child4Tag.setSn("child4");

        DeviceTopologyDiffModel diffModel = new DeviceTopologyDiffModel();
        diffModel.setOperationType(BIND.getIndex());
        diffModel.setFatherDeviceTag(fatherTag);
        diffModel.setChildDeviceTag(child1Tag);
        diffModels.add(diffModel);


        DeviceTopologyDiffModel diffModel1 = new DeviceTopologyDiffModel();
        diffModel1.setOperationType(BIND.getIndex());
        diffModel1.setFatherDeviceTag(fatherTag);
        diffModel1.setChildDeviceTag(child2Tag);
        diffModels.add(diffModel1);

        DeviceTopologyDiffModel diffModel2 = new DeviceTopologyDiffModel();
        diffModel2.setOperationType(UNBIND.getIndex());
        diffModel2.setFatherDeviceTag(child3Tag);
        diffModel2.setChildDeviceTag(child4Tag);
        diffModels.add(diffModel2);

        byte[] encode = topologyDiffEncoder.encode(diffSendModel, (byte) 1);

        TopologyDiffDecoder topologyDiffDecoder = new TopologyDiffDecoder();
        TopologyDiffSendModel decodeModel = topologyDiffDecoder.decode(encode);
    }


    @Test
    @DisplayName("hex topology转码")
    public void testHexTopologyDecode() {
        byte[] bytes =
                {0x01, 0x0b, 0x52, 0x54, 0x7a, 0x51, 0x64, 0x56, 0x44, 0x50, 0x51, 0x45, 0x6d, 0x45, 0x72, 0x35, 0x67,
                        0x32, 0x14, 0x63, 0x68, 0x6f, 0x6e, 0x67, 0x63, 0x74, 0x65, 0x63, 0x68, 0x44, 0x65, 0x76, 0x69,
                        0x63, 0x65, 0x30, 0x30, 0x30, 0x31, (byte) 0x80, 0x02, 0x06, 0x63, 0x68, 0x69, 0x6c, 0x64, 0x32,
                        (byte) 0x80, 0x00, 0x06, 0x63, 0x68, 0x69, 0x6c, 0x64, 0x31, (byte) 0x80, 0x00};
        TopologyDecoder topologyDecoder = new TopologyDecoder();
        TopologySendModel sendModel = topologyDecoder.decode(bytes);
        System.out.println(JSONObject.toJSONString(sendModel));
    }

    @Test
    @DisplayName("hex Topology Diff转码")
    public void testHexTopologyDiffDecode() {
        byte[] bytes =
                {0x01, 0x0b, 0x0b, 0x52, 0x54, 0x7a, 0x51, 0x64, 0x56, 0x44, 0x50, 0x51, 0x45, 0x6d, 0x45, 0x72, 0x35,
                        0x67, 0x32, 0x02, (byte) 0xc1, 0x14, 0x63, 0x68, 0x6f, 0x6e, 0x67, 0x63, 0x74, 0x65, 0x63, 0x68, 0x44,
                        0x65, 0x76, 0x69, 0x63, 0x65, 0x30, 0x30, 0x30, 0x31, 0x06, 0x63, 0x68, 0x69, 0x6c, 0x64, 0x32,
                        (byte) 0xc1, 0x14, 0x63, 0x68, 0x6f, 0x6e, 0x67, 0x63, 0x74, 0x65, 0x63, 0x68, 0x44, 0x65, 0x76, 0x69,
                        0x63, 0x65, 0x30, 0x30, 0x30, 0x31, 0x06, 0x63, 0x68, 0x69, 0x6c, 0x64, 0x31};
        TopologyDiffDecoder diffDecoder = new TopologyDiffDecoder();
        TopologyDiffSendModel diffSendModel = diffDecoder.decode(bytes);
        System.out.println(JSONObject.toJSONString(diffSendModel));
    }
}
