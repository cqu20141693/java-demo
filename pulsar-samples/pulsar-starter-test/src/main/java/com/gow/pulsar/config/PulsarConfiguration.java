package com.gow.pulsar.config;

import com.gow.pulsar.core.container.PulsarContainer;
import com.gow.pulsar.core.container.ack.AckMode;
import com.gow.pulsar.core.domain.ContainerProperties;
import com.gow.pulsar.core.domain.PulsarProperties;
import com.gow.pulsar.core.factory.PulsarFactory;
import org.apache.pulsar.client.api.PulsarClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gow
 * @date 2021/7/26
 */
@Configuration
public class PulsarConfiguration {

    @Bean("sharedContainer")
    public PulsarContainer sharedContainer(PulsarProperties properties, PulsarFactory pulsarFactory,
                                           PulsarClient client) {
        PulsarContainer pulsarContainer = new PulsarContainer(properties, pulsarFactory, client);
        ContainerProperties containerProperties = pulsarContainer.containerProperties();
        containerProperties.setAckMode(AckMode.MANUAL_IMMEDIATE);
        return pulsarContainer;
    }

}
