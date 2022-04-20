package com.gow.codec.model.batch;

import java.util.List;
import lombok.Data;

/**
 * @author gow
 * @date 2021/9/17
 */
@Data
public class ProxyStreamModel {
    // 设备SN
    private String SN;
    // 数据
    private List<SingleStreamModel> proxy;
}
