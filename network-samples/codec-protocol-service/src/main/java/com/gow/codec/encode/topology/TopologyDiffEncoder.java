package com.gow.codec.encode.topology;

import static com.gow.codec.model.EncodeTypeEnum.TYPE_STRING;
import static com.gow.codec.util.CodecUtils.getVariableNumberBytes;
import com.gow.codec.ProtocolEncoder;
import com.gow.codec.exception.EncodeException;
import com.gow.codec.model.topology.DeviceTopologyDiffModel;
import com.gow.codec.model.topology.TopologyDiffSendModel;
import com.gow.codec.model.topology.TopologyOperationType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gow
 * @date 2021/9/23
 */
public class TopologyDiffEncoder implements ProtocolEncoder<TopologyDiffSendModel> {
    @Override
    public byte[] encode(TopologyDiffSendModel topologyDiffSendModel, Byte version) {
        String gatewayGroupKey = topologyDiffSendModel.getGatewayGroupKey();
        TopologyDiffContext.Builder builder = new TopologyDiffContext.Builder(version)
                .cloudAndSendVersion(getVariableNumberBytes(topologyDiffSendModel.getCloudVersion()),
                        getVariableNumberBytes(topologyDiffSendModel.getSendVersion()))
                .gatewayGroupKey(TYPE_STRING.getTypeConvert().convertToBytes(gatewayGroupKey));
        List<DeviceTopologyDiffModel> models = topologyDiffSendModel.getModels();
        if (models != null && models.size() > 0) {
            ArrayList<TopologyOpRecord> topologyOpRecords = new ArrayList<>();
            for (DeviceTopologyDiffModel model : models) {
                if (model == null) {
                    throw new EncodeException("topology diff model null error");
                }
                TopologyOpRecord.Builder builder1 = new TopologyOpRecord.Builder(
                        TYPE_STRING.getTypeConvert().convertToBytes(model.getFatherDeviceTag().getSn()),
                        TYPE_STRING.getTypeConvert().convertToBytes(model.getChildDeviceTag().getSn()));
                if (!gatewayGroupKey.equals(model.getFatherDeviceTag().getGroupKey())) {
                    builder1.fatherGroupKey(
                            TYPE_STRING.getTypeConvert().convertToBytes(model.getFatherDeviceTag().getGroupKey()));
                }
                if (!gatewayGroupKey.equals(model.getChildDeviceTag().getGroupKey())) {
                    builder1.childGroupKey(
                            TYPE_STRING.getTypeConvert().convertToBytes(model.getChildDeviceTag().getGroupKey()));
                }
                TopologyOperationType type =
                        TopologyOperationType.parseFromIndex(model.getOperationType());
                if (type == null) {
                    throw new EncodeException("topology diff model operation type error");
                }
                builder1.bind(type);
                topologyOpRecords.add(builder1.build());
            }
            builder.topologyOpRecords(topologyOpRecords);
        }
        return builder.build().encode();
    }
}
