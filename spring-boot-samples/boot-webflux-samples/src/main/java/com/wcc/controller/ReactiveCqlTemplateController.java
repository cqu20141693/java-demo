package com.wcc.controller;

import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.BatchStatementBuilder;
import com.datastax.oss.driver.api.core.cql.BatchType;
import com.gow.algorithm.SnowFlakeIdGenerator;
import com.wcc.cassandra.model.Actor;
import org.apache.commons.lang3.RandomStringUtils;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.data.cassandra.core.ReactiveCassandraTemplate;
import org.springframework.data.cassandra.core.cql.ReactiveCqlTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wujt
 */
@RestController
@RequestMapping("/cql")
public class ReactiveCqlTemplateController {

    @Autowired
    private ReactiveCqlTemplate reactiveCqlTemplate;
    private final BatchStatementBuilder batch = BatchStatement.builder(BatchType.LOGGED);

    @Autowired
    private CassandraProperties cassandraProperties;
    @Autowired
    private ReactiveCassandraTemplate reactiveCassandraTemplate;


    @GetMapping("save")
    public Mono<Boolean> save(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) {
        long nextId = SnowFlakeIdGenerator.getDefaultGenerator().nextId();
        BatchStatement build = batch.build();
        Mono<Boolean> applied = reactiveCqlTemplate.execute(
                "INSERT INTO t_actor (id,first_name, last_name) VALUES (?,?,?)",
                nextId, firstName, lastName);
        return applied;
    }

    @GetMapping("batchSave")
    public Mono<Boolean> batchSave(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                                   @RequestParam("count") Byte count) {
        ArrayList<Actor> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            long nextId = SnowFlakeIdGenerator.getDefaultGenerator().nextId();
            Actor actor = new Actor().setId(nextId).setLastName(lastName + i)
                    .setFirstName(firstName + i);
            list.add(actor);
        }
        return batch().flatMap(i -> reactiveCassandraTemplate.getReactiveCqlOperations().execute("BEGIN BATCH \n" +
                        "INSERT INTO t_actor (id,first_name, last_name) VALUES (?,?,?);" +
                        "INSERT INTO t_actor (id,first_name, last_name) VALUES (?,?,?);" +
                        "APPLY BATCH;", 100L, firstName, lastName, 400L, firstName, lastName)
                .then(reactiveCassandraTemplate.batchOps().insert(list).execute().map(writeResult -> {
                    return writeResult.wasApplied();
                })));

    }

    public Mono<Boolean> batch() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("deviceId", "device");
        map.put("property", "name");
        map.put("partition", 1L);
        map.put("virtualId", "1234567");
        map.put("timestamp", System.currentTimeMillis());
        map.put("createTime", System.currentTimeMillis());
        map.put("id", RandomStringUtils.randomAlphabetic(6));
        map.put("value", "123");
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("deviceId", "device");
        map2.put("partition", 1L);
        map2.put("virtualId", "1234567");
        map2.put("timestamp", System.currentTimeMillis());
        map2.put("content", "name");
        map2.put("createTime", System.currentTimeMillis());
        map2.put("id", RandomStringUtils.randomAlphabetic(6));
        map2.put("messageId", RandomStringUtils.randomAlphabetic(6));
        map2.put("type", "test");
        String keyspace = cassandraProperties.getKeyspaceName();
        List<Publisher<Tuple2<String, Map<String, Object>>>> all = new ArrayList<>(2);
        all.add(Flux.just(map).map(m -> Tuples.of("properties_test", map)));
        all.add(Flux.just(map2).map(m -> Tuples.of("device_log_test", map2)));
        return Flux.merge(all).bufferTimeout(500, Duration.ofSeconds(1)).flatMap(
                list -> {
                    if (list.size() == 0) {
                        System.out.println("occur list empty");
                        return Flux.just(true);
                    } else {
                        return reactiveCqlTemplate.execute(createCql(list, keyspace), creatArgus(list));
                    }
                }
        ).then(Mono.just(true));

    }

    private static Object[] creatArgus(List<Tuple2<String, Map<String, Object>>> list) {
        ArrayList<Object> objects = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getT1().startsWith("properties_")) {
                objects.add(list.get(i).getT2().get("deviceId"));
                objects.add(list.get(i).getT2().get("property"));
                objects.add(list.get(i).getT2().get("partition"));
                objects.add(list.get(i).getT2().get("virtualId"));
                objects.add(list.get(i).getT2().get("timestamp"));
                objects.add(list.get(i).getT2().get("createTime"));
                objects.add(list.get(i).getT2().get("id"));
                objects.add(list.get(i).getT2().get("value"));
            } else if (list.get(i).getT1().startsWith("device_log_")) {
                objects.add(list.get(i).getT2().get("deviceId"));
                objects.add(list.get(i).getT2().get("partition"));
                objects.add(list.get(i).getT2().get("virtualId"));
                objects.add(list.get(i).getT2().get("timestamp"));
                objects.add(list.get(i).getT2().get("content"));
                objects.add(list.get(i).getT2().get("createTime"));
                objects.add(list.get(i).getT2().get("id"));
                objects.add(list.get(i).getT2().get("messageId"));
                objects.add(list.get(i).getT2().get("type"));
            }
        }
        return objects.toArray(new Object[0]);
    }

    private static String createCql(List<Tuple2<String, Map<String, Object>>> list, String keyspace) {
        StringBuilder builder = new StringBuilder("BEGIN BATCH ");
        for (Tuple2<String, Map<String, Object>> mapTuple2 : list) {
            String table = mapTuple2.getT1();
            if (table.startsWith("device_log_")) {

                builder.append("INSERT INTO ").append(keyspace).append(".").append(table)
                        .append(" (\"deviceId\",partition,\"virtualId\",timestamp,content, \"createTime\",id,\"messageId\",type) " +
                                "VALUES (?,?,?,?,?,?,?,?,?);");
            } else if (table.startsWith("properties_")) {
                builder.append("INSERT INTO ").append(keyspace).append(".").append(table)
                        .append(" (\"deviceId\",property,partition,\"virtualId\",timestamp, \"createTime\",id,value) " +
                                "VALUES (?,?,?,?,?,?,?,?);");
            } else {

            }
        }
        builder.append("APPLY BATCH;");
        return builder.toString();
    }

    public static void main(String[] args) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("deviceId", "device");
        map.put("property", "name");
        map.put("partition", 1L);
        map.put("virtualId", "1234567");
        map.put("timestamp", System.currentTimeMillis());
        map.put("createTime", System.currentTimeMillis());
        map.put("id", RandomStringUtils.randomAlphabetic(6));
        map.put("value", "123");
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("deviceId", "device");
        map2.put("partition", 1L);
        map2.put("virtualId", "1234567");
        map2.put("timestamp", System.currentTimeMillis());
        map2.put("content", "name");
        map2.put("createTime", System.currentTimeMillis());
        map2.put("id", RandomStringUtils.randomAlphabetic(6));
        map2.put("messageId", RandomStringUtils.randomAlphabetic(6));
        map2.put("type", "test");
        ArrayList<Tuple2<String, Map<String, Object>>> list = new ArrayList<>();
        list.add(Tuples.of("properties_", map));
        list.add(Tuples.of("device_log_", map2));
        String cc_iiot = createCql(list, "cc_iiot");
        Object[] objects = creatArgus(list);
        System.out.println(cc_iiot);

    }
}
