package com.wujt;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author wujt
 */
public class FileWordCount {


    public static void main(String[] args) throws Exception {
        // the host and the port to connect to
        ClassLoader classLoader = LocalFileDemo.class.getClassLoader();
        java.net.URL appURL = classLoader.getResource("app.txt");

        // get the execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Optional.ofNullable(appURL).map(url -> {
            String path = url.getPath();
            // get input data by connecting to the socket
            handleStream(env, path);
            return true;
        }).orElseThrow(() -> new Exception("app.txt is not exit"));


    }

    private static void handleStream(StreamExecutionEnvironment env, String path) {
        DataStream<String> text = env.readTextFile(path);
        // parse the data, group it, window it, and aggregate the counts
        DataStream<WordWithCount> windowCounts = text

                .flatMap(new FlatMapFunction<String, WordWithCount>() {
                    @Override
                    public void flatMap(String value, Collector<WordWithCount> out) {
                        for (String word : value.split("\\s")) {
                            out.collect(new WordWithCount(word, 1L));
                        }
                    }
                })

                .keyBy(value -> value.word)
               // .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
                .timeWindow(Time.of(10, TimeUnit.SECONDS))

                .reduce(new ReduceFunction<WordWithCount>() {
                    @Override
                    public WordWithCount reduce(WordWithCount a, WordWithCount b) {
                        return new WordWithCount(a.word, a.count + b.count);
                    }
                });

        // print the results with a single thread, rather than in parallel
        windowCounts.print("windowCount").setParallelism(1);
        //windowCounts.addSink(new PrintSinkFunction<>());
        try {
            env.execute("Socket Window WordCount");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Data type for words with count.
     */
    public static class WordWithCount {

        public String word;
        public long count;

        public WordWithCount() {
        }

        public WordWithCount(String word, long count) {
            this.word = word;
            this.count = count;
        }

        @Override
        public String toString() {
            return word + " : " + count;
        }
    }
}
