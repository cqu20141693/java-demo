package com.wujt.functions;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple2;

/**
 * @author wujt
 *
 * 常见聚合函数
 * keyedStream.sum(0) | keyedStream.sum("key");
 * keyedStream.min(0) | keyedStream.min("key");
 * keyedStream.max(0) | keyedStream.max("key");
 * keyedStream.minBy(0) | keyedStream.minBy("key");
 * keyedStream.maxBy(0) | keyedStream.maxBy("key");
 *特别地： min 和 minBy 都会返回整个元素，只是 min 会根据用户指定的字段取最小值，并且把这个值保存在对应的位置，
 * 而对于其他的字段，并不能保证其数值正确。max 和 maxBy 同理
 *对于 Aggregations 函数，Flink 帮助我们封装了状态数据，这些状态数据不会被清理，
 * 所以在实际生产环境中应该尽量避免在一个无限流上使用 Aggregations。而且，对于同一个 keyedStream ，只能调用一次 Aggregation 函数。
 *
 *
 */
public class SumAggregateFunction implements AggregateFunction<Tuple2<String, Integer>, Object, Tuple2<String, Integer>> {
    @Override
    public Object createAccumulator() {
        return null;
    }

    @Override
    public Object add(Tuple2<String, Integer> tuple2, Object o) {
        return null;
    }

    @Override
    public Object merge(Object o, Object acc1) {
        return null;
    }

    @Override
    public Tuple2<String, Integer> getResult(Object o) {
        return null;
    }
}
