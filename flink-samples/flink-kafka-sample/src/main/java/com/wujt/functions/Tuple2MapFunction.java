package com.wujt.functions;

import com.wujt.model.StreamData;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;

/**
 * @author wujt
 */
public class Tuple2MapFunction implements MapFunction<StreamData, Tuple2<String,Integer>> {
    @Override
    public Tuple2<String,Integer> map(StreamData data) throws Exception {
        return Tuple2.of(data.getDeviceKey(),1);
    }
}
