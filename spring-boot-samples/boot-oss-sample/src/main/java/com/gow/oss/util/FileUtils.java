package com.gow.oss.util;

import org.springframework.http.MediaType;

import java.util.Optional;

/**
 * @author wujt  2021/5/21
 */
public class FileUtils {
    /**
     * 请求类型
     *
     * @param contentType
     * @return
     */
    public static String getImageType(String contentType) {
        return Optional.ofNullable(contentType).map(type -> {
            switch (type) {
                case MediaType.IMAGE_JPEG_VALUE:
                    return "jpg";
                case MediaType.IMAGE_GIF_VALUE:
                    return "gif";
                case MediaType.IMAGE_PNG_VALUE:
                    return "png";
            }
            return null;
        }).orElse(null);
    }

    /**
     * 文件路径转网络地址路径
     *
     * @param path
     * @return
     */
    public static String filePathConvertUri(String path) {
        return path.replaceAll("\\\\", "/");
    }
}
