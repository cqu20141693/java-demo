package com.gow.pulsar.core.domain;


import static org.apache.pulsar.client.api.ConsumerCryptoFailureAction.FAIL;
import static org.apache.pulsar.client.api.HashingScheme.JavaStringHash;
import static org.apache.pulsar.client.api.MessageId.earliest;
import static org.apache.pulsar.client.api.MessageRoutingMode.RoundRobinPartition;
import static org.apache.pulsar.client.api.ProducerAccessMode.Shared;
import static org.apache.pulsar.client.api.RegexSubscriptionMode.PersistentOnly;
import static org.apache.pulsar.client.api.SubscriptionInitialPosition.Latest;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.pulsar.client.api.BatchReceivePolicy;
import org.apache.pulsar.client.api.BatcherBuilder;
import org.apache.pulsar.client.api.CompressionType;
import org.apache.pulsar.client.api.ConsumerCryptoFailureAction;
import org.apache.pulsar.client.api.CryptoKeyReader;
import org.apache.pulsar.client.api.HashingScheme;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.MessageRoutingMode;
import org.apache.pulsar.client.api.ProducerAccessMode;
import org.apache.pulsar.client.api.ProducerCryptoFailureAction;
import org.apache.pulsar.client.api.RegexSubscriptionMode;
import org.apache.pulsar.client.api.SubscriptionInitialPosition;
import org.apache.pulsar.client.api.SubscriptionMode;
import org.apache.pulsar.shade.com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author gow
 * @date 2021/7/2
 */
@Configuration
@ConfigurationProperties("gow.pulsar")
@Data
public class PulsarProperties {
    private String cluster = "dev";
    //broker或discovery-service或pulsar-proxy地址
    @NotNull
    private String serviceUrl;
    @NotNull
    private ClientProperties client;

    private ProducerProperties producer = new ProducerProperties();
    private ConsumerProperties consumer = new ConsumerProperties();
    private ReaderProperties reader;
    private AdminProperties admin;

    /**
     * @author gow
     * @date 2021/7/19
     */
    @Data
    public static class ClientProperties {
        @NotNull
        private String role;
        // JWTAuthentication
        @NotNull
        private String jwtToken;
        // Name of the authentication plugin
        private String authPluginClassName = null;
        // String represents parameters for the authentication plugin
        //Example : key1:val1,key2:val2
        private String authParams = null;
        //操作超时时间
        private Long operationTimeoutMs = 30000L;
        //状态收集时间间隔
        private Long statsIntervalSeconds = 60L;
        //netty IO处理线程数，Boostrap.group(EventLoopGroup(numIoThreads)),默认broker集群数量
        private Integer numIoThreads = 1;
        // The listener thread pool is shared across all the consumers and readers using the "listener" model to get
        // messages
        // For a given consumer, the listener is always invoked from the same thread to ensure ordering
        // If you want multiple threads to process a single topic, you need to create a shared subscription and multiple
        // consumers for this subscription. This does not ensure ordering.
        //消费者订阅线程数, 保证每个消费者被单个线程处理
        private Integer numListenerThreads = 1;
        //每个broker（与客户端）连接数
        private int connectionsPerBroker = 1;
        //是否延迟发送（Nagle算法，不延迟，意味着立即发送）
        private Boolean useTcpNoDelay = true;
        private Boolean useTls = false;
        private String tlsTrustCertsFilePath = null;
        private Boolean tlsAllowInsecureConnection = false;
        private Boolean tlsHostnameVerificationEnable = false;
        // The number of concurrent lookup requests allowed to send on each broker connection to prevent overload on
        // broker
        private Integer concurrentLookupRequest = 5000;
        //The maximum number of lookup requests allowed on each broker connection to prevent overload on broker
        private Integer maxLookupRequest = 50000;
        private Integer maxNumberOfRejectedRequestPerConnection = 50;
        //保活间隔时间
        private Integer keepAliveIntervalSeconds = 30;
        private Integer connectionTimeoutMs = 10000;
        private Integer requestTimeoutMs = 60000;
        private Integer defaultBackoffIntervalNanos = Math.toIntExact(TimeUnit.MILLISECONDS.toNanos(100));
        private Long maxBackoffIntervalNanos = TimeUnit.SECONDS.toNanos(30);
    }

    /**
     * Super user, used to configure element data
     *
     * @author gow
     * @date 2021/7/5
     */
    @Data
    public static class AdminProperties {
        private String webServiceUrl = "http://localhost:8080";
        private String adminToken;
        private String authPluginClassName;
        private String authParams;
        private Boolean useTsl = false;
        private Boolean tlsAllowInsecureConnection = false;
        private String tlsTrustCertsFilePath;
    }

    /**
     * @author gow
     * @date 2021/7/2
     */
    @Data
    public static class ReaderProperties {

        private Set<String> topicNames = new HashSet();
        @JsonIgnore
        private MessageId startMessageId = earliest;
        @JsonIgnore
        private long startMessageFromRollbackDurationInSec = 1;
        private int receiverQueueSize = 1000;
        private String readerName = null;
        private String subscriptionRolePrefix = null;
        private String subscriptionName = null;
        private CryptoKeyReader cryptoKeyReader = null;
        private ConsumerCryptoFailureAction cryptoFailureAction = FAIL;
        private boolean readCompacted = false;
    }

    /**
     * @author gow
     * @date 2021/7/2
     */
    @Data
    public static class ConsumerProperties {

        /**
         * durable的订阅，会有cursor来记录订阅的位置信息
         * non-durable的订阅则没有cursor
         */
        private SubscriptionMode subscriptionMode = SubscriptionMode.Durable;
        //  Latest(0),Earliest(1);
        private SubscriptionInitialPosition subscriptionInitialPosition = Latest;
        // PersistentOnly: only subscribe to persistent topics.
        // NonPersistentOnly: only subscribe to non-persistent topics.
        // AllTopics: subscribe to both persistent and non-persistent topics.
        private RegexSubscriptionMode regexSubscriptionMode = PersistentOnly;
        // Size of a consumer's receiver queue.
        // A value higher than the default value increases consumer throughput,
        // though at the expense of more memory utilization.
        private Integer receiverQueueSize = 1000;
        private BatchReceivePolicy batchReceivePolicy = BatchReceivePolicy.DEFAULT_POLICY;
        // Group a consumer acknowledgment for a specified time.
        // Setting a group time of 0 sends out acknowledgments immediately.
        // A longer ack group time is more efficient at the expense of a slight increase in message re-deliveries
        // after a failure.
        private Long acknowledgementsGroupTimeMicros = TimeUnit.MILLISECONDS.toMicros(100);
        private boolean batchIndexAcknowledgmentEnabled = true;
        //
        private Long negativeAckRedeliveryDelayMicros = TimeUnit.MINUTES.toMicros(1);
        // This setting reduces the receiver queue size for individual partitions if the total receiver queue size
        // exceeds this value.
        private Integer maxTotalReceiverQueueSizeAcrossPartitions = 50000;
        private String consumerName = null;
        private Long ackTimeoutMillis = 0L;
        //Granularity of the ack-timeout redelivery.
        private Long tickDurationMillis = 1000L;
        // Priority level for a consumer to which a broker gives more priority while dispatching messages in the shared
        //  subscription mode.The broker follows descending priorities. For example, 0=max-priority, 1, 2,...
        //In shared subscription mode, the broker first dispatches messages to the max priority level consumers if they
        // have permits. Otherwise, the broker considers next priority level consumers.
        private Integer priorityLevel = 0;
        // FAIL: this is the default option to fail messages until crypto succeeds.
        // DISCARD: silently acknowledge and not deliver message to an application.
        // CONSUME: deliver encrypted messages to applications. It is the application's responsibility to decrypt the
        // message.
        private ConsumerCryptoFailureAction cryptoFailureAction = FAIL;
        // properties is application defined metadata attached to a consumer.
        // When getting a topic stats, associate this metadata with the consumer stats for easier identification.
        private SortedMap<String, String> properties = new TreeMap<>();
        // If enabling readCompacted, a consumer reads messages from a compacted topic rather than reading a full
        // message
        // backlog of a topic. Only enabling readCompacted on subscriptions to persistent topics, which have a single
        // active consumer (like failure or exclusive subscriptions).Attempting to enable it on subscriptions to
        // non-persistent topics or on shared subscriptions leads to a subscription call throwing a
        // PulsarClientException.
        private Boolean readCompacted = false;
        // Topic auto discovery period(minute) when using a pattern for topic's consumer.
        private Integer patternAutoDiscoveryPeriod = 1;
        // By default, some messages are probably redelivered many times, even to the extent that it never stops.
        // By using the dead letter mechanism, messages have the max redelivery count. When exceeding the maximum number
        // of redeliveries, messages are sent to the Dead Letter Topic and acknowledged automatically
        // 2.8.0 Currently, dead letter topic is enabled only in the shared subscription mode.
        private Boolean enableRetry = false;
        private Integer maxRedeliverCount;
        private String retryLetterTopic;
        private String deadLetterTopic;
        // this is only for partitioned consumers.
        private Boolean autoUpdatePartitions = true;
        // If replicateSubscriptionState is enabled, a subscription state is replicated to geo-replicated clusters.
        private Boolean replicateSubscriptionState = false;
        // chunk message consumption configuration
        private Integer maxPendingChunkedMessage = 1000;
        private Boolean autoAckOldestChunkedMessageOnQueueFull = true;
        private Long expireTimeOfIncompleteChunkedMessageMillis = 3600 * 1000L;

    }

    /**
     * @author gow
     * @date 2021/7/21
     *
     * topic info
     */
    @Data
    @EqualsAndHashCode
    public static class TopicInfo {
        private String name;
        private PulsarSchemaType schema = PulsarSchemaType.String;
        private Class<?> jsonClass;
    }

    /**
     * @author gow
     * @date 2021/7/2
     */
    @Data
    public static class ProducerProperties {

        private Set<TopicInfo> topics = new HashSet<>();
        private String producerName;
        // If a message is not acknowledged by a server
        // before the sendTimeout expires, an error occurs.
        private Integer sendTimeoutMs = 30000;
        //when the outgoing message queue is full,
        // true : the Send and SendAsync methods of producer block,
        // false : the Send and SendAsync methods of producer fail and ProducerQueueIsFullError exceptions occur.
        // reference:  MaxPendingMessages
        private Boolean blockIfQueueFull = false;
        // The maximum size of a queue holding pending messages.
        private Integer maxPendingMessages = 1000;
        //The maximum number of pending messages across partitions.
        private Integer maxPendingMessagesAcrossPartitions = 50000;
        //Message routing logic,Available options are as follows:
        // pulsar.RoundRobinDistribution: round robin
        // pulsar.UseSinglePartition: publish all messages to a single partition
        // pulsar.CustomPartition: a custom partitioning scheme
        private MessageRoutingMode messageRoutingMode = RoundRobinPartition;
        //Hashing function determining the partition,Available options are as follows:
        // pulsar.JavaStringHash: the equivalent of String.hashCode() in Java
        // pulsar.Murmur3_32Hash: applies the Murmur3 hashing function
        // pulsar.BoostHash: applies the hashing function from C++'s Boost library
        private HashingScheme hashingScheme = JavaStringHash;
        /**
         * Producer should take action when encryption fails
         * FAIL: if encryption fails, unencrypted messages fail to send.
         * SEND: if encryption fails, unencrypted messages are sent.
         */
        private ProducerCryptoFailureAction cryptoFailureAction = ProducerCryptoFailureAction.FAIL;
        // Batching time period of sending messages.
        private Long batchingMaxPublishDelayMicros = TimeUnit.MILLISECONDS.toMicros(100);
        // The maximum number of messages permitted in a batch.
        private Integer batchingMaxMessages = 1000;
        //Enable batching of messages.
        private Boolean batchingEnabled = true;
        // key_Shared模式要用KEY_BASED,才能保证同一个key的message在一个batch里
        private BatcherBuilder batcherBuilder = BatcherBuilder.DEFAULT;
        //Message data compression type used by a producer.default: no compression
        //Available options:
        //LZ4
        //ZLIB
        //ZSTD
        //SNAPPY
        private CompressionType compressionType = CompressionType.NONE;

        private Integer batchMaxBytes = 10 * 1024 * 1024 * 8;
        /**
         * Shared :
         * By default multiple producers can publish on a topic.
         * Exclusive:
         * Require exclusive access for producer. Fail immediately if there's already a producer connected.
         * WaitForExclusive :
         * Producer creation is pending until it can acquire exclusive access.
         */
        private ProducerAccessMode accessMode = Shared;
        /**
         * Batching and chunking cannot be enabled simultaneously. To enable chunking, you must disable batching in
         * advance.
         * Chunking is only supported for persisted topics.
         * Chunking is only supported for the exclusive and failover subscription modes.
         */
        private Boolean chunkingEnabled = false;
    }

}
