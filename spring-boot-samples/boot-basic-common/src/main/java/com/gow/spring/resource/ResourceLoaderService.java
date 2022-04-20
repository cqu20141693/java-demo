package com.gow.spring.resource;

import com.gow.common.ResourceEnum;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author wujt  2021/5/18
 * @date 2021/5/24 14:42
 */
@Component
public class ResourceLoaderService implements ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public InputStream getResourceStream(String filePath, ResourceEnum resourceEnum) throws Exception {
        Resource resource;
        switch (resourceEnum) {
            case URL:
                resource = resourceLoader.getResource(filePath);
                break;
            case FILE:
                resource = resourceLoader.getResource("file:" + filePath);
                break;
            case CLASSPATH:
                resource = resourceLoader.getResource("classpath:" + filePath);
                break;
            default:
                throw new Exception("resource is not null");
        }

        return resource.getInputStream();
    }

    public void showResourceData(String filePath, ResourceEnum resourceEnum) throws Exception {

        InputStream in = this.getResourceStream(filePath, resourceEnum);
        //直接使用fastjson进行转化
//        JSONObject temp = JSON.parseObject(resource.getInputStream(), StandardCharsets.UTF_8, JSONObject.class,
//                // 自动关闭流
//                Feature.AutoCloseSource,
//                // 允许注释
//                Feature.AllowComment,
//                // 允许单引号
//                Feature.AllowSingleQuotes,
//                // 使用 Big decimal
//                Feature.UseBigDecimal);
//       // System.out.println(temp);

        //将资源流进行逐行读写
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            System.out.println(line);
        }
        reader.close();
    }

}

