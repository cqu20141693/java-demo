package com.cc.controller;

import com.alibaba.fastjson.JSONObject;
import com.cc.tsl.Generator;
import com.cc.tsl.model.BasicModel;
import com.cc.tsl.model.BasicProperties;
import com.cc.tsl.model.TslModel;
import com.gow.common.ResourceEnum;
import com.gow.spring.resource.ResourceLoaderService;
import com.gow.utils.EasyExcelUtil;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("task/gega")
public class GegaController {

    @Autowired
    private ResourceLoaderService resourceLoaderService;

    @GetMapping("tls/create")
    public void create(HttpServletResponse httpServletResponse) throws Exception {

        InputStream resourceStream = resourceLoaderService.getResourceStream("test.json", ResourceEnum.CLASSPATH);
        BasicModel model = JSONObject.parseObject(resourceStream, BasicModel.class);
        Generator.createExcel(model);
        System.out.println(JSONObject.toJSONString(model));
        ArrayList<TslModel> tslModels = new ArrayList<>();
        BasicProperties geoProperties = new BasicProperties();
        geoProperties.setName("geo");
        geoProperties.setVal("(115.875577,40.169975)");
        geoProperties.setDesc("经纬度");

        BasicProperties tProperties = new BasicProperties();
        tProperties.setName("temperature");
        tProperties.setVal("25");
        tProperties.setDesc("温度");

        /**
         *     {
         *       "name": "geo",
         *       "val": "116.312857,39.954769",
         *       "desc": "经纬度"
         *     },
         *     {
         *       "name": "temperature",
         *       "val": "25",
         *       "desc": "温度℃"
         *     },
         */
        List<BasicProperties> models = model.getModels();
        model.getDevices().forEach(productInfo -> {
            ArrayList<BasicProperties> list = new ArrayList<>();
            list.add(geoProperties);
            list.add(tProperties);
            for (Integer index : productInfo.getIndexes()) {
                list.add(models.get(index));
            }
            TslModel tslModel = new TslModel();
            tslModel.setCategory(model.getCategory());
            tslModel.setDevice(productInfo.getName());
            tslModel.setProperties(JSONObject.toJSONString(list));

            tslModels.add(tslModel);
        });
        // 下面几行是为了解决文件名乱码的问题
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + new String("tsl.xlsx".getBytes(), StandardCharsets.ISO_8859_1));
        httpServletResponse.setContentType("application/vnd.ms-excel;charset=UTF-8");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setHeader("Cache-Control", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        EasyExcelUtil.writeWithSheets(httpServletResponse.getOutputStream())
                .writeModel(TslModel.class, tslModels, "sheet1")
                .finish();
    }
}
