package com.gow.oss.config;

import com.gow.oss.model.OSSTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wujt  2021/5/21
 */
@Configuration
@ConfigurationProperties("oss")
@Data
public class OSSModuleConfig {

    /**
     * url 路径
     *
     * @date 2021/5/21 15:48
     */
    private String endPoint;
    /**
     * id
     *
     * @date 2021/5/21 15:49
     */
    private String accessKeyId;
    /**
     * secret
     *
     * @date 2021/5/21 15:49
     */
    private String accessKeySecret;
    /**
     * bucket
     *
     * @date 2021/5/21 15:49
     */
    private String bucketName;
    /**
     * type: file,aliyun,huaweiyun
     *
     * @date 2021/5/21 15:49
     */
    private OSSTypeEnum ossType = OSSTypeEnum.file;
}
