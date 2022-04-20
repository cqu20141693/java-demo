package com.cc.minio.service;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    public String upload(String bucketName, String object, String fileName, String contentType) {
        try {
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucketName)
                            .object(object)
                            .filename(fileName)
//                            .contentType(contentType)
                            .build());
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    public void delete(String bucket, String object) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(object).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPresignedObjectUrl(String bucket, String object) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucket)
                    .object(object)
                    .expiry(600, TimeUnit.SECONDS)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
