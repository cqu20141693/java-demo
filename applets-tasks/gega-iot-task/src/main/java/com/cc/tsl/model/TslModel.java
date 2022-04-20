package com.cc.tsl.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TslModel {
    @ExcelProperty(value = "产品类别", index = 0)
    private String category;
    @ExcelProperty(value = "产品名称", index = 1)
    private String device;
    @ExcelProperty(value = "模型", index = 2)
    private String properties;

}
