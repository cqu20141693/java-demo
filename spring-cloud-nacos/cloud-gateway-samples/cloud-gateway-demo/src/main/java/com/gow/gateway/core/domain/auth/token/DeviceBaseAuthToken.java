package com.gow.gateway.core.domain.auth.token;

import com.gow.gateway.core.domain.auth.DeviceTokenType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author gow
 * @date 2021/7/18 0018
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceBaseAuthToken extends AuthToken {
    /**
     * device token type {@link DeviceTokenType}
     */
    protected DeviceTokenType deviceTokenType = DeviceTokenType.one_type_one_key;

    public DeviceTokenType getDeviceTokenType() {
        return deviceTokenType;
    }

    public void setDeviceTokenType(DeviceTokenType deviceTokenType) {
        this.deviceTokenType = deviceTokenType;
    }

}
