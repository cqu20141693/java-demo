package com.wujt;

import com.alibaba.fastjson.JSON;
import com.wujt.functions.SumAggregateFunction;
import com.wujt.functions.Tuple2MapFunction;
import com.wujt.model.StreamData;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author wujt
 */
public class CheckPointDemo {

    public static void main(String[] args) {
        //创建运行环境
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //设置kafka配置
        Properties properties = new Properties();
        //String servers = "kafka-dev-outer-0.kafka-dev.svc.cluster.local:9092,kafka-dev-outer-1.kafka-dev.svc.cluster.local:9092,kafka-dev-outer-2.kafka-dev.svc.cluster.local:9092";
        String servers = "test-kafka-outer-0.testdb.svc.cluster.local:9092,test-kafka-outer-1.testdb.svc.cluster.local:9092,test-kafka-outer-2.testdb.svc.cluster.local:9092";
        properties.setProperty("bootstrap.servers", servers);
        properties.setProperty("group.id", "wjt-flink");
        String originTopic = "origin-data-topic";
        String chanTopic = "chan-conn-topic";
        String checkedTopic = "data-topic-checked";
        ArrayList<String> topics = new ArrayList<>();
        topics.add(originTopic);
        topics.add(chanTopic);
        topics.add(checkedTopic);
        FlinkKafkaConsumer<String> flinkKafkaConsumer =
                new FlinkKafkaConsumer<String>(topics,
                        new SimpleStringSchema(),
                        properties
                );
        //设置消费的起点
        flinkKafkaConsumer.setCommitOffsetsOnCheckpoints(true);
        DataStream<String> stream = env.addSource(flinkKafkaConsumer);
        DataStream<StreamData> dataStream = stream.map((v) -> {
            //System.out.println(v);
            StreamData streamData = JSON.parseObject(v, StreamData.class);
            return streamData;
        });
        dataStream.addSink(new PrintSinkFunction<>());

        SingleOutputStreamOperator<Tuple2<String, Integer>> sum = dataStream
                .map(new Tuple2MapFunction())
                .returns(Types.TUPLE(Types.STRING, Types.INT))
                .keyBy(tuple2 -> tuple2.f1)
                //.window(EventTimeSessionWindows.withGap(Time.seconds(3L)))
                .timeWindow(Time.of(10, TimeUnit.SECONDS))
                //计算个数，计算第1列
               .sum(1); //replace with aggregate
               // .aggregate(new SumAggregateFunction());
        sum.print("counter").setParallelism(1);
        try {
            String jobName = "kafka producer";
            env.execute(jobName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
