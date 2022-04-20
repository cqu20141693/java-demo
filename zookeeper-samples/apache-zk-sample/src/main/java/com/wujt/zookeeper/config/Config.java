package com.wujt.zookeeper.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author gow
 * @date 2021/6/25
 */
@Configuration
@ConfigurationProperties("zookeeper")
public class Config {

    private int writes = 10;
    private int size = 1024;
    private String connectString = "localhost:2181";
    private int clients = 1;
    private int samples = 5000;
    private boolean cleanup = true;
    private String nodeRoot = "/runoob";
    private String eventString = "";
    private ArrayList<EventDescription> eventList = new ArrayList<>();
    Iterator<EventDescription> eventIterator;

    public void loadConfig() {
        /*
         * Dealing with events
         */
        String[] events = eventString.split(";");
        System.out.println("Event length: " + events.length);
        if (!eventString.isEmpty()) {
            for (int i = 0; i < events.length; i++) {
                eventList.add(new EventDescription(events[i]));
            }
        } else {
            eventList.add(new EventDescription(new String(this.writes + ":" + this.size)));
        }

        eventIterator = eventList.iterator();
    }

    public int getWrites() {
        return writes;
    }

    public int getSize() {
        return size;
    }

    public String getNodeRoot() {
        return nodeRoot;
    }

    public String getConnectString() {
        return connectString;
    }

    public int getClients() {
        return clients;
    }

    public int getSamples() {
        return samples;
    }

    public boolean needsCleanup() {
        return cleanup;
    }

    public void setWrites(int writes) {
        this.writes = writes;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public void setClients(int clients) {
        this.clients = clients;
    }

    public void setSamples(int samples) {
        this.samples = samples;
    }

    public void setCleanup(boolean cleanup) {
        this.cleanup = cleanup;
    }

    public void setNodeRoot(String nodeRoot) {
        this.nodeRoot = nodeRoot;
    }

    public void setEventString(String eventString) {
        this.eventString = eventString;
    }

    public EventDescription nextEvent() {
        if (eventIterator.hasNext()) {
            return eventIterator.next();
        }

        return null;
    }


}
