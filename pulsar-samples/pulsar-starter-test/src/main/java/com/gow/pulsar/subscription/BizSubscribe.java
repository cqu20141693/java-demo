package com.gow.pulsar.subscription;

import com.gow.pulsar.core.annotation.PulsarSubscribe;
import com.gow.pulsar.core.domain.PulsarSchemaType;
import com.gow.pulsar.test.schema.model.RawDataV2;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.SubscriptionType;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/7/19
 */
@Component
@Slf4j
public class BizSubscribe {

    @PulsarSubscribe(topic = "${pulsar.topic.in.fail-over}", subscriptionName =
            "pulsar-subscribe-consumer", schema = PulsarSchemaType.String,
            containerId = "", subscriptionType = SubscriptionType.Failover)
    public void receiveString(Message<String> msg) throws PulsarClientException {
        log.info(" receiveString String data={}", msg.getValue());

//        consumer.acknowledgeCumulative(msg.getMessageId());
    }

    @PulsarSubscribe(topic = "persistent://gow/persistent/p-failover-test-topic", subscriptionName =
            "pulsar-subscribe-consumer", schema = PulsarSchemaType.String,
            containerId = "", subscriptionType = SubscriptionType.Failover)
    public void receiveString(String msg) throws PulsarClientException {
        log.info("receiveString String data={}", msg);
    }

    @PulsarSubscribe(topic = "persistent://gow/persistent/json-schema-test-topic", pattern = true, subscriptionName =
            "pulsar-subscribe-consumer", schema = PulsarSchemaType.Json,
            jsonClass = RawDataV2.class,
            containerId = "sharedContainer", subscriptionType = SubscriptionType.Shared)
    public void receiveRawData(Message<RawDataV2> msg) {
        // log.info("receiveRawData RawMsg={}", JSONObject.toJSONString(msg.getValue()));
    }

    @PulsarSubscribe(topic = "persistent://gow/persistent/delay-event-topic", subscriptionName =
            "pulsar-subscribe-consumer", containerId = "sharedContainer", subscriptionType = SubscriptionType.Shared)
    public void receiveDelayEvent(Message<String> msg) {
        // log.info("receiveDelayEvent RawMsg={}", JSONObject.toJSONString(msg.getValue()));
    }
}
