package com.wujt.producer;

import com.wujt.config.TopicConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * @author wujt  2021/4/29
 */
@Component
public class MsgProducer {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    @Autowired
    private TopicConfig topicConfig;
    public void send(String key,String value){
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topicConfig.getDataTopic(),key,value);
        ListenableFuture<SendResult<String, String>> listenableFuture = kafkaTemplate.send(record);

        // 保证缓冲区内的数据先发送
        // 比如当
      //  kafkaTemplate.flush();
    }
}
