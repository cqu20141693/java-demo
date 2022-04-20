package com.gow.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.gow.oss.config.OSSModuleConfig;
import com.obs.services.ObsClient;
import com.obs.services.model.ObsObject;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author wujt  2021/5/21
 */
@Service
@RefreshScope
public class OSSFacadeImpl implements OSSFacade {

    @Autowired
    private OSSModuleConfig config;


    @Override
    public void putObject(String filePath, InputStream inStream) throws IOException {
        switch (config.getOssType()) {
            case aliyun:
                OSS oss = new OSSClientBuilder().build(config.getEndPoint(), config.getAccessKeyId(), config.getAccessKeySecret());
                oss.putObject(config.getBucketName(), filePath, inStream);
                oss.shutdown();
                inStream.close();
                break;
            case huaweiyun:
                ObsClient obsClient = new ObsClient(config.getAccessKeyId(), config.getAccessKeySecret(), config.getEndPoint());
                obsClient.putObject(config.getBucketName(), filePath, inStream);
                obsClient.close();
                inStream.close();
                break;
            case file:
            default:

        }
    }

    @Override
    public byte[] getObject(String objectName) throws IOException {

        switch (config.getOssType()) {
            case aliyun:
                OSS oss = new OSSClientBuilder().build(config.getEndPoint(), config.getAccessKeyId(), config.getAccessKeySecret());
                OSSObject ossObject = oss.getObject(config.getBucketName(), objectName);
                byte[] bytes = IOUtils.toByteArray(ossObject.getObjectContent());
                oss.shutdown();
                return bytes;
            case huaweiyun:
                ObsClient obsClient = new ObsClient(config.getAccessKeyId(), config.getAccessKeySecret(), config.getEndPoint());
                ObsObject obsObject = obsClient.getObject(config.getBucketName(), objectName);
                InputStream content = obsObject.getObjectContent();
                byte[] buffer = IOUtils.toByteArray(content);
                obsClient.close();
                return buffer;
            case file:
            default:

        }
        return null;
    }
}
