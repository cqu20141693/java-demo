package com.cc.netwok.security;

import com.cc.netwok.domain.CertificateEntity;
import com.cc.netwok.security.Certificate;
import org.springframework.web.multipart.MultipartRequest;

/**
 * CA 证书管理器，用于统一管理证书
 * wcc 2022/5/20
 */
public interface CertificateManager {
    /**
     * 根据ID获取证书信息,如果证书不存在则返回
     *
     * @param id ID
     * @return 证书信息
     */
    Certificate getCertificate(String id);

    /**
     * 需要先上传证书 得到Base64
     *
     * @param info
     * @return
     */
    Integer saveCertificate(CertificateEntity info);

    /**
     * 上传证书并返回证书BASE64
     *
     * @param multipartRequest
     * @return
     */
    String upload(MultipartRequest multipartRequest);
}
