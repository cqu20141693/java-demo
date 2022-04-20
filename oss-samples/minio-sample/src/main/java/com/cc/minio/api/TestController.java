package com.cc.minio.api;

import com.cc.minio.service.MinioService;
import com.gow.common.Result;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("minio")
public class TestController {

    @Autowired
    private MinioService minioService;

    @Delete("remove")
    public Result<String> remove(@RequestParam("bucket") String bucket, @RequestParam("object") String object) {
        minioService.delete(bucket, object);
        return Result.ok("success");
    }

    @GetMapping("getPresignedObjectUrl")
    public Result<String> getPresignedObjectUrl(@RequestParam("bucket") String bucket, @RequestParam("object") String object) {

        return Result.ok(minioService.getPresignedObjectUrl(bucket, object));
    }
}
