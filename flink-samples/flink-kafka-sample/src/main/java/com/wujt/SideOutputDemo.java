package com.wujt;

import com.wujt.functions.ProcessFunctionImpl;
import com.wujt.functions.Tuple2MapFunction;
import lombok.val;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.util.concurrent.TimeUnit;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;

/**
 * @author wujt
 */
public class SideOutputDemo {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<Integer> stream = env.fromElements(1, 2, 3, 4);
        // 定义 output tag 用于标识输出流
        OutputTag<Integer> even = new OutputTag<Integer>("even");
        OutputTag<String> odd = new OutputTag<String>("odd");

        SingleOutputStreamOperator<Integer> mainDataStream = stream.process(new ProcessFunctionImpl(even, odd));
        DataStream<Integer> evenOutputStream = mainDataStream.getSideOutput(even);
        evenOutputStream.print("even");
        //    even:11> 2
        //    even:1> 4

        DataStream<String> out = mainDataStream.getSideOutput(odd);
        out.print("odd");
//    odd:7> odd-1
//    odd:9> odd-3

        SingleOutputStreamOperator<Object> apply = stream.map(v -> Tuple1.of(String.valueOf(v)))
                .returns(Types.TUPLE(Types.STRING))
                .keyBy(Tuple1::toString)
                .timeWindow(Time.of(10, TimeUnit.SECONDS))
                //计算个数，计算第1列
                .apply(new WindowFunction<Tuple1<String>, Object, String, TimeWindow>() {
                    @Override
                    public void apply(String s, TimeWindow timeWindow, Iterable<Tuple1<String>> iterable, Collector<Object> collector) throws Exception {
                        collector.collect(Integer.valueOf(s));
                    }
                });
    }
}
