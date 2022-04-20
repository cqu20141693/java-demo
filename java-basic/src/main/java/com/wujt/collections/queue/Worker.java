package com.wujt.collections.queue;

import java.util.Objects;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author wujt
 */
public class Worker implements Delayed {

    private String name;
    /**
     * 延迟时间
     */
    private long delayTime;
    /**
     * 可使用的时间点
     */
    private Long availableTime;

    /**
     * @param name
     * @param delayTime 纳秒级别可控制
     */
    public Worker(String name, long delayTime) {
        this.name = name;
        this.delayTime = delayTime;
        this.availableTime = TimeUnit.NANOSECONDS.convert(delayTime, TimeUnit.MILLISECONDS)
                + System.nanoTime();
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(availableTime - System.nanoTime(), unit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        Worker that = (Worker) o;
        return availableTime > that.availableTime ? 1 : (availableTime < that.availableTime ? -1 : 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Worker)) return false;
        Worker worker = (Worker) o;
        return delayTime == worker.delayTime &&
                name.equals(worker.name) &&
                Objects.equals(availableTime, worker.availableTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, delayTime, availableTime);
    }

    @Override
    public String toString() {
        return "Worker{" +
                "name='" + name + '\'' +
                ", delayTime=" + delayTime +
                ", availableTime=" + availableTime +
                '}';
    }
}
