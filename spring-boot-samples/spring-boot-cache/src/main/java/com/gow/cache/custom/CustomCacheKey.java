package com.gow.cache.custom;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author gow
 * @date 2021/7/12 0012
 */
@Data
@Accessors(chain = true)
public class CustomCacheKey {
    private String groupKey;

    private String sn;

    private String cmdTag;
}
