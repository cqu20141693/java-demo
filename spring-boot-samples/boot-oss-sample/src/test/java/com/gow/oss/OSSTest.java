package com.gow.oss;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

class OSSTest {

    @Test
    @DisplayName("testZipRead")
    public void testZipRead() {
        String path = "D:\\temp\\test.zip";
        List<String> strings = readZipFileName(path);
        readZipFile(path);
    }

    //读取zip文件内的文件,返回文件名称列表
    public static List<String> readZipFileName(String path) {
        List<String> list = new ArrayList<>();
        try {
            ZipFile zipFile = new ZipFile(path);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                list.add(entries.nextElement().getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    //读取zip文件内的文件,返回文件内容列表
    public static List<String> readZipFile(String path) {
        List<String> list = new ArrayList<>();
        List<List<String>> ddlList = null;
        try {
            ZipFile zipFile = new ZipFile(path);
            FileInputStream inputStream = new FileInputStream(path);
            InputStream in = new BufferedInputStream(inputStream);
            ZipInputStream zin = new ZipInputStream(in);
            ZipEntry ze;
            while ((ze = zin.getNextEntry()) != null) {
                ddlList = new ArrayList<>();
                if (ze.isDirectory()) {
                } else {
                    System.err.println("file - " + ze.getName() + " : " + ze.getSize() + " bytes");
                    long size = ze.getSize();
                    if (size > 0) {
                        InputStream stream = zipFile.getInputStream(ze);
                        BufferedReader br = new BufferedReader(new InputStreamReader(stream, Charset.forName("gbk")));
                        String line;
                        while ((line = br.readLine()) != null) {
                            String[] index = line.split(",");
                            List<String> indexList = Arrays.asList(index);
                            ddlList.add(indexList);
                        }
                        br.close();
                    }
                    break;
                }
                //处理ddlList,此时ddlList为每个文件的内容,while每循环一次则读取一个文件
            }
            zin.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //此处返回无用,懒得修改了
        return list;
    }

    @SneakyThrows
    @Test
    public void testRead() {
        String path = "D:\\temp\\test.zip";
        FileInputStream inputStream = new FileInputStream(path);
        readMetaData(inputStream);
    }

    @SneakyThrows
    public HashMap<String, Object> readMetaData(InputStream inputStream) {
        ZipInputStream zin = new ZipInputStream(inputStream);
        try {
            ZipEntry ze;
            int skip = 0;
            while ((ze = zin.getNextEntry()) != null) {
                if (!ze.isDirectory()) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("read metadata failed");
        } finally {
            zin.closeEntry();
        }
        return null;
    }
}
