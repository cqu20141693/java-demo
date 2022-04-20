package com.wujt.server.mqtt.util;

import io.netty.handler.codec.mqtt.MqttProperties;

/**
 * @author wujt
 */

public final class ByteProperty extends MqttProperties.MqttProperty<Byte> {
    public ByteProperty(int propertyId, Byte value) {
        super(propertyId, value);
    }

    public String toString() {
        return "ByteProperty(" + this.propertyId() + ", " + this.value() + ")";
    }
}