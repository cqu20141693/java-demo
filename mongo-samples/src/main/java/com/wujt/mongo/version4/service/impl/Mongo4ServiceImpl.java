package com.wujt.mongo.version4.service.impl;

import com.wujt.mongo.version4.model.ThingsModel;
import com.wujt.mongo.version4.service.Mongo4Service;
import com.wujt.mongo.version4.util.MongoSwapUtil;
import com.wujt.mongo.version4.util.MongoUtil;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoSocketOpenException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wujt
 * @version 1.0
 * @date 2020/3/16
 */
@Component
@Slf4j
public class Mongo4ServiceImpl implements Mongo4Service {

    private String collectionName = "things-model";

    @Autowired
    private MongoTemplate template;

    @Override
    @Transactional
    public void testMongoTransaction(Boolean tx1) throws Exception {
        ThingsModel model = new ThingsModel();
        model.setName("dev");
        model.setIdentifier("开发");
        // model.setType(0);
        // model.setData(new Model());
        template.insert(model, collectionName);
        model.setName("prod");
        model.setIdentifier("生产");
        if (tx1) {
            throw new Exception("hello");
        }
        template.insert(model, collectionName);

    }

    @Override
    public void testWatch(String saveuUri, String saveCollection, String watchUri, String watchCollection) {
        log.info("开始监听");
        //创建保存监听事件数据的连接池
        MongoTemplate template = null;
        int index = saveuUri.lastIndexOf("/");
        template = MongoUtil.createMgTemplate(saveuUri.substring(0, index), saveuUri.substring(index + 1));
        //使用watch 方法进行监听
        try {
            index = watchUri.lastIndexOf("/");
            String uri = watchUri.substring(0, index);
            String dbName = watchUri.substring(index + 1);
            MongoClientURI mongoClientURI = new MongoClientURI(uri);
            MongoClient client = new MongoClient(mongoClientURI);
            MongoDatabase db = client.getDatabase(dbName);
            MongoCollection coll = db.getCollection(watchCollection);
            MongoCursor cursor = coll.watch().fullDocument(FullDocument.UPDATE_LOOKUP).iterator();
            while (cursor.hasNext()) {
                Map map = new HashMap();
                ChangeStreamDocument<Document> document = (ChangeStreamDocument<Document>) cursor.next();
                String operationType = document.getOperationType().getValue();
                try {
                    map = MongoSwapUtil.mongoSwapMap(document, operationType);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                // 自动对id 进行自增
                map.put("_id", 1);
                map.put("operator", operationType);

                template.insert(map, saveCollection);
                log.info(operationType + " success!");
            }
        } catch (MongoSocketOpenException e) {
            log.info("MongoSocketOpenException occur!");
        }
    }
}
