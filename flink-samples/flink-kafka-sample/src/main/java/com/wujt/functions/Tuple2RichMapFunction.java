package com.wujt.functions;

import com.wujt.model.StreamData;
import org.apache.flink.api.common.functions.IterationRuntimeContext;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;

/**
 * @author wujt
 */
public class Tuple2RichMapFunction extends RichMapFunction<StreamData, Tuple2<String, Integer>> {
    @Override
    public Tuple2<String, Integer> map(StreamData data) throws Exception {
        return Tuple2.of(data.getDeviceKey(), 1);
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
    }

    @Override
    public void close() throws Exception {
        super.close();
    }

    @Override
    public IterationRuntimeContext getIterationRuntimeContext() {
        return super.getIterationRuntimeContext();
    }
}
