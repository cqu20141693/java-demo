package com.gow;

import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;

/**
 * @author wujt  2021/5/18
 */
@SpringBootApplication
public class ValidateApp {
    public static void main(String[] args) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name","gow");
        map.put("age",25);
        String value="{\"name\":\"gow\",\"age\":25}";
        System.out.println(JSONObject.toJSONString(map));
        SpringApplication.run(ValidateApp.class,args);
    }
}
