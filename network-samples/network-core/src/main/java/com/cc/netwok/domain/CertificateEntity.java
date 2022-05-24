package com.cc.netwok.domain;

import com.cc.netwok.security.CertificateType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 证书模型信息
 * wcc 2022/5/20
 */
@Data
public class CertificateEntity {
    @Schema(description = "id")
    private String id;
    @Schema(description = "证书名称")
    private String name;
    @Schema(description = "证书类型")
    private CertificateType instance;
    @Schema(description = "证书配置")
    private CertificateConfig configs;
    @Schema(description = "说明")
    private String description;
    @Schema(
            description = "创建者ID(只读)"
            , accessMode = Schema.AccessMode.READ_ONLY
    )
    private String creatorId;
    @Schema(
            description = "创建时间(只读)"
            , accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long createTime;

    @Getter
    @Setter
    public static class CertificateConfig {

        @Schema(description = "证书内容(base64)")
        private String keystoreBase64;

        @Schema(description = "信任库内容(base64)")
        private String trustKeyStoreBase64;

        @Schema(description = "证书密码")
        private String keystorePwd;

        @Schema(description = "信任库密码")
        private String trustKeyStorePwd;
    }
}
