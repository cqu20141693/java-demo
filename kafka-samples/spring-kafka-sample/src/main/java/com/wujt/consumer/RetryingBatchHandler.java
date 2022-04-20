package com.wujt.consumer;

import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.adapter.RetryingMessageListenerAdapter;
import org.springframework.retry.support.RetryTemplate;

/**
 * @author wujt  2021/4/29
 */
public class RetryingBatchHandler extends RetryingMessageListenerAdapter<String,String> {
    public RetryingBatchHandler(MessageListener<String, String> messageListener, RetryTemplate retryTemplate) {
        super(messageListener, retryTemplate);
    }
}
