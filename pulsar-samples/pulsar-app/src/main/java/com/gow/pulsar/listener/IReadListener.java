package com.gow.pulsar.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.Reader;
import org.apache.pulsar.client.api.ReaderListener;

/**
 * @author gow
 * @date 2021/7/6
 */
@Slf4j
public class IReadListener implements ReaderListener<String> {
    @Override
    public void received(Reader<String> reader, Message<String> msg) {
        log.info("consumer topic={} receive msg,key={},value={},messageId={},pubTime={},eventTime={}",
                reader.getTopic(), msg.getKey(), msg.getData(), msg.getMessageId(), msg.getPublishTime(),
                msg.getEventTime());
    }

    @Override
    public void reachedEndOfTopic(Reader<String> reader) {
        ReaderListener.super.reachedEndOfTopic(reader);
    }
}
