package com.gow.oss;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author wujt  2021/5/21
 */
public interface OSSFacade {
    /**
     * oss 存储文件
     *
     * @param filePath
     * @param inStream
     * @return PutObjectResult
     */
    void putObject(String filePath, InputStream inStream) throws IOException;


    /**
     * 获取对象string
     *
     * @param objectName
     * @return byte[]
     */
    byte[] getObject(String objectName) throws IOException;
}
