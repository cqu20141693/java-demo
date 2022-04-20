package com.wujt.zookeeper.config;

/**
 * @author gow
 * @date 2021/6/25
 */
public class EventDescription {


    public int delimiter;
    public int size;

    public EventDescription(String event)
            throws IllegalArgumentException {
        String[] pair = event.split(":");
        if (pair.length != 2) {
            System.out.println("Configuration error: " + event);
            throw new IllegalArgumentException();
        }

        delimiter = Integer.decode(pair[0]);
        size = Integer.decode(pair[1]);
    }
}
