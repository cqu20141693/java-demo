package com.gow.rest;

import com.gow.common.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @author wujt  2021/5/21
 */
public interface OSSService {

    /**
     * 上传用户头像
     *
     * @param userKey
     * @param source
     * @return
     * @throws IOException
     */
    Result uploadUserAvatarBase64(String userKey, Map<String, String> source) throws IOException;

    /**
     * 上传企业营业执照
     *
     * @param userKey
     * @param file
     * @return
     */
    Result uploadLicense(String userKey, MultipartFile file);
}
