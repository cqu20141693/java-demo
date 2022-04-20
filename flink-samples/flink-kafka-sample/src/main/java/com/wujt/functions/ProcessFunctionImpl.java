package com.wujt.functions;

import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * @author wujt
 */
public class ProcessFunctionImpl extends ProcessFunction<Integer, Integer> {

    private OutputTag<Integer> evenTag;
    private OutputTag<String> oddTag;

    public ProcessFunctionImpl(OutputTag<Integer> even, OutputTag<String> odd) {
        this.evenTag = even;
        this.oddTag = odd;
    }

    @Override
    public void processElement(Integer value, Context context, Collector<Integer> collector) {
        // 分流逻辑，分为奇数、偶数两个流
        if (value % 2 == 0) {
            context.output(evenTag, value);
        } else {
            context.output(oddTag, String.valueOf(value));
        }
    }
}
