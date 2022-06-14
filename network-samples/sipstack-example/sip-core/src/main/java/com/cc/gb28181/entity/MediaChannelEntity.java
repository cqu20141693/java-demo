package com.cc.gb28181.entity;

import com.alibaba.fastjson.JSONObject;
import com.cc.gb28181.GB28181Constants;
import com.cc.gb28181.GB28181DeviceChannel;
import com.cc.gb28181.gateway.GB28181MediaGatewayProvider;
import com.cc.gb28181.media.DeviceChannel;
import com.cc.gb28181.media.enums.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 流媒体通道信息
 * wcc 2022/5/24
 */
@Getter
@Setter
public class MediaChannelEntity {
    @Schema(description = "id")
    private String id;

    @Schema(description = "设备ID")
    private String deviceId;

    @Schema(description = "设备名称")
    private String deviceName;

    @Schema(description = "通道ID")
    private String channelId;

    @Schema(description = "通道名称")
    private String name;

    @Schema(description = "厂商")
    private String manufacturer;

    @Schema(description = "型号")
    private String model;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "通道配置", hidden = true)
    private ChannelFeature[] features;


    @Schema(description = "接入方式")
    private String provider;

    @Schema(description = "通道状态")
    private ChannelStatus status;

    @Schema(description = "其他信息")
    private Map<String, Object> others;

    @Schema(description = "说明")
    private String description;

    @Schema(description = "父级通道ID")
    private String parentChannelId;

    @Schema(description = "子设备数量")
    private Integer subCount;

    @Schema(description = "行政区划代码")
    private String civilCode;

    @Schema(description = "云台类型")
    private PTZType ptzType;

    @Schema(description = "目录类型")
    private CatalogTypeGroup catalogType;

    @Schema(description = "目录代码")
    private String catalogCode;

    @Schema(description = "经度")
    private Double longitude;

    @Schema(description = "纬度")
    private Double latitude;

    public void generateId() {
        setId(DigestUtils.md5Hex(String.format("%s-%s", deviceId, channelId)));
    }

    public void with(GB28181DeviceChannel channel) {
        setParentChannelId(channel.getParentId());
        generateId();
        setProvider(GB28181MediaGatewayProvider.ID);
        CatalogType type = CatalogType.matchGB28181DeviceId(channel.getChannelId());

        setCatalogType(type.getGroup());
        setCatalogCode(CatalogType.getCatalogTypeCode(channel.getChannelId()));
        if (channel.getInfo() != null) {
            setPtzType(channel.getInfo().getPtzType());
        } else {
            setPtzType(PTZType.unknown);
        }
        //删除通知
        if ("DEL".equals(channel.getEvent())) {
            setStatus(ChannelStatus.delete);
        }
        if (channel.getStatus() == null) {
            setStatus(ChannelStatus.of(channel.getEvent()));
        }
    }

    public GB28181DeviceChannel toGB28181DeviceChannel() {
        GB28181DeviceChannel channel = new GB28181DeviceChannel();
        // copy(this, channel1);
        if (others != null) {
            //copy(others, channel);
        }
        channel.setDeviceId(deviceId);
        //尝试从其他配置里获取国标通道ID,否则使用channelId.
        channel.setChannelId(getGB28181ChannelId());
        channel.setStatus(status);
        channel.setName(name);
        channel.setManufacturer(manufacturer);
        channel.setModel(model);
        channel.setParentId(parentChannelId);
        if (ptzType != null) {
            GB28181DeviceChannel.Info info = new GB28181DeviceChannel.Info();
            info.setPtzType(ptzType);
            channel.setInfo(info);
        }
        return channel;
    }

    public boolean isGB28181ProxyStream() {
        return !GB28181MediaGatewayProvider.ID.equals(getProvider())
                || getOther("gb28181ProxyStream").map(r -> (Boolean) r).orElse(false);
    }

    public String getGB28181ChannelId() {
        return getOther(GB28181Constants.gb28181ChannelIdKey).map(String::valueOf).orElse(channelId);
    }

    @SneakyThrows
    public <T> T othersToType(Class<T> type) {
        if (others == null) {
            return type.newInstance();
        }
        JSONObject copy = new JSONObject(others);
        copy.putIfAbsent("id", getId());
        return JSONObject.toJavaObject(copy, type);
    }

    public Optional<Object> getOther(String key) {
        return Optional.ofNullable(others)
                .map(o -> o.get(key));
    }

    public void withOther(String key, Object value) {
        if (others == null) {
            others = new HashMap<>();
        }
        others.put(key, value);
    }

    /**
     * 填充others信息
     *
     * @param others         others
     * @param ignoreIfAbsent 是否忽略已经存在的key
     */
    public void withOther(Map<String, Object> others, boolean ignoreIfAbsent) {
        if (others == null) {
            return;
        }
        if (this.others == null) {
            this.others = new HashMap<>();
        }
        if (ignoreIfAbsent) {
            others.forEach(this.others::putIfAbsent);
        } else {
            this.others.putAll(others);
        }
    }

    public DeviceChannel toDeviceChannel() {
        DeviceChannel channel = new DeviceChannel();
        channel.setName(this.name);
        channel.setOptions(this.others);
        channel.setDeviceId(this.deviceId);
        channel.setId(channelId);
        return channel;
    }
}
