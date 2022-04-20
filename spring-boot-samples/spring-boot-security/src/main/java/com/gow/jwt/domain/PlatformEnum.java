package com.gow.jwt.domain;

import java.util.Optional;

/**
 * @author gow
 * @date 2021/8/2
 */
public enum PlatformEnum {
    agriculture,
    ;

    public static PlatformEnum createByName(String value) {
        return Optional.ofNullable(value).map(PlatformEnum::valueOf).orElse(agriculture);
    }

}
