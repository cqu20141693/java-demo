package com.cc.minio;

import com.cc.minio.service.MinioService;
import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class MinioApp implements CommandLineRunner {

    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MinioService minioService;
    public static void main(String[] args) {
        SpringApplication.run(MinioApp.class, args);
    }

    @Override
    public void run(String... args) {
        checker();
    }

    private boolean checker() {
        try {

            String bucketName = "firmware";
            String name = "ga-ea/test1.whl";
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            //minioService.upload(bucketName,name,"D:\\temp\\test1.whl","");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
