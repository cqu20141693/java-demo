package com.gow.codec.base;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MockModel {
    @ExcelProperty(value = "产品名称", index = 0)
    private String productName;
    @ExcelProperty(value = "产品模型", index = 1)
    private String properties;
    @ExcelProperty(value = "产品设备", index = 2)
    private String devices;
}
