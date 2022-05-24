package com.cc.netwok.security;

import com.cc.netwok.domain.CertificateEntity;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 证书服务
 * wcc 2022/5/20
 */
public class CertificateService implements CertificateManager {

    Map<String, CertificateEntity> cache = new HashMap<>();

    @Override
    public Certificate getCertificate(String id) {
        return Optional.ofNullable(cache.get(id)).map(entity -> {
            DefaultCertificate defaultCertificate = new DefaultCertificate(entity.getId(), entity.getName());
            return entity.getInstance().init(defaultCertificate, entity.getConfigs());
        }).orElse(null);
    }

    @Override
    public Integer saveCertificate(CertificateEntity info) {
        cache.put(info.getId(), info);
        return 1;
    }

    @SneakyThrows
    @Override
    public String upload(MultipartRequest multipart) {
        MultipartFile file = multipart.getFile("file");
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("error.not_file");
        } else {
            return Base64.encodeBase64String(StreamUtils.copyToByteArray(file.getInputStream()));
        }
    }
}
