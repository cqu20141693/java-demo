package com.cc.things.metadata;


import com.cc.things.metadata.unit.ValueUnit;

/**
 * wcc 2022/6/4
 */
public interface UnitSupported {
    ValueUnit getUnit();

    void setUnit(ValueUnit unit);
}
