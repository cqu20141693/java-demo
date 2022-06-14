package com.cc.things.metadata.unit;


import java.util.List;
import java.util.Optional;

/**
 * 自定义单位提供商,用于自定义单位
 * wcc 2022/6/4
 */
public interface ValueUnitSupplier {

    /**
     * 根据ID获取单位
     *
     * @param id ID
     * @return 单位
     */
    Optional<ValueUnit> getById(String id);

    /**
     * 获取全部单位
     *
     * @return 全部单位
     */
    List<ValueUnit> getAll();
}
