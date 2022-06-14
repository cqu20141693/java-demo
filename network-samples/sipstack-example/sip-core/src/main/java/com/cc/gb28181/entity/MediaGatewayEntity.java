package com.cc.gb28181.entity;

import com.cc.gb28181.media.MediaGatewayProvider;
import com.cc.gb28181.media.enums.GatewayStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * GB28181国标网关配置
 * wcc 2022/6/4
 */
@Data
public class MediaGatewayEntity {

    @Schema(description = "id")
    private String id;

    @NotBlank
    @Schema(description = "名称")
    private String name;

    @Schema(description = "说明")
    private String description;

    /**
     * @see MediaGatewayProvider#getId()
     */
    @NotBlank
    @Schema(description = "网关提供商")
    private String provider;

    @NotBlank
    @Schema(description = "产品ID")
    private String productId;

    @NotBlank
    @Schema(description = "流媒体服务ID")
    private String mediaServerId;

    private Map<String, Object> configuration;

    @Schema(description = "状态", accessMode = Schema.AccessMode.READ_ONLY)
    private GatewayStatus status;

    @Schema(description = "网络组件ID")
    private String networkId;

    public static MediaGatewayEntity build(String id, String mediaServerId, String provider, String name, String productId, String description, String networkId, Map<String, Object> configuration) {
        MediaGatewayEntity entity = new MediaGatewayEntity();
        if (!StringUtils.isEmpty(id)) entity.setId(id);
        entity.setMediaServerId(mediaServerId);
        entity.setProvider(provider);
        entity.setName(name);
        entity.setProductId(productId);
        entity.setDescription(description);
        entity.setNetworkId(networkId);
        entity.setConfiguration(configuration);
        return entity;
    }
}
