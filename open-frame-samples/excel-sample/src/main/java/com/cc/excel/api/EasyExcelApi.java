package com.cc.excel.api;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.cc.excel.api.model.OtherInfo;
import com.cc.excel.api.model.VegetableInfo;
import com.cc.excel.util.EasyUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * wcc 2022/8/23
 */

@RestController
@RequestMapping("easy/excel/mock")
public class EasyExcelApi {

    @GetMapping("autofill")
    public void download(HttpServletResponse response) {
        //其他信息
        OtherInfo otherInfo = new OtherInfo();
        otherInfo.setDate(new Date())
                .setPhone("13000000000")
                .setMarketName("城东菜市场");
        //蔬菜信息
        List<VegetableInfo> vegetableInfos = new ArrayList<>();
        vegetableInfos.add(new VegetableInfo().setName("白菜")
                .setUnitPrice("1.50")
                .setTradePrice("1.20")
                .setArrivalDate(new Date())
                .setPlaceOrigin("河北省"));
        vegetableInfos.add(new VegetableInfo().setName("胡萝北")
                .setUnitPrice("1.00")
                .setTradePrice("0.80")
                .setArrivalDate(new Date())
                .setPlaceOrigin("辽宁省"));
        vegetableInfos.add(new VegetableInfo().setName("西红柿")
                .setUnitPrice("2.11")
                .setTradePrice("1.88")
                .setArrivalDate(new Date())
                .setPlaceOrigin("河南省"));
        int[] arr = {1};
        vegetableInfos = vegetableInfos.stream().
                peek(vegetableInfo ->vegetableInfo.setId(arr[0] ++)).collect(Collectors.toList());

        EasyUtil.setExcelHttpServletResponse(response, "test.xlsx");
        //获取模板
        ClassPathResource classPathResource = new ClassPathResource("excel/text.xlsx");
        try (InputStream inputStream = classPathResource.getInputStream();
             ServletOutputStream outputStream = response.getOutputStream()) {
            //设置输出流和模板信息
            ExcelWriter excelWriter = EasyExcel.write(outputStream).withTemplate(inputStream).build();
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            //开启自动换行,自动换行表示每次写入一条list数据是都会重新生成一行空行,此选项默认是关闭的,需要提前设置为true
            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
            excelWriter.fill(vegetableInfos, fillConfig, writeSheet);
            excelWriter.fill(otherInfo, writeSheet);
            excelWriter.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
