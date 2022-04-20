package com.wujt.functions;

import com.wujt.model.StreamData;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

/**
 * @author wujt
 */
public class FlatMapFunctionImpl implements FlatMapFunction<StreamData, String> {

    @Override
    public void flatMap(StreamData data, Collector<String> collector) throws Exception {
        collector.collect(data.getDeviceKey());
    }
}
