package com.wujt.config;

/**
 * @author wujt  2021/4/29
 */
public class KafkaServerProperties {
    /**
     * unclean.leader.election.enable=false
     * 关闭unclean leader选举，即不允许非ISR中的副本被选举为leader，以避免数据丢失
     *
     * replication.factor >= 3
     * 这个完全是个人建议了，参考了Hadoop及业界通用的三备份原则
     *
     * min.insync.replicas > 1 消息至少要被写入到这么多副本才算成功，也是提升数据持久性的一个参数与acks配合使用
     *
     * 保证replication.factor > min.insync.replicas  如果两者相等，当一个副本挂掉了分区也就没法正常工作了。通常设置replication.factor = min.insync.replicas + 1即可
     */
}
