package com.wujt.compress;


import com.alibaba.fastjson.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 解压缩工具示例
 * wcc 2022/5/20
 */
public class CompressDemo {

    public static void main(String[] args) throws IOException {
        String path = "D:\\work\\gee-ga\\edge-plugin\\gateway.zip";
        String opc = "D:\\work\\gee-ga\\edge-plugin\\iiot_mapper_opc-1.0.0-py3-none-any.whl";
        String gateway = "D:\\work\\gee-ga\\edge-plugin\\thingsboard_gateway-2.0.9-py3-none-any.whl";
        String mysql = "D:\\work\\gee-ga\\edge-plugin\\iiot_driver_mysql-1.0.0-py3-none-any.whl";
        zipRead(path);

        zipRead(opc);
        zipRead(gateway);
        zipRead(mysql);
    }

    private static void zipRead(String path) throws IOException {
        try (ZipInputStream zip = new ZipInputStream(new FileInputStream(path))) {
            ZipEntry entry = null;
            while ((entry = zip.getNextEntry()) != null) {
                String name = entry.getName();
                if (!entry.isDirectory() && name.contains("package_metadata.json")) {
                    //read11(zip);
                    int size = (int) entry.getSize();
                    byte[] bytes = new byte[size];
                    zip.read(bytes);
                    JSONObject map = JSONObject.parseObject(bytes, JSONObject.class);
                    System.out.println(map);
                }
            }
        }
    }

    private static void read11(ZipInputStream zip) throws IOException {
        byte[] allBytes = zip.readAllBytes();
        HashMap<String, Object> map = JSONObject.parseObject(allBytes, HashMap.class);
        System.out.println(map);
    }
}
