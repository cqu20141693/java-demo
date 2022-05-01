package com.cc.network;

/**
 * wcc 2022/4/26
 */
public interface ConfigScope {
    String getId();

    default String getName() {
        return getId();
    }

    static ConfigScope of(String id) {
        return () -> id;
    }
}
