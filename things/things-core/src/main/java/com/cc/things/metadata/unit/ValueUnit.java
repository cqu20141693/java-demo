package com.cc.things.metadata.unit;

import com.cc.things.metadata.FormatSupport;
import com.cc.things.metadata.Metadata;

import java.util.Map;

/**
 * 值单位
 * wcc 2022/6/4
 **/
public interface ValueUnit extends Metadata, FormatSupport {

    /**
     * 单位符号
     *
     * @return 符号
     */
    String getSymbol();

    @Override
    default Map<String, Object> getExpands() {
        return null;
    }
}
