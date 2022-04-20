package com.wujt.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.InterruptException;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.errors.TimeoutException;
import org.springframework.kafka.support.ProducerListener;

/**
 * @author wujt  2021/4/28
 * https://blog.csdn.net/lbh199466/article/details/102968695
 */
@Slf4j
public class KafkaProducerListener implements ProducerListener<String, String> {


    @Override
    public void onSuccess(String topic, Integer partition, String key, String value, RecordMetadata recordMetadata) {
        log.info("ACK from ProducerListener topic:{} partition:{} message: {} offset:  {}", topic, partition, value, recordMetadata.offset());
    }

    @Override
    public void onError(String topic, Integer partition, String key, String value, Exception exception) {
        // 出现了异常
        if (exception instanceof InterruptException) {
            // 线程中断异常
        } else if (exception instanceof SerializationException) {
            // 序列化时的异常
        } else if (exception instanceof TimeoutException) {
            // 表示重试失败 ，此处异常表示发送过程中的异常
            // 服务端和客户端交互存在问题，消息是可用的
        } else if (exception instanceof KafkaException) {
            // api 调用异常
        }
        log.error("send topic {} error occur，retry failed ", topic);
        // 如果出现异常，表示消息发送失败，此时应改将消息进行存储 或者是消息通知，打印日志
        log.info("suggest 調用一個可用服務，設置到當前消息的處理模式為丢弃还是写文件，如果保证数据不丢失，必须写文件，" +
                "后续需要恢复时读取写入，并开启一个试探线程，每个一定时间进行kafka可用性探测，修改数据写入模式，并激活文件数据读取");
    }

}
