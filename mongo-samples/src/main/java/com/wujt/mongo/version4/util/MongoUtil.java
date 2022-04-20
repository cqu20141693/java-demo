package com.wujt.mongo.version4.util;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * @author wujt
 * @version 1.0
 * @date 2020/3/13
 */
@Slf4j
public class MongoUtil {

    /**
     * 创建连接到mongoDB的客户端
     *
     * @param uri 形式为: mongodb://[username:password@]host1[:port1][,host2[:port2],...[,hostN[:portN]]][/[database][?options]]
     * @return MongoClient对象
     */
    public static MongoClient createClent(String uri) {
        MongoClient client = null;
        try {
            MongoClientURI url = new MongoClientURI(uri);
            client = new MongoClient(url);
        } catch (Exception e) {
            log.error("createMongoClient failed. connect to uri={} failed. e={}", uri, e);
        }
        return client;
    }

    /**
     * 创建操作MongoDB的MongoTemplate对象
     *
     * @param client       客户端，通过createClient得到
     * @param databaseName 所操作的数据库的名字
     * @return
     */
    public static MongoTemplate createMongoTemplate(MongoClient client, String databaseName) {
        MongoTemplate mongoTemplate = null;
        try {
            SimpleMongoDbFactory factory = new SimpleMongoDbFactory(client, databaseName);
            DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
            MongoMappingContext context = new MongoMappingContext();
            MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
            mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
            mongoTemplate = new MongoTemplate(factory, mappingConverter);
        } catch (Exception e) {
            log.error("createMongoTemplate failed. database={}, e={}", databaseName, e);
        }
        return mongoTemplate;
    }

    public static MongoTemplate createMgTemplate(String uri, String database) {
        MongoClient client = null;
        if (MongoClientManager.containsClient(uri)) {
            client = MongoClientManager.getMap().get(uri);
        } else {
            try {
                MongoClientURI url = new MongoClientURI(uri);
                client = new MongoClient(url);
                MongoClientManager.setMap(uri, client);
            } catch (Exception e) {
                log.error("createMongoClient failed. connect to uri={} failed. e={}", uri, e);
            }
        }
        return createMongoTemplate(client, database);
    }

}
