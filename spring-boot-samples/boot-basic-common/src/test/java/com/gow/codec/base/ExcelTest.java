package com.gow.codec.base;

import com.gow.utils.EasyExcelUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ExcelTest {
    @Test
    public void testExcelWrite() {
        ArrayList<MockModel> mockModels = new ArrayList<>();
        MockModel model = new MockModel("农业", "properties", "devices");
        mockModels.add(model);
        mockModels.add(model);
        EasyExcelUtil.write("D:\\temp\\test.xlsx", MockModel.class, mockModels);
        System.out.println("write file");
    }

    @Test
    public void testExcelRead() {

        List<MockModel> ts = EasyExcelUtil.syncReadModel("filePath", MockModel.class);
        System.out.println(ts.size());
    }

}
