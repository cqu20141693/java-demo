package com.gow.test.fastjson;

import com.alibaba.fastjson.JSONArray;
import com.gow.test.fastjson.model.ModelInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gow
 * @date 2021/7/21
 */
public class FastjsonDemo {
    public static void main(String[] args) {
        testJSONArrayConvert();

        ModelInfo modelInfo = new ModelInfo();
        modelInfo.setName("gow");
        modelInfo.setAge(25);

        ModelInfo modelInfo1 = new ModelInfo();
        modelInfo1.setName("yq");
        modelInfo1.setAge(22);
        modelInfo.setInfo(modelInfo1);

        ArrayList<ModelInfo> objects = new ArrayList<>();
        objects.add(modelInfo);

        String s = JSONArray.toJSONString(objects);
        List<ModelInfo> modelInfos = JSONArray.parseArray(s, ModelInfo.class);
        System.out.println(modelInfos.size());

    }

    /**
     * 测试 JSONArray.parseArray(JSONArray.toJSONString(Array))
     */
    private static void testJSONArrayConvert() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "gow");
        map.put("age", 25);

        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        maps.add(map);
        maps.add(map);
        String s = JSONArray.toJSONString(maps);
        System.out.println(s);
        JSONArray jsonArray = JSONArray.parseArray(s);
        System.out.println(jsonArray);
    }
}
