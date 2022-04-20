package com.wujt.sms.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;
import java.util.Map;

/**
 * @author wujt
 */
@Configuration
@Data
public class SMSConfig {

    @SneakyThrows
    public String getSignatureName() {
        return new String(Base64.getDecoder().decode(signatureName), "utf-8");
    }

    @Value("${sms.signatureName}")
    private String signatureName;

    @Value("${sms.aliyun.accesskey}")
    private String accesskey;

    @Value("${sms.aliyun.secretkey}")
    private String secretkey;

    @Value("#{${sms.aliyun.tplMap: {}}}")
    private Map<String, String> tplMap;

    @Bean
    public IAcsClient acsClient() {
        //初始化 acsClient
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", this.accesskey, this.secretkey);
        DefaultProfile.addEndpoint("cn-hangzhou", "Dysmsapi", "dysmsapi.aliyuncs.com");
        return new DefaultAcsClient(profile);
    }
}
