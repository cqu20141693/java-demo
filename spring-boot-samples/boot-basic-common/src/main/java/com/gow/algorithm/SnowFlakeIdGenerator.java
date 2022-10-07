package com.gow.algorithm;

import java.util.Random;

/**
 * @author gow
 * @date 2021/10/20
 * <p>
 * SnowFlake算法的缺点：
 * 依赖于系统时钟的一致性。如果某台机器的系统时钟回拨，有可能造成ID冲突，或者ID乱序。
 * 还有，在启动之前，如果这台机器的系统时间回拨过，那么有可能出现ID重复的危险。
 * <p>
 * 优点：
 * 有序递增的整数，可以用作数据库主键
 * 生成参数存在时间戳，并且时间戳时控制大小的直接因素，所以id顺序即为时间顺序
 * <p>
 * 为什么雪花算法第一位是0，保证生成的整数为正数且递增
 * <p>
 * 当发现当前时间小于上一次时间？发生时间回拨
 */
public class SnowFlakeIdGenerator {


    /**
     * 起始的时间戳
     */
    private final static long START_STAMP = 0L;

    /**
     * 每一部分占用的位数
     */
    private final static long SEQUENCE_BIT = 12; //序列号占用的位数
    private final static long MACHINE_BIT = 5;   //机器标识占用的位数
    private final static long DATACENTER_BIT = 5;//数据中心占用的位数

    /**
     * 每一部分的最大值
     */
    private final static long MAX_DATACENTER = ~(-1L << DATACENTER_BIT);
    private final static long MAX_MACHINE = ~(-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT; //12
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT; // 17
    private final static long TIMESTAMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT; // 22

    private final long datacenterId;  //数据中心
    private final long machineId;     //机器标识
    private long sequence = 0L; //序列号
    private long lastStamp = System.currentTimeMillis();//上一次时间戳,程序重启默认当前时间

    public SnowFlakeIdGenerator(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    /**
     * 产生下一个ID
     *
     * @return
     */
    public synchronized long nextId() {
        long currStamp = getNewStamp();
        if (currStamp < lastStamp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id" + currStamp + ":" + lastStamp);
        }

        if (currStamp == lastStamp) {
            //相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStamp = getNextMill();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastStamp = currStamp;

        return (currStamp - START_STAMP) << TIMESTAMP_LEFT //时间戳部分22
                | datacenterId << DATACENTER_LEFT       //数据中心部分17
                | machineId << MACHINE_LEFT             //机器标识部分12
                | sequence;                             //序列号部分
    }

    private long getNextMill() {
        long mill = getNewStamp();
        while (mill <= lastStamp) {
            mill = getNewStamp();
        }
        return mill;
    }

    private long getNewStamp() {
        return System.currentTimeMillis();
    }

    // default IdGenerator
    private static final SnowFlakeIdGenerator generator;

    static {
        Random random = new Random();
        long workerId = Long.getLong("id-worker", random.nextInt(31));
        long dataCenterId = Long.getLong("id-datacenter", random.nextInt(31));
        generator = new SnowFlakeIdGenerator(workerId, dataCenterId);
    }

    public static SnowFlakeIdGenerator getDefaultGenerator() {
        return generator;
    }

}
