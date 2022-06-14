package com.wujt.crypto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 加解密和签名
 * wcc 2022/5/25
 */
@Data
public class CryptoDemo {

    @SneakyThrows
    public static void main(String[] args) {

        String path = "D:\\work\\gee-ga\\edge-plugin\\iiot_mapper_request-1.0.6-py3-none-any.whl";
        String pat1h = "D:\\work\\gee-ga\\edge-plugin\\iiot_driver_mysql-1.0.0-py3-none-any.whl";
        test(pat1h);
    }

    private static void test(String path) throws IOException {
        FileInputStream inputStream = new FileInputStream(path);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        String md5Hex = DigestUtils.md5Hex(bytes);
        System.out.println(md5Hex);
        JSONObject jsonObject = readMetaData(byteArrayInputStream);
        System.out.println(jsonObject);
    }

    public static JSONObject readMetaData(InputStream inputStream) {
        JSONObject meta = new JSONObject();
        try (
                InputStream in = new BufferedInputStream(inputStream);
                ZipInputStream zip = new ZipInputStream(in)) {

            ZipEntry entry;
            while ((entry = zip.getNextEntry()) != null) {
                String fileName = entry.getName();
                if (!entry.isDirectory() && fileName.contains("package_metadata.json")) {
                    int size = (int) entry.getSize();
                    byte[] bytes = new byte[size];
                    zip.read(bytes);
                    meta = JSONObject.parseObject(bytes, JSONObject.class);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return meta;
    }
}
