package com.cc.things;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * wcc 2022/6/4
 */
public class ThingTypes {

    private static final Map<String, ThingType> types = new ConcurrentHashMap<>();

    public static void register(ThingType thingType) {
        types.put(thingType.getId(), thingType);
    }

    public static Optional<ThingType> lookup(String typeId) {
        return Optional.ofNullable(types.get(typeId));
    }

    public static ThingType lookupOrElse(String typeId, Function<String, ThingType> orElse) {
        ThingType existing = types.get(typeId);
        if (null != existing) {
            return existing;
        }
        return orElse.apply(typeId);
    }

}
