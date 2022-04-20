package com.gow.codec.model.batch;

import java.util.List;
import lombok.Data;

/**
 * @author gow
 * @date 2021/7/29
 */
@Data
public class BatchStreamModel {
    private Long bizTime;
    // 自身数据
    private List<SingleStreamModel> self;
    // 代理数据
    private List<ProxyStreamModel> proxy;
}
