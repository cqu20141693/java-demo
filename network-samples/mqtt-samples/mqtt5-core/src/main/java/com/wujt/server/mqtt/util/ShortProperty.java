package com.wujt.server.mqtt.util;

import io.netty.handler.codec.mqtt.MqttProperties;

/**
 * @author wujt
 */

public final class ShortProperty extends MqttProperties.MqttProperty<Short> {
    public ShortProperty(int propertyId, Short value) {
        super(propertyId, value);
    }

    public String toString() {
        return "ShortProperty(" + this.propertyId() + ", " + this.value() + ")";
    }
}